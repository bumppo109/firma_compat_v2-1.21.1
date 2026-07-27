package com.bumppo109.firma_compat.addon.everycompat.loaders;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.everycompat.modules.gemsrealm.CompatMetalModule;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;


public class GemsRealmCompat {

    private GemsRealmCompat() {}

    public static void register() {

        CompatMetalModule metalModule = new CompatMetalModule(FirmaCompat.MODID);
        EveryCompatAPI.registerModule(metalModule);
    }
}