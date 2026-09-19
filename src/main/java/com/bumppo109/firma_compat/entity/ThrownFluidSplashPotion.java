package com.bumppo109.firma_compat.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class ThrownFluidSplashPotion
        extends AbstractThrownFluidPotion {

    public ThrownFluidSplashPotion(
            EntityType<? extends ThrownFluidSplashPotion> type,
            Level level
    ) {
        super(type, level);
    }

    public ThrownFluidSplashPotion(
            Level level,
            LivingEntity owner,
            net.minecraft.world.item.ItemStack stack
    ) {
        super(
                ModEntities.THROWN_FLUID_SPLASH_POTION.get(),
                level,
                owner,
                stack
        );
    }

    @Override
    protected Item getDefaultItem() {
        return Items.GLASS_BOTTLE;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (level().isClientSide) {
            return;
        }

        Entity hitEntity = result instanceof EntityHitResult entityHit
                ? entityHit.getEntity()
                : null;

        applySplash(hitEntity);

        level().levelEvent(
                hasInstantEffect() ? 2007 : 2002,
                blockPosition(),
                getPotionColor()
        );

        discard();
    }

    private void applySplash(Entity hitEntity) {
        List<MobEffectInstance> effects = getEffects();

        if (effects.isEmpty()) {
            return;
        }

        AABB box = getBoundingBox()
                .inflate(4.0D, 2.0D, 4.0D);

        List<LivingEntity> entities =
                level().getEntitiesOfClass(
                        LivingEntity.class,
                        box
                );

        for (LivingEntity target : entities) {
            if (!target.isAffectedByPotions()) {
                continue;
            }

            double distance = distanceToSqr(target);

            if (distance >= 16.0D) {
                continue;
            }

            double intensity;

            if (target == hitEntity) {
                intensity = 1.0D;
            } else {
                intensity = Math.max(
                        0.0D,
                        1.0D - Math.sqrt(distance) / 4.0D
                );
            }

            for (MobEffectInstance effect : effects) {
                applyEffect(
                        target,
                        effect,
                        intensity
                );
            }
        }
    }
}
