package com.bumppo109.firma_compat.materials.food;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public record Food(
        ResourceLocation id,
        Supplier<Item> item,
        @Nullable Supplier<Block> foodBlock,
        int hunger,
        float saturation,
        float water,
        float decayModifier,
        List<FoodIngredient> ingredients

) {

    public Food(
            ResourceLocation id,
            Supplier<Item> item,
            int hunger,
            float saturation,
            float water,
            float decayModifier,
            List<FoodIngredient> ingredients

    ) {
        this(
                id,
                item,
                null,
                hunger,
                saturation,
                water,
                decayModifier,
                ingredients
        );
    }
}
