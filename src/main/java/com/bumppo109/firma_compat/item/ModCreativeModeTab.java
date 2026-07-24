package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
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
    }

    private static void addWood(CreativeModeTab.Output output) {

        for (CompatWood wood : CompatWood.VALUES) {

            add(output, ModItems.LUMBER.get(wood));

            var blocks = ModBlocks.WOODS.get(wood);

            if (blocks == null) {
                continue;
            }

            for (CompatWood.BlockType type : CompatWood.BlockType.values()) {

                if (!type.needsItem()) {
                    continue;
                }

                add(output, blocks.get(type));
            }
        }
    }

    private static void addRocks(CreativeModeTab.Output output) {

        for (CompatRock rock : CompatRock.VALUES) {

            var blocks = ModBlocks.ROCK_BLOCKS.get(rock);

            if (blocks == null) {
                continue;
            }

            for (CompatRock.BlockType type : CompatRock.BlockType.values()) {

                if (!type.needsItem()) {
                    continue;
                }

                add(output, blocks.get(type));
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