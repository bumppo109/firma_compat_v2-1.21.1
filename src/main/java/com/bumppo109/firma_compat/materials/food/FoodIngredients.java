package com.bumppo109.firma_compat.materials.food;

import com.bumppo109.firma_compat.FirmaCompat;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.stream.Stream;

public final class FoodIngredients {

    private static final Map<ResourceLocation, FoodIngredient> REGISTRY =
            new LinkedHashMap<>();

    private static final Map<Item, FoodIngredient> BY_ITEM =
            new IdentityHashMap<>();

    private static boolean initialized = false;

    private FoodIngredients() {}

    /**
     * Registers a food ingredient without resolving its item supplier.
     *
     * Item uniqueness is validated during bootstrap, after all addon
     * registrations have had an opportunity to occur.
     */
    public static FoodIngredient register(FoodIngredient ingredient) {
        if (REGISTRY.containsKey(ingredient.id())) {
            throw new IllegalArgumentException(
                    "Duplicate food ingredient ID: " + ingredient.id()
            );
        }

        REGISTRY.put(ingredient.id(), ingredient);
        return ingredient;
    }

    /**
     * Resolves all food item suppliers and builds the item lookup.
     *
     * This must be called after all addons have registered their
     * FoodIngredients.
     */
    public static void bootstrap() {
        if (initialized) {
            return;
        }

        initialized = true;

        for (FoodIngredient ingredient : REGISTRY.values()) {
            Item item = ingredient.foodItem().get();

            FoodIngredient previous = BY_ITEM.putIfAbsent(item, ingredient);

            if (previous != null) {
                throw new IllegalArgumentException(
                        "Item is already registered as a food ingredient: " +
                                item +
                                " (" + previous.id() +
                                " and " + ingredient.id() + ")"
                );
            }
        }
    }

    public static FoodIngredient get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static FoodIngredient getByItem(Item item) {
        return BY_ITEM.get(item);
    }

    public static Collection<FoodIngredient> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                path
        );
    }

    public static Stream<FoodIngredient> preserveIngredients() {
        return Stream.of(
                GLOW_BERRIES,
                SWEET_BERRIES
        );
    }

    // -------------------------------------------------------------------------
    // Firma Compat ingredients
    // -------------------------------------------------------------------------

    public static final FoodIngredient GLOW_BERRIES = register(
            new FoodIngredient(
                    id("glow_berries"),
                    () -> Items.GLOW_BERRIES,
                    true,
                    0.5f,
                    5.0f,
                    4.9f,
                    0.0f,
                    0.9f,
                    0.0f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient SWEET_BERRIES = register(
            new FoodIngredient(
                    id("sweet_berries"),
                    () -> Items.SWEET_BERRIES,
                    true,
                    0.5f,
                    5.0f,
                    4.9f,
                    0.0f,
                    0.9f,
                    0.0f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient KELP = register(
            new FoodIngredient(
                    id("kelp"),
                    () -> Items.KELP,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.9f,
                    0.2f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient DRIED_KELP = register(
            new FoodIngredient(
                    id("dried_kelp"),
                    () -> Items.DRIED_KELP,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.9f,
                    0.3f,
                    0.0f,
                    0.0f
            )
    );

    // -------------------------------------------------------------------------
    // TFC ingredients
    // -------------------------------------------------------------------------

    public static final FoodIngredient RED_APPLE = register(
            new FoodIngredient(
                    id("red_apple"),
                    TFCItems.FOOD.get(Food.RED_APPLE),
                    0.2f,
                    0.0f,
                    1.7f,
                    0.0f,
                    0.9f,
                    0.0f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_BEET = register(
            new FoodIngredient(
                    id("tfc_beet"),
                    TFCItems.FOOD.get(Food.BEET),
                    2.0f,
                    0.0f,
                    0.5f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_CARROT = register(
            new FoodIngredient(
                    id("tfc_carrot"),
                    TFCItems.FOOD.get(Food.CARROT),
                    2.0f,
                    0.0f,
                    0.5f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.5f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_COD = register(
            new FoodIngredient(
                    id("tfc_cod"),
                    TFCItems.FOOD.get(Food.COD),
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f
            )
    );

    public static final FoodIngredient TFC_COOKED_COD = register(
            new FoodIngredient(
                    id("tfc_cooked_cod"),
                    TFCItems.FOOD.get(Food.COOKED_COD),
                    1.0f,
                    0.0f,
                    2.25f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.0f
            )
    );

    public static final FoodIngredient TFC_COOKED_PORK = register(
            new FoodIngredient(
                    id("tfc_cooked_pork"),
                    TFCItems.FOOD.get(Food.COOKED_PORK),
                    2.0f,
                    0.0f,
                    1.5f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.5f
            )
    );

    public static final FoodIngredient TFC_PORK = register(
            new FoodIngredient(
                    id("tfc_pork"),
                    TFCItems.FOOD.get(Food.PORK),
                    0.0f,
                    0.0f,
                    2.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.5f
            )
    );

    public static final FoodIngredient TFC_BEEF = register(
            new FoodIngredient(
                    id("tfc_beef"),
                    TFCItems.FOOD.get(Food.BEEF),
                    0.0f,
                    0.0f,
                    2.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.0f
            )
    );

    public static final FoodIngredient TFC_COOKED_BEEF = register(
            new FoodIngredient(
                    id("tfc_cooked_beef"),
                    TFCItems.FOOD.get(Food.COOKED_BEEF),
                    2.0f,
                    0.0f,
                    1.5f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.5f
            )
    );

    public static final FoodIngredient TFC_COOKED_MUTTON = register(
            new FoodIngredient(
                    id("tfc_cooked_mutton"),
                    TFCItems.FOOD.get(Food.COOKED_MUTTON),
                    2.0f,
                    0.0f,
                    2.25f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.5f
            )
    );

    public static final FoodIngredient TFC_MUTTON = register(
            new FoodIngredient(
                    id("tfc_mutton"),
                    TFCItems.FOOD.get(Food.MUTTON),
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.5f
            )
    );

    public static final FoodIngredient TFC_SALMON = register(
            new FoodIngredient(
                    id("tfc_salmon"),
                    TFCItems.FOOD.get(Food.SALMON),
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f
            )
    );

    public static final FoodIngredient TFC_COOKED_SALMON = register(
            new FoodIngredient(
                    id("tfc_cooked_salmon"),
                    TFCItems.FOOD.get(Food.COOKED_SALMON),
                    1.0f,
                    0.0f,
                    2.25f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.0f
            )
    );

    public static final FoodIngredient TFC_WHEAT = register(
            new FoodIngredient(
                    id("tfc_wheat"),
                    TFCItems.FOOD.get(Food.WHEAT),
                    0.0f,
                    0.0f,
                    2.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_WHEAT_BREAD = register(
            new FoodIngredient(
                    id("tfc_wheat_bread"),
                    TFCItems.FOOD.get(Food.WHEAT_BREAD),
                    1.0f,
                    0.0f,
                    1.0f,
                    0.0f,
                    1.5f,
                    0.0f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_RABBIT = register(
            new FoodIngredient(
                    id("tfc_rabbit"),
                    TFCItems.FOOD.get(Food.RABBIT),
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.5f
            )
    );

    public static final FoodIngredient TFC_COOKED_RABBIT = register(
            new FoodIngredient(
                    id("tfc_cooked_rabbit"),
                    TFCItems.FOOD.get(Food.COOKED_RABBIT),
                    1.0f,
                    0.0f,
                    2.25f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.5f
            )
    );

    public static final FoodIngredient TFC_MELON_SLICE = register(
            new FoodIngredient(
                    id("tfc_melon_slice"),
                    TFCItems.FOOD.get(Food.MELON_SLICE),
                    0.4f,
                    10.0f,
                    2.5f,
                    0.0f,
                    0.7f,
                    0.0f,
                    0.0f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_CHICKEN = register(
            new FoodIngredient(
                    id("tfc_chicken"),
                    TFCItems.FOOD.get(Food.CHICKEN),
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    1.5f
            )
    );

    public static final FoodIngredient TFC_COOKED_CHICKEN = register(
            new FoodIngredient(
                    id("tfc_cooked_chicken"),
                    TFCItems.FOOD.get(Food.COOKED_CHICKEN),
                    2.0f,
                    0.0f,
                    2.25f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    2.5f
            )
    );

    public static final FoodIngredient TFC_POTATO = register(
            new FoodIngredient(
                    id("tfc_potato"),
                    TFCItems.FOOD.get(Food.POTATO),
                    0.5f,
                    0.0f,
                    0.666f,
                    0.2f,
                    0.0f,
                    0.0f,
                    0.5f,
                    0.0f
            )
    );

    public static final FoodIngredient TFC_BAKED_POTATO = register(
            new FoodIngredient(
                    id("tfc_baked_potato"),
                    TFCItems.FOOD.get(Food.BAKED_POTATO),
                    2.0f,
                    0.0f,
                    1.0f,
                    0.4f,
                    0.0f,
                    0.0f,
                    1.2f,
                    0.0f
            )
    );
}
