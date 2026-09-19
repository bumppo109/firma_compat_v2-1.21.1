package com.bumppo109.firma_compat.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class ThrownFluidLingeringPotion
        extends AbstractThrownFluidPotion {

    public ThrownFluidLingeringPotion(
            EntityType<? extends ThrownFluidLingeringPotion> type,
            Level level
    ) {
        super(type, level);
    }

    public ThrownFluidLingeringPotion(
            Level level,
            LivingEntity owner,
            net.minecraft.world.item.ItemStack stack
    ) {
        super(
                ModEntities.THROWN_FLUID_LINGERING_POTION.get(),
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

        makeCloud();

        level().levelEvent(
                hasInstantEffect() ? 2007 : 2002,
                blockPosition(),
                getPotionColor()
        );

        discard();
    }

    private void makeCloud() {
        List<MobEffectInstance> effects = getEffects();

        if (effects.isEmpty()) {
            return;
        }

        AreaEffectCloud cloud =
                new AreaEffectCloud(
                        level(),
                        getX(),
                        getY(),
                        getZ()
                );

        if (getOwner() instanceof LivingEntity living) {
            cloud.setOwner(living);
        }

        cloud.setRadius(3.0F);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setWaitTime(10);
        cloud.setDuration(600);

        cloud.setRadiusPerTick(
                -cloud.getRadius()
                        / (float) cloud.getDuration()
        );

        for (MobEffectInstance effect : effects) {
            cloud.addEffect(effect);
        }

        level().addFreshEntity(cloud);
    }
}
