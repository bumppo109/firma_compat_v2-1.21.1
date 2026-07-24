package com.bumppo109.firma_compat.datagen.worldgen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.world.CompatSingleBlockVein;
import com.bumppo109.firma_compat.world.CompatVein;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Locale;

public class ModBiomeModifierProvider {

    // Add more for each vein in CompatVein (you can generate them dynamically in bootstrap)
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        // Use the vanilla overworld tag (#minecraft:is_overworld) – works with TFC biomes too
        TagKey<Biome> overworldTag = TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("is_overworld"));

        //Single Block Vein
        for (CompatSingleBlockVein vein : CompatSingleBlockVein.values()) {
            String veinName = vein.name().toLowerCase(Locale.ROOT);

            // Placed feature path (matches ModPlacedFeatures)
            ResourceLocation placedRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "overworld/vein/" + veinName
            );

            ResourceKey<PlacedFeature> placedKey = ResourceKey.create(Registries.PLACED_FEATURE, placedRl);

            Holder<PlacedFeature> placedHolder;
            try {
                placedHolder = placedFeatures.getOrThrow(placedKey);
            } catch (Exception e) {
                System.out.println("Warning: Skipping biome modifier for vein '" + veinName + "' – missing placed feature: " + placedRl);
                continue;
            }

            // Modifier name: add_<veinName>_vein
            ResourceLocation modifierRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "add_" + veinName + "_vein"
            );

            ResourceKey<BiomeModifier> modifierKey = ResourceKey.create(
                    NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    modifierRl
            );

            // Add the placed feature to all overworld biomes in UNDERGROUND_ORES step
            context.register(modifierKey, new BiomeModifiers.AddFeaturesBiomeModifier(
                    biomes.getOrThrow(overworldTag),                    // biomes to modify
                    HolderSet.direct(placedHolder),                     // features to add
                    GenerationStep.Decoration.UNDERGROUND_ORES          // step (matches TFC veins)
            ));

            System.out.println("Registered biome modifier: " + modifierKey.location() + " (for TestVein: " + veinName + ")");
        }

        //Vein
        for (CompatVein vein : CompatVein.values()) {
            String veinName = vein.name().toLowerCase(Locale.ROOT);

            //see copper & iron_vein_placer features
            if(veinName.equals("surface_hematite") || veinName.equals("surface_limonite") || veinName.equals("surface_magnetite")
            || veinName.equals("normal_tetrahedrite") || veinName.equals("surface_tetrahedrite") || veinName.equals("surface_malachite") || veinName.equals("normal_malachite") || veinName.equals("surface_native_copper")){
                continue;
            }

            // Placed feature path (matches ModPlacedFeatures)
            ResourceLocation placedRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "overworld/vein/" + veinName
            );

            ResourceKey<PlacedFeature> placedKey = ResourceKey.create(Registries.PLACED_FEATURE, placedRl);

            Holder<PlacedFeature> placedHolder;
            try {
                placedHolder = placedFeatures.getOrThrow(placedKey);
            } catch (Exception e) {
                System.out.println("Warning: Skipping biome modifier for vein '" + veinName + "' – missing placed feature: " + placedRl);
                continue;
            }

            // Modifier name: add_<veinName>_vein
            ResourceLocation modifierRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "add_" + veinName + "_vein"
            );

            ResourceKey<BiomeModifier> modifierKey = ResourceKey.create(
                    NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    modifierRl
            );

            // Add the placed feature to all overworld biomes in UNDERGROUND_ORES step
            context.register(modifierKey, new BiomeModifiers.AddFeaturesBiomeModifier(
                    biomes.getOrThrow(overworldTag),                    // biomes to modify
                    HolderSet.direct(placedHolder),                     // features to add
                    GenerationStep.Decoration.UNDERGROUND_ORES          // step (matches TFC veins)
            ));

            System.out.println("Registered biome modifier: " + modifierKey.location() + " (for TestVein: " + veinName + ")");
        }
    }

    /*
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        TagKey<Biome> overworldTag = TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("is_overworld"));

        for (CompatVein vein : CompatVein.values()) {
            ResourceLocation modifierRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "add_" + vein.veinName + "_vein"
            );

            ResourceKey<BiomeModifier> modifierKey = ResourceKey.create(
                    NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    modifierRl
            );

            ResourceLocation placedRl = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    vein.veinName + "_vein_placed"
            );

            ResourceKey<PlacedFeature> placedKey = ResourceKey.create(Registries.PLACED_FEATURE, placedRl);

            context.register(modifierKey, new BiomeModifiers.AddFeaturesBiomeModifier(
                    biomes.getOrThrow(overworldTag),
                    HolderSet.direct(placedFeatures.getOrThrow(placedKey)),
                    GenerationStep.Decoration.UNDERGROUND_ORES
            ));

            System.out.println("Registered biome modifier: " + modifierKey.location());
        }
    }

     */

    /*
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        // Overworld tag – safe for both vanilla and TFC (TFC biomes are overworld)
        TagKey<Biome> overworldTag = TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("is_overworld"));

        // Register each vein's modifier
        registerVeinModifier(context, placedFeatures, biomes, overworldTag,
                "amethyst", ADD_AMETHYST_VEIN);

        registerVeinModifier(context, placedFeatures, biomes, overworldTag,
                "cinnabar", ADD_CINNABAR_VEIN);

        registerVeinModifier(context, placedFeatures, biomes, overworldTag,
                "diamond", ADD_DIAMOND_VEIN);

        registerVeinModifier(context, placedFeatures, biomes, overworldTag,
                "normal_bismuthinite", ADD_NORMAL_BISMUTHINITE_VEIN);

        // ... add more calls for every vein in CompatVein
        // Or better: loop over CompatVein.values() (see alternative below)
    }

     */

    /**
     * Helper to reduce repetition.
     */
    private static void registerVeinModifier(
            BootstrapContext<BiomeModifier> context,
            HolderGetter<PlacedFeature> placedFeatures,
            HolderGetter<Biome> biomes,
            TagKey<Biome> biomeTag,
            String veinName,
            ResourceKey<BiomeModifier> modifierKey
    ) {
        ResourceLocation placedRl = ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                veinName + "_vein_placed"
        );

        ResourceKey<PlacedFeature> placedKey = ResourceKey.create(Registries.PLACED_FEATURE, placedRl);

        context.register(modifierKey, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(biomeTag),  // #minecraft:is_overworld
                HolderSet.direct(placedFeatures.getOrThrow(placedKey)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name)
        );
    }
}