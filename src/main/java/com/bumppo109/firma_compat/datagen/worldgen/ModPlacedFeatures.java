package com.bumppo109.firma_compat.datagen.worldgen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.world.CompatSingleBlockVein;
import com.bumppo109.firma_compat.world.CompatVein;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;
import java.util.Locale;

public class ModPlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        //Single Block Veins
        for (CompatSingleBlockVein vein : CompatSingleBlockVein.values()) {
            String veinName = vein.name().toLowerCase(Locale.ROOT);

            // Same path as in configured features
            ResourceLocation configuredRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "overworld/vein/" + veinName
            );

            ResourceKey<ConfiguredFeature<?, ?>> configuredKey = ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    configuredRl
            );

            // Lookup the configured feature we just registered
            Holder<ConfiguredFeature<?, ?>> configuredHolder;
            try {
                configuredHolder = configuredFeatures.getOrThrow(configuredKey);
            } catch (Exception e) {
                System.err.println("Skipping placed feature for " + veinName + " – configured feature not found: " + configuredRl);
                continue; // Skip if configured wasn't registered (sync safety)
            }

            // Generate placement modifiers based on vein data
            List<PlacementModifier> modifiers = createEmptyPlacement(vein);

            // Same path for placed feature (common pattern: same folder/name as configured)
            ResourceKey<PlacedFeature> placedKey = ResourceKey.create(
                    Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "overworld/vein/" + veinName)
            );

            PlacedFeature placed = new PlacedFeature(configuredHolder, modifiers);

            context.register(placedKey, placed);

            System.out.println("Registered placed feature: " + placedKey.location() + " (for CompatVein: " + veinName + ")");
        }

        //Veins
        for (CompatVein vein : CompatVein.values()) {
            String veinName = vein.name().toLowerCase(Locale.ROOT);

            // Same path as in configured features
            ResourceLocation configuredRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "overworld/vein/" + veinName
            );

            ResourceKey<ConfiguredFeature<?, ?>> configuredKey = ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    configuredRl
            );

            // Lookup the configured feature we just registered
            Holder<ConfiguredFeature<?, ?>> configuredHolder;
            try {
                configuredHolder = configuredFeatures.getOrThrow(configuredKey);
            } catch (Exception e) {
                System.err.println("Skipping placed feature for " + veinName + " – configured feature not found: " + configuredRl);
                continue; // Skip if configured wasn't registered (sync safety)
            }

            // Generate placement modifiers based on vein data
            List<PlacementModifier> modifiers = createEmptyPlacement(vein);

            // Same path for placed feature (common pattern: same folder/name as configured)
            ResourceKey<PlacedFeature> placedKey = ResourceKey.create(
                    Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "overworld/vein/" + veinName)
            );

            PlacedFeature placed = new PlacedFeature(configuredHolder, modifiers);

            context.register(placedKey, placed);

            System.out.println("Registered placed feature: " + placedKey.location() + " (for CompatVein: " + veinName + ")");
        }
    }

    /*
    private static List<PlacementModifier> createPlacementModifiers(CompatVein vein) {
        // Core filters: rarity + square spread + biome
        List<PlacementModifier> mods = List.of(
                RarityFilter.onAverageOnceEvery(vein.rarity),  // vein.rarity = chunks per vein attempt
                InSquarePlacement.spread(),
                BiomeFilter.biome()
        );

        // Height placement (uniform vs triangle based on range – your old logic)
        HeightRangePlacement height;
        if (vein.minY >= -64 && vein.maxY <= 320) {
            height = HeightRangePlacement.uniform(
                    VerticalAnchor.absolute(vein.minY),
                    VerticalAnchor.absolute(vein.maxY)
            );
        } else {
            height = HeightRangePlacement.triangle(
                    VerticalAnchor.absolute(vein.minY),
                    VerticalAnchor.absolute(vein.maxY)
            );
        }

        // Add height after the common filters (TFC typically does this order)
        mods = append(mods, height);

        // Future: type-specific extras (e.g. CountPlacement for clusters, Offset for pipes)
        // switch (vein.veinType) { ... }

        return mods;
    }

     */

    private static List<PlacementModifier> createEmptyPlacement(CompatVein vein) {
        return List.of();
    }
    private static List<PlacementModifier> createEmptyPlacement(CompatSingleBlockVein vein) {
        return List.of();
    }

    /*
    private static List<PlacementModifier> createPlacementModifiers(CompatVein vein) {
        List<PlacementModifier> mods = List.of(
                RarityFilter.onAverageOnceEvery(vein.rarity),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
        );

        HeightRangePlacement heightPlacement = (vein.minY >= -64 && vein.maxY <= 320)
                ? HeightRangePlacement.uniform(
                VerticalAnchor.absolute(vein.minY),
                VerticalAnchor.absolute(vein.maxY))
                : HeightRangePlacement.triangle(
                VerticalAnchor.absolute(vein.minY),
                VerticalAnchor.absolute(vein.maxY));

        switch (vein.veinType) {
            case DISC, CLUSTER, PIPE -> mods = append(mods, heightPlacement);
            // All three types use the same height placement in your current logic
            // You can add type-specific logic later if needed
        }

        return mods;
    }

     */

    // Overload for BlockVein (same logic for now – add differences later if needed)
    /*
    private static List<PlacementModifier> createPlacementModifiers(BlockVein bVein) {
        List<PlacementModifier> mods = List.of(
                RarityFilter.onAverageOnceEvery(bVein.rarity),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
        );

        HeightRangePlacement heightPlacement = (bVein.minY >= -64 && bVein.maxY <= 320)
                ? HeightRangePlacement.uniform(
                VerticalAnchor.absolute(bVein.minY),
                VerticalAnchor.absolute(bVein.maxY))
                : HeightRangePlacement.triangle(
                VerticalAnchor.absolute(bVein.minY),
                VerticalAnchor.absolute(bVein.maxY));

        return Stream.concat(
                mods.stream(),
                Stream.of(heightPlacement)
        ).collect(Collectors.toList());
    }

    private static <T> List<T> append(List<T> original, T element) {
        return Stream.concat(
                original.stream(),
                Stream.of(element)
        ).collect(Collectors.toList());
    }

     */
}