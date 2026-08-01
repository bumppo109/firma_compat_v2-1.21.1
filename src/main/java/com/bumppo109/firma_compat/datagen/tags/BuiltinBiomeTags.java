package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.world.biome.BiomeExtension;
import net.dries007.tfc.world.biome.TFCBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.bumppo109.firma_compat.util.ModTags.Biomes.FORESTED_BIOMES;

public class BuiltinBiomeTags extends TagsProvider<Biome> {

    public BuiltinBiomeTags(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, Registries.BIOME, lookupProvider, FirmaCompat.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ModTags.Biomes.FORESTED_BIOMES)
                .addTags(Tags.Biomes.IS_FOREST)
                .addTags(Tags.Biomes.IS_TAIGA)
                .addTags(Tags.Biomes.IS_JUNGLE);

    //TFC biomes
        HolderLookup.RegistryLookup<Biome> biomeLookup = provider.lookupOrThrow(Registries.BIOME);

        HolderLookup.RegistryLookup<BiomeExtension> tfcLookup = provider.lookupOrThrow(TFCBiomes.KEY);

        var tfcTag = tag(ModTags.Biomes.IS_TFC_OVERWORLD);

        tfcLookup.listElements().forEach(extension -> {
            tfcTag.add(ResourceKey.create(
                    Registries.BIOME,
                    extension.key().location()
            ));
        });
    }

    @Override
    public String getName() {
        return "FirmaCompat Biome Tags";
    }
}
