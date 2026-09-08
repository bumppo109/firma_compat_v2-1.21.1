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
                    10.0f,
                    4.5f,
                    List.of(FoodIngredients.BEETROOT, FoodIngredients.BEETROOT, FoodIngredients.BEETROOT, FoodIngredients.BEETROOT)
            )
    );

    public static final Food MUSHROOM_STEW = register(
            new Food(
                    id("mushroom_stew"),
                    () -> Items.MUSHROOM_STEM,
                    6,
                    7.2f,
                    10.0f,
                    4.5f,
                    List.of(FoodIngredients.RED_MUSHROOM, FoodIngredients.BROWN_MUSHROOM)
            )
    );

    public static final Food RABBIT_STEW = register(
            new Food(
                    id("rabbit_stew"),
                    () -> Items.RABBIT_STEW,
                    6,
                    7.2f,
                    10.0f,
                    4.5f,
                    List.of(FoodIngredients.CARROT, FoodIngredients.BAKED_POTATO, FoodIngredients.RED_MUSHROOM, FoodIngredients.COOKED_RABBIT)
            )
    );

    public static final Food SUSPICIOUS_STEW = register(
            new Food(
                    id("suspicious_stew"),
                    () -> Items.SUSPICIOUS_STEW,
                    6,
                    7.2f,
                    10.0f,
                    4.5f,
                    List.of(FoodIngredients.RED_MUSHROOM, FoodIngredients.BROWN_MUSHROOM)
            )
    );

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                path
        );
    }
}
