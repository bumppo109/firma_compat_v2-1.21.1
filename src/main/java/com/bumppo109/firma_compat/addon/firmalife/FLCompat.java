package com.bumppo109.firma_compat.addon.firmalife;

import com.bumppo109.firma_compat.addon.ModCompat;
import com.bumppo109.firma_compat.addon.ModCompatHandler;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLItems;
import com.bumppo109.firma_compat.block.CompatWood;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class FLCompat {
    private FLCompat() {}

    public static void register(IEventBus modEventBus) {
        CompatFLBlocks.BLOCKS.register(modEventBus);
        CompatFLItems.ITEMS.register(modEventBus);
    }

    public static void addFLItems(CreativeModeTab.Output output) {
        for (CompatWood wood : CompatWood.VALUES) {

            add(output, CompatFLBlocks.FOOD_SHELVES.get(wood));
            add(output, CompatFLBlocks.HANGERS.get(wood));
            add(output, CompatFLBlocks.JARBNETS.get(wood));
            add(output, CompatFLBlocks.KEGS.get(wood));
            add(output, CompatFLBlocks.STOMPING_BARRELS.get(wood));
            add(output, CompatFLBlocks.BARREL_PRESSES.get(wood));
            add(output, CompatFLBlocks.WINE_SHELVES.get(wood));
        }

        CompatFLBlocks.CHROMITE_ORES.forEach((rock, gradeIdMap) -> {
            gradeIdMap.forEach((grade, blockId) -> {
                add(output, blockId);
            });
        });
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
