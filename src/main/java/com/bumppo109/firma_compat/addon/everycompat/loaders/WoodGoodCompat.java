package com.bumppo109.firma_compat.addon.everycompat.loaders;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.everycompat.modules.woodgood.CompatWoodGoodModule;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;


public class WoodGoodCompat {

    private WoodGoodCompat() {}

    public static void register() {

        CompatWoodGoodModule woodModule =
                new CompatWoodGoodModule(FirmaCompat.MODID);

        EveryCompatAPI.registerModule(woodModule);
    }
}