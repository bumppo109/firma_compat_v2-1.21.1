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

    public static final FoodIngredient BROWN_MUSHROOM = register(
            new FoodIngredient(
                    id("brown_mushroom"),
                    () -> Items.BROWN_MUSHROOM,
                    false,
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.8f,
                    0.0f
            )
    );

    public static final FoodIngredient RED_MUSHROOM = register(
            new FoodIngredient(
                    id("red_mushroom"),
                    () -> Items.RED_MUSHROOM,
                    false,
                    0.0f,
                    0.0f,
                    3.0f,
                    0.0f,
                    0.0f,
                    0.0f,
                    0.8f,
                    0.0f
            )
    );

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

    public static final FoodIngredient APPLE = register(
            new FoodIngredient(
                    id("apple"),
                    () -> Items.APPLE,
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

    public static final FoodIngredient BEETROOT = register(
            new FoodIngredient(
                    id("beetroot"),
                    () -> Items.BEETROOT,
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

    public static final FoodIngredient CARROT = register(
            new FoodIngredient(
                    id("carrot"),
                    () -> Items.CARROT,
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

    public static final FoodIngredient COD = register(
            new FoodIngredient(
                    id("cod"),
                    () -> Items.COD,
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

    public static final FoodIngredient COOKED_COD = register(
            new FoodIngredient(
                    id("cooked_cod"),
                    () -> Items.COOKED_COD,
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

    public static final FoodIngredient COOKED_PORKCHOP = register(
            new FoodIngredient(
                    id("cooked_porkchop"),
                    () -> Items.COOKED_PORKCHOP,
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

    public static final FoodIngredient PORKCHOP = register(
            new FoodIngredient(
                    id("porkchop"),
                    () -> Items.PORKCHOP,
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

    public static final FoodIngredient BEEF = register(
            new FoodIngredient(
                    id("beef"),
                    () -> Items.BEEF,
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

    public static final FoodIngredient COOKED_BEEF = register(
            new FoodIngredient(
                    id("cooked_beef"),
                    () -> Items.COOKED_BEEF,
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

    public static final FoodIngredient COOKED_MUTTON = register(
            new FoodIngredient(
                    id("cooked_mutton"),
                    () -> Items.COOKED_MUTTON,
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

    public static final FoodIngredient MUTTON = register(
            new FoodIngredient(
                    id("mutton"),
                    () -> Items.MUTTON,
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

    public static final FoodIngredient SALMON = register(
            new FoodIngredient(
                    id("salmon"),
                    () -> Items.SALMON,
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

    public static final FoodIngredient COOKED_SALMON = register(
            new FoodIngredient(
                    id("cooked_salmon"),
                    () -> Items.COOKED_SALMON,
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

    public static final FoodIngredient WHEAT = register(
            new FoodIngredient(
                    id("wheat"),
                    () -> Items.WHEAT,
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

    public static final FoodIngredient BREAD = register(
            new FoodIngredient(
                    id("bread"),
                    () -> Items.BREAD,
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

    public static final FoodIngredient RABBIT = register(
            new FoodIngredient(
                    id("rabbit"),
                    () -> Items.RABBIT,
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

    public static final FoodIngredient COOKED_RABBIT = register(
            new FoodIngredient(
                    id("cooked_rabbit"),
                    () -> Items.COOKED_RABBIT,
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

    public static final FoodIngredient MELON_SLICE = register(
            new FoodIngredient(
                    id("melon_slice"),
                    () -> Items.MELON_SLICE,
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

    public static final FoodIngredient CHICKEN = register(
            new FoodIngredient(
                    id("chicken"),
                    () -> Items.CHICKEN,
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

    public static final FoodIngredient COOKED_CHICKEN = register(
            new FoodIngredient(
                    id("cooked_chicken"),
                    () -> Items.COOKED_CHICKEN,
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

    public static final FoodIngredient POTATO = register(
            new FoodIngredient(
                    id("potato"),
                    () -> Items.POTATO,
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

    public static final FoodIngredient BAKED_POTATO = register(
            new FoodIngredient(
                    id("baked_potato"),
                    () -> Items.BAKED_POTATO,
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
