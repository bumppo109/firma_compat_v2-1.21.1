package com.bumppo109.firma_compat.addon.everycompat.modules.woodgood;

import com.bumppo109.firma_compat.FirmaCompat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.therighthon.rnr.common.block.RNRBlocks;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class RnRWoodGoodModule extends EveryCompatModule {

    public final ItemOnlyEntrySet<WoodType, Item> SHINGLE;
    public final SimpleEntrySet<WoodType, Block> SHINGLES;
    public final SimpleEntrySet<WoodType, Block> SHINGLES_STAIRS;
    public final SimpleEntrySet<WoodType, Block> SHINGLES_SLAB;

    public RnRWoodGoodModule(String modId) {
        super(modId, "tfc");

        Supplier<CreativeModeTab> tab = getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        SHINGLE = ItemOnlyEntrySet.builder(WoodType.class, "shingle",
                        getModItem("oak_shingle"), () -> VanillaWoodTypes.OAK,
                        w -> new Item(new Item.Properties())
                )
                .requiresChildren("log")
                .addTexture(modRes("item/oak_shingle"), PaletteStrategies.MAIN_CHILD)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLE);

        SHINGLES = SimpleEntrySet.builder(WoodType.class, "shingles",
                        getModBlock("oak_shingles"), () -> VanillaWoodTypes.OAK,
                        w -> new Block(Utils.copyPropertySafe(w.planks))
                )
                .requiresChildren("log")
                .addTexture(modRes("block/oak_shingles"), PaletteStrategies.MAIN_CHILD)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLES);

        SHINGLES_STAIRS = SimpleEntrySet.builder(WoodType.class, "shingles_stairs",
                        getModBlock("oak_shingles_stairs"), () -> VanillaWoodTypes.OAK,
                        w -> new StairBlock(SHINGLES.blocks.get(w).defaultBlockState(), Utils.copyPropertySafe(w.planks))
                )
                .requiresFromMap(SHINGLES.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLES_STAIRS);

        SHINGLES_SLAB = SimpleEntrySet.builder(WoodType.class, "shingles_slab",
                        getModBlock("oak_shingles_slab"), () -> VanillaWoodTypes.OAK,
                        w -> new SlabBlock(Utils.copyPropertySafe(w.planks))
                )
                .requiresFromMap(SHINGLES.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLES_SLAB);
    }
}
