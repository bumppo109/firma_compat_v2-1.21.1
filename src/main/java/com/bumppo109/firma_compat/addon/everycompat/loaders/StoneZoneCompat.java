package com.bumppo109.firma_compat.addon.everycompat.loaders;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.everycompat.modules.stonezone.CompatStoneZoneModule;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;


public class StoneZoneCompat {

    private StoneZoneCompat() {}

    public static void register() {

        CompatStoneZoneModule stoneModule =
                new CompatStoneZoneModule(FirmaCompat.MODID);

        EveryCompatAPI.registerModule(stoneModule);
    }
}