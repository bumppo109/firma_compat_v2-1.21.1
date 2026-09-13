package com.bumppo109.firma_compat.addon.everycompat.modules.stonezone;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnR;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRBlocks;
import com.bumppo109.firma_compat.block.CompatRock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.therighthon.rnr.common.RNRTags;
import com.therighthon.rnr.common.block.PathHeightBlock;
import com.therighthon.rnr.common.block.PathSlabBlock;
import com.therighthon.rnr.common.block.PathStairBlock;
import com.therighthon.rnr.common.block.RNRBlocks;
import net.dries007.tfc.common.TFCTags;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.StoneZone;
import net.mehvahdjukaar.stone_zone.api.StoneZoneModule;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneChildKeys;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RnRStoneZoneModule extends StoneZoneModule {

    public final ItemOnlyEntrySet<StoneType, Item> FLAGSTONE;
    public final SimpleEntrySet<StoneType, Block> FLAGSTONES;
    public final SimpleEntrySet<StoneType, Block> FLAGSTONES_STAIRS;
    public final SimpleEntrySet<StoneType, Block> FLAGSTONES_SLAB;
    public final SimpleEntrySet<StoneType, Block> COBBLED_ROAD;
    public final SimpleEntrySet<StoneType, Block> COBBLED_ROAD_STAIRS;
    public final SimpleEntrySet<StoneType, Block> COBBLED_ROAD_SLAB;
    public final SimpleEntrySet<StoneType, Block> SETT_ROAD;
    public final SimpleEntrySet<StoneType, Block> SETT_ROAD_STAIRS;
    public final SimpleEntrySet<StoneType, Block> SETT_ROAD_SLAB;

    public RnRStoneZoneModule(String modId) {
        super(modId,"tfc");

        Supplier<CreativeModeTab> tab = getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        FLAGSTONE = ItemOnlyEntrySet.builder(StoneType.class, "flagstone",
                        getModItem("andesite_flagstone"), () -> VanillaStoneTypes.ANDESITE,
                        w -> new Item(new Item.Properties())
                )
                .addTexture(modRes("item/andesite_flagstone"), PaletteStrategies.MAIN_CHILD)
                .addTag(RNRTags.Items.FLAGSTONE_ROAD_ITEMS, Registries.ITEM)
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(FLAGSTONE);

        FLAGSTONES = SimpleEntrySet.builder(StoneType.class, "flagstones",
                        getModBlock("andesite_flagstones"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathHeightBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.FLAGSTONES).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.FLAGSTONE_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/andesite_flagstones"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(FLAGSTONES);

        FLAGSTONES_STAIRS = SimpleEntrySet.builder(StoneType.class, "flagstones_stairs",
                        getModBlock("andesite_flagstones_stairs"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathStairBlock(() -> FLAGSTONE.blocks.get(stoneType).defaultBlockState(),
                                Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.FLAGSTONES).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.FLAGSTONE_ROAD_STAIRS, Registries.BLOCK)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(FLAGSTONES_STAIRS);

        FLAGSTONES_SLAB = SimpleEntrySet.builder(StoneType.class, "flagstones_slab",
                        getModBlock("andesite_flagstones_slab"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathSlabBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.FLAGSTONES).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.FLAGSTONE_ROAD_SLABS, Registries.BLOCK)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(FLAGSTONES_SLAB);

        COBBLED_ROAD = SimpleEntrySet.builder(StoneType.class, "cobbled_road",
                        getModBlock("andesite_cobbled_road"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathHeightBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.COBBLED_ROAD).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.COBBLED_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/andesite_cobble"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD);

        COBBLED_ROAD_STAIRS = SimpleEntrySet.builder(StoneType.class, "cobbled_road_stairs",
                        getModBlock("andesite_cobbled_road_stairs"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathStairBlock(() -> FLAGSTONE.blocks.get(stoneType).defaultBlockState(),
                                Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.COBBLED_ROAD).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.COBBLED_ROAD_STAIRS, Registries.BLOCK)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD_STAIRS);

        COBBLED_ROAD_SLAB = SimpleEntrySet.builder(StoneType.class, "cobbled_road_slab",
                        getModBlock("andesite_cobbled_road_slab"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathSlabBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.COBBLED_ROAD).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.COBBLED_ROAD_SLABS, Registries.BLOCK)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD_SLAB);

        SETT_ROAD = SimpleEntrySet.builder(StoneType.class, "sett_road",
                        getModBlock("andesite_sett_road"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathHeightBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.SETT_ROAD).get()))
                )
                //.requiresChildren(VanillaStoneChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.SETT_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/andesite_sett"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(SETT_ROAD);

        SETT_ROAD_STAIRS = SimpleEntrySet.builder(StoneType.class, "sett_road_stairs",
                        getModBlock("andesite_sett_road_stairs"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathStairBlock(() -> FLAGSTONE.blocks.get(stoneType).defaultBlockState(),
                                Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.SETT_ROAD).get()))
                )
                //.requiresChildren(VanillaStoneChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.SETT_ROAD_STAIRS, Registries.BLOCK)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(SETT_ROAD_STAIRS);

        SETT_ROAD_SLAB = SimpleEntrySet.builder(StoneType.class, "sett_road_slab",
                        getModBlock("andesite_sett_road_slab"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new PathSlabBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnR.SETT_ROAD).get()))
                )
                //.requiresChildren(VanillaStoneChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.SUPPORTS_LANDSLIDE, Registries.BLOCK)
                .addTag(RNRTags.Blocks.SETT_ROAD_SLABS, Registries.BLOCK)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(SETT_ROAD_SLAB);
    }
}
