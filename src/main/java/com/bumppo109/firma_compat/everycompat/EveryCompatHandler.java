package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;

import static com.bumppo109.firma_compat.FirmaCompat.*;

public class EveryCompatHandler {

    private EveryCompatHandler() {}

    public static void registerModules() {
        if(isGemsRealmLoaded){
            CompatMetalModule metalModule = new CompatMetalModule("firma_compat");
            EveryCompatAPI.registerModule(metalModule);
        }
        if(isStoneZoneLoaded){
            CompatStoneZoneModule stoneModule = new CompatStoneZoneModule("firma_compat");
            EveryCompatAPI.registerModule(stoneModule);
        }
        if(isWoodGoodLoaded){
            CompatWoodGoodModule woodGoodModule = new CompatWoodGoodModule("firma_compat");
            EveryCompatAPI.registerModule(woodGoodModule);
        }
    }
}