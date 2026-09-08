package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.materials.food.FoodIngredient;
import com.bumppo109.firma_compat.materials.food.FoodIngredients;
import net.dries007.tfc.common.Lore;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.entities.TFCEntities;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCMinecartItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bumppo109.firma_compat.materials.food.FoodIngredients.preserveIngredients;
import static net.dries007.tfc.common.items.TFCItems.EMPTY_JAR;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, FirmaCompat.MODID);

    //Wood
    public static final Map<CompatWood, ItemId> LUMBER = Helpers.mapOf(
            CompatWood.class,
            wood -> wood.woodMaterial().planks() != null,
            wood -> register(wood.name() + "_lumber")
    );

    public static final Map<CompatWood, ItemId> SUPPORTS = Helpers.mapOf(
            CompatWood.class,
            wood -> wood.woodMaterial().log() != null,
            wood -> register(
                    wood.name() + "_support",
                    () -> new StandingAndWallBlockItem(
                            ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.VERTICAL_SUPPORT).get(),
                            ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get(),
                            new Item.Properties(),
                            Direction.DOWN
                    )
            )
    );

    public static final ItemId COMPAT_CHEST_MINECART = register("compat_chest_minecart",
            (() -> new TFCMinecartItem(new Item.Properties(), TFCEntities.CHEST_MINECART,
                    () -> ModBlocks.COMPAT_CHEST.get().asItem())));

    //Rock
    public static final Map<CompatRock, ItemId> BRICK = Helpers.mapOf(CompatRock.class, CompatRock::makesBrick, rock ->
            register(rock.equals(CompatRock.BLACKSTONE) ? "polished_blackstone_brick" : rock.getSerializedName() + "_brick"));
    public static final ItemId QUARTZ_BRICK = register("quartz_brick");
    public static final ItemId PRISMARINE_BRICK = register("prismarine_brick");

    //Natural
    public static final ItemId MUD_BRICK = register("mud_brick");
    public static final ItemId UNFIRED_POT = register("unfired_pot");

    //Metal
    public static final Map<CompatMetal, Map<CompatMetal.ItemType, ItemId>> METAL_ITEMS = Helpers.mapOf(CompatMetal.class, metal ->
            Helpers.mapOf(CompatMetal.ItemType.class, type -> type.has(metal), type ->
                    register(metal.name() + "_" + type.name(), () -> type.create(metal))
            )
    );

    public static final Map<Metal, ItemId> TFC_NUGGETS = Helpers.mapOf(Metal.class,metal ->
            register("tfc_" + metal.getSerializedName() + "_nugget"));

    public static final Map<CompatMetal, ItemId> METAL_FLUID_BUCKETS = Helpers.mapOf(CompatMetal.class, metal ->
            register("bucket/metal/" + metal.name(), () -> new BucketItem(ModFluids.METALS.get(metal).getSource(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    public static final ItemId SCRAP_NETHERITE_INGOT = register("scrap_netherite_ingot");
    public static final ItemId UNFINISHED_LANTERN = register("unfinished_lantern");

    //Food
    public static final Map<FoodIngredient, ItemId> FRUIT_PRESERVES =
            preserveIngredients()
                    .collect(Collectors.toMap(
                            ingredient -> ingredient,
                            ingredient -> register(
                                    "jar/" + ingredient.id().getPath(),
                                    () -> new Item(
                                            new Item.Properties()
                                                    .component(Lore.TYPE, Lore.SEALED)
                                    )
                            ),
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

    public static final Map<FoodIngredient, ItemId> UNSEALED_FRUIT_PRESERVES =
            preserveIngredients()
                    .collect(Collectors.toMap(
                            ingredient -> ingredient,
                            ingredient -> register(
                                    "jar/" + ingredient.id().getPath() + "_unsealed",
                                    () -> new Item(
                                            new Item.Properties()
                                                    .component(Lore.TYPE, Lore.UNSEALED)
                                                    .craftRemainder(EMPTY_JAR.asItem())
                                    )
                            ),
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

    public static final Map<FoodIngredient, ItemId> JAM =
            preserveIngredients()
                    .collect(Collectors.toMap(
                            ingredient -> ingredient,
                            ingredient -> register(
                                    ingredient.id().getPath() + "_jam",
                                    () -> new Item(new Item.Properties())
                            ),
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));



    private static ItemId register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> ItemId register(String name, Supplier<T> item)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }

    public record ItemId(DeferredHolder<Item, Item> holder) implements RegistryHolder<Item, Item>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get();
        }
    }
}