package com.bumppo109.firma_compat.datagen.worldgen;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatRock.BlockType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class LooseRockWorldgenProvider implements DataProvider {

    private final PackOutput output;

    public LooseRockWorldgenProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        final Path outputPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK);

        final List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CompatRock rock : CompatRock.VALUES) {
            futures.add(generatePatchConfigured(cache, outputPath, rock));
            futures.add(generatePatchPlaced(cache, outputPath, rock));

            futures.add(generateLooseConfigured(cache, outputPath, rock));
            futures.add(generateLoosePlaced(cache, outputPath, rock));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    /*
     * -------------------------------------------------------------------------
     * Names / paths
     * -------------------------------------------------------------------------
     */

    private static String looseName(CompatRock rock) {
        return rock.getBlock(BlockType.LOOSE)
                .get()
                .getDescriptionId()
                .replace("block." + FirmaCompat.MODID + ".", "");
    }

    private static String loosePatchName(CompatRock rock) {
        return looseName(rock) + "_patch";
    }

    private static Path configuredPath(Path outputPath, String name) {
        return outputPath
                .resolve(FirmaCompat.MODID)
                .resolve("worldgen")
                .resolve("configured_feature")
                .resolve(name + ".json");
    }

    private static Path placedPath(Path outputPath, String name) {
        return outputPath
                .resolve(FirmaCompat.MODID)
                .resolve("worldgen")
                .resolve("placed_feature")
                .resolve(name + ".json");
    }

    private static String blockId(CompatRock rock) {
        Block block = rock.getBlock(BlockType.LOOSE).get();

        ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.BLOCK
                .getKey(block);

        if (id == null) {
            throw new IllegalStateException(
                    "Could not find registry id for loose rock block: " + block
            );
        }

        return id.toString();
    }

    /*
     * -------------------------------------------------------------------------
     * CONFIGURED FEATURE #1
     *
     * loose_<rock>_patch
     *
     * {
     *   "type": "minecraft:random_patch",
     *   "config": {
     *     "tries": 5,
     *     "xz_spread": 15,
     *     "y_spread": 1,
     *     "feature": "firma_compat:loose_<rock>"
     *   }
     * }
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generatePatchConfigured(
            CachedOutput cache,
            Path outputPath,
            CompatRock rock
    ) {
        String loose = looseName(rock);
        String patch = loosePatchName(rock);

        JsonObject root = new JsonObject();

        root.addProperty("type", "minecraft:random_patch");

        JsonObject config = new JsonObject();
        config.addProperty("tries", 5);
        config.addProperty("xz_spread", 15);
        config.addProperty("y_spread", 1);
        config.addProperty(
                "feature",
                FirmaCompat.MODID + ":" + loose
        );

        root.add("config", config);

        return DataProvider.saveStable(
                cache,
                root,
                configuredPath(outputPath, patch)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * PLACED FEATURE #1
     *
     * loose_<rock>_patch
     *
     * {
     *   "feature": "firma_compat:loose_<rock>_patch",
     *   "placement": [
     *     {
     *       "type": "minecraft:count",
     *       "count": 8
     *     },
     *     {
     *       "type": "minecraft:in_square"
     *     },
     *     {
             "type": "minecraft:rarity_filter",
             "chance": 4
           },
     *     {
              "type": "minecraft:heightmap",
              "heightmap": "OCEAN_FLOOR_WG"
           },
     *     {
     *       "type": "minecraft:environment_scan",
     *       "direction_of_search": "down",
     *       "max_steps": 8,
     *       "target_condition": {
     *         "type": "minecraft:matching_blocks",
     *         "blocks": "minecraft:stone"
     *       }
     *     }
     *   ]
     * }
     *
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generatePatchPlaced(
            CachedOutput cache,
            Path outputPath,
            CompatRock rock
    ) {
        String loose = looseName(rock);
        String patch = loosePatchName(rock);

        JsonObject root = new JsonObject();

        root.addProperty(
                "feature",
                FirmaCompat.MODID + ":" + patch
        );

        JsonArray placement = new JsonArray();

        /*
         * Count
         */

        JsonObject count = new JsonObject();

        count.addProperty(
                "type",
                "minecraft:count"
        );

        count.addProperty("count", 8);

        placement.add(count);

        /*
         * In Square
         */

        JsonObject square = new JsonObject();

        square.addProperty(
                "type",
                "minecraft:in_square"
        );

        placement.add(square);

        /*
         * Rarity filter
         */

        JsonObject rarity = new JsonObject();

        rarity.addProperty(
                "type",
                "minecraft:rarity_filter"
        );

        rarity.addProperty("chance", 4);

        placement.add(rarity);

        /*
         * Heightmap
         */

        JsonObject height = new JsonObject();

        height.addProperty(
                "type",
                "minecraft:heightmap"
        );

        height.addProperty("heightmap","OCEAN_FLOOR_WG");

        placement.add(height);

        /*
         * Environment scan
         */

        JsonObject environmentScan = new JsonObject();

        environmentScan.addProperty(
                "type",
                "minecraft:environment_scan"
        );

        environmentScan.addProperty(
                "direction_of_search",
                "down"
        );

        environmentScan.addProperty(
                "max_steps",
                8
        );

        JsonObject targetCondition = new JsonObject();

        targetCondition.addProperty(
                "type",
                "minecraft:matching_blocks"
        );

        targetCondition.addProperty(
                "blocks",
                BuiltInRegistries.BLOCK.getKey(rock.base()).toString()
        );

        environmentScan.add(
                "target_condition",
                targetCondition
        );

        placement.add(environmentScan);

        root.add("placement", placement);

        return DataProvider.saveStable(
                cache,
                root,
                placedPath(outputPath, patch)
        );
    }


    /*
     * -------------------------------------------------------------------------
     * CONFIGURED FEATURE #2
     *
     * loose_<rock>
     *
     * Random selector:
     *
     * 30% -> count 2
     * 20% -> count 3
     * 50% -> count 1
     *
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generateLooseConfigured(
            CachedOutput cache,
            Path outputPath,
            CompatRock rock
    ) {
        String loose = looseName(rock);
        String blockId = blockId(rock);

        JsonObject root = new JsonObject();

        root.addProperty(
                "type",
                "minecraft:random_selector"
        );

        JsonObject config = new JsonObject();

        /*
         * features
         */

        JsonArray features = new JsonArray();

        features.add(weightedBlockWithFluidFeature(
                blockId,
                2,
                0.3F
        ));

        features.add(weightedBlockWithFluidFeature(
                blockId,
                3,
                0.2F
        ));

        config.add("features", features);

        /*
         * default
         */

        config.add(
                "default",
                placedBlockWithFluidFeature(blockId, 1)
        );

        root.add("config", config);

        return DataProvider.saveStable(
                cache,
                root,
                configuredPath(outputPath, loose)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * PLACED FEATURE #2
     *
     * loose_<rock>
     *
     * This is the placement/filter chain from the supplied JSON.
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generateLoosePlaced(
            CachedOutput cache,
            Path outputPath,
            CompatRock rock
    ) {
        String loose = looseName(rock);
        String blockId = blockId(rock);

        JsonObject root = new JsonObject();

        /*
         * feature
         */

        root.addProperty(
                "feature",
                FirmaCompat.MODID + ":" + loose
        );

        JsonArray placement = new JsonArray();

        /*
         * 1. heightmap
         */

        JsonObject heightmap = new JsonObject();
        heightmap.addProperty(
                "type",
                "minecraft:heightmap"
        );
        heightmap.addProperty(
                "heightmap",
                "OCEAN_FLOOR_WG"
        );

        placement.add(heightmap);

        /*
         * 2. Air / water matching block predicate
         */

        JsonObject airWaterFilter = new JsonObject();
        airWaterFilter.addProperty(
                "type",
                "minecraft:block_predicate_filter"
        );

        JsonObject airWaterPredicate = new JsonObject();
        airWaterPredicate.addProperty(
                "type",
                "minecraft:matching_blocks"
        );

        JsonArray airWaterBlocks = new JsonArray();
        airWaterBlocks.add("minecraft:air");
        airWaterBlocks.add("minecraft:water");

        airWaterPredicate.add(
                "blocks",
                airWaterBlocks
        );

        airWaterFilter.add(
                "predicate",
                airWaterPredicate
        );

        placement.add(airWaterFilter);

        /*
         * 3. Not:
         *
         * all_of:
         *   block above is air
         *   current block is water
         */

        JsonObject notFilter = new JsonObject();
        notFilter.addProperty(
                "type",
                "minecraft:block_predicate_filter"
        );

        JsonObject notPredicate = new JsonObject();
        notPredicate.addProperty(
                "type",
                "minecraft:not"
        );

        JsonObject allOf = new JsonObject();
        allOf.addProperty(
                "type",
                "minecraft:all_of"
        );

        JsonArray predicates = new JsonArray();

        /*
         * Above must be air
         */

        JsonObject aboveAir = new JsonObject();
        aboveAir.addProperty(
                "type",
                "minecraft:matching_blocks"
        );

        JsonArray aboveOffset = new JsonArray();
        aboveOffset.add(0);
        aboveOffset.add(1);
        aboveOffset.add(0);

        aboveAir.add("offset", aboveOffset);
        aboveAir.addProperty("blocks", "minecraft:air");

        predicates.add(aboveAir);

        /*
         * Current block must be water
         */

        JsonObject currentWater = new JsonObject();
        currentWater.addProperty(
                "type",
                "minecraft:matching_blocks"
        );
        currentWater.addProperty(
                "blocks",
                "minecraft:water"
        );

        predicates.add(currentWater);

        allOf.add("predicates", predicates);
        notPredicate.add("predicate", allOf);
        notFilter.add("predicate", notPredicate);

        placement.add(notFilter);

        /*
         * 4. TFC would_survive_with_fluid
         */

        JsonObject surviveFilter = new JsonObject();
        surviveFilter.addProperty(
                "type",
                "minecraft:block_predicate_filter"
        );

        JsonObject survivePredicate = new JsonObject();
        survivePredicate.addProperty(
                "type",
                "tfc:would_survive_with_fluid"
        );

        JsonObject state = new JsonObject();

        state.addProperty(
                "Name",
                blockId
        );

        JsonObject properties = new JsonObject();
        properties.addProperty("fluid", "empty");
        properties.addProperty("count", "1");

        state.add("Properties", properties);

        survivePredicate.add("state", state);
        surviveFilter.add("predicate", survivePredicate);

        placement.add(surviveFilter);

        root.add("placement", placement);

        return DataProvider.saveStable(
                cache,
                root,
                placedPath(outputPath, loose)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * RANDOM SELECTOR HELPERS
     * -------------------------------------------------------------------------
     */

    private static JsonObject weightedBlockWithFluidFeature(
            String blockId,
            int count,
            float chance
    ) {
        JsonObject weighted = new JsonObject();

        weighted.addProperty("chance", chance);
        weighted.add(
                "feature",
                placedBlockWithFluidFeature(blockId, count)
        );

        return weighted;
    }

    /**
     * Produces:
     *
     * {
     *   "feature": {
     *     "type": "tfc:block_with_fluid",
     *     "config": {
     *       "to_place": {
     *         "type": "minecraft:simple_state_provider",
     *         "state": {
     *           "Name": "...",
     *           "Properties": {
     *             "fluid": "empty",
     *             "count": "2"
     *           }
     *         }
     *       }
     *     }
     *   },
     *   "placement": []
     * }
     */
    private static JsonObject placedBlockWithFluidFeature(
            String blockId,
            int count
    ) {
        JsonObject placedFeature = new JsonObject();

        JsonObject feature = new JsonObject();
        feature.addProperty(
                "type",
                "tfc:block_with_fluid"
        );

        JsonObject config = new JsonObject();

        JsonObject toPlace = new JsonObject();
        toPlace.addProperty(
                "type",
                "minecraft:simple_state_provider"
        );

        JsonObject state = new JsonObject();
        state.addProperty(
                "Name",
                blockId
        );

        JsonObject properties = new JsonObject();

        /*
         * BlockState properties are serialized as strings.
         *
         * This is why count is deliberately:
         *
         *     "count": "2"
         *
         * rather than:
         *
         *     "count": 2
         */
        properties.addProperty("fluid", "empty");
        properties.addProperty("count", Integer.toString(count));

        state.add("Properties", properties);

        toPlace.add("state", state);
        config.add("to_place", toPlace);

        feature.add("config", config);

        placedFeature.add("feature", feature);

        placedFeature.add(
                "placement",
                new JsonArray()
        );

        return placedFeature;
    }

    @Override
    public String getName() {
        return "Firma Compat - CompatRock Worldgen";
    }
}
