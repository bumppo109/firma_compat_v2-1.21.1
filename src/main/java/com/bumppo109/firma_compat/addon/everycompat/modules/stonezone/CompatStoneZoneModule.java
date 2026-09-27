package com.bumppo109.firma_compat.addon.everycompat.modules.stonezone;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.world.CompatVein;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.RockRopeAnchorBlock;
import net.dries007.tfc.common.blocks.RopeAnchorBlock;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.StoneZone;
import net.mehvahdjukaar.stone_zone.api.StoneZoneEntrySet;
import net.mehvahdjukaar.stone_zone.api.StoneZoneModule;
import net.mehvahdjukaar.stone_zone.api.set.VanillaRockChildKeys;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.mehvahdjukaar.every_compat.misc.UtilityTag.createAndAddCustomTags;
import static net.mehvahdjukaar.every_compat.misc.UtilityTag.getATagOrCreateANew;

public class CompatStoneZoneModule extends StoneZoneModule {

    public final SimpleEntrySet<StoneType, Block> COBBLE, MOSSY_COBBLE, COBBLESTONE, MOSSY_COBBLESTONE, HARDENED;
    public final SimpleEntrySet<StoneType, Block> LOOSE, MOSSY_LOOSE;
    public SimpleEntrySet<StoneType, Block> ROPE_ANCHOR;
    public SimpleEntrySet<StoneType, Block> SPIKE;
    public final ItemOnlyEntrySet<StoneType, Item> BRICK;
    public final SimpleEntrySet<StoneType, Block> AQUEDUCT;

    public final Map<String, SimpleEntrySet<StoneType, Block>> ORE_ENTRY_SETS = new HashMap<>();

    public CompatStoneZoneModule(String modId) {
        super(modId, "tfc");

        Supplier<CreativeModeTab> tab =
                getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        HARDENED = StoneZoneEntrySet.of(
                        StoneType.class,
                        "",
                        "hardened",
                        getModBlock("hardened_stone", Block.class),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/hardened"),
                        Registries.BLOCK
                )
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/hardened"),
                        Registries.ITEM
                )
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(HARDENED);

        ROPE_ANCHOR = StoneZoneEntrySet.of(
                        StoneType.class,
                        "rope_anchor",
                        () -> getModBlock("stone_rope_anchor").get(),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new RockRopeAnchorBlock(
                                ExtendedProperties.of(Utils.copyPropertySafe(stoneType.stone)),
                                () -> (RockSpikeBlock) SPIKE.blocks.get(stoneType)
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .noItem()
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(ROPE_ANCHOR);

        SPIKE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "spike",
                        () -> getModBlock("stone_spike").get(),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new RockSpikeBlock(
                                Utils.copyPropertySafe(stoneType.stone)
                                        .lightLevel(ModBlocks.lavaLoggedBlockEmission()),
                                () -> (RopeAnchorBlock) ROPE_ANCHOR.blocks.get(stoneType)
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/spike"),
                        Registries.BLOCK
                )
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(SPIKE);

        LOOSE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "",
                        "loose",
                        getModBlock("loose_stone", Block.class),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(
                                BlockBehaviour.Properties.of()
                                        .strength(0.05f, 0.0f)
                                        .noCollission()
                        )
                )
                .addTexture(modRes("item/loose_stone"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/loose"),
                        Registries.BLOCK
                )
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/loose"),
                        Registries.ITEM
                )
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("tfc", "stones/loose/metamorphic"),
                        Registries.ITEM
                )
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(LOOSE);

        MOSSY_LOOSE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "",
                        "mossy_loose",
                        getModBlock("mossy_loose_stone", Block.class),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(
                                BlockBehaviour.Properties.of()
                                        .strength(0.05f, 0.0f)
                                        .noCollission()
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTextureM(
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/mossy_stone"
                        ),
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/mossy_raw_mask"
                        )
                )
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/loose"),
                        Registries.BLOCK
                )
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("c", "stones/loose"),
                        Registries.ITEM
                )
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("tfc", "stones/loose/metamorphic"),
                        Registries.ITEM
                )
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_LOOSE);

        BRICK = ItemOnlyEntrySet.builder(
                        StoneType.class,
                        "brick",
                        getModItem("stone_brick"),
                        () -> VanillaStoneTypes.STONE,
                        w -> new Item(new Item.Properties())
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(modRes("item/stone_brick"))
                .addTag(
                        ResourceLocation.fromNamespaceAndPath("rnr", "sett_road_items"),
                        Registries.ITEM
                )
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(BRICK);

        COBBLE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "cobble",
                        getModBlock("stone_cobble", Block.class),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/stone_cobble"
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(COBBLE);

        MOSSY_COBBLE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "cobble",
                        "mossy",
                        getModBlock("mossy_stone_cobble", Block.class),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTextureM(
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/mossy_stone_cobble"
                        ),
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/mossy_cobble_mask"
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_COBBLE);

        COBBLESTONE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "cobblestone",
                        getModBlock("andesite_cobblestone", Block.class),
                        () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(COBBLESTONE);

        MOSSY_COBBLESTONE = StoneZoneEntrySet.of(
                        StoneType.class,
                        "cobblestone",
                        "mossy",
                        getModBlock("mossy_andesite_cobblestone", Block.class),
                        () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTextureM(
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/mossy_andesite_cobble"
                        ),
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/mossy_cobble_mask"
                        )
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_COBBLESTONE);

        AQUEDUCT = StoneZoneEntrySet.of(
                        StoneType.class,
                        "brick_aqueduct",
                        getModBlock("stone_brick_aqueduct"),
                        () -> VanillaStoneTypes.STONE,
                        stoneType -> new AqueductBlock(
                                Utils.copyPropertySafe(stoneType.block)
                        )
                )
                .requiresChildren(VanillaRockChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.AQUEDUCTS, Registries.BLOCK)
                .addRecipe(modRes("crafting/stone_brick_aqueduct"))
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .excludeBlockTypes("nomansland:silt")
                .setTab(tab)
                .build();
        this.addEntry(AQUEDUCT);

        registerOreEntrySets(tab);
    }

    private void registerOreEntrySets(Supplier<CreativeModeTab> tab) {
        // Ungraded ores.
        for (Ore ore : Ore.values()) {
            if (ore.isGraded() || !ore.hasBlock()) {
                continue;
            }

            if (ModBlocks.skippedOre(ore)) {
                continue;
            }

            String oreName = ore.name().toLowerCase(Locale.ROOT);

            SimpleEntrySet<StoneType, Block> ungradedSet =
                    StoneZoneEntrySet.of(
                                    StoneType.class,
                                    oreName + "_ore",
                                    getModBlock("stone_" + oreName + "_ore"),
                                    () -> VanillaStoneTypes.STONE,
                                    stoneType -> new Block(
                                            Utils.copyPropertySafe(stoneType.block)
                                    )
                            )
                            .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                            .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                            .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                            .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                            .copyParentDrop()
                            .setRenderType(RenderLayer.CUTOUT)
                            .setTab(tab)
                            .excludeBlockTypes("tfc:.*")
                            .excludeBlockTypes("nomansland:silt")
                            .build();

            this.addEntry(ungradedSet);
            ORE_ENTRY_SETS.put("ungraded_" + oreName, ungradedSet);
        }

        // Graded ores.
        for (Ore ore : Ore.values()) {
            if (!ore.isGraded() || !ore.hasBlock()) {
                continue;
            }

            if (ModBlocks.skippedOre(ore)) {
                continue;
            }

            String oreName = ore.name().toLowerCase(Locale.ROOT);

            for (Ore.Grade grade : Ore.Grade.values()) {
                String gradeName = grade.name().toLowerCase(Locale.ROOT);

                SimpleEntrySet<StoneType, Block> gradedSet =
                        StoneZoneEntrySet.of(
                                        StoneType.class,
                                        oreName + "_ore",
                                        gradeName,
                                        getModBlock(
                                                gradeName
                                                        + "_stone_"
                                                        + oreName
                                                        + "_ore"
                                        ),
                                        () -> VanillaStoneTypes.STONE,
                                        stoneType -> new Block(
                                                Utils.copyPropertySafe(stoneType.block)
                                        )
                                )
                                .addTag(
                                        BlockTags.MINEABLE_WITH_PICKAXE,
                                        Registries.BLOCK
                                )
                                .addTag(
                                        TFCTags.Blocks.CAN_COLLAPSE,
                                        Registries.BLOCK
                                )
                                .addTag(
                                        TFCTags.Blocks.CAN_START_COLLAPSE,
                                        Registries.BLOCK
                                )
                                .addTag(
                                        TFCTags.Blocks.CAN_TRIGGER_COLLAPSE,
                                        Registries.BLOCK
                                )
                                .copyParentDrop()
                                .setRenderType(RenderLayer.CUTOUT)
                                .setTab(tab)
                                .excludeBlockTypes("tfc:.*")
                                .excludeBlockTypes("nomansland:silt")
                                .build();

                this.addEntry(gradedSet);
                ORE_ENTRY_SETS.put(
                        gradeName + "_" + oreName,
                        gradedSet
                );
            }
        }
    }

    @Override
    public void addDynamicClientResources(
            Consumer<ResourceGenTask> executor
    ) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {
            LOOSE.blocks.forEach((stoneType, block) -> {
                if (stoneType == null) {
                    return;
                }

                ResourceLocation rawResLoc =
                        BuiltInRegistries.BLOCK.getKey(stoneType.block);

                ResourceLocation looseResLoc =
                        BuiltInRegistries.BLOCK.getKey(
                                LOOSE.blocks.get(stoneType)
                        );

                ResourceLocation mossyLooseResLoc =
                        BuiltInRegistries.BLOCK.getKey(
                                MOSSY_LOOSE.blocks.get(stoneType)
                        );

                String loosePath = looseResLoc.getPath();
                String mossyPath = mossyLooseResLoc.getPath();
                String rawNamespace = rawResLoc.getNamespace();
                String rawPath = rawResLoc.getPath();

                ResourceLocation looseTargetLoc =
                        ResourceLocation.fromNamespaceAndPath(
                                "tfc",
                                "gui/knapping/" + loosePath
                        );

                ResourceLocation mossyLooseTargetLoc =
                        ResourceLocation.fromNamespaceAndPath(
                                "tfc",
                                "gui/knapping/" + mossyPath
                        );

                ResourceLocation rawSource =
                        ResourceLocation.fromNamespaceAndPath(
                                rawNamespace,
                                "block/" + rawPath + ".png"
                        );

                try (TextureImage rawTexture =
                             TextureImage.open(manager, rawSource)) {

                    sink.addTextureIfNotPresent(
                            manager,
                            looseTargetLoc,
                            () -> rawTexture
                    );
                } catch (IOException e) {
                    FirmaCompat.LOGGER.error(
                            "Failed to copy knapping texture for {} from {} : {}",
                            rawResLoc,
                            rawSource,
                            e.getMessage()
                    );
                }

                try (TextureImage rawTexture =
                             TextureImage.open(manager, rawSource)) {

                    sink.addTextureIfNotPresent(
                            manager,
                            mossyLooseTargetLoc,
                            () -> rawTexture
                    );
                } catch (IOException e) {
                    FirmaCompat.LOGGER.error(
                            "Failed to copy knapping texture for {} from {} : {}",
                            rawResLoc,
                            rawSource,
                            e.getMessage()
                    );
                }
            });
        });
    }

    @Override
    public void addDynamicServerResources(
            Consumer<ResourceGenTask> executor
    ) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {

            /*
             * -------------------------------------------------------------
             * StoneZone recipes / tags
             * -------------------------------------------------------------
             */
            for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
                if (stoneType.getNamespace().equals("minecraft")
                        || stoneType.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation rockTag =
                        ResourceLocation.fromNamespaceAndPath(
                                stoneType.getNamespace(),
                                "stone_type/" + stoneType.getTypeName()
                        );

                UtilityTag.createAndAddCustomTags(
                        rockTag,
                        sink,
                        stoneType.stone
                );

                ResourceLocation looseRes =
                        Utils.getID(LOOSE.items.get(stoneType));

                ResourceLocation mossyLooseRes =
                        Utils.getID(MOSSY_LOOSE.items.get(stoneType));

                ResourceLocation brickRes =
                        Utils.getID(BRICK.items.get(stoneType));

                for (StoneZoneEntry entry : StoneZoneEntry.values()) {
                    if (!entry.isVanilla()) {
                        continue;
                    }

                    try {
                        StaticResource recipeTemplate =
                                StaticResource.getOrThrow(
                                        manager,
                                        ResType.RECIPES.getPath(
                                                entry.stoneRecipe()
                                        )
                                );

                        sink.addSimilarJsonResource(
                                manager,
                                recipeTemplate,
                                text -> text
                                        .replace(
                                                "firma_compat:mossy_loose_andesite",
                                                mossyLooseRes.toString()
                                        )
                                        .replace(
                                                "firma_compat:loose_andesite",
                                                looseRes.toString()
                                        )
                                        .replace(
                                                "firma_compat:andesite_brick",
                                                brickRes.toString()
                                        )
                                        .replace(
                                                "firma_compat:andesite_"
                                                        + entry.getSerializedName(),
                                                "stonezone:tfc/"
                                                        + stoneType.getNamespace()
                                                        + "/"
                                                        + stoneType.getTypeName()
                                                        + "_"
                                                        + entry.getSerializedName()
                                        ),
                                path -> replacePathName(
                                        path,
                                        entry,
                                        stoneType
                                )
                        );
                    } catch (Exception e) {
                        FirmaCompat.LOGGER.debug(
                                "Failed to grab recipe for {}",
                                entry.stoneRecipe()
                        );
                    }
                }
            }

            /*
             * -------------------------------------------------------------
             * TFC vein JSON generation
             * -------------------------------------------------------------
             *
             * Every CompatVein becomes:
             *
             * worldgen/configured_feature/vein/<vein>.json
             */
            for (CompatVein vein : CompatVein.values()) {
                writeVeinJson(sink, vein);
                writePlacedVeinJson(sink, vein);
            }
            writeCompatVeinTag(sink);

            /*
            Loose Rock Feature & Tag
             */
            JsonObject loosePlacedTag = new JsonObject();
            JsonArray looseValues = new JsonArray();

            for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
                if (stoneType.getNamespace().equals("minecraft") || stoneType.getNamespace().equals("tfc")) continue;

                ResourceLocation looseRes = Utils.getID(LOOSE.blocks.get(stoneType));
                ResourceLocation rawRes = BuiltInRegistries.BLOCK.getKey(stoneType.block);

                StaticResource loosePatchPlace = StaticResource.getOrThrow(manager,
                        ResType.GENERIC.getPath(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"worldgen/placed_feature/loose_stone_patch.json")));
                StaticResource loosePatch = StaticResource.getOrThrow(manager,
                        ResType.GENERIC.getPath(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"worldgen/configured_feature/loose_stone_patch.json")));
                StaticResource loosePlace = StaticResource.getOrThrow(manager,
                        ResType.GENERIC.getPath(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"worldgen/placed_feature/loose_stone.json")));
                StaticResource loose = StaticResource.getOrThrow(manager,
                        ResType.GENERIC.getPath(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"worldgen/configured_feature/loose_stone.json")));

                sink.addSimilarJsonResource(manager, loosePatchPlace,
                        text -> text
                                .replace("\"feature\": \"firma_compat:loose_stone_patch\"", "\"feature\": \"everycomp:" + stoneType.getNamespace() + "/loose_" + stoneType.getTypeName() + "_patch\"")
                                .replace("firma_compat:loose_stone", looseRes.toString())
                                .replace("minecraft:stone", rawRes.toString()),
                        path -> path.replace("loose_stone",stoneType.getNamespace() + "/loose_" + stoneType.getTypeName())
                );
                sink.addSimilarJsonResource(manager, loosePatch,
                        text -> text
                                .replace("\"feature\": \"firma_compat:loose_stone\"", "\"feature\": \"everycomp:" + stoneType.getNamespace() + "/loose_" + stoneType.getTypeName() + "\"")
                                .replace("firma_compat:loose_stone", looseRes.toString()),
                        path -> path.replace("loose_stone",stoneType.getNamespace() + "/loose_" + stoneType.getTypeName())
                );
                sink.addSimilarJsonResource(manager, loosePlace,
                        text -> text
                                .replace("\"feature\": \"firma_compat:loose_stone\"", "\"feature\": \"everycomp:" + stoneType.getNamespace() + "/loose_" + stoneType.getTypeName() + "\"")
                                .replace("firma_compat:loose_stone", looseRes.toString()),
                        path -> path.replace("loose_stone",stoneType.getNamespace() + "/loose_" + stoneType.getTypeName())
                );
                sink.addSimilarJsonResource(manager, loose,
                        text -> text
                                .replace("firma_compat:loose_stone", looseRes.toString()),
                        path -> path.replace("loose_stone",stoneType.getNamespace() + "/loose_" + stoneType.getTypeName())
                );
                //populate tag contents
                    JsonObject entry = new JsonObject();

                    entry.addProperty(
                            "id",
                            "everycomp:" + stoneType.getNamespace() + "/loose_" + stoneType.getTypeName() + "_patch"
                    );

                    entry.addProperty(
                            "required",
                            false
                    );

                looseValues.add(entry);
            }

            //Finalize Loose Rock Placed Feature Tag
            loosePlacedTag.add("values", looseValues);

            ResourceLocation loosePlacedTagOut =
                    ResourceLocation.fromNamespaceAndPath(
                            FirmaCompat.MODID,
                            "worldgen/placed_feature/stonezone_loose_rocks"
                    );

            sink.addJson(
                    loosePlacedTagOut,
                    loosePlacedTag,
                    ResType.TAGS
            );

            //Hardened Replacement Map
            JsonObject hardenedMap = new JsonObject();
            JsonObject hardPair = new JsonObject();

            for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
                if (stoneType.getNamespace().equals("minecraft") || stoneType.getNamespace().equals("tfc")) continue;

                ResourceLocation rawRes = BuiltInRegistries.BLOCK.getKey(stoneType.block);
                ResourceLocation hardRes = Utils.getID(HARDENED.blocks.get(stoneType));

                hardPair.addProperty(rawRes.toString(), hardRes.toString());
            }

            for (CompatRock rock : CompatRock.values()) {
                ResourceLocation baseRes = BuiltInRegistries.BLOCK.getKey(rock.base());
                ResourceLocation rockHard = BuiltInRegistries.BLOCK.getKey(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.HARDENED).get());

                hardPair.addProperty(baseRes.toString(), rockHard.toString());
            }

            hardenedMap.addProperty("replace",true);
            hardenedMap.add("values", hardPair);

            ResourceLocation hardenedMapLoc =
                    ResourceLocation.fromNamespaceAndPath(
                            FirmaCompat.MODID,
                            "data_maps/block/worldgen/hardened_rock_replacement.json"
                    );

            sink.addJson(
                    hardenedMapLoc,
                    hardenedMap,
                    ResType.GENERIC
            );
        });
    }

    private String replacePathName(
            String path,
            StoneZoneEntry entry,
            StoneType stoneType
    ) {
        String mossyReplacement =
                path.replace(
                        "mossy_andesite",
                        stoneType.getNamespace()
                                + "/mossy_"
                                + stoneType.getTypeName()
                );

        String replacement =
                path.replace(
                        "andesite",
                        stoneType.getNamespace()
                                + "/"
                                + stoneType.getTypeName()
                );

        return entry.name().startsWith("MOSSY")
                ? mossyReplacement
                : replacement;
    }

    /**
     * Writes one complete TFC vein configured-feature JSON.
     */
    private void writeVeinJson(
            ResourceSink sink,
            CompatVein vein
    ) {
        String name = vein.name().toLowerCase(Locale.ROOT);

        JsonObject root = createVeinJson(vein);

        ResourceLocation output =
                ResourceLocation.fromNamespaceAndPath(
                        StoneZone.MOD_ID,
                        "worldgen/configured_feature/vein/" + name + ".json"
                );

        sink.addJson(output, root, ResType.GENERIC);
    }

    /**
     * Writes the placed feature that references the generated configured vein.
     *
     * Example:
     *
     * {
     *   "feature": "stonezone:vein/amethyst",
     *   "placement": []
     * }
     */
    private void writePlacedVeinJson(
            ResourceSink sink,
            CompatVein vein
    ) {
        String name = vein.name().toLowerCase(Locale.ROOT);

        JsonObject root = new JsonObject();

        root.addProperty(
                "feature",
                StoneZone.MOD_ID + ":vein/" + name
        );

        root.add(
                "placement",
                new JsonArray()
        );

        ResourceLocation output =
                ResourceLocation.fromNamespaceAndPath(
                        StoneZone.MOD_ID,
                        "worldgen/placed_feature/vein/" + name + ".json"
                );

        sink.addJson(output, root, ResType.GENERIC);
    }

    /**
     * Generates the StoneZone configured-feature tag containing every
     * CompatVein generated by this module.
     */
    private void writeCompatVeinTag(
            ResourceSink sink
    ) {
        JsonObject tag = new JsonObject();

        tag.addProperty("replace", false);

        JsonArray values = new JsonArray();

        for (CompatVein vein : CompatVein.values()) {
            if (vein.isCopper() || vein.isIron()) continue;

            String name = vein.name().toLowerCase(Locale.ROOT);

            JsonObject entry = new JsonObject();

            entry.addProperty(
                    "id",
                    StoneZone.MOD_ID + ":vein/" + name
            );

            entry.addProperty(
                    "required",
                    false
            );

            values.add(entry);
        }

        tag.add("values", values);

        ResourceLocation output =
                ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "worldgen/placed_feature/stonezone_veins"
                );

        sink.addJson(
                output,
                tag,
                ResType.TAGS
        );
    }


    /**
     * Builds the complete configured-feature JSON for one vein.
     */
    private JsonObject createVeinJson(CompatVein vein) {
        JsonObject root = new JsonObject();

        root.addProperty(
                "type",
                getVeinTypeId(vein.veinType)
        );

        JsonObject config = new JsonObject();

        addCommonVeinConfig(config, vein);

        JsonArray blocks = buildReplacementRules(vein);
        config.add("blocks", blocks);

        if (vein.gradedVeinClass != null) {
            JsonObject indicator = buildIndicatorJson(vein);
            if (indicator != null) {
                config.add("indicator", indicator);
            }
        }

        root.add("config", config);

        return root;
    }

    /**
     * Adds the fields shared by all TFC vein types.
     */
    private void addCommonVeinConfig(
            JsonObject config,
            CompatVein vein
    ) {
        config.addProperty("rarity", vein.rarity);
        config.addProperty("density", vein.density);
        config.addProperty("min_y", vein.minY);
        config.addProperty("max_y", vein.maxY);

        /*
         * Stable per-vein random name.
         *
         * TFC uses this to give each configured feature its own random stream.
         */
        config.addProperty(
                "random_name",
                vein.name().toLowerCase(Locale.ROOT)
        );

        /*
         * These are the standard TFC vein defaults.
         */
        config.addProperty("project", false);
        config.addProperty("project_offset", false);
        config.addProperty("near_lava", false);

        switch (vein.veinType) {
            case DISC -> {
                if (vein.size != null) {
                    config.addProperty("size", vein.size);
                }

                if (vein.height != null) {
                    config.addProperty("height", vein.height);
                }
            }

            case CLUSTER -> {
                if (vein.size != null) {
                    config.addProperty("size", vein.size);
                }
            }

            case PIPE -> {
                if (vein.minSkew != null) {
                    config.addProperty("min_skew", vein.minSkew);
                }

                if (vein.maxSkew != null) {
                    config.addProperty("max_skew", vein.maxSkew);
                }

                if (vein.minSlant != null) {
                    config.addProperty("min_slant", vein.minSlant);
                }

                if (vein.maxSlant != null) {
                    config.addProperty("max_slant", vein.maxSlant);
                }

                if (vein.sign != null) {
                    config.addProperty("sign", vein.sign);
                }

                if (vein.pipeHeight != null) {
                    config.addProperty("height", vein.pipeHeight);
                }

                if (vein.radius != null) {
                    config.addProperty("radius", vein.radius);
                }
            }
        }
    }

    private String getVeinTypeId(
            CompatVein.VeinType type
    ) {
        return switch (type) {
            case DISC -> "tfc:disc_vein";
            case CLUSTER -> "tfc:cluster_vein";
            case PIPE -> "tfc:pipe_vein";
        };
    }

    /**
     * Builds the indicator section for graded veins.
     *
     * The indicator block itself is the small ore block supplied by
     * CompatVein.indicator.
     */
    private JsonObject buildIndicatorJson(
            CompatVein vein
    ) {
        if (vein.indicator == null) {
            return null;
        }

        if (vein.indicatorRarity == null
                || vein.indicatorDepth == null
                || vein.indicatorUnderRarity == null
                || vein.indicatorCount == null) {
            return null;
        }

        JsonObject indicator = new JsonObject();

        indicator.addProperty(
                "rarity",
                vein.indicatorRarity
        );

        indicator.addProperty(
                "depth",
                vein.indicatorDepth
        );

        indicator.addProperty(
                "underground_rarity",
                vein.indicatorUnderRarity
        );

        indicator.addProperty(
                "underground_count",
                vein.indicatorCount
        );

        JsonArray blocks = new JsonArray();

        JsonObject block = new JsonObject();
        block.addProperty(
                "block",
                BuiltInRegistries.BLOCK
                        .getKey(vein.indicator)
                        .toString()
        );
        block.addProperty("weight", 1);

        blocks.add(block);

        indicator.add("blocks", blocks);

        return indicator;
    }

    /**
     * Builds every block replacement rule for a vein.
     *
     * StoneZone rocks are generated first.
     * CompatRock/TFC rocks are generated second.
     */
    private JsonArray buildReplacementRules(
            CompatVein vein
    ) {
        JsonArray rules = new JsonArray();

        addStoneZoneReplacementRules(rules, vein);
        addCompatRockReplacementRules(rules, vein);

        return rules;
    }

    /**
     * Adds replacements for all non-Minecraft/non-TFC StoneZone rocks.
     */
    private void addStoneZoneReplacementRules(
            JsonArray rules,
            CompatVein vein
    ) {
        List<StoneType> stoneTypes = new ArrayList<>();

        for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
            if (stoneType == null) {
                continue;
            }

            String namespace = stoneType.getNamespace();

            /*
             * Minecraft/TFC rocks are represented by CompatRock.
             */
            if (namespace.equals("minecraft")
                    || namespace.equals("tfc")) {
                continue;
            }

            stoneTypes.add(stoneType);
        }

        /*
         * Never rely on registry iteration order for generated resources.
         */
        stoneTypes.sort(
                Comparator.comparing(
                        stoneType -> stoneType.getNamespace()
                                + ":"
                                + stoneType.getTypeName()
                )
        );

        for (StoneType stoneType : stoneTypes) {
            addStoneZoneReplacementRule(
                    rules,
                    vein,
                    stoneType
            );
        }
    }

    private void addStoneZoneReplacementRule(
            JsonArray rules,
            CompatVein vein,
            StoneType stoneType
    ) {
        String oreName =
                vein.ore.name().toLowerCase(Locale.ROOT);

        ResourceLocation target =
                BuiltInRegistries.BLOCK.getKey(
                        stoneType.block
                );

        /*
         * Ungraded ore:
         *
         * stone -> stone_ore
         */
        if (!vein.ore.isGraded()) {
            SimpleEntrySet<StoneType, Block> entrySet =
                    ORE_ENTRY_SETS.get(
                            "ungraded_" + oreName
                    );

            if (entrySet == null) {
                return;
            }

            Block oreBlock =
                    entrySet.blocks.get(stoneType);

            if (isInvalidBlock(oreBlock)) {
                return;
            }

            rules.add(
                    createDirectReplacementRule(
                            target,
                            oreBlock
                    )
            );

            return;
        }

        /*
         * Graded ore:
         *
         * stone -> poor / normal / rich
         */
        SimpleEntrySet<StoneType, Block> poorSet =
                ORE_ENTRY_SETS.get(
                        "poor_" + oreName
                );

        SimpleEntrySet<StoneType, Block> normalSet =
                ORE_ENTRY_SETS.get(
                        "normal_" + oreName
                );

        SimpleEntrySet<StoneType, Block> richSet =
                ORE_ENTRY_SETS.get(
                        "rich_" + oreName
                );

        if (poorSet == null
                || normalSet == null
                || richSet == null) {
            return;
        }

        Block poor = poorSet.blocks.get(stoneType);
        Block normal = normalSet.blocks.get(stoneType);
        Block rich = richSet.blocks.get(stoneType);

        if (isInvalidBlock(poor)
                || isInvalidBlock(normal)
                || isInvalidBlock(rich)) {
            return;
        }

        rules.add(
                createWeightedReplacementRule(
                        target,
                        getGradedWeights(
                                vein,
                                poor,
                                normal,
                                rich
                        )
                )
        );
    }

    /**
     * Adds replacements for every CompatRock.
     *
     * CompatRock covers TFC rocks and the compatibility rocks represented
     * by this mod.
     */
    private void addCompatRockReplacementRules(
            JsonArray rules,
            CompatVein vein
    ) {
        for (CompatRock rock : CompatRock.VALUES) {
            if (rock == null
                    || rock.rockMaterial() == null) {
                continue;
            }

            Block target =
                    rock.rockMaterial()
                            .raw()
                            .base()
                            .get();

            if (isInvalidBlock(target)) {
                continue;
            }

            if (vein.ore.isGraded()) {
                addGradedCompatRockRule(
                        rules,
                        vein,
                        target,
                        rock
                );
            } else {
                addUngradedCompatRockRule(
                        rules,
                        vein,
                        target,
                        rock
                );
            }
        }
    }

    private void addUngradedCompatRockRule(
            JsonArray rules,
            CompatVein vein,
            Block target,
            CompatRock rock
    ) {
        var oreMap = ModBlocks.ORES.get(rock);

        if (oreMap == null) {
            return;
        }

        var oreId = oreMap.get(vein.ore);

        if (oreId == null) {
            return;
        }

        Block oreBlock = oreId.get();

        if (isInvalidBlock(oreBlock)) {
            return;
        }

        rules.add(
                createDirectReplacementRule(
                        BuiltInRegistries.BLOCK.getKey(target),
                        oreBlock
                )
        );
    }

    private void addGradedCompatRockRule(
            JsonArray rules,
            CompatVein vein,
            Block target,
            CompatRock rock
    ) {
        var oreMap = ModBlocks.GRADED_ORES.get(rock);

        if (oreMap == null) {
            return;
        }

        var gradeMap = oreMap.get(vein.ore);

        if (gradeMap == null) {
            return;
        }

        var poorId =
                gradeMap.get(Ore.Grade.POOR);

        var normalId =
                gradeMap.get(Ore.Grade.NORMAL);

        var richId =
                gradeMap.get(Ore.Grade.RICH);

        if (poorId == null
                || normalId == null
                || richId == null) {
            return;
        }

        Block poor = poorId.get();
        Block normal = normalId.get();
        Block rich = richId.get();

        if (isInvalidBlock(poor)
                || isInvalidBlock(normal)
                || isInvalidBlock(rich)) {
            return;
        }

        rules.add(
                createWeightedReplacementRule(
                        BuiltInRegistries.BLOCK.getKey(target),
                        getGradedWeights(
                                vein,
                                poor,
                                normal,
                                rich
                        )
                )
        );
    }

    private static JsonObject createDirectReplacementRule(
            ResourceLocation target,
            Block replacement
    ) {
        JsonObject rule = new JsonObject();

        JsonArray replace = new JsonArray();
        replace.add(target.toString());
        rule.add("replace", replace);

        JsonArray with = new JsonArray();

        JsonObject block = new JsonObject();
        block.addProperty(
                "block",
                BuiltInRegistries.BLOCK
                        .getKey(replacement)
                        .toString()
        );

        /*
         * No weight for a direct replacement.
         */
        with.add(block);

        rule.add("with", with);

        return rule;
    }

    private static JsonObject createWeightedReplacementRule(
            ResourceLocation target,
            List<Pair<BlockState, Double>> weights
    ) {
        JsonObject rule = new JsonObject();

        JsonArray replace = new JsonArray();
        replace.add(target.toString());
        rule.add("replace", replace);

        JsonArray with = new JsonArray();

        for (Pair<BlockState, Double> pair : weights) {
            BlockState state = pair.getFirst();

            if (state == null || state.isAir()) {
                continue;
            }

            ResourceLocation blockId =
                    BuiltInRegistries.BLOCK.getKey(
                            state.getBlock()
                    );

            JsonObject entry = new JsonObject();

            entry.addProperty(
                    "block",
                    blockId.toString()
            );

            entry.addProperty(
                    "weight",
                    pair.getSecond()
            );

            with.add(entry);
        }

        rule.add("with", with);

        return rule;
    }

    private static List<Pair<BlockState, Double>> getGradedWeights(
            CompatVein vein,
            Block poor,
            Block normal,
            Block rich
    ) {
        return switch (vein.gradedVeinClass) {
            case SURFACE -> List.of(
                    Pair.of(
                            poor.defaultBlockState(),
                            70.0
                    ),
                    Pair.of(
                            normal.defaultBlockState(),
                            25.0
                    ),
                    Pair.of(
                            rich.defaultBlockState(),
                            5.0
                    )
            );

            case NORMAL -> List.of(
                    Pair.of(
                            poor.defaultBlockState(),
                            35.0
                    ),
                    Pair.of(
                            normal.defaultBlockState(),
                            40.0
                    ),
                    Pair.of(
                            rich.defaultBlockState(),
                            25.0
                    )
            );

            case RICH -> List.of(
                    Pair.of(
                            poor.defaultBlockState(),
                            15.0
                    ),
                    Pair.of(
                            normal.defaultBlockState(),
                            25.0
                    ),
                    Pair.of(
                            rich.defaultBlockState(),
                            60.0
                    )
            );
        };
    }

    private static boolean isInvalidBlock(Block block) {
        return block == null || block == Blocks.AIR;
    }
}
