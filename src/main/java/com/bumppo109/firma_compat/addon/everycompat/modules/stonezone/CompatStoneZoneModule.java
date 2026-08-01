package com.bumppo109.firma_compat.addon.everycompat.modules.stonezone;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.world.CompatSingleBlockVein;
import com.bumppo109.firma_compat.world.CompatVein;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.RockRopeAnchorBlock;
import net.dries007.tfc.common.blocks.RopeAnchorBlock;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.util.collections.Weighted;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
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
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bumppo109.firma_compat.block.ModBlocks.GRADED_ORES;
import static com.bumppo109.firma_compat.block.ModBlocks.ORES;

public class CompatStoneZoneModule extends StoneZoneModule {

    public final SimpleEntrySet<StoneType, Block> COBBLE, MOSSY_COBBLE, HARDENED_COBBLE, MOSSY_HARDENED_COBBLE, HARDENED;
    public final SimpleEntrySet<StoneType, Block> LOOSE, MOSSY_LOOSE;
    public SimpleEntrySet<StoneType, Block> ROPE_ANCHOR;
    public SimpleEntrySet<StoneType, Block> SPIKE;
    public final ItemOnlyEntrySet<StoneType, Item> BRICK;
    public final SimpleEntrySet<StoneType, Block> AQUEDUCT;

    public final Map<String, SimpleEntrySet<StoneType, Block>> ORE_ENTRY_SETS = new HashMap<>();

    public CompatStoneZoneModule(String modId) {
        super(modId, "tfc");

        Supplier<CreativeModeTab> tab = getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        HARDENED = StoneZoneEntrySet.of(StoneType.class,"","hardened",
                        getModBlock("hardened_stone", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/hardened"), Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/hardened"), Registries.ITEM)
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(HARDENED);

        ROPE_ANCHOR = StoneZoneEntrySet.of(StoneType.class,"rope_anchor",
                        () -> getModBlock("stone_rope_anchor").get(), () -> VanillaStoneTypes.STONE,
                        stoneType -> new RockRopeAnchorBlock(ExtendedProperties.of(Utils.copyPropertySafe(stoneType.stone)), () -> (RockSpikeBlock) SPIKE.blocks.get(stoneType))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .noItem()
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(ROPE_ANCHOR);

        SPIKE = StoneZoneEntrySet.of(StoneType.class,"spike",
                        () -> getModBlock("stone_spike").get(), () -> VanillaStoneTypes.STONE,
                        stoneType -> new RockSpikeBlock(Utils.copyPropertySafe(stoneType.stone).lightLevel(ModBlocks.lavaLoggedBlockEmission()), () -> (RopeAnchorBlock) ROPE_ANCHOR.blocks.get(stoneType))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/spike"), Registries.BLOCK)
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(SPIKE);

        LOOSE = StoneZoneEntrySet.of(StoneType.class,"loose",
                        getModBlock("stone_loose", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(BlockBehaviour.Properties.of().strength(0.05f, 0.0f).noCollission())
                )
                .addTexture(modRes("item/stone_loose"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/loose"), Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/loose"), Registries.ITEM)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc","stones/loose/metamorphic"), Registries.ITEM)
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(LOOSE);

        MOSSY_LOOSE = StoneZoneEntrySet.of(StoneType.class, "loose","mossy",
                        getModBlock("mossy_stone_loose", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(BlockBehaviour.Properties.of().strength(0.05f, 0.0f).noCollission())
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/loose"), Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/loose"), Registries.ITEM)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc","stones/loose/metamorphic"), Registries.ITEM)
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_LOOSE);

        BRICK = ItemOnlyEntrySet.builder(StoneType.class, "brick",
                        getModItem("stone_brick"), () -> VanillaStoneTypes.STONE,
                        w -> new Item(new Item.Properties())
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(modRes("item/stone_brick"))
                .addTag(ResourceLocation.fromNamespaceAndPath("rnr","sett_road_items"), Registries.ITEM)
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(BRICK);

        COBBLE = StoneZoneEntrySet.of(StoneType.class,"cobble",
                        getModBlock("stone_cobble", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/stone_cobble"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(COBBLE);

        MOSSY_COBBLE = StoneZoneEntrySet.of(StoneType.class,"cobble", "mossy",
                        getModBlock("mossy_stone_cobble", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTextureM(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/mossy_stone_cobble"),
                        ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "template/block/mossy_cobble_overlay"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_COBBLE);

        HARDENED_COBBLE = StoneZoneEntrySet.of(StoneType.class,"cobble","hardened",
                        getModBlock("hardened_stone_cobble", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/stone_cobble"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(HARDENED_COBBLE);

        MOSSY_HARDENED_COBBLE = StoneZoneEntrySet.of(StoneType.class,"cobble", "mossy_hardened",
                        getModBlock("mossy_hardened_stone_cobble", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTextureM(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/mossy_stone_cobble"),
                        ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "template/block/mossy_cobble_overlay"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_HARDENED_COBBLE);

        AQUEDUCT = StoneZoneEntrySet.of(StoneType.class,"brick_aqueduct",
                        getModBlock("stone_brick_aqueduct"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new AqueductBlock((Utils.copyPropertySafe(stoneType.block)))
                )
                .requiresChildren(VanillaRockChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.AQUEDUCTS, Registries.BLOCK)
                .addRecipe(modRes("crafting/stone_brick_aqueduct"))
                .dropSelf()
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("nomansland:silt")
                .setTab(tab)
                .build();
        this.addEntry(AQUEDUCT);

        for (Ore ore : Ore.values()) {
            if (ore.isGraded() || !ore.hasBlock()) continue;
            if (ModBlocks.skippedOre(ore)) continue;

            String oreName = ore.name().toLowerCase();

            SimpleEntrySet<StoneType, Block> ungradedSet = StoneZoneEntrySet.of(StoneType.class, oreName + "_ore",
                            getModBlock("stone_" + oreName + "_ore"), () -> VanillaStoneTypes.STONE,
                            stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                    )
                    .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                    .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                    .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                    .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                    .copyParentDrop()
                    .setRenderType(RenderLayer.CUTOUT)
                    .setTab(tab)
                    .excludeBlockTypes("tfc:.*").excludeBlockTypes("nomansland:silt")
                    .build();

            this.addEntry(ungradedSet);
            ORE_ENTRY_SETS.put("ungraded_" + oreName, ungradedSet);
        }

        // 2. Graded ores (poor/normal/rich variants for each graded ore)
        for (Ore ore : Ore.values()) {
            if (!ore.isGraded() || !ore.hasBlock()) continue;
            if (ModBlocks.skippedOre(ore)) continue;

            String oreName = ore.name().toLowerCase();

            for (Ore.Grade grade : Ore.Grade.values()) {
                String gradeName = grade.name().toLowerCase();

                SimpleEntrySet<StoneType, Block> gradedSet = StoneZoneEntrySet.of(StoneType.class,oreName + "_ore", gradeName,
                                getModBlock(gradeName + "_stone_" + oreName + "_ore"), () -> VanillaStoneTypes.STONE,
                                stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                        )
                        .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                        .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                        .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                        .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                        .copyParentDrop()
                        .setRenderType(RenderLayer.CUTOUT)
                        .setTab(tab)
                        .excludeBlockTypes("tfc:.*").excludeBlockTypes("nomansland:silt")
                        .build();

                this.addEntry(gradedSet);
                ORE_ENTRY_SETS.put(gradeName + "_" + oreName, gradedSet);
            }
        }
    }

    @Override
    public void addDynamicClientResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {
            //Knapping texture
            LOOSE.blocks.forEach((stoneType, block) -> {
                if (stoneType == null) return;

                ResourceLocation rawResLoc = BuiltInRegistries.BLOCK.getKey(stoneType.block);
                ResourceLocation looseResLoc = BuiltInRegistries.BLOCK.getKey(LOOSE.blocks.get(stoneType));

                String rawPath = rawResLoc.getPath();
                String loosePath = looseResLoc.getPath();
                String rawNamespace = rawResLoc.getNamespace();

                // Target path: tfc:gui/knapping/granite.png (or tfc:gui/knapping/rock/granite.png – see note below)
                ResourceLocation looseTargetLoc = ResourceLocation.fromNamespaceAndPath("tfc","gui/knapping/" + loosePath);
                ResourceLocation mossyLooseTargetLoc = ResourceLocation.fromNamespaceAndPath("tfc","gui/knapping/mossy_" + loosePath);

                // Source texture: minecraft:block/granite.png (add .png if missing)
                ResourceLocation sourceLoc = ResourceLocation.fromNamespaceAndPath(rawNamespace,"block/" + rawPath + ".png");

                try (TextureImage rawTexture = TextureImage.open(manager, sourceLoc)) {
                    // Only add if not already present (prevents overwrite / spam)
                    sink.addTextureIfNotPresent(manager, looseTargetLoc, () -> rawTexture);
                    sink.addTextureIfNotPresent(manager, mossyLooseTargetLoc, () -> rawTexture);
                } catch (IOException e) {
                    FirmaCompat.LOGGER.error("Failed to copy knapping texture for {} from {} : {}",
                            rawResLoc, sourceLoc, e.getMessage());
                }
            });
        });
    }

    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for(StoneType stoneType : StoneTypeRegistry.INSTANCE){
                ResourceLocation rockTag = ResourceLocation.fromNamespaceAndPath(stoneType.getNamespace(), "stone_type/" + stoneType.getTypeName());
                UtilityTag.createAndAddCustomTags(rockTag, sink, stoneType.stone);
            }

            //Building Hardend Data Map
            JsonObject hardenedDataMap = new JsonObject();
            JsonObject hardenedArray = new JsonObject();

            for(CompatRock rock : CompatRock.VALUES){
                Block rawBlock = rock.rockMaterial().raw().base().get();
                Block hardenedRock = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.HARDENED).get();

                if (rawBlock == null || hardenedRock == null) continue;

                String rawId = BuiltInRegistries.BLOCK.getKey(rawBlock).toString();
                String hardenedId = BuiltInRegistries.BLOCK.getKey(hardenedRock).toString();

                hardenedArray.addProperty(rawId, hardenedId);
            }
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                Block rawBlock = stone.block;
                Block hardenedRock = HARDENED.blocks.get(stone);

                if (rawBlock == null || hardenedRock == null) continue;

                String rawId = BuiltInRegistries.BLOCK.getKey(rawBlock).toString();
                String hardenedId = BuiltInRegistries.BLOCK.getKey(hardenedRock).toString();

                hardenedArray.addProperty(rawId, hardenedId);
            }
            // Finalize the map
            hardenedDataMap.add("values", hardenedArray);

            // Write the file
            ResourceLocation hardenedDataMapPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "data_maps/block/worldgen/hardened_rock_replacement.json"
            );

            sink.addJson(hardenedDataMapPath, hardenedDataMap, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated hardened replacement map with {} entries: {}", hardenedArray.size(), hardenedDataMapPath);

            //Loose Block Worldgen
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                Block looseBlock = LOOSE.blocks.get(stone);
                String loosePath = Utils.getID(looseBlock).getPath();
                String looseNamespace = Utils.getID(looseBlock).getNamespace();

                //TODO - is there a better way to get the stoneType Tag?
                ResourceLocation rockTag = ResourceLocation.fromNamespaceAndPath(stone.getNamespace(), "stone_type/" + stone.getTypeName());

                generateLoosePlacedFeature(sink, rockTag, looseBlock);
                generateLooseConfiguredFeature(sink, looseBlock);
            }

            //Configured Vein Feature Files
            for (CompatVein vein : CompatVein.values()) {
                String veinName = vein.name().toLowerCase(Locale.ROOT);

                JsonArray blocksArray = new JsonArray();

                // ────────────────────────────────────────────────────────────────
                // PART 1: Dynamic rules from StoneZone stone types
                // ────────────────────────────────────────────────────────────────
                for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
                    Block target = stoneType.block;
                    if (target == null || target == Blocks.AIR) continue;

                    String targetId = BuiltInRegistries.BLOCK.getKey(target).toString();

                    List<Pair<BlockState, Double>> weights = getOreWeightsForStone(vein, stoneType);
                    if (weights == null || weights.isEmpty()) continue;

                    JsonObject rule = createRule(targetId, weights);
                    blocksArray.add(rule);
                }

                // ────────────────────────────────────────────────────────────────
                // PART 2: Append fixed rules (from your original map or hardcoded)
                // ────────────────────────────────────────────────────────────────
                Map<Block, IWeighted<BlockState>> fixedMap = buildReplacementMap(vein);  // your original method
                // OR if you have a different fixed map: Map<Block, IWeighted<BlockState>> fixedMap = getFixedMapForVein(vein);

                for (CompatRock rock : CompatRock.VALUES) {
                    Block target = rock.rockMaterial().raw().base().get();  // ← the block this ore should replace
                    if (target == null || target == Blocks.AIR) continue;

                    String targetId = BuiltInRegistries.BLOCK.getKey(target).toString();

                    List<Pair<BlockState, Double>> weights;

                    if (!vein.ore.isGraded()) {
                        // Non-graded ore
                        var oreMap = ModBlocks.ORES.get(rock);
                        if (oreMap == null) continue;

                        ModBlocks.Id<Block> oreId = oreMap.get(vein.ore);
                        if (oreId == null) continue;

                        Block oreBlock = oreId.get();
                        if (oreBlock == null || oreBlock == Blocks.AIR) continue;

                        weights = List.of(Pair.of(oreBlock.defaultBlockState(), 1.0));
                    } else {
                        // Graded ore (poor / normal / rich)
                        var rockOreMap = ModBlocks.GRADED_ORES.get(rock);
                        if (rockOreMap == null) continue;

                        var gradeMap = rockOreMap.get(vein.ore);
                        if (gradeMap == null) continue;

                        ModBlocks.Id<Block> poorId  = gradeMap.get(Ore.Grade.POOR);
                        ModBlocks.Id<Block> normalId = gradeMap.get(Ore.Grade.NORMAL);
                        ModBlocks.Id<Block> richId   = gradeMap.get(Ore.Grade.RICH);

                        if (poorId == null || normalId == null || richId == null) continue;

                        Block poor   = poorId.get();
                        Block normal = normalId.get();
                        Block rich   = richId.get();

                        if (poor == null || normal == null || rich == null ||
                                poor == Blocks.AIR || normal == Blocks.AIR || rich == Blocks.AIR) {
                            continue;
                        }

                        // Use your original weighting logic (adjust field name if needed)
                        weights = switch (vein.gradedVeinClass) {  // or vein.gradedVeinClass if that's the actual field
                            case SURFACE -> List.of(
                                    Pair.of(poor.defaultBlockState(),   70.0),
                                    Pair.of(normal.defaultBlockState(), 25.0),
                                    Pair.of(rich.defaultBlockState(),    5.0)
                            );
                            case RICH -> List.of(
                                    Pair.of(poor.defaultBlockState(),   15.0),
                                    Pair.of(normal.defaultBlockState(), 25.0),
                                    Pair.of(rich.defaultBlockState(),   60.0)
                            );
                            case NORMAL -> List.of(
                                    Pair.of(poor.defaultBlockState(),   35.0),
                                    Pair.of(normal.defaultBlockState(), 40.0),
                                    Pair.of(rich.defaultBlockState(),   25.0)
                            ); // fallback / balanced
                        };
                    }

                    // Create and append the rule
                    JsonObject rule = createRule(targetId, weights);
                    blocksArray.add(rule);
                }

                if (blocksArray.size() == 0) {
                    FirmaCompat.LOGGER.warn("Skipping vein {}: no valid replacements at all", veinName);
                    continue;
                }

                // Build config (rest unchanged)
                JsonObject configJson = new JsonObject();
                configJson.addProperty("rarity", vein.rarity);
                configJson.addProperty("density", vein.density);
                configJson.addProperty("min_y", vein.minY);
                configJson.addProperty("max_y", vein.maxY);
                configJson.addProperty("random_name", veinSeedFromName(veinName));
                configJson.add("blocks", blocksArray);

                // Type-specific fields...
                switch (vein.veinType) {
                    case DISC -> {
                        configJson.addProperty("size", vein.size != null ? vein.size : 20);
                        configJson.addProperty("height", vein.height != null ? vein.height : 4);
                    }
                    case CLUSTER -> {
                        configJson.addProperty("size", vein.size != null ? vein.size : 20);
                    }
                    case PIPE -> {
                        configJson.addProperty("height", vein.pipeHeight != null ? vein.pipeHeight : 60);
                        configJson.addProperty("radius", vein.radius != null ? vein.radius : 5);
                        configJson.addProperty("min_skew", vein.minSkew != null ? vein.minSkew : 5);
                        configJson.addProperty("max_skew", vein.maxSkew != null ? vein.maxSkew : 13);
                        configJson.addProperty("min_slant", vein.minSlant != null ? vein.minSlant : 0);
                        configJson.addProperty("max_slant", vein.maxSlant != null ? vein.maxSlant : 2);
                        configJson.addProperty("sign", vein.sign != null ? vein.sign.floatValue() : 0.0f);
                    }
                }

                JsonObject root = new JsonObject();
                root.addProperty("type", "tfc:" + vein.veinType.name().toLowerCase(Locale.ROOT) + "_vein");
                root.add("config", configJson);

                ResourceLocation path = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "worldgen/configured_feature/stonezone/vein/" + veinName + ".json"
                );

                sink.addJson(path, root, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated vein configured feature: {}", path);
            }

            //Placed Vein Feature Files
            for (CompatVein vein : CompatVein.values()) {
                String veinName = vein.name().toLowerCase(Locale.ROOT);

                JsonObject placedJson = new JsonObject();

                // Reference to the configured feature
                String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;
                // Alternative if you used overworld prefix:
                // String featurePath = FirmaCompat.MODID + ":overworld/vein/" + veinName;

                placedJson.addProperty("feature", featurePath);

                // Empty placement modifiers (as requested)
                placedJson.add("placement", new JsonArray());

                // Write individual placed feature
                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "worldgen/placed_feature/stonezone/vein/" + veinName + ".json"
                );

                sink.addJson(placedPath, placedJson, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

            //Copper Vein Placer Configured Feature
            JsonObject configuredCopperVeinPlacer = new JsonObject();
            List<JsonObject> featureCopperEntries = new ArrayList<>();
            int veinCopperPlacerCount = 0;

            for (CompatVein vein : CompatVein.values()) {
                if(vein.ore == Ore.MALACHITE || vein.ore == Ore.NATIVE_COPPER || vein.ore == Ore.TETRAHEDRITE){
                    String veinName = vein.name().toLowerCase(Locale.ROOT);

                    String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;

                    JsonObject entry = new JsonObject();
                    entry.addProperty("chance", 0f);   // placeholder - will set equal value later
                    entry.addProperty("feature", featurePath);
                    featureCopperEntries.add(entry);
                    veinCopperPlacerCount++;
                }
                float equalChance = 1.0f / veinCopperPlacerCount;
                for (JsonObject entry : featureCopperEntries) {
                    entry.addProperty("chance", equalChance);
                }

                JsonObject config = new JsonObject();
                JsonArray featuresArray = new JsonArray();
                for (JsonObject entry : featureCopperEntries) {
                    featuresArray.add(entry);
                }
                config.add("features", featuresArray);

                String defaultFeature = FirmaCompat.MODID + ":stonezone/vein/" + CompatVein.SURFACE_NATIVE_COPPER.name().toLowerCase(Locale.ROOT);
                // Or use the overworld version if preferred:
                // String defaultFeature = FirmaCompat.MODID + ":overworld/vein/native_copper";

                config.addProperty("default", defaultFeature);

                configuredCopperVeinPlacer.addProperty("type", "minecraft:random_selector");
                configuredCopperVeinPlacer.add("config", config);

                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                        "worldgen/configured_feature/stonezone_copper_vein_placer.json"
                );

                sink.addJson(placedPath, configuredCopperVeinPlacer, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

            //Iron vein placer configured feature
            JsonObject configuredIronVeinPlacer = new JsonObject();
            List<JsonObject> featureIronEntries = new ArrayList<>();
            int veinIronPlacerCount = 0;

            for (CompatVein vein : CompatVein.values()) {
                if(vein.ore == Ore.HEMATITE || vein.ore == Ore.LIMONITE || vein.ore == Ore.MAGNETITE){
                    String veinName = vein.name().toLowerCase(Locale.ROOT);

                    String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;

                    JsonObject entry = new JsonObject();
                    entry.addProperty("chance", 0f);   // placeholder - will set equal value later
                    entry.addProperty("feature", featurePath);
                    featureIronEntries.add(entry);
                    veinIronPlacerCount++;
                }
                float equalChance = 1.0f / veinIronPlacerCount;
                for (JsonObject entry : featureIronEntries) {
                    entry.addProperty("chance", equalChance);
                }

                JsonObject config = new JsonObject();
                JsonArray featuresArray = new JsonArray();
                for (JsonObject entry : featureIronEntries) {
                    featuresArray.add(entry);
                }
                config.add("features", featuresArray);

                String defaultFeature = FirmaCompat.MODID + ":stonezone/vein/" + CompatVein.SURFACE_HEMATITE.name().toLowerCase(Locale.ROOT);
                // Or use the overworld version if preferred:
                // String defaultFeature = FirmaCompat.MODID + ":overworld/vein/native_copper";

                config.addProperty("default", defaultFeature);

                configuredIronVeinPlacer.addProperty("type", "minecraft:random_selector");
                configuredIronVeinPlacer.add("config", config);

                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                        "worldgen/configured_feature/stonezone_iron_vein_placer.json"
                );

                sink.addJson(placedPath, configuredIronVeinPlacer, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

            //Copper & Iron vein placer placed feature files
            JsonObject copperVeinPlacerJson = new JsonObject();
            JsonObject ironVeinPlacerJson = new JsonObject();

            copperVeinPlacerJson.addProperty("feature", "firma_compat:stonezone_copper_vein_placer");
            ironVeinPlacerJson.addProperty("feature", "firma_compat:stonezone_iron_vein_placer");

            // Empty placement modifiers (as requested)
            copperVeinPlacerJson.add("placement", new JsonArray());
            ironVeinPlacerJson.add("placement", new JsonArray());

            // Write individual placed feature
            ResourceLocation copperVeinPlacerPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "worldgen/placed_feature/stonezone_copper_vein_placer.json"
            );
            ResourceLocation ironVeinPlacerPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "worldgen/placed_feature/stonezone_iron_vein_placer.json"
            );

            sink.addJson(copperVeinPlacerPath, copperVeinPlacerJson, ResType.GENERIC);
            sink.addJson(ironVeinPlacerPath, ironVeinPlacerJson, ResType.GENERIC);

            //Tag - vein features
            JsonObject tagJson = new JsonObject();
            tagJson.addProperty("replace", false);  // Optional: prevents overriding vanilla/other mods

            JsonArray valuesArray = new JsonArray();

            for (CompatVein vein : CompatVein.values()) {
                //copper & iron veins are handled with the copper/iron placer features
                if(vein.ore != Ore.NATIVE_COPPER && vein.ore != Ore.MALACHITE && vein.ore != Ore.TETRAHEDRITE &&
                        vein.ore != Ore.HEMATITE && vein.ore != Ore.LIMONITE && vein.ore != Ore.MAGNETITE){
                    String veinName = vein.name().toLowerCase(Locale.ROOT);
                    String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;
                    valuesArray.add(featurePath);
                }
            }
            valuesArray.add("firma_compat:stonezone_copper_vein_placer");
            valuesArray.add("firma_compat:stonezone_iron_vein_placer");

            tagJson.add("values", valuesArray);

            // Write the tag file
            ResourceLocation tagPath = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "tags/worldgen/placed_feature/stonezone_veins.json"  // or whatever name you prefer
            );

            sink.addJson(tagPath, tagJson, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated placed feature tag: {}", tagPath);

            //Tag - loose features
            JsonObject looseTagJson = new JsonObject();
            //tagJson.addProperty("replace", false);  // Optional: prevents overriding vanilla/other mods

            JsonArray looseValuesArray = new JsonArray();

            for (StoneType stone : StoneTypeRegistry.INSTANCE) {
                String loosePath = Utils.getID(LOOSE.blocks.get(stone)).getPath();
                String looseNamespace = Utils.getID(LOOSE.blocks.get(stone)).getNamespace();

                if(LOOSE.blocks.get(stone) != null){
                    String featurePath = FirmaCompat.MODID + ":loose/" + loosePath;
                    looseValuesArray.add(featurePath);
                }
            }

            looseTagJson.add("values", looseValuesArray);

            // Write the tag file
            ResourceLocation looseTagPath = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "tags/worldgen/placed_feature/stonezone_loose.json"
            );

            sink.addJson(looseTagPath, looseTagJson, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated placed feature tag: {}", looseTagPath);

            //Biome Modifier File
            JsonObject biomeModifier = new JsonObject();

            String placedFeatureTag = FirmaCompat.MODID + ":stonezone_veins";

            biomeModifier.addProperty("type", "neoforge:add_features");
            biomeModifier.addProperty("biomes", "#c:is_overworld");
            biomeModifier.addProperty("features", "#" + placedFeatureTag);
            biomeModifier.addProperty("step", "underground_ores");

            // Write individual placed feature
            ResourceLocation biomeModifierPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "neoforge/biome_modifier/add_stonezone_veins.json"
            );

            sink.addJson(biomeModifierPath, biomeModifier, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated biome modifier: {}", biomeModifierPath);

            //Collapse Recipes
            for (StoneType stone : StoneTypeRegistry.INSTANCE) {
                // Skip if required blocks are missing
                Block looseCobble = COBBLE.blocks.get(stone);
                if (looseCobble == null) continue;

                Block hardened = HARDENED.blocks.get(stone);
                if (hardened == null) continue;

                String rawId           = BuiltInRegistries.BLOCK.getKey(stone.stone).toString();
                String hardenedId      = BuiltInRegistries.BLOCK.getKey(hardened).toString();
                String looseCobbleId   = BuiltInRegistries.BLOCK.getKey(looseCobble).toString();

                String looseNs         = Utils.getID(looseCobble).getNamespace();
                String stoneName       = stone.getTypeName();

                // ────────────────────────────────────────────────────────────────
                // 1. Main collapse recipe: raw + hardened + ALL poor ores + ALL non-graded ores
                //    → result: loose cobble
                // ────────────────────────────────────────────────────────────────
                JsonArray poorCollapseArray = new JsonArray();
                poorCollapseArray.add(rawId);
                poorCollapseArray.add(hardenedId);

                for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                    String key = entry.getKey();
                    SimpleEntrySet<StoneType, Block> oreSet = entry.getValue();

                    // Include only:
                    // - keys starting with "poor_" (poor graded ores)
                    // - keys that have NO prefix at all (non-graded ores)
                    boolean isPoor = key.startsWith("poor_");
                    boolean isUngraded = !key.startsWith("poor_") &&
                            !key.startsWith("normal_") &&
                            !key.startsWith("rich_");

                    if (!isPoor && !isUngraded) {
                        continue;
                    }

                    Block oreBlock = oreSet.blocks.get(stone);
                    if (oreBlock != null && oreBlock != Blocks.AIR) {
                        String oreId = BuiltInRegistries.BLOCK.getKey(oreBlock).toString();
                        poorCollapseArray.add(oreId);

                        FirmaCompat.LOGGER.debug("Poor/ungraded collapse: {} for {} → {}",
                                key, stoneName, oreId);
                    }
                }

                JsonObject poorCollapseJson = new JsonObject();
                poorCollapseJson.addProperty("type", "tfc:collapse");
                poorCollapseJson.add("ingredient", poorCollapseArray);
                poorCollapseJson.addProperty("result", looseCobbleId);

                ResourceLocation poorLoc = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "recipe/collapse/" + looseNs + "/" + stoneName + ".json"
                );
                sink.addJson(poorLoc, poorCollapseJson, ResType.GENERIC);

                // ────────────────────────────────────────────────────────────────
                // 2. Rich ores collapse → corresponding normal ore
                // ────────────────────────────────────────────────────────────────
                for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                    String key = entry.getKey();
                    if (!key.startsWith("rich_")) continue;

                    String normalKey = "normal_" + key.substring("rich_".length());
                    SimpleEntrySet<StoneType, Block> normalSet = ORE_ENTRY_SETS.get(normalKey);
                    if (normalSet == null) continue;

                    Block richBlock = entry.getValue().blocks.get(stone);
                    Block normalBlock = normalSet.blocks.get(stone);

                    if (richBlock != null && richBlock != Blocks.AIR &&
                            normalBlock != null && normalBlock != Blocks.AIR) {

                        String richId   = BuiltInRegistries.BLOCK.getKey(richBlock).toString();
                        String normalId = BuiltInRegistries.BLOCK.getKey(normalBlock).toString();

                        JsonObject recipe = new JsonObject();
                        recipe.addProperty("type", "tfc:collapse");
                        JsonArray ing = new JsonArray();
                        ing.add(richId);
                        recipe.add("ingredient", ing);
                        recipe.addProperty("result", normalId);

                        String oreType = key.substring("rich_".length());
                        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "recipe/collapse/" + looseNs + "/rich_to_normal/" + stoneName + "_" + oreType + ".json"
                        );
                        sink.addJson(loc, recipe, ResType.GENERIC);

                        FirmaCompat.LOGGER.debug("Rich→Normal: {} → {} for {}", richId, normalId, stoneName);
                    }
                }

                // ────────────────────────────────────────────────────────────────
                // 3. Normal ores collapse → corresponding poor ore
                // ────────────────────────────────────────────────────────────────
                for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                    String key = entry.getKey();
                    if (!key.startsWith("normal_")) continue;

                    String poorKey = "poor_" + key.substring("normal_".length());
                    SimpleEntrySet<StoneType, Block> poorSet = ORE_ENTRY_SETS.get(poorKey);
                    if (poorSet == null) continue;

                    Block normalBlock = entry.getValue().blocks.get(stone);
                    Block poorBlock   = poorSet.blocks.get(stone);

                    if (normalBlock != null && normalBlock != Blocks.AIR &&
                            poorBlock != null && poorBlock != Blocks.AIR) {

                        String normalId = BuiltInRegistries.BLOCK.getKey(normalBlock).toString();
                        String poorId   = BuiltInRegistries.BLOCK.getKey(poorBlock).toString();

                        JsonObject recipe = new JsonObject();
                        recipe.addProperty("type", "tfc:collapse");
                        JsonArray ing = new JsonArray();
                        ing.add(normalId);
                        recipe.add("ingredient", ing);
                        recipe.addProperty("result", poorId);

                        String oreType = key.substring("normal_".length());
                        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "recipe/collapse/" + looseNs + "/normal_to_poor/" + stoneName + "_" + oreType + ".json"
                        );
                        sink.addJson(loc, recipe, ResType.GENERIC);

                        FirmaCompat.LOGGER.debug("Normal→Poor: {} → {} for {}", normalId, poorId, stoneName);
                    }
                }

                FirmaCompat.LOGGER.info("Generated all collapse recipes for stone: {}", stoneName);
            }

            // Generate individual loot tables for EVERY ore block variant
            for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                String oreKey = entry.getKey(); // e.g. "poor_native_copper"
                SimpleEntrySet<StoneType, Block> oreSet = entry.getValue();

                // Determine the base dropped item for this ore variant
                ResourceLocation baseDroppedItem;
                String oreName;
                String gradePrefix = "";

                if (oreKey.startsWith("poor_")) {
                    gradePrefix = "poor_";
                    oreName = oreKey.substring("poor_".length());
                } else if (oreKey.startsWith("normal_")) {
                    gradePrefix = "normal_";
                    oreName = oreKey.substring("normal_".length());
                } else if (oreKey.startsWith("rich_")) {
                    gradePrefix = "rich_";
                    oreName = oreKey.substring("rich_".length());
                } else if (oreKey.startsWith("ungraded_")) {
                    oreName = oreKey.substring("ungraded_".length());
                } else {
                    FirmaCompat.LOGGER.warn("Unexpected ore key format: {}", oreKey);
                    continue;
                }

                baseDroppedItem = ResourceLocation.fromNamespaceAndPath("tfc", "ore/" + gradePrefix + oreName);

                // Now generate a loot table for EVERY stone variant of this ore
                for (Map.Entry<StoneType, Block> blockEntry : oreSet.blocks.entrySet()) {
                    StoneType stoneType = blockEntry.getKey();
                    Block oreBlock = blockEntry.getValue();

                    if (oreBlock == null || oreBlock == Blocks.AIR) continue;

                    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(oreBlock);
                    if (blockId == null) continue;

                    // Loot table path MUST match the block's registry path
                    // e.g. data/firma_compat/loot_tables/blocks/poor_stone_native_copper_ore.json
                    ResourceLocation lootLoc = ResourceLocation.fromNamespaceAndPath(
                            blockId.getNamespace(),
                            "loot_table/blocks/" + blockId.getPath() + ".json"
                    );

                    // Build loot table JSON (identical structure for all)
                    JsonObject lootTable = new JsonObject();
                    lootTable.addProperty("type", "minecraft:block");

                    JsonArray pools = new JsonArray();
                    JsonObject pool = new JsonObject();
                    pool.addProperty("rolls", 1);

                    JsonArray entries = new JsonArray();
                    JsonObject oreEntry = new JsonObject();
                    oreEntry.addProperty("type", "minecraft:item");
                    oreEntry.addProperty("name", baseDroppedItem.toString());
                    entries.add(oreEntry);

                    pool.add("entries", entries);

                    JsonArray conditions = new JsonArray();
                    JsonObject survivesExplosion = new JsonObject();
                    survivesExplosion.addProperty("condition", "minecraft:survives_explosion");
                    conditions.add(survivesExplosion);

                    pool.add("conditions", conditions);
                    pools.add(pool);

                    lootTable.add("pools", pools);

                    // Write the loot table
                    sink.addJson(lootLoc, lootTable, ResType.GENERIC);

                    FirmaCompat.LOGGER.debug("Generated loot table for block {} → drops {}",
                            blockId, baseDroppedItem);
                }
            }
        });
    }

    private JsonObject createRule(String targetId, List<Pair<BlockState, Double>> weights) {
        JsonObject rule = new JsonObject();

        JsonArray replaceArray = new JsonArray();
        replaceArray.add(targetId);
        rule.add("replace", replaceArray);

        JsonArray withArray = new JsonArray();
        for (Pair<BlockState, Double> pair : weights) {
            String oreId = BuiltInRegistries.BLOCK.getKey(pair.getFirst().getBlock()).toString();

            JsonObject entry = new JsonObject();
            entry.addProperty("block", oreId);

            double weight = pair.getSecond();
            if (Math.abs(weight - 1.0) > 0.001) {
                entry.addProperty("weight", weight);
            }

            withArray.add(entry);
        }

        rule.add("with", withArray);
        return rule;
    }

    private List<Pair<BlockState, Double>> getOreWeightsForStone(CompatVein vein, StoneType stoneType) {
        String oreName = vein.ore.name().toLowerCase(Locale.ROOT);

        if (!vein.ore.isGraded()) {
            // Non-graded ore
            String ungradedKey = "ungraded_" + oreName;
            SimpleEntrySet<StoneType, Block> entrySet = ORE_ENTRY_SETS.get(ungradedKey);
            if (entrySet == null) return null;

            Block oreBlock = entrySet.blocks.get(stoneType);
            if (oreBlock == null || oreBlock == Blocks.AIR) return null;

            return List.of(Pair.of(oreBlock.defaultBlockState(), 1.0));
        } else {
            // Graded ore
            SimpleEntrySet<StoneType, Block> poorSet   = ORE_ENTRY_SETS.get("poor_"   + oreName);
            SimpleEntrySet<StoneType, Block> normalSet = ORE_ENTRY_SETS.get("normal_" + oreName);
            SimpleEntrySet<StoneType, Block> richSet   = ORE_ENTRY_SETS.get("rich_"   + oreName);

            if (poorSet == null || normalSet == null || richSet == null) return null;

            Block poor   = poorSet.blocks.get(stoneType);
            Block normal = normalSet.blocks.get(stoneType);
            Block rich   = richSet.blocks.get(stoneType);

            if (poor == null || normal == null || rich == null ||
                    poor == Blocks.AIR || normal == Blocks.AIR || rich == Blocks.AIR) {
                return null;
            }

            // Reuse your original weighting logic
            return switch (vein.gradedVeinClass) {  // ← note: you used String "normal"/"surface" here
                case SURFACE -> List.of(
                        Pair.of(poor.defaultBlockState(),   70.0),
                        Pair.of(normal.defaultBlockState(), 25.0),
                        Pair.of(rich.defaultBlockState(),    5.0)
                );
                case RICH -> List.of(
                        Pair.of(poor.defaultBlockState(),   15.0),
                        Pair.of(normal.defaultBlockState(), 25.0),
                        Pair.of(rich.defaultBlockState(),   60.0)
                );
                case NORMAL -> List.of(
                        Pair.of(poor.defaultBlockState(),   35.0),
                        Pair.of(normal.defaultBlockState(), 40.0),
                        Pair.of(rich.defaultBlockState(),   25.0));
            };
        }
    }

    /*
    private JsonObject heightProviderJson(int minY, int maxY) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:uniform"); // or "absolute" if you prefer
        JsonObject min = new JsonObject();
        min.addProperty("absolute", minY);
        JsonObject max = new JsonObject();
        max.addProperty("absolute", maxY);
        obj.add("min_inclusive", min);
        obj.add("max_inclusive", max);
        return obj;
    }

    //Block Map
    private static Map<Block, IWeighted<BlockState>> buildSingleReplacementMap(CompatSingleBlockVein vein) {
        Map<Block, IWeighted<BlockState>> map = new HashMap<>();

        int added = 0;
        int skipped = 0;

        for (CompatRock rock : CompatRock.VALUES) {
            Block target = rock.rockMaterial().raw().base().get();  // ← fixed: use vanilla equiv (STONE, GRANITE, etc.)
            if (target == null || target == Blocks.AIR) {
                skipped++;
                continue;
            }
            if (vein.oreBlock == null || vein.oreBlock == Blocks.AIR) {
                skipped++;
                continue;
            }

            map.put(target, new Weighted<>(
                    List.of(Pair.of(vein.oreBlock.defaultBlockState(), 1.0))
            ));
            added++;

        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

     */

    //Graded BlockMap
    private static Map<Block, IWeighted<BlockState>> buildReplacementMap(CompatVein vein) {
        Map<Block, IWeighted<BlockState>> map = new HashMap<>();

        int added = 0;
        int skipped = 0;

        for (CompatRock rock : CompatRock.VALUES) {
            Block target = rock.rockMaterial().raw().base().get();
            if (target == null || target == Blocks.AIR) {
                skipped++;
                continue;
            }

            if (!vein.ore.isGraded()) {
                var oreId = ModBlocks.ORES.get(rock).get(vein.ore);
                if (oreId == null) {
                    skipped++;
                    continue;
                }
                Block oreBlock = oreId.get();
                if (oreBlock == null || oreBlock == Blocks.AIR) {
                    skipped++;
                    continue;
                }

                map.put(target, new Weighted<>(
                        List.of(Pair.of(oreBlock.defaultBlockState(), 1.0))
                ));
                added++;
            } else {
                var oreMap = ModBlocks.GRADED_ORES.get(rock);
                if (oreMap == null) {
                    skipped++;
                    continue;
                }
                var gradeMap = oreMap.get(vein.ore);
                if (gradeMap == null) {
                    skipped++;
                    continue;
                }

                var poorId   = gradeMap.get(Ore.Grade.POOR);
                var normalId = gradeMap.get(Ore.Grade.NORMAL);
                var richId   = gradeMap.get(Ore.Grade.RICH);

                if (poorId == null || normalId == null || richId == null) {
                    skipped++;
                    continue;
                }

                Block poor   = poorId.get();
                Block normal = normalId.get();
                Block rich   = richId.get();

                if (poor == null || normal == null || rich == null) {
                    skipped++;
                    continue;
                }

                List<Pair<BlockState, Double>> weights = switch(vein.gradedVeinClass){
                    case SURFACE -> List.of(
                            Pair.of(poor.defaultBlockState(),   70.0),
                            Pair.of(normal.defaultBlockState(), 25.0),
                            Pair.of(rich.defaultBlockState(),    5.0));
                    case NORMAL -> List.of(
                            Pair.of(poor.defaultBlockState(),   35.0),
                            Pair.of(normal.defaultBlockState(), 40.0),
                            Pair.of(rich.defaultBlockState(),   25.0));
                    case RICH -> List.of(
                            Pair.of(poor.defaultBlockState(),   15.0),
                            Pair.of(normal.defaultBlockState(), 25.0),
                            Pair.of(rich.defaultBlockState(),   60.0));
                };

                map.put(target, new Weighted<>(weights));
                added++;
            }
        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

    //BlockMap Helpers

    // Helper: get non-graded ore block for rock (from your ORES map)
    private static Block getOreBlock(Ore ore, CompatRock rock) {
        // Assuming your registration provides access; adjust if needed
        return ORES.get(rock).get(ore).get();
    }

    // Helper: get graded ore block for rock + grade (from your GRADED_ORES map)
    private static Block getGradedOreBlock(Ore ore, CompatRock rock, Ore.Grade grade) {
        // Assuming your registration provides access; adjust if needed
        return GRADED_ORES.get(rock).get(ore).get(grade).get();
    }

    // Simple deterministic seed – same as TFC mostly uses
    private static long veinSeedFromName(String name) {
        long hash = 0;
        for (char c : name.toCharArray()) {
            hash = 31 * hash + c;
        }
        return hash & 0x7FFFFFFFFFFFFFFFL; // positive
    }

    public static void generateLoosePlacedFeature(
            ResourceSink sink,
            ResourceLocation rockTag,
            Block looseBlock
    ) {
        if (looseBlock == null) {
            FirmaCompat.LOGGER.warn("Skipping placed feature: invalid Block");
            return;
        }

        String loosePath = Utils.getID(looseBlock).getPath();
        String looseNamespace = Utils.getID(looseBlock).getNamespace();

        String looseBlockId = BuiltInRegistries.BLOCK.getKey(looseBlock).toString();

        // Build the predicate for tfc:would_survive_with_fluid
        JsonObject survivePredicate = new JsonObject();
        survivePredicate.addProperty("type", "tfc:would_survive_with_fluid");

        JsonObject stateObj = new JsonObject();
        stateObj.addProperty("Name", looseBlockId);

        JsonObject propertiesObj = new JsonObject();
        propertiesObj.addProperty("fluid", "empty");
        stateObj.add("Properties", propertiesObj);

        survivePredicate.add("state", stateObj);

        // Build the full block predicate filter
        JsonObject filterObj = new JsonObject();
        filterObj.addProperty("type", "minecraft:block_predicate_filter");
        filterObj.add("predicate", survivePredicate);

        // Build the first filter (above acacia logs)
        JsonObject rockFilter = new JsonObject();
        rockFilter.addProperty("type", "minecraft:block_predicate_filter");

        JsonObject allOfPredicate = new JsonObject();
        allOfPredicate.addProperty("type", "minecraft:all_of");

        JsonArray predicatesArray = new JsonArray();

        // Predicate 1: matching acacia logs below
        JsonObject logMatch = new JsonObject();
        logMatch.addProperty("type", "minecraft:matching_block_tag");
        logMatch.add("offset", jsonArray(0, -1, 0));
        logMatch.addProperty("tag", String.valueOf(rockTag));
        predicatesArray.add(logMatch);

        // Predicate 2: sturdy face upward
        JsonObject sturdyFace = new JsonObject();
        sturdyFace.addProperty("type", "minecraft:has_sturdy_face");
        sturdyFace.add("offset", jsonArray(0, -1, 0));
        sturdyFace.addProperty("direction", "up");
        predicatesArray.add(sturdyFace);

        allOfPredicate.add("predicates", predicatesArray);
        rockFilter.add("predicate", allOfPredicate);

        // Combine both filters
        JsonArray placementArray = new JsonArray();
        placementArray.add(rockFilter);
        placementArray.add(filterObj);

        // Root JSON
        JsonObject root = new JsonObject();
        root.addProperty("feature", FirmaCompat.MODID + ":" + "loose/" + loosePath);
        root.add("placement", placementArray);

        // Write the file
        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "worldgen/placed_feature/loose/" + loosePath + ".json");

        sink.addJson(path, root, ResType.GENERIC);
        FirmaCompat.LOGGER.info("Generated loose rock bamboo placed feature: {}", path);
    }

    // Tiny helper to create [0, -1, 0] array
    private static JsonArray jsonArray(int... values) {
        JsonArray arr = new JsonArray();
        for (int v : values) arr.add(v);
        return arr;
    }

    public static void generateLooseConfiguredFeature(
            ResourceSink sink,
            Block looseBlock
    ) {
        if (looseBlock == null || looseBlock == Blocks.AIR) {
            FirmaCompat.LOGGER.warn("Skipping random_selector feature: invalid loose block");
            return;
        }

        String blockId = BuiltInRegistries.BLOCK.getKey(looseBlock).toString();
        String blockPath = Utils.getID(looseBlock).getPath();

        // Helper function to create one nested "feature" object with given count
        Function<Integer, JsonObject> createFluidFeature = count -> {
            JsonObject state = new JsonObject();
            state.addProperty("Name", blockId);

            JsonObject props = new JsonObject();
            props.addProperty("fluid", "empty");
            props.addProperty("count", String.valueOf(count));
            state.add("Properties", props);

            JsonObject toPlace = new JsonObject();
            toPlace.addProperty("type", "minecraft:simple_state_provider");
            toPlace.add("state", state);

            JsonObject config = new JsonObject();
            config.add("to_place", toPlace);

            JsonObject featureObj = new JsonObject();
            featureObj.addProperty("type", "tfc:block_with_fluid");
            featureObj.add("config", config);

            JsonObject wrapper = new JsonObject();
            wrapper.add("feature", featureObj);
            wrapper.add("placement", new JsonArray()); // empty placement

            return wrapper;
        };

        // Build the "features" array with chances
        JsonArray featuresArray = new JsonArray();

        // Chance 0.4 → count 2
        JsonObject entry2 = new JsonObject();
        entry2.addProperty("chance", 0.4);
        entry2.add("feature", createFluidFeature.apply(2));
        featuresArray.add(entry2);

        // Chance 0.2 → count 3
        JsonObject entry3 = new JsonObject();
        entry3.addProperty("chance", 0.2);
        entry3.add("feature", createFluidFeature.apply(3));
        featuresArray.add(entry3);

        // Default (remaining chance) → count 1
        JsonObject defaultFeature = createFluidFeature.apply(1);

        // Root config
        JsonObject config = new JsonObject();
        config.add("features", featuresArray);
        config.add("default", defaultFeature);

        // Root JSON
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:random_selector");
        root.add("config", config);

        // Write the file
        String blockName = BuiltInRegistries.BLOCK.getKey(looseBlock).getPath();

        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "worldgen/configured_feature/" + "loose/" + blockPath + ".json"
        );

        sink.addJson(path, root, ResType.GENERIC);
        FirmaCompat.LOGGER.info("Generated random_selector loose count feature: {}", path);
    }
}