package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
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


    private static void addItems(CreativeModeTab.Output output) {
        addWood(output);
        addRocks(output);
        ModBlocks.AQUEDUCTS.forEach((compatRockSets, blockId) -> add(output, blockId));
        add(output, ModBlocks.BRICK_AQUEDUCT);
        add(output, ModBlocks.RED_NETHER_BRICK_AQUEDUCT);
        add(output, ModItems.QUARTZ_BRICK);
        add(output, ModBlocks.QUARTZ_BRICK_AQUEDUCT);
        add(output, ModItems.PRISMARINE_BRICK);
        add(output, ModBlocks.PRISMARINE_BRICK_AQUEDUCT);

        add(output, ModBlocks.CASSITERITE_GRAVEL_DEPOSIT);
        add(output, ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT);
        add(output, ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT);
        add(output, ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT);

        add(output, ModBlocks.CLAY_DIRT);
        add(output, ModBlocks.CLAY_GRASS_BLOCK);
        add(output, ModBlocks.CLAY_PODZOL);
        add(output, ModBlocks.KAOLIN_CLAY_DIRT);
        add(output, ModBlocks.KAOLIN_CLAY_GRASS_BLOCK);
        add(output, ModBlocks.KAOLIN_CLAY_PODZOL);
        add(output, ModBlocks.COMPAT_FARMLAND);
        add(output, ModBlocks.DRYING_MUD_BRICK);
        add(output, ModItems.MUD_BRICK);

        add(output, ModItems.UNFIRED_POT);

        add(output, ModBlocks.COMPAT_CHEST);
        add(output, ModBlocks.COMPAT_TRAPPED_CHEST);
        add(output, ModItems.COMPAT_CHEST_MINECART);

        add(output, ModItems.POOR_NETHERITE_INGOT);

        ModItems.METAL_ITEMS.forEach((compatMetal, itemTypeItemIdMap) -> {
            if (compatMetal.isDummy()) return;
            itemTypeItemIdMap.forEach((itemType, itemId) -> {
                add(output, itemId);
            });
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