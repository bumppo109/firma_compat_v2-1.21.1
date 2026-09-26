package com.bumppo109.firma_compat.addon.everycompat.modules.stonezone;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnR;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRItems;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.materials.RockMaterial;
import com.therighthon.rnr.common.item.RNRItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Locale;
import java.util.function.Supplier;

public enum StoneZoneEntry {
    BRICK(TfcAddon.VANILLA, ModItems.BRICK.get(CompatRock.ANDESITE)),
    COBBLE(TfcAddon.VANILLA, () -> ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.COBBLE).get().asItem()),
    MOSSY_COBBLE(TfcAddon.VANILLA, () -> ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.MOSSY_COBBLE).get().asItem()),
    COBBLESTONE(TfcAddon.VANILLA, () -> ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.COBBLESTONE).get().asItem()),
    MOSSY_COBBLESTONE(TfcAddon.VANILLA, () -> ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.MOSSY_COBBLESTONE).get().asItem()),

    FLAGSTONE(TfcAddon.RNR, CompatRnRItems.FLAGSTONE.get(CompatRock.ANDESITE)),
    FLAGSTONES(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRnR.FLAGSTONES).get().asItem()),
    FLAGSTONES_STAIRS(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_STAIRS.get(CompatRock.ANDESITE).get(CompatRnR.FLAGSTONES).get().asItem()),
    FLAGSTONES_SLAB(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_SLABS.get(CompatRock.ANDESITE).get(CompatRnR.FLAGSTONES).get().asItem()),
    SETT_ROAD(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRnR.SETT_ROAD).get().asItem()),
    SETT_ROAD_STAIRS(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_STAIRS.get(CompatRock.ANDESITE).get(CompatRnR.SETT_ROAD).get().asItem()),
    SETT_ROAD_SLAB(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_SLABS.get(CompatRock.ANDESITE).get(CompatRnR.SETT_ROAD).get().asItem()),
    COBBLED_ROAD(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRnR.COBBLED_ROAD).get().asItem()),
    COBBLED_ROAD_STAIRS(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_STAIRS.get(CompatRock.ANDESITE).get(CompatRnR.COBBLED_ROAD).get().asItem()),
    COBBLED_ROAD_SLAB(TfcAddon.RNR, () -> CompatRnRBlocks.ROCK_SLABS.get(CompatRock.ANDESITE).get(CompatRnR.COBBLED_ROAD).get().asItem()),
    ;

private final String serializedName;
private final TfcAddon addon;
private final Supplier<Item> itemSupplier;

    StoneZoneEntry(TfcAddon addon, Supplier<Item> itemSupplier) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.addon = addon;
        this.itemSupplier = itemSupplier;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public boolean isVanilla() {
        return this.addon.equals(TfcAddon.VANILLA);
    }
    public boolean isFirmaLife() {
        return this.addon.equals(TfcAddon.FIRMALIFE);
    }
    public boolean isRnR() {
        return this.addon.equals(TfcAddon.RNR);
    }

    public ResourceLocation stoneRecipe() {
        return switch (this) {
            case FLAGSTONES, COBBLED_ROAD, SETT_ROAD -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block_mod/andesite_" + this.getSerializedName());
            case FLAGSTONES_STAIRS, FLAGSTONES_SLAB,
                 COBBLED_ROAD_STAIRS, COBBLED_ROAD_SLAB,
                 SETT_ROAD_STAIRS, SETT_ROAD_SLAB -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"mattock/andesite_" + this.getSerializedName());
            case MOSSY_COBBLESTONE -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"crafting/mossy_andesite_cobblestone");
            case MOSSY_COBBLE -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"crafting/mossy_andesite_cobble");
            default -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"crafting/andesite_" + this.getSerializedName());
        };
    }

    public Supplier<Item> stoneItem() {
        return this.itemSupplier;
    }

    public enum TfcAddon {
        VANILLA,
        FIRMALIFE,
        RNR
    }
}
