package com.bumppo109.firma_compat.materials.food;

public final class FoodRegistryBootstrap {

    private static boolean initialized = false;

    private FoodRegistryBootstrap() {}

    public static void bootstrap() {
        if (initialized) {
            return;
        }

        initialized = true;

        // Force initialization of built-in entries.
        FoodIngredients.values();
        Foods.values();
    }
}

