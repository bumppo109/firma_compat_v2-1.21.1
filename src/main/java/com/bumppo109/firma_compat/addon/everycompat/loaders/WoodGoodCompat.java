package com.bumppo109.firma_compat.addon.everycompat.loaders;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.ModCompat;
import com.bumppo109.firma_compat.addon.everycompat.modules.woodgood.CompatWoodGoodModule;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;


public class WoodGoodCompat {

    private WoodGoodCompat() {}

    public static void register() {

        CompatWoodGoodModule woodModule = new CompatWoodGoodModule(FirmaCompat.MODID);
        EveryCompatAPI.registerModule(woodModule);

        /*
        if(ModCompat.loaded("firmalife")) {
            FLWoodGoodModule flWoodGoodModule = new FLWoodGoodModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(flWoodGoodModule);
        }
        if(ModCompat.loaded("rnr")) {
            RnRWoodGoodModule rnRWoodGoodModule = new RnRWoodGoodModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(rnRWoodGoodModule);
        }

         */
    }
}