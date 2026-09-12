package com.bumppo109.firma_compat.fluid;

import net.dries007.tfc.common.fluids.FluidHelpers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public final class FluidPotionHelper {

    private FluidPotionHelper() {
    }

    /**
     * Gets the fluid contained in a TFC fluid-container item.
     */
    public static FluidStack getFluid(ItemStack stack) {
        if (stack.isEmpty()) {
            return FluidStack.EMPTY;
        }

        return FluidHelpers.getContainedFluidInTank(stack);
    }

    /**
     * Resolves the Potion definition represented by the fluid
     * contained in the item.
     */
    public static Optional<Potion> getPotion(ItemStack stack) {
        FluidStack fluid = getFluid(stack);

        if (fluid.isEmpty()) {
            return Optional.empty();
        }

        return ModFluids.potionFromFluid(
                fluid.getFluid()
        );
    }

    /**
     * Gets fresh MobEffectInstances for the potion in the container.
     */
    public static List<MobEffectInstance> getEffects(
            ItemStack stack
    ) {
        return getPotion(stack)
                .map(Potion::effectInstances)
                .orElse(List.of());
    }

    /**
     * Returns the potion fluid itself.
     */
    public static Fluid getPotionFluid(ItemStack stack) {
        FluidStack fluid = getFluid(stack);

        return fluid.isEmpty()
                ? null
                : fluid.getFluid();
    }
}
