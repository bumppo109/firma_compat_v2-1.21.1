package com.bumppo109.firma_compat.addon;

import com.bumppo109.firma_compat.addon.everycompat.loaders.GemsRealmCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.StoneZoneCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.WoodGoodCompat;


public class ModCompatHandler {

    private ModCompatHandler(){}


    public static void registerModules() {

        if(ModCompat.loaded("gemsrealm")) {
            GemsRealmCompat.register();
        }

        if(ModCompat.loaded("stonezone")) {
            StoneZoneCompat.register();
        }

        if(ModCompat.loaded("everycomp")) {
            WoodGoodCompat.register();
        }
    }
}