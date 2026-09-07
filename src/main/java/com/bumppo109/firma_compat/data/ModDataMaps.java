package com.bumppo109.firma_compat.data;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class ModDataMaps {

    public static final DataMapType<Block, ResourceLocation> HARDENED_ROCK_REPLACEMENT =
            DataMapType.builder(
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "worldgen/hardened_rock_replacement"),
                    Registries.BLOCK,
                    ResourceLocation.CODEC
            ).build();

    public static final DataMapType<Block, ResourceLocation> VANILLA_ORE_REPLACEMENT =
            DataMapType.builder(
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "worldgen/vanilla_ore_replacement"),
                    Registries.BLOCK,
                    ResourceLocation.CODEC
            ).build();

    // Register the data map type (called from mod constructor or event bus)
    public static void register(RegisterDataMapTypesEvent event) {
        event.register(HARDENED_ROCK_REPLACEMENT);
        event.register(VANILLA_ORE_REPLACEMENT);
    }
}