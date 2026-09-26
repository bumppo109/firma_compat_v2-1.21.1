package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import com.bumppo109.firma_compat.world.CompatVein;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
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
            String name = vein.name().toLowerCase(Locale.ROOT);

            veins.addOptional(
                    ResourceLocation.fromNamespaceAndPath(
                            FirmaCompat.MODID,
                            "vein/" + name
                    )
            );
        }
    }

    @Override
    public String getName() {
        return "FirmaCompat Placed Feature Tags";
    }
}
