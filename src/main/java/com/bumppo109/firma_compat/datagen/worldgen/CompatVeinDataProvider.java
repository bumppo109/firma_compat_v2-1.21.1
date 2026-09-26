package com.bumppo109.firma_compat.datagen.worldgen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.world.CompatVein;
import com.bumppo109.firma_compat.world.CompatVein.GradedVeinClass;
import com.bumppo109.firma_compat.world.CompatVein.VeinType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CompatVeinDataProvider implements DataProvider {

    private final PackOutput.PathProvider configuredFeaturePath;
    private final PackOutput.PathProvider placedFeaturePath;

    public CompatVeinDataProvider(PackOutput output) {
        this.configuredFeaturePath = output.createPathProvider(
                PackOutput.Target.DATA_PACK,
                "worldgen/configured_feature"
        );

        this.placedFeaturePath = output.createPathProvider(
                PackOutput.Target.DATA_PACK,
                "worldgen/placed_feature"
        );
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (CompatVein vein : CompatVein.values()) {
            String name = vein.name().toLowerCase(java.util.Locale.ROOT);

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "vein/" + name
            );

            Path configuredPath = configuredFeaturePath.json(id);
            Path placedPath = placedFeaturePath.json(id);

            futures.add(DataProvider.saveStable(
                    output,
                    createConfiguredFeature(vein),
                    configuredPath
            ));

            futures.add(DataProvider.saveStable(
                    output,
                    createPlacedFeature(id),
                    placedPath
            ));
        }

        return CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new)
        );
    }

    private static JsonObject createPlacedFeature(ResourceLocation configuredFeatureId) {
        JsonObject json = new JsonObject();

        json.addProperty(
                "feature",
                configuredFeatureId.toString()
        );

        json.add("placement", new JsonArray());

        return json;
    }

    private static JsonObject createConfiguredFeature(CompatVein vein) {
        JsonObject root = new JsonObject();

        root.addProperty("type", featureType(vein.veinType));

        JsonObject config = new JsonObject();

        config.addProperty("rarity", vein.rarity);
        config.addProperty("density", vein.density);
        config.addProperty("min_y", vein.minY);
        config.addProperty("max_y", vein.maxY);

        if (vein.size != null) {
            config.addProperty("size", vein.size);
        }

        if (vein.height != null) {
            config.addProperty("height", vein.height);
        }

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

        config.addProperty(
                "random_name",
                vein.name().toLowerCase(java.util.Locale.ROOT)
        );

        config.add("blocks", createBlockReplacements(vein));

        if (vein.gradedVeinClass != null) {
            config.add("indicator", createIndicator(vein));
        }

        root.add("config", config);

        return root;
    }

    private static String featureType(VeinType type) {
        return switch (type) {
            case CLUSTER -> "tfc:cluster_vein";
            case DISC -> "tfc:disc_vein";
            case PIPE -> "tfc:pipe_vein";
        };
    }

    private static JsonArray createBlockReplacements(CompatVein vein) {
        JsonArray blocks = new JsonArray();

        for (CompatRock rock : CompatRock.VALUES) {
            JsonObject replacement = new JsonObject();

            JsonArray replace = new JsonArray();
            replace.add(BuiltInRegistries.BLOCK
                    .getKey(rock.base())
                    .toString());

            replacement.add("replace", replace);

            JsonArray with = new JsonArray();

            if (vein.gradedVeinClass == null) {
                JsonObject block = new JsonObject();

                block.addProperty(
                        "block",
                        oreBlockId(rock, vein.ore)
                );

                with.add(block);
            } else {
                addGradedReplacement(
                        with,
                        rock,
                        vein.ore,
                        vein.gradedVeinClass
                );
            }

            replacement.add("with", with);
            blocks.add(replacement);
        }

        return blocks;
    }

    private static void addGradedReplacement(
            JsonArray with,
            CompatRock rock,
            Ore ore,
            GradedVeinClass veinClass
    ) {
        int[] weights = gradedWeights(veinClass);

        Ore.Grade[] grades = {
                Ore.Grade.POOR,
                Ore.Grade.NORMAL,
                Ore.Grade.RICH
        };

        for (int i = 0; i < grades.length; i++) {
            JsonObject block = new JsonObject();

            block.addProperty("weight", weights[i]);
            block.addProperty(
                    "block",
                    gradedOreBlockId(rock, ore, grades[i])
            );

            with.add(block);
        }
    }

    private static int[] gradedWeights(GradedVeinClass veinClass) {
        return switch (veinClass) {
            /*
             * Surface veins:
             * 60% poor
             * 30% normal
             * 10% rich
             */
            case SURFACE -> new int[]{60, 30, 10};

            /*
             * Normal TFC vein:
             * 35% poor
             * 40% normal
             * 25% rich
             */
            case NORMAL -> new int[]{35, 40, 25};

            /*
             * Rich TFC vein:
             * 15% poor
             * 25% normal
             * 60% rich
             */
            case RICH -> new int[]{15, 25, 60};
        };
    }

    private static JsonObject createIndicator(CompatVein vein) {
        JsonObject indicator = new JsonObject();

        indicator.addProperty("rarity", vein.indicatorRarity);
        indicator.addProperty("depth", vein.indicatorDepth);
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

        blocks.add(block);

        indicator.add("blocks", blocks);

        return indicator;
    }

    private static String oreBlockId(
            CompatRock rock,
            Ore ore
    ) {
        return FirmaCompat.MODID
                + ":"
                + rock.getSerializedName()
                + "_"
                + ore.name().toLowerCase(java.util.Locale.ROOT)
                + "_ore";
    }

    private static String gradedOreBlockId(
            CompatRock rock,
            Ore ore,
            Ore.Grade grade
    ) {
        return FirmaCompat.MODID
                + ":"
                + grade.name().toLowerCase(java.util.Locale.ROOT)
                + "_"
                + rock.getSerializedName()
                + "_"
                + ore.name().toLowerCase(java.util.Locale.ROOT)
                + "_ore";
    }

    @Override
    public String getName() {
        return "Firma Compat Vein Features";
    }
}
