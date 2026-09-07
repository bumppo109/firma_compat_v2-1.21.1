package com.bumppo109.firma_compat.materials.food;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public record Food(
        ResourceLocation id,
        Supplier<Item> item,
        int hunger,
        float saturation,
        float water,
        float decayModifier,
        List<FoodIngredient> ingredients

) {
}
