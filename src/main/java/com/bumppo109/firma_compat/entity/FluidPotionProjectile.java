package com.bumppo109.firma_compat.entity;

import com.bumppo109.firma_compat.fluid.FluidPotionHelper;
import com.bumppo109.firma_compat.fluid.Potion;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class FluidPotionProjectile extends ThrowableItemProjectile {

    private static final double SPLASH_RANGE = 4.0D;
    private static final double SPLASH_RANGE_SQ =
            SPLASH_RANGE * SPLASH_RANGE;

    public FluidPotionProjectile(
            EntityType<? extends FluidPotionProjectile> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    public FluidPotionProjectile(
            EntityType<? extends FluidPotionProjectile> entityType,
            Level level,
            LivingEntity shooter
    ) {
        super(entityType, level);

        setOwner(shooter);

        setPos(
                shooter.getX(),
                shooter.getEyeY() - 0.1D,
                shooter.getZ()
        );

        setRot(
                shooter.getYRot(),
                shooter.getXRot()
        );
    }

    public FluidPotionProjectile(
            EntityType<? extends FluidPotionProjectile> entityType,
            Level level,
            double x,
            double y,
            double z
    ) {
        super(entityType, level);

        setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            System.out.println(
                    "[FirmaCompat] TICK " +
                            "pos=" + position() +
                            " delta=" + getDeltaMovement() +
                            " removed=" + isRemoved()
            );
        }
    }


    @Override
    protected Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05D;
    }

    @Override
    protected void onHit(HitResult result) {
        System.out.println(
                "[FirmaCompat] Fluid potion HIT: " +
                        result.getType() +
                        " at " +
                        result.getLocation()
        );
        /*
         * Server-authoritative potion behavior.
         */
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Optional<Potion> potionOptional =
                containedPotion();

        if (potionOptional.isEmpty()) {
            discard();
            return;
        }

        Potion potion = potionOptional.get();

        /*
         * Make sure the impact position is the exact point where
         * the projectile hit.
         */
        Vec3 impact = result.getLocation();

        setPos(
                impact.x,
                impact.y,
                impact.z
        );

        if (isLingering()) {

            createAreaEffectCloud(
                    serverLevel,
                    potion
            );

        } else {

            Entity directHit =
                    result instanceof EntityHitResult entityHit
                            ? entityHit.getEntity()
                            : null;

            applySplash(
                    serverLevel,
                    potion.effects(),
                    directHit,
                    impact
            );
        }

        /*
         * Vanilla projectile impact event.
         *
         * This tells the client to create the thrown-item
         * impact particles.
         */
        level().broadcastEntityEvent(
                this,
                (byte) 3
        );

        /*
         * Vanilla potion impact sound.
         */
        level().playSound(
                null,
                getX(),
                getY(),
                getZ(),
                SoundEvents.SPLASH_POTION_BREAK,
                SoundSource.NEUTRAL,
                1.0F,
                0.8F +
                        level().getRandom().nextFloat()
                                * 0.2F
        );

        /*
         * Potion color event.
         *
         * 2007 = instant effect
         * 2002 = normal potion
         */
        int eventId =
                hasInstantEffect(potion)
                        ? 2007
                        : 2002;

        level().levelEvent(
                eventId,
                blockPosition(),
                potion.color()
        );

        discard();
    }

    /**
     * Resolves the potion represented by the fluid in the
     * projectile's ItemStack.
     */
    private Optional<Potion> containedPotion() {
        ItemStack stack = getItem();

        if (stack.isEmpty()) {
            return Optional.empty();
        }

        return FluidPotionHelper.getPotion(stack);
    }

    private boolean isLingering() {
        return getType() ==
                ModEntities.FLUID_LINGERING_POTION.get();
    }

    /**
     * Applies splash effects around the actual impact point.
     */
    private void applySplash(
            ServerLevel level,
            Potion.EffectData[] effects,
            Entity directHit,
            Vec3 impact
    ) {
        AABB bounds =
                new AABB(
                        impact.x,
                        impact.y,
                        impact.z,
                        impact.x,
                        impact.y,
                        impact.z
                ).inflate(
                        SPLASH_RANGE,
                        2.0D,
                        SPLASH_RANGE
                );

        List<LivingEntity> entities =
                level.getEntitiesOfClass(
                        LivingEntity.class,
                        bounds
                );

        Entity effectSource =
                getEffectSource();

        for (LivingEntity living : entities) {

            if (!living.isAffectedByPotions()) {
                continue;
            }

            /*
             * Measure distance from the IMPACT point rather than
             * the projectile's current position.
             */
            double distanceSq =
                    living.distanceToSqr(impact);

            if (distanceSq >= SPLASH_RANGE_SQ) {
                continue;
            }

            double intensity;

            if (living == directHit) {
                intensity = 1.0D;
            } else {
                intensity =
                        1.0D -
                                Math.sqrt(distanceSq)
                                        / SPLASH_RANGE;
            }

            if (intensity <= 0.0D) {
                continue;
            }

            for (Potion.EffectData effect : effects) {

                Holder<MobEffect> holder =
                        effect.effect();

                MobEffect mobEffect =
                        holder.value();

                /*
                 * Instant effects such as Healing/Harming.
                 */
                if (mobEffect.isInstantenous()) {

                    mobEffect.applyInstantenousEffect(
                            this,
                            getOwner(),
                            living,
                            effect.level(),
                            intensity
                    );

                    continue;
                }

                /*
                 * Normal effects scale their duration based
                 * on distance from the impact.
                 */
                int duration =
                        (int) (
                                effect.duration()
                                        * intensity
                                        + 0.5D
                        );

                /*
                 * Match vanilla's splash behavior.
                 */
                if (duration <= 20) {
                    continue;
                }

                MobEffectInstance scaled =
                        new MobEffectInstance(
                                holder,
                                duration,
                                effect.level(),
                                false,
                                true
                        );

                living.addEffect(
                        scaled,
                        effectSource
                );
            }
        }
    }

    /**
     * Creates the lingering potion cloud.
     */
    private void createAreaEffectCloud(
            ServerLevel level,
            Potion potion
    ) {
        AreaEffectCloud cloud =
                new AreaEffectCloud(
                        level,
                        getX(),
                        getY(),
                        getZ()
                );

        if (getOwner() instanceof LivingEntity living) {
            cloud.setOwner(living);
        }

        /*
         * 1.21.1 does not have AreaEffectCloud#setFixedColor().
         *
         * The particle itself carries the color.
         */
        cloud.setParticle(
                ColorParticleOption.create(
                        ParticleTypes.ENTITY_EFFECT,
                        potion.color()
                )
        );

        cloud.setRadius(3.0F);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setWaitTime(10);
        cloud.setDuration(600);

        cloud.setRadiusPerTick(
                -cloud.getRadius()
                        / cloud.getDuration()
        );

        for (Potion.EffectData effect : potion.effects()) {

            cloud.addEffect(
                    new MobEffectInstance(
                            effect.effect(),
                            effect.duration(),
                            effect.level()
                    )
            );
        }

        level.addFreshEntity(cloud);
    }

    private boolean hasInstantEffect(Potion potion) {
        for (Potion.EffectData effect : potion.effects()) {

            if (effect.effect()
                    .value()
                    .isInstantenous()) {

                return true;
            }
        }

        return false;
    }
}
