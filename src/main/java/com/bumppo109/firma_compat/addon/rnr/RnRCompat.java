package com.bumppo109.firma_compat.addon.rnr;

import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnR;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRItems;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class RnRCompat {
    private RnRCompat() {}

    public static void register(IEventBus modEventBus) {
        CompatRnRBlocks.BLOCKS.register(modEventBus);
        CompatRnRItems.ITEMS.register(modEventBus);
    }

    public static void addRnRItems(CreativeModeTab.Output output) {
        for (CompatWood wood : CompatWood.VALUES) {
            add(output, CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood));
            add(output, CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood));
            add(output, CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood));
        }
            add(output, CompatRnRBlocks.TAMPED_MUD);
            add(output, CompatRnRBlocks.TAMPED_DIRT);
            add(output, CompatRnRBlocks.GRAVEL_ROAD);
            add(output, CompatRnRBlocks.GRAVEL_ROAD_STAIRS);
            add(output, CompatRnRBlocks.GRAVEL_ROAD_SLAB);
            add(output, CompatRnRBlocks.OVER_HEIGHT_GRAVEL);
            add(output, CompatRnRBlocks.MACADAM_ROAD);
            add(output, CompatRnRBlocks.MACADAM_ROAD_STAIRS);
            add(output, CompatRnRBlocks.MACADAM_ROAD_SLAB);

        for (CompatRock rock : CompatRock.VALUES) {
            add(output, CompatRnRBlocks.ROCK_BLOCKS.get(rock).get(CompatRnR.FLAGSTONES));
            add(output, CompatRnRBlocks.ROCK_STAIRS.get(rock).get(CompatRnR.FLAGSTONES));
            add(output, CompatRnRBlocks.ROCK_SLABS.get(rock).get(CompatRnR.FLAGSTONES));
            add(output, CompatRnRBlocks.ROCK_BLOCKS.get(rock).get(CompatRnR.COBBLED_ROAD));
            add(output, CompatRnRBlocks.ROCK_STAIRS.get(rock).get(CompatRnR.COBBLED_ROAD));
            add(output, CompatRnRBlocks.ROCK_SLABS.get(rock).get(CompatRnR.COBBLED_ROAD));
            add(output, CompatRnRBlocks.ROCK_BLOCKS.get(rock).get(CompatRnR.SETT_ROAD));
            add(output, CompatRnRBlocks.ROCK_STAIRS.get(rock).get(CompatRnR.SETT_ROAD));
            add(output, CompatRnRBlocks.ROCK_SLABS.get(rock).get(CompatRnR.SETT_ROAD));
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
