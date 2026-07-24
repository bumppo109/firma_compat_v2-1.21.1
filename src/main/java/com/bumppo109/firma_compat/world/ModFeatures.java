package com.bumppo109.firma_compat.world;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, FirmaCompat.MODID);

    //Compat Erosion, ore replacer
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> COMPAT_EROSION =
            FEATURES.register("compat_erosion", () -> new CompatErosion(NoneFeatureConfiguration.CODEC));
}