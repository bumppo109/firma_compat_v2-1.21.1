package com.bumppo109.firma_compat.addon.everycompat.modules.stonezone;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.everycompat.modules.woodgood.WoodGoodEntry;
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
import net.mehvahdjukaar.every_compat.api.*;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureOps;
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

    public final SimpleEntrySet<StoneType, Block> COBBLE, MOSSY_COBBLE, COBBLESTONE, MOSSY_COBBLESTONE, HARDENED;
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

        LOOSE = StoneZoneEntrySet.of(StoneType.class,"","loose",
                        getModBlock("loose_stone", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(BlockBehaviour.Properties.of().strength(0.05f, 0.0f).noCollission())
                )
                .addTexture(modRes("item/loose_stone"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/loose"), Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("c","stones/loose"), Registries.ITEM)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc","stones/loose/metamorphic"), Registries.ITEM)
                .copyParentDrop()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(LOOSE);

        MOSSY_LOOSE = StoneZoneEntrySet.of(StoneType.class, "","mossy_loose",
                        getModBlock("mossy_loose_stone", Block.class), () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(BlockBehaviour.Properties.of().strength(0.05f, 0.0f).noCollission())
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTextureM(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/mossy_stone"), ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/mossy_raw_mask"))
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
                .addTexture(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/stone_cobble"))
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
                .addTextureM(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/mossy_stone_cobble"), ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/mossy_cobble_mask"))
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.BLOCK)
                .addTag(Tags.Blocks.COBBLESTONES_NORMAL, Registries.ITEM)
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTab(tab)
                .build();
        this.addEntry(MOSSY_COBBLE);

        COBBLESTONE = StoneZoneEntrySet.of(StoneType.class,"cobblestone",
                        getModBlock("andesite_cobblestone", Block.class), () -> VanillaStoneTypes.ANDESITE,
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

        MOSSY_COBBLESTONE = StoneZoneEntrySet.of(StoneType.class,"cobblestone", "mossy",
                        getModBlock("mossy_andesite_cobblestone", Block.class), () -> VanillaStoneTypes.ANDESITE,
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
        this.addEntry(MOSSY_COBBLESTONE);

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
                ResourceLocation mossyLooseResLoc = BuiltInRegistries.BLOCK.getKey(MOSSY_LOOSE.blocks.get(stoneType));

                String rawPath = rawResLoc.getPath();
                String loosePath = looseResLoc.getPath();
                String mossyPath = mossyLooseResLoc.getPath();
                String rawNamespace = rawResLoc.getNamespace();

                // Target path: tfc:gui/knapping/granite.png (or tfc:gui/knapping/rock/granite.png – see note below)
                ResourceLocation looseTargetLoc = ResourceLocation.fromNamespaceAndPath("tfc","gui/knapping/" + loosePath);
                ResourceLocation mossyLooseTargetLoc = ResourceLocation.fromNamespaceAndPath("tfc","gui/knapping/" + mossyPath);

                // Source texture: minecraft:block/granite.png (add .png if missing)
                ResourceLocation rawSource = ResourceLocation.fromNamespaceAndPath(rawNamespace,"block/" + rawPath + ".png");
                ResourceLocation mossyRawSource = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,"block/tfc/" + stoneType.getNamespace() + "/mossy_" + stoneType.getTypeName() + ".png");

                try (TextureImage rawTexture = TextureImage.open(manager, rawSource)) {
                    sink.addTextureIfNotPresent(manager, looseTargetLoc, () -> rawTexture);
                } catch (IOException e) {
                    FirmaCompat.LOGGER.error("Failed to copy knapping texture for {} from {} : {}",
                            rawResLoc, rawSource, e.getMessage());
                }

                try (TextureImage rawTexture = TextureImage.open(manager, rawSource)) {
                    sink.addTextureIfNotPresent(manager, mossyLooseTargetLoc, () -> rawTexture);
                } catch (IOException e) {
                    FirmaCompat.LOGGER.error("Failed to copy knapping texture for {} from {} : {}",
                            rawResLoc, rawSource, e.getMessage());
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
                if (stoneType.getNamespace().equals("minecraft") || stoneType.getNamespace().equals("tfc")) continue;

                ResourceLocation rockTag = ResourceLocation.fromNamespaceAndPath(stoneType.getNamespace(), "stone_type/" + stoneType.getTypeName());
                UtilityTag.createAndAddCustomTags(rockTag, sink, stoneType.stone);
                ResourceLocation looseRes = Utils.getID(LOOSE.items.get(stoneType));
                ResourceLocation mossyLooseRes = Utils.getID(MOSSY_LOOSE.items.get(stoneType));
                ResourceLocation brickRes = Utils.getID(BRICK.items.get(stoneType));

                for (StoneZoneEntry entry : StoneZoneEntry.values()) {
                    if (!entry.isVanilla()) continue;

                    try {
                        StaticResource recipeTemplate = StaticResource.getOrThrow(manager,
                                ResType.RECIPES.getPath(entry.stoneRecipe()));

                        sink.addSimilarJsonResource(
                                manager,
                                recipeTemplate,
                                text -> text
                                        .replace("firma_compat:mossy_loose_andesite", mossyLooseRes.toString())
                                        .replace("firma_compat:loose_andesite", looseRes.toString())
                                        .replace("firma_compat:andesite_brick", brickRes.toString())
                                        .replace("firma_compat:andesite_" + entry.getSerializedName(),
                                                "stonezone:tfc/" + stoneType.getNamespace() + "/" + stoneType.getTypeName() + "_" + entry.getSerializedName()),
                                path -> replacePathName(path, entry, stoneType)
                        );
                    } catch (Exception e) {
                        FirmaCompat.LOGGER.debug("Failed to grab recipe for {}", entry.stoneRecipe());
                    }
                }
            }

            for (CompatVein vein : CompatVein.values()) {
                buildReplacementRules(vein);
            }
        });
    }

    public final String replacePathName(String path, StoneZoneEntry entry, StoneType stoneType) {
        String mossyReplacement = path.replace("mossy_andesite",stoneType.getNamespace() + "/mossy_" + stoneType.getTypeName());
        String replacement = path.replace("andesite",stoneType.getNamespace() + "/" + stoneType.getTypeName());

        return entry.name().startsWith("MOSSY") ? mossyReplacement : replacement;
    }
    private List<Pair<BlockState, Double>> buildReplacementWeights(CompatVein vein) {
        List<Pair<BlockState, Double>> weights = new ArrayList<>();

        addStoneZoneReplacements(weights, vein);
        addCompatRockReplacements(weights, vein);

        return weights;
    }

    /**
     * Builds the replacement rules for a vein.
     *
     * Ungraded ores use direct one-to-one replacement rules:
     *
     * {
     *   "replace": ["minecraft:stone"],
     *   "with": [
     *     { "block": "firma_compat:stone_amethyst_ore" }
     *   ]
     * }
     *
     * Graded ores use weighted replacement lists.
     */
    private JsonArray buildReplacementRules(CompatVein vein) {
        JsonArray rules = new JsonArray();

        /*
         * StoneZone types are registered dynamically, so iterate its registry
         * rather than maintaining a static list.
         */
        addStoneZoneReplacementRules(rules, vein);

        /*
         * CompatRock is registered elsewhere.
         * Append these after the StoneZone entries.
         */
        addCompatRockReplacementRules(rules, vein);

        return rules;
    }

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
             * Minecraft/TFC rocks are represented by CompatRock and are
             * therefore generated separately below.
             */
            if (namespace.equals("minecraft") || namespace.equals("tfc")) {
                continue;
            }

            stoneTypes.add(stoneType);
        }

        /*
         * Resource generation should be deterministic even though the registry
         * iteration order is not something we should depend on.
         */
        stoneTypes.sort(
                Comparator.comparing(
                        stoneType -> stoneType.getNamespace()
                                + ":"
                                + stoneType.getTypeName()
                )
        );

        for (StoneType stoneType : stoneTypes) {
            addStoneZoneReplacementRule(rules, vein, stoneType);
        }
    }

    private void addStoneZoneReplacementRule(
            JsonArray rules,
            CompatVein vein,
            StoneType stoneType
    ) {
        String oreName = vein.ore.name().toLowerCase(Locale.ROOT);

        if (!vein.ore.isGraded()) {
            SimpleEntrySet<StoneType, Block> entrySet =
                    ORE_ENTRY_SETS.get("ungraded_" + oreName);

            if (entrySet == null) {
                return;
            }

            Block oreBlock = entrySet.blocks.get(stoneType);

            if (isInvalidBlock(oreBlock)) {
                return;
            }

            rules.add(createDirectReplacementRule(
                    BuiltInRegistries.BLOCK.getKey(stoneType.block),
                    oreBlock
            ));

            return;
        }

        SimpleEntrySet<StoneType, Block> poorSet =
                ORE_ENTRY_SETS.get("poor_" + oreName);

        SimpleEntrySet<StoneType, Block> normalSet =
                ORE_ENTRY_SETS.get("normal_" + oreName);

        SimpleEntrySet<StoneType, Block> richSet =
                ORE_ENTRY_SETS.get("rich_" + oreName);

        if (poorSet == null || normalSet == null || richSet == null) {
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

        List<Pair<BlockState, Double>> weights =
                getGradedWeights(vein, poor, normal, rich);

        rules.add(createWeightedReplacementRule(
                BuiltInRegistries.BLOCK.getKey(stoneType.block),
                weights
        ));
    }

    private void addCompatRockReplacementRules(
            JsonArray rules,
            CompatVein vein
    ) {
        for (CompatRock rock : CompatRock.VALUES) {
            if (rock == null || rock.rockMaterial() == null) {
                continue;
            }

            Block target = rock.rockMaterial().raw().base().get();

            if (isInvalidBlock(target)) {
                continue;
            }

            if (!vein.ore.isGraded()) {
                addUngradedCompatRockRule(rules, vein, target, rock);
            } else {
                addGradedCompatRockRule(rules, vein, target, rock);
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

        rules.add(createDirectReplacementRule(
                BuiltInRegistries.BLOCK.getKey(target),
                oreBlock
        ));
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

        var poorId = gradeMap.get(Ore.Grade.POOR);
        var normalId = gradeMap.get(Ore.Grade.NORMAL);
        var richId = gradeMap.get(Ore.Grade.RICH);

        if (poorId == null || normalId == null || richId == null) {
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

        rules.add(createWeightedReplacementRule(
                BuiltInRegistries.BLOCK.getKey(target),
                getGradedWeights(vein, poor, normal, rich)
        ));
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
                BuiltInRegistries.BLOCK.getKey(replacement).toString()
        );

        /*
         * Deliberately no "weight" property.
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
                    BuiltInRegistries.BLOCK.getKey(state.getBlock());

            JsonObject entry = new JsonObject();
            entry.addProperty("block", blockId.toString());
            entry.addProperty("weight", pair.getSecond());

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
                    Pair.of(poor.defaultBlockState(), 70.0),
                    Pair.of(normal.defaultBlockState(), 25.0),
                    Pair.of(rich.defaultBlockState(), 5.0)
            );

            case NORMAL -> List.of(
                    Pair.of(poor.defaultBlockState(), 35.0),
                    Pair.of(normal.defaultBlockState(), 40.0),
                    Pair.of(rich.defaultBlockState(), 25.0)
            );

            case RICH -> List.of(
                    Pair.of(poor.defaultBlockState(), 15.0),
                    Pair.of(normal.defaultBlockState(), 25.0),
                    Pair.of(rich.defaultBlockState(), 60.0)
            );
        };
    }



    /**
     * Adds ore replacement blocks generated by StoneZone.
     *
     * StoneZone's registry is dynamic, so we iterate the registry rather than
     * maintaining our own list of stone types. This means stones added by other
     * mods during startup are automatically included.
     */
    private void addStoneZoneReplacements(
            List<Pair<BlockState, Double>> weights,
            CompatVein vein
    ) {
        String oreName = vein.ore.name().toLowerCase(Locale.ROOT);

        // Sort for deterministic generated JSON.
        List<StoneType> stoneTypes = new ArrayList<>();

        for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
            if (stoneType == null) continue;

            /*
             * Vanilla Minecraft and TFC rocks are represented by CompatRock.
             * StoneZone entries from other mods are handled here.
             */
            String namespace = stoneType.getNamespace();

            if (namespace.equals("minecraft") || namespace.equals("tfc")) {
                continue;
            }

            stoneTypes.add(stoneType);
        }

        stoneTypes.sort(
                Comparator.comparing(
                        stoneType -> stoneType.getNamespace() + ":" + stoneType.getTypeName()
                )
        );

        for (StoneType stoneType : stoneTypes) {
            addStoneZoneReplacement(weights, vein, stoneType, oreName);
        }
    }

    /**
     * Adds the appropriate generated ore block for one StoneZone stone type.
     */
    private void addStoneZoneReplacement(
            List<Pair<BlockState, Double>> weights,
            CompatVein vein,
            StoneType stoneType,
            String oreName
    ) {
        if (!vein.ore.isGraded()) {
            SimpleEntrySet<StoneType, Block> entrySet =
                    ORE_ENTRY_SETS.get("ungraded_" + oreName);

            if (entrySet == null) {
                return;
            }

            Block oreBlock = entrySet.blocks.get(stoneType);

            if (isInvalidBlock(oreBlock)) {
                return;
            }

            /*
             * Ungraded ores are a direct one-to-one replacement.
             *
             * The weight of 1.0 is intentional: this is the only possible
             * replacement for this particular stone type.
             */
            weights.add(Pair.of(
                    oreBlock.defaultBlockState(),
                    1.0
            ));

            return;
        }

        SimpleEntrySet<StoneType, Block> poorSet =
                ORE_ENTRY_SETS.get("poor_" + oreName);

        SimpleEntrySet<StoneType, Block> normalSet =
                ORE_ENTRY_SETS.get("normal_" + oreName);

        SimpleEntrySet<StoneType, Block> richSet =
                ORE_ENTRY_SETS.get("rich_" + oreName);

        if (poorSet == null || normalSet == null || richSet == null) {
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

        addGradedWeights(weights, vein, poor, normal, rich);
    }

    /**
     * Adds ore replacement blocks for the TFC/compatibility rocks represented
     * by CompatRock.
     */
    private void addCompatRockReplacements(
            List<Pair<BlockState, Double>> weights,
            CompatVein vein
    ) {
        if (!vein.ore.isGraded()) {
            addUngradedCompatRockReplacements(weights, vein);
        } else {
            addGradedCompatRockReplacements(weights, vein);
        }
    }

    private void addUngradedCompatRockReplacements(
            List<Pair<BlockState, Double>> weights,
            CompatVein vein
    ) {
        for (CompatRock rock : CompatRock.VALUES) {
            var oreMap = ModBlocks.ORES.get(rock);

            if (oreMap == null) {
                continue;
            }

            var oreId = oreMap.get(vein.ore);

            if (oreId == null) {
                continue;
            }

            Block oreBlock = oreId.get();

            if (isInvalidBlock(oreBlock)) {
                continue;
            }

            /*
             * Ungraded ores are direct replacements, so there is only one
             * possible state and therefore its weight is 1.0.
             */
            weights.add(Pair.of(
                    oreBlock.defaultBlockState(),
                    1.0
            ));
        }
    }

    private void addGradedCompatRockReplacements(
            List<Pair<BlockState, Double>> weights,
            CompatVein vein
    ) {
        var gradedOreMap = ModBlocks.GRADED_ORES;

        for (CompatRock rock : CompatRock.VALUES) {
            var oreMap = gradedOreMap.get(rock);

            if (oreMap == null) {
                continue;
            }

            var gradeMap = oreMap.get(vein.ore);

            if (gradeMap == null) {
                continue;
            }

            var poorId = gradeMap.get(Ore.Grade.POOR);
            var normalId = gradeMap.get(Ore.Grade.NORMAL);
            var richId = gradeMap.get(Ore.Grade.RICH);

            if (poorId == null || normalId == null || richId == null) {
                continue;
            }

            Block poor = poorId.get();
            Block normal = normalId.get();
            Block rich = richId.get();

            if (isInvalidBlock(poor)
                    || isInvalidBlock(normal)
                    || isInvalidBlock(rich)) {
                continue;
            }

            addGradedWeights(weights, vein, poor, normal, rich);
        }
    }

    private static void addGradedWeights(
            List<Pair<BlockState, Double>> weights,
            CompatVein vein,
            Block poor,
            Block normal,
            Block rich
    ) {
        switch (vein.gradedVeinClass) {
            case SURFACE -> {
                weights.add(Pair.of(poor.defaultBlockState(), 70.0));
                weights.add(Pair.of(normal.defaultBlockState(), 25.0));
                weights.add(Pair.of(rich.defaultBlockState(), 5.0));
            }

            case NORMAL -> {
                weights.add(Pair.of(poor.defaultBlockState(), 35.0));
                weights.add(Pair.of(normal.defaultBlockState(), 40.0));
                weights.add(Pair.of(rich.defaultBlockState(), 25.0));
            }

            case RICH -> {
                weights.add(Pair.of(poor.defaultBlockState(), 15.0));
                weights.add(Pair.of(normal.defaultBlockState(), 25.0));
                weights.add(Pair.of(rich.defaultBlockState(), 60.0));
            }
        }
    }

    private static boolean isInvalidBlock(Block block) {
        return block == null || block == Blocks.AIR;
    }


}