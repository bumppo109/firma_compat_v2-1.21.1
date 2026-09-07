package com.bumppo109.firma_compat.materials.food;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.*;

public final class Foods {

    private static final Map<ResourceLocation, Food> REGISTRY =
            new LinkedHashMap<>();

    private Foods() {}

    public static Food register(Food food) {
        Food previous =
                REGISTRY.putIfAbsent(food.id(), food);

        if (previous != null) {
            throw new IllegalArgumentException(
                    "Duplicate food: " + food.id()
            );
        }

        return food;
    }

    public static Food get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Collection<Food> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }


    public static final Food BEETROOT_SOUP = register(
            new Food(
                    id("beetroot_soup"),
                    () -> Items.BEETROOT_SOUP,
                    6,
                    7.2f,
                    0.0f,
                    1.0f,
                    List.of(FoodIngredients.TFC_BEET, FoodIngredients.TFC_BEET, FoodIngredients.TFC_BEET, FoodIngredients.TFC_BEET, FoodIngredients.TFC_BEET, FoodIngredients.TFC_BEET)
            )
    );

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                path
        );
    }
}
