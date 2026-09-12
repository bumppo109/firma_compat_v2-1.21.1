package com.bumppo109.firma_compat.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FluidBrewingRecipeInput(
        ItemStack container,
        ItemStack catalyst
) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> container;
            case 1 -> catalyst;
            default -> throw new IndexOutOfBoundsException(
                    "Invalid recipe input index: " + index
            );
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
