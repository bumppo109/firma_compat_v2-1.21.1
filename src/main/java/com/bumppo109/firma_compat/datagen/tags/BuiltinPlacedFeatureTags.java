package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import com.bumppo109.firma_compat.world.CompatVein;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class BuiltinPlacedFeatureTags extends TagsProvider<PlacedFeature> {

    public BuiltinPlacedFeatureTags(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(
                output,
                Registries.PLACED_FEATURE,
                lookupProvider,
                FirmaCompat.MODID,
                existingFileHelper
        );
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var veins = tag(ModTags.PlacedFeatures.VEINS);

        for (CompatVein vein : CompatVein.values()) {
            if (vein.isCopper() || vein.isIron()) continue;

            String name = vein.name().toLowerCase(Locale.ROOT);

            veins.addOptional(
                    ResourceLocation.fromNamespaceAndPath(
                            FirmaCompat.MODID,
                            "vein/" + name
                    )
            );
        }

        tag(ModTags.PlacedFeatures.REMOVE_FEATURES)
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("disk_clay")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_clay")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_copper")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_copper_large")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_diamond")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_diamond_buried")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_diamond_large")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_diamond_medium")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_emerald")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_gold")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_gold_deltas")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_gold_extra")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_gold_lower")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_iron_middle")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_iron_small")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_iron_upper")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_lapis")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_lapis_buried")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_redstone")))
                .add(TagEntry.element(ResourceLocation.withDefaultNamespace("ore_redstone_lower")))

                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_coal_lower"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_coal_upper"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_copper"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_copper_large"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_diamond"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_diamond_buried"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_diamond_large"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_diamond_medium"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_emerald"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_gold"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_gold_extra"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_gold_lower"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_gold_middle"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_iron_small"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_iron_middle"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_iron_upper"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_lapis"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_lapis_buried"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_redstone"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("natures_spirit","ore_redstone_extra"))

                .addOptional(ResourceLocation.fromNamespaceAndPath("legendarysurvivaloverhaul","ice_fern_placed"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("legendarysurvivaloverhaul","sun_fern_placed"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("legendarysurvivaloverhaul","water_plant_placed"))
        ;
    }

    @Override
    public String getName() {
        return "FirmaCompat Placed Feature Tags";
    }
}
