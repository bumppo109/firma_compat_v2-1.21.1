package com.bumppo109.firma_compat.entity;

import net.dries007.tfc.common.fluids.FluidHelpers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class FluidPotionThrowing {

    private FluidPotionThrowing() {}

    public static void throwSplash(
            Level level,
            LivingEntity owner,
            ItemStack stack
    ) {
        if (FluidHelpers.getContainedFluid(stack).isEmpty()) {
            return;
        }

        ThrownFluidSplashPotion projectile =
                new ThrownFluidSplashPotion(
                        level,
                        owner,
                        stack.copy()
                );

        projectile.shootFromRotation(
                owner,
                owner.getXRot(),
                owner.getYRot(),
                0.0F,
                0.5F,
                1.0F
        );

        level.addFreshEntity(projectile);
    }

    public static void throwLingering(
            Level level,
            LivingEntity owner,
            ItemStack stack
    ) {
        if (FluidHelpers.getContainedFluid(stack).isEmpty()) {
            return;
        }

        ThrownFluidLingeringPotion projectile =
                new ThrownFluidLingeringPotion(
                        level,
                        owner,
                        stack.copy()
                );

        projectile.shootFromRotation(
                owner,
                owner.getXRot(),
                owner.getYRot(),
                0.0F,
                0.5F,
                1.0F
        );

        level.addFreshEntity(projectile);
    }

}
