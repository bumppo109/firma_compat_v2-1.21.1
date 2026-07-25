package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;
import net.xelbayria.gems_realm.api.set.metal.MetalTypeRegistry;

import static com.bumppo109.firma_compat.FirmaCompat.*;

public class EveryCompatHandler {

    private EveryCompatHandler() {}

    public static void registerModules() {
        if(isGemsRealmLoaded){
            MetalTypeRegistry.INSTANCE
                    .addSimpleFinder(ResourceLocation.withDefaultNamespace("netherite"))
                    .metalBlock(() -> Blocks.NETHERITE_BLOCK);
            CompatMetalModule metalModule = new CompatMetalModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(metalModule);
        }
        /*
        if(isStoneZoneLoaded){
            CompatStoneZoneModule stoneModule = new CompatStoneZoneModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(stoneModule);
        }
        if(isWoodGoodLoaded){
            CompatWoodGoodModule woodGoodModule = new CompatWoodGoodModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(woodGoodModule);
        }
         */
    }
}