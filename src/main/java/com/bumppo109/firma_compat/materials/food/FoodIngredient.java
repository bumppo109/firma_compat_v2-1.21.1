package com.bumppo109.firma_compat.materials.food;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public record FoodIngredient(
        ResourceLocation id,
        Supplier<Item> foodItem,
        boolean hasJam,
        boolean edible,
        int hunger,
        float saturation,
        float water,
        float decayModifier,
        float grain,
        float fruit,
        float vegetables,
        float protein,
        float dairy
) {

    /**
     * Convenience constructor for normal edible ingredients.
     */
    public FoodIngredient(
            ResourceLocation id,
            Supplier<Item> foodItem,
            float saturation,
            float water,
            float decayModifier,
            float grain,
            float fruit,
            float vegetables,
            float protein,
            float dairy
    ) {
        this(
                id,
                foodItem,
                false,
                true,
                4,
                saturation,
                water,
                decayModifier,
                grain,
                fruit,
                vegetables,
                protein,
                dairy
        );
    }

    /**
     * Convenience constructor for normal edible ingredients.
     */
    public FoodIngredient(
            ResourceLocation id,
            Supplier<Item> foodItem,
            boolean hasJam,
            float saturation,
            float water,
            float decayModifier,
            float grain,
            float fruit,
            float vegetables,
            float protein,
            float dairy
    ) {
        this(
                id,
                foodItem,
                hasJam,
                true,
                4,
                saturation,
                water,
                decayModifier,
                grain,
                fruit,
                vegetables,
                protein,
                dairy
        );
    }
}
