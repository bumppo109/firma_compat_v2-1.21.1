package com.bumppo109.firma_compat.datagen.worldgen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.world.CompatSingleBlockVein;
import com.bumppo109.firma_compat.world.CompatVein;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.world.feature.vein.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.*;

public class ModConfiguredFeatures {

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        //SingleBlock Vein
        for (CompatSingleBlockVein vein : CompatSingleBlockVein.values()) {
            String veinName = vein.name().toLowerCase(Locale.ROOT);

            // 1. Build replacement map
            Map<Block, IWeighted<BlockState>> blockSwapMap = buildSingleReplacementMap(vein);

            // 2. Optional indicator – use list constructor
            Optional<Indicator> indicator = Optional.empty();
            if (vein.indicator != null) {
                List<Pair<BlockState, Double>> indicatorWeights = List.of(
                        Pair.of(vein.indicator.defaultBlockState(), 1.0)
                );
                var states = new WeightedHelper<BlockState>(indicatorWeights);

                indicator = Optional.of(new Indicator(
                        vein.indicatorDepth != null ? vein.indicatorDepth : 3,
                        vein.indicatorRarity != null ? vein.indicatorRarity : 12,
                        vein.indicatorUnderRarity != null ? vein.indicatorUnderRarity : 3,
                        vein.indicatorCount != null ? vein.indicatorCount : 2,
                        states
                ));
            }

            // 3. Base config
            VeinConfig base = new VeinConfig(
                    blockSwapMap,
                    indicator,
                    vein.rarity,
                    vein.density,
                    vein.minY,
                    vein.maxY,
                    vein.project,
                    vein.project_offset,
                    veinSeedFromName(veinName),
                    false
            );

            // 4. Type-specific config + feature ID
            IVeinConfig config;
            ResourceLocation featureId;

            switch (vein.veinType) {
                case DISC -> {
                    config = new DiscVeinConfig(
                            base,
                            vein.size != null ? vein.size : 20,
                            vein.height != null ? vein.height : 4
                    );
                    featureId = ResourceLocation.fromNamespaceAndPath("tfc", "disc_vein");
                }
                case CLUSTER -> {
                    config = new ClusterVeinConfig(
                            base,
                            vein.size != null ? vein.size : 20
                    );
                    featureId = ResourceLocation.fromNamespaceAndPath("tfc", "cluster_vein");
                }
                case PIPE -> {
                    config = new PipeVeinConfig(
                            base,
                            vein.pipeHeight != null ? vein.pipeHeight : 60,
                            vein.radius != null ? vein.radius : 5,
                            vein.minSkew != null ? vein.minSkew : 5,
                            vein.maxSkew != null ? vein.maxSkew : 13,
                            vein.minSlant != null ? vein.minSlant : 0,
                            vein.maxSlant != null ? vein.maxSlant : 2,
                            vein.sign != null ? vein.sign.floatValue() : 0.0f
                    );
                    featureId = ResourceLocation.fromNamespaceAndPath("tfc", "pipe_vein");
                }
                default -> throw new IllegalStateException("Unknown vein type: " + vein.veinType);
            }

            // 5. Get Feature
            @SuppressWarnings("unchecked")
            Feature<IVeinConfig> feature = (Feature<IVeinConfig>) BuiltInRegistries.FEATURE.get(featureId);
            if (feature == null) {
                System.err.println("TFC vein feature missing: " + featureId);
                continue;
            }

            // 6. Register
            ConfiguredFeature<?, ?> configured = new ConfiguredFeature<>(feature, config);

            ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "overworld/vein/" + veinName)
            );

            context.register(key, configured);
            System.out.println("Registered vein configured feature: " + key.location());
        }

        //Vein
        for (CompatVein vein : CompatVein.values()) {
            String veinName = vein.name().toLowerCase(Locale.ROOT);

            // 1. Build replacement map
            Map<Block, IWeighted<BlockState>> blockSwapMap = buildReplacementMap(vein);

            // 2. Optional indicator – use list constructor
            Optional<Indicator> indicator = Optional.empty();
            if (vein.indicator != null) {
                List<Pair<BlockState, Double>> indicatorWeights = List.of(
                        Pair.of(vein.indicator.defaultBlockState(), 1.0)
                );
                var states = new WeightedHelper<BlockState>(indicatorWeights);

                indicator = Optional.of(new Indicator(
                        vein.indicatorDepth != null ? vein.indicatorDepth : 3,
                        vein.indicatorRarity != null ? vein.indicatorRarity : 12,
                        vein.indicatorUnderRarity != null ? vein.indicatorUnderRarity : 3,
                        vein.indicatorCount != null ? vein.indicatorCount : 2,
                        states
                ));
            }

            // 3. Base config
            VeinConfig base = new VeinConfig(
                    blockSwapMap,
                    indicator,
                    vein.rarity,
                    vein.density,
                    vein.minY,
                    vein.maxY,
                    false,  // aquifer
                    false,  // canReplaceFluids
                    veinSeedFromName(veinName),
                    false   // debug
            );

            // 4. Type-specific config + feature ID
            IVeinConfig config;
            ResourceLocation featureId;

            switch (vein.veinType) {
                case DISC -> {
                    config = new DiscVeinConfig(
                            base,
                            vein.size != null ? vein.size : 20,
                            vein.height != null ? vein.height : 4
                    );
                    featureId = ResourceLocation.fromNamespaceAndPath("tfc", "disc_vein");
                }
                case CLUSTER -> {
                    config = new ClusterVeinConfig(
                            base,
                            vein.size != null ? vein.size : 20
                    );
                    featureId = ResourceLocation.fromNamespaceAndPath("tfc", "cluster_vein");
                }
                case PIPE -> {
                    config = new PipeVeinConfig(
                            base,
                            vein.pipeHeight != null ? vein.pipeHeight : 60,
                            vein.radius != null ? vein.radius : 5,
                            vein.minSkew != null ? vein.minSkew : 5,
                            vein.maxSkew != null ? vein.maxSkew : 13,
                            vein.minSlant != null ? vein.minSlant : 0,
                            vein.maxSlant != null ? vein.maxSlant : 2,
                            vein.sign != null ? vein.sign.floatValue() : 0.0f
                    );
                    featureId = ResourceLocation.fromNamespaceAndPath("tfc", "pipe_vein");
                }
                default -> throw new IllegalStateException("Unknown vein type: " + vein.veinType);
            }

            // 5. Get Feature
            @SuppressWarnings("unchecked")
            Feature<IVeinConfig> feature = (Feature<IVeinConfig>) BuiltInRegistries.FEATURE.get(featureId);
            if (feature == null) {
                System.err.println("TFC vein feature missing: " + featureId);
                continue;
            }

            // 6. Register
            ConfiguredFeature<?, ?> configured = new ConfiguredFeature<>(feature, config);

            ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "overworld/vein/" + veinName)
            );

            context.register(key, configured);
            System.out.println("Registered vein configured feature: " + key.location());
        }
    }

    // Helper: build the weighted replacement map depending on graded or not
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

                map.put(target, new WeightedHelper<>(
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

                map.put(target, new WeightedHelper<>(weights));
                added++;
            }
        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

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

            map.put(target, new WeightedHelper<>(
                    List.of(Pair.of(vein.oreBlock.defaultBlockState(), 1.0))
            ));
            added++;

        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

    // Helper: get non-graded ore block for rock (from your ORES map)
    private static Block getOreBlock(Ore ore, CompatRock rock) {
        return ModBlocks.ORES.get(rock).get(ore).get();
    }

    // Helper: get graded ore block for rock + grade (from your GRADED_ORES map)
    private static Block getGradedOreBlock(Ore ore, CompatRock rock, Ore.Grade grade) {
        return ModBlocks.GRADED_ORES.get(rock).get(ore).get(grade).get();
    }

    // Simple deterministic seed – same as TFC mostly uses
    private static long veinSeedFromName(String name) {
        long hash = 0;
        for (char c : name.toCharArray()) {
            hash = 31 * hash + c;
        }
        return hash & 0x7FFFFFFFFFFFFFFFL; // positive
    }
}
