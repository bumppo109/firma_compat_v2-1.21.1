package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.neoforged.fml.ModList;

import static com.bumppo109.firma_compat.FirmaCompat.isStoneZoneLoaded;
import static com.bumppo109.firma_compat.FirmaCompat.isWoodGoodLoaded;

public class EveryCompatHandler {

    private EveryCompatHandler() {}

    public static void registerModules() {
        if(isStoneZoneLoaded){
            CompatStoneZoneModule stoneModule = new CompatStoneZoneModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(stoneModule);
        }
        if(isWoodGoodLoaded){
            CompatWoodGoodModule woodGoodModule = new CompatWoodGoodModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(woodGoodModule);
        }
    }
}
