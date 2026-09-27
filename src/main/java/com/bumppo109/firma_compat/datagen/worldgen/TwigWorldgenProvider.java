package com.bumppo109.firma_compat.datagen.worldgen;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.CompatWood.BlockType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public class TwigWorldgenProvider implements DataProvider {

    private final PackOutput output;

    public TwigWorldgenProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        final Path outputPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK);

        final List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CompatWood wood : CompatWood.VALUES) {
            /*
             * Only generate twig worldgen when this wood actually has
             * a TWIG variant.
             *
             * This excludes BAMBOO with your current hasVariant() logic.
             */
            if (!BlockType.TWIG.hasVariant(wood)) {
                continue;
            }

            futures.add(generateTwigPatchPlaced(cache, outputPath, wood));
            futures.add(generateTwigPatchConfigured(cache, outputPath, wood));

            futures.add(generateTwigPlaced(cache, outputPath, wood));
            futures.add(generateTwigConfigured(cache, outputPath, wood));
        }

        return CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * Names / paths
     * -------------------------------------------------------------------------
     */

    private static String twigName(CompatWood wood) {
        return BlockType.TWIG.nameFor(wood);
    }

    private static String twigPatchName(CompatWood wood) {
        return twigName(wood) + "_patch";
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

    /*
     * -------------------------------------------------------------------------
     * Log tag
     * -------------------------------------------------------------------------
     *
     * Normal overworld woods:
     *
     *   minecraft:acacia_logs
     *   minecraft:birch_logs
     *   minecraft:cherry_logs
     *   minecraft:dark_oak_logs
     *   minecraft:jungle_logs
     *   minecraft:mangrove_logs
     *   minecraft:oak_logs
     *   minecraft:spruce_logs
     *
     * Nether woods use stems:
     *
     *   minecraft:crimson_stems
     *   minecraft:warped_stems
     * -------------------------------------------------------------------------
     */

    private static String logTag(CompatWood wood) {
        return switch (wood) {
            case CRIMSON -> "minecraft:crimson_stems";
            case WARPED -> "minecraft:warped_stems";
            default -> "minecraft:" + wood.getSerializedName() + "_logs";
        };
    }

    /*
     * -------------------------------------------------------------------------
     * PLACED FEATURE #1
     *
     * <wood>_twig_patch
     *
     * {
     *   "feature": "firma_compat:twig/acacia_patch",
     *   "placement": [
     *     {
     *       "type": "minecraft:count",
     *       "count": 16
     *     },
     *     {
     *       "type": "minecraft:in_square"
     *     },
     *     {
     *       "type": "minecraft:heightmap",
     *       "heightmap": "MOTION_BLOCKING_NO_LEAVES"
     *     },
     *     {
     *       "type": "minecraft:block_predicate_filter",
     *       "predicate": {
     *         "type": "minecraft:any_of",
     *         "predicates": [...]
     *       }
     *     },
     *     {
     *       "type": "minecraft:biome"
     *     }
     *   ]
     * }
     *
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generateTwigPatchPlaced(
            CachedOutput cache,
            Path outputPath,
            CompatWood wood
    ) {
        String twig = twigName(wood);
        String patch = twigPatchName(wood);
        String logTag = logTag(wood);

        JsonObject root = new JsonObject();

        root.addProperty(
                "feature",
                FirmaCompat.MODID + ":" + twig + "_patch"
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
        count.addProperty(
                "count",
                16
        );

        placement.add(count);

        /*
         * In square
         */

        JsonObject inSquare = new JsonObject();
        inSquare.addProperty(
                "type",
                "minecraft:in_square"
        );

        placement.add(inSquare);

        /*
         * Heightmap
         */

        JsonObject heightmap = new JsonObject();
        heightmap.addProperty(
                "type",
                "minecraft:heightmap"
        );
        heightmap.addProperty(
                "heightmap",
                "MOTION_BLOCKING_NO_LEAVES"
        );

        placement.add(heightmap);

        /*
         * Block predicate filter
         */

        JsonObject blockPredicateFilter = new JsonObject();
        blockPredicateFilter.addProperty(
                "type",
                "minecraft:block_predicate_filter"
        );

        JsonObject anyOf = new JsonObject();
        anyOf.addProperty(
                "type",
                "minecraft:any_of"
        );

        JsonArray predicates = new JsonArray();

        /*
         * Center
         */

        predicates.add(
                matchingLogTag(logTag, 0, 0, 0)
        );

        /*
         * Cardinal directions
         */

        predicates.add(
                matchingLogTag(logTag, -1, 0, 0)
        );

        predicates.add(
                matchingLogTag(logTag, 1, 0, 0)
        );

        predicates.add(
                matchingLogTag(logTag, 0, 0, -1)
        );

        predicates.add(
                matchingLogTag(logTag, 0, 0, 1)
        );

        /*
         * Diagonals
         */

        predicates.add(
                matchingLogTag(logTag, 1, 0, 1)
        );

        predicates.add(
                matchingLogTag(logTag, 1, 0, -1)
        );

        predicates.add(
                matchingLogTag(logTag, -1, 0, 1)
        );

        predicates.add(
                matchingLogTag(logTag, -1, 0, -1)
        );

        anyOf.add(
                "predicates",
                predicates
        );

        blockPredicateFilter.add(
                "predicate",
                anyOf
        );

        placement.add(blockPredicateFilter);

        /*
         * Biome
         */

        JsonObject biome = new JsonObject();
        biome.addProperty(
                "type",
                "minecraft:biome"
        );

        placement.add(biome);

        root.add(
                "placement",
                placement
        );

        return DataProvider.saveStable(
                cache,
                root,
                placedPath(outputPath, patch)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * Helper for matching_block_tag
     * -------------------------------------------------------------------------
     */

    private static JsonObject matchingLogTag(
            String tag,
            int x,
            int y,
            int z
    ) {
        JsonObject predicate = new JsonObject();

        predicate.addProperty(
                "type",
                "minecraft:matching_block_tag"
        );

        predicate.addProperty(
                "tag",
                tag
        );

        /*
         * The center predicate has no offset in the source JSON.
         */

        if (x != 0 || y != 0 || z != 0) {
            JsonArray offset = new JsonArray();

            offset.add(x);
            offset.add(y);
            offset.add(z);

            predicate.add(
                    "offset",
                    offset
            );
        }

        return predicate;
    }

    /*
     * -------------------------------------------------------------------------
     * CONFIGURED FEATURE #1
     *
     * <wood>_twig_patch
     *
     * {
     *   "type": "minecraft:random_patch",
     *   "config": {
     *     "tries": 8,
     *     "xz_spread": 5,
     *     "y_spread": 3,
     *     "feature": "firma_compat:twig/acacia"
     *   }
     * }
     *
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generateTwigPatchConfigured(
            CachedOutput cache,
            Path outputPath,
            CompatWood wood
    ) {
        String twig = twigName(wood);
        String patch = twigPatchName(wood);

        JsonObject root = new JsonObject();

        root.addProperty(
                "type",
                "minecraft:random_patch"
        );

        JsonObject config = new JsonObject();

        config.addProperty(
                "tries",
                8
        );

        config.addProperty(
                "xz_spread",
                5
        );

        config.addProperty(
                "y_spread",
                3
        );

        config.addProperty(
                "feature",
                FirmaCompat.MODID + ":" + twig
        );

        root.add(
                "config",
                config
        );

        return DataProvider.saveStable(
                cache,
                root,
                configuredPath(outputPath, patch)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * PLACED FEATURE #2
     *
     * <wood>_twig
     *
     * {
     *   "feature": "firma_compat:twig/acacia",
     *   "placement": [
     *     {
     *       "type": "minecraft:block_predicate_filter",
     *       "predicate": {
     *         "type": "minecraft:matching_block_tag",
     *         "tag": "minecraft:replaceable"
     *       }
     *     },
     *     {
     *       "type": "block_predicate_filter",
     *       "predicate": {
     *         "type": "tfc:would_survive_with_fluid",
     *         "state": {
     *           "Name": "firma_compat:acacia_twig",
     *           "Properties": {
     *             "fluid": "empty"
     *           }
     *         }
     *       }
     *     }
     *   ]
     * }
     *
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generateTwigPlaced(
            CachedOutput cache,
            Path outputPath,
            CompatWood wood
    ) {
        String twig = twigName(wood);
        String blockId = FirmaCompat.MODID + ":" + twig;

        JsonObject root = new JsonObject();

        root.addProperty(
                "feature",
                blockId
        );

        JsonArray placement = new JsonArray();

        /*
         * Replaceable filter
         */

        JsonObject replaceableFilter = new JsonObject();

        replaceableFilter.addProperty(
                "type",
                "minecraft:block_predicate_filter"
        );

        JsonObject replaceablePredicate = new JsonObject();

        replaceablePredicate.addProperty(
                "type",
                "minecraft:matching_block_tag"
        );

        replaceablePredicate.addProperty(
                "tag",
                "minecraft:replaceable"
        );

        replaceableFilter.add(
                "predicate",
                replaceablePredicate
        );

        placement.add(replaceableFilter);

        /*
         * TFC would_survive_with_fluid
         */

        JsonObject surviveFilter = new JsonObject();

        /*
         * Your source JSON uses the unqualified ID here:
         *
         * "type": "block_predicate_filter"
         *
         * Keep it exactly as supplied.
         */
        surviveFilter.addProperty(
                "type",
                "block_predicate_filter"
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

        properties.addProperty(
                "fluid",
                "empty"
        );

        state.add(
                "Properties",
                properties
        );

        survivePredicate.add(
                "state",
                state
        );

        surviveFilter.add(
                "predicate",
                survivePredicate
        );

        placement.add(surviveFilter);

        root.add(
                "placement",
                placement
        );

        return DataProvider.saveStable(
                cache,
                root,
                placedPath(outputPath, twig)
        );
    }

    /*
     * -------------------------------------------------------------------------
     * CONFIGURED FEATURE #2
     *
     * <wood>_twig
     *
     * {
     *   "type": "tfc:block_with_fluid",
     *   "config": {
     *     "to_place": {
     *       "type": "minecraft:simple_state_provider",
     *       "state": {
     *         "Name": "firma_compat:acacia_twig",
     *         "Properties": {
     *           "fluid": "empty"
     *         }
     *       }
     *     }
     *   }
     * }
     *
     * -------------------------------------------------------------------------
     */

    private CompletableFuture<?> generateTwigConfigured(
            CachedOutput cache,
            Path outputPath,
            CompatWood wood
    ) {
        String twig = twigName(wood);
        String blockId = FirmaCompat.MODID + ":" + twig;

        JsonObject root = new JsonObject();

        root.addProperty(
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

        properties.addProperty(
                "fluid",
                "empty"
        );

        state.add(
                "Properties",
                properties
        );

        toPlace.add(
                "state",
                state
        );

        config.add(
                "to_place",
                toPlace
        );

        root.add(
                "config",
                config
        );

        return DataProvider.saveStable(
                cache,
                root,
                configuredPath(outputPath, twig)
        );
    }

    @Override
    public String getName() {
        return "Firma Compat - Twig Worldgen";
    }
}
