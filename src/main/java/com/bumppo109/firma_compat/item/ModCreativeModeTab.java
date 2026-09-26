package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.ModCompat;
import com.bumppo109.firma_compat.addon.firmalife.FLCompat;
import com.bumppo109.firma_compat.addon.rnr.RnRCompat;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.materials.food.FoodIngredient;
import com.bumppo109.firma_compat.materials.food.FoodIngredients;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FirmaCompat.MODID);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FIRMA_COMPAT_TAB =
            CREATIVE_TABS.register(FirmaCompat.MODID, () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.LUMBER.get(CompatWood.OAK)))
                            .title(Component.translatable("firma_compat.creative_tab.firma_compat"))
                            .displayItems((parameters, output) -> addItems(output))
                            .build()
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FIRMA_COMPAT_TFC_ADDITIONS =
            CREATIVE_TABS.register("firma_compat_tfc_additions", () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModBlocks.TFC_CHISELED_SANDSTONE.get(SandBlockType.BROWN)))
                            .title(Component.translatable("firma_compat.creative_tab.firma_compat_tfc_additions"))
                            .displayItems((parameters, output) -> addTFCItems(output))
                            .build()
            );

    private static void addItems(CreativeModeTab.Output output) {
        FoodIngredients.preserveIngredients().forEach(ingredient -> {
            add(output, ModItems.FRUIT_PRESERVES.get(ingredient));
            add(output, ModItems.UNSEALED_FRUIT_PRESERVES.get(ingredient));
            add(output, ModItems.JAM.get(ingredient));
        });
        add(output, ModBlocks.FLUID_BREWING_STAND);
        for (Glass glass : Glass.values()) {
            add(output, ModItems.SPLASH_POTIONS.get(glass));
            add(output, ModItems.LINGERING_POTIONS.get(glass));
        }
        add(output, ModBlocks.COMPAT_CHEST);
        add(output, ModBlocks.COMPAT_TRAPPED_CHEST);
        add(output, ModItems.COMPAT_CHEST_MINECART);

        addWood(output);
        addRocks(output);

        ModBlocks.AQUEDUCTS.forEach((rockSets, blockId) -> {
            add(output, blockId);
        });
        add(output, ModBlocks.BRICK_AQUEDUCT);
        add(output, ModItems.PRISMARINE_BRICK);
        add(output, ModBlocks.PRISMARINE_BRICK_AQUEDUCT);
        add(output, ModItems.QUARTZ_BRICK);
        add(output, ModBlocks.QUARTZ_BRICK_AQUEDUCT);
        add(output, ModBlocks.RED_NETHER_BRICK_AQUEDUCT);

        ModItems.METAL_ITEMS.forEach((compatMetal, itemTypeItemIdMap) -> {
            itemTypeItemIdMap.forEach((itemType, itemId) -> {
                if (compatMetal.equals(CompatMetal.SCRAP_NETHERITE) && itemType.equals(CompatMetal.ItemType.NUGGET)) {
                    add(output, ModItems.SCRAP_NETHERITE_INGOT);
                }
                add(output, itemId);
            });
        });
        add(output, ModItems.UNFINISHED_LANTERN);
        add(output, ModBlocks.LANTERN);
        ModBlocks.COMPAT_LANTERNS.forEach((metal, lampBlockId) -> {
            add(output, lampBlockId);
        });

        ModBlocks.CLAY_BLOCKS.forEach((material, soilBlockTypeIdMap) -> {
            soilBlockTypeIdMap.forEach((soilBlockType, blockId) -> {
                add(output, blockId);
            });
        });
        ModBlocks.KAOLIN_CLAY_BLOCKS.forEach((material, soilBlockTypeIdMap) -> {
            soilBlockTypeIdMap.forEach((soilBlockType, blockId) -> {
                add(output, blockId);
            });
        });
        ModBlocks.ORE_DEPOSITS.forEach((oreDeposit, blockId) -> {
            add(output, blockId);
        });
        add(output, ModBlocks.COMPAT_FARMLAND);
        add(output, ModItems.MUD_BRICK);
        add(output, ModBlocks.DRYING_MUD_BRICK);
        add(output, ModItems.UNFIRED_POT);

        ModBlocks.ORES.forEach((compatRock, oreIdMap) -> {
            oreIdMap.forEach((ore, blockId) -> {
                add(output, blockId);
            });
        });
        ModBlocks.GRADED_ORES.forEach((compatRock, oreMapMap) -> {
            oreMapMap.forEach((ore, gradeIdMap) -> {
                gradeIdMap.forEach((grade, blockId) -> {
                    add(output, blockId);
                });
            });
        });

        //Firmalife
        if (ModCompat.loaded("firmalife")) {
            FLCompat.addFLItems(output);
        }
        //RnR
        if (ModCompat.loaded("rnr")) {
            RnRCompat.addRnRItems(output);
        }

    }

    private static void addTFCItems(CreativeModeTab.Output output) {
        ModBlocks.TFC_ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(output, blockId);
            });
        });
        ModBlocks.TFC_SUSPICIOUS_GRAVEL.forEach((rock, blockId) -> {
            add(output, blockId);
        });
        ModBlocks.TFC_SUSPICIOUS_SAND.forEach((sand, blockId) -> {
            add(output, blockId);
        });
        ModBlocks.TFC_CHISELED_SANDSTONE.forEach((sand, blockId) -> {
            add(output, blockId);
        });
        ModItems.TFC_NUGGETS.forEach((metal, itemId) -> {
            add(output, itemId);
        });
        // Filled silica glass bottles
        ModFluids.POTIONS.forEach((potion, fluidHolder) -> {
            ItemStack bottle = new ItemStack(TFCItems.SILICA_GLASS_BOTTLE.get());

            bottle.set(
                    TFCComponents.FLUID,
                    new FluidComponent(
                            new FluidStack(fluidHolder.getSource(), 250)
                    )
            );

            output.accept(bottle);
        });
    }

    private static void addWood(CreativeModeTab.Output output) {
        for (CompatWood wood : CompatWood.VALUES) {
            add(output, ModItems.LUMBER.get(wood));
            for (CompatWood.BlockType blockType : CompatWood.BlockType.values()) {
                if (blockType.needsItem()) {
                    add(output, ModBlocks.WOODS.get(wood).get(blockType));
                }
            }
            add(output, ModItems.SUPPORTS.get(wood));
        }
    }

    private static void addRocks(CreativeModeTab.Output output) {
        for (CompatRock rock : CompatRock.VALUES) {
            var blocks = ModBlocks.ROCK_BLOCKS.get(rock);
            add(output, ModItems.BRICK.get(rock));
            add(output, ModBlocks.ROCK_ANVILS.get(rock));
            for (CompatRock.BlockType type : CompatRock.BlockType.values()) {
                if (type.needsItem()) {
                    add(output, blocks.get(type));
                }
                if (type.hasVariants()) {
                    add(output, ModBlocks.ROCK_DECORATIONS.get(rock).get(type).slab());
                    add(output, ModBlocks.ROCK_DECORATIONS.get(rock).get(type).stair());
                    add(output, ModBlocks.ROCK_DECORATIONS.get(rock).get(type).wall());
                }
            }
        }
    }

    private static void add(CreativeModeTab.Output output, Supplier<? extends ItemLike> supplier) {

        if (supplier == null) {
            return;
        }

        ItemLike itemLike = supplier.get();

        if (itemLike == null) {
            return;
        }

        Item item = itemLike.asItem();

        if (item == Items.AIR) {
            return;
        }

        output.accept(item);
    }
}