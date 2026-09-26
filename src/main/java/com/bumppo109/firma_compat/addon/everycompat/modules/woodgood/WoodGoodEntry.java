package com.bumppo109.firma_compat.addon.everycompat.modules.woodgood;

import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnR;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRItems;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import net.minecraft.world.item.Item;

import java.util.Locale;
import java.util.function.Supplier;

public enum WoodGoodEntry {
    LUMBER(TfcAddon.VANILLA, ModItems.LUMBER.get(CompatWood.OAK)),
    SUPPORT(TfcAddon.VANILLA, ModItems.SUPPORTS.get(CompatWood.OAK)),
    TWIG(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.TWIG).get().asItem()),
    LOG_FENCE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.LOG_FENCE).get().asItem()),
    VERTICAL_SUPPORT(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.VERTICAL_SUPPORT).get().asItem()),
    HORIZONTAL_SUPPORT(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get().asItem()),
    TOOL_RACK(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.TOOL_RACK).get().asItem()),
    LOOM(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.LOOM).get().asItem()),
    SLUICE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.SLUICE).get().asItem()),
    BARREL(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.BARREL).get().asItem()),
    SCRIBING_TABLE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.SCRIBING_TABLE).get().asItem()),
    SEWING_TABLE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.SEWING_TABLE).get().asItem()),
    SHELF(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.SHELF).get().asItem()),
    AXLE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.AXLE).get().asItem()),
    BLADED_AXLE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.BLADED_AXLE).get().asItem()),
    ENCASED_AXLE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.ENCASED_AXLE).get().asItem()),
    CLUTCH(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.CLUTCH).get().asItem()),
    GEAR_BOX(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.GEAR_BOX).get().asItem()),
    WINDMILL(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.WINDMILL).get().asItem()),
    WATER_WHEEL(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.WATER_WHEEL).get().asItem()),
    CRATE(TfcAddon.VANILLA, () -> ModBlocks.WOODS.get(CompatWood.OAK).get(CompatWood.BlockType.CRATE).get().asItem()),

    KEG(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.KEGS.get(CompatWood.OAK).get().asItem()),
    FOOD_SHELF(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.FOOD_SHELVES.get(CompatWood.OAK).get().asItem()),
    HANGER(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.HANGERS.get(CompatWood.OAK).get().asItem()),
    JARBNET(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.JARBNETS.get(CompatWood.OAK).get().asItem()),
    WINE_SHELF(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.WINE_SHELVES.get(CompatWood.OAK).get().asItem()),
    STOMPING_BARREL(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.STOMPING_BARRELS.get(CompatWood.OAK).get().asItem()),
    BARREL_PRESS(TfcAddon.FIRMALIFE, () -> CompatFLBlocks.BARREL_PRESSES.get(CompatWood.OAK).get().asItem()),

    SHINGLE(TfcAddon.RNR, CompatRnRItems.SHINGLE.get(CompatWood.OAK)),
    SHINGLES(TfcAddon.RNR, () -> CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(CompatWood.OAK).get().asItem()),
    SHINGLES_STAIRS(TfcAddon.RNR, () -> CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(CompatWood.OAK).get().asItem()),
    SHINGLES_SLAB(TfcAddon.RNR, () -> CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(CompatWood.OAK).get().asItem())
    ;

private final String serializedName;
private final TfcAddon addon;
private final Supplier<Item> itemSupplier;

    WoodGoodEntry(TfcAddon addon, Supplier<Item> itemSupplier) {
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

    public Supplier<Item> oakItem() {
        return this.itemSupplier;
    }
    
    public enum TfcAddon {
        VANILLA,
        FIRMALIFE,
        RNR
    }
}
