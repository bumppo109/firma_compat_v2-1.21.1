package com.bumppo109.firma_compat.addon;

import com.bumppo109.firma_compat.addon.everycompat.loaders.GemsRealmCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.StoneZoneCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.WoodGoodCompat;
import com.bumppo109.firma_compat.addon.firmalife.FLCompat;
import com.bumppo109.firma_compat.addon.firmalife.FLCompatEvents;
import com.bumppo109.firma_compat.addon.rnr.RnRCompat;
import net.neoforged.bus.api.IEventBus;


public class ModCompatHandler {

    private ModCompatHandler(){}

    public static void registerEveryCompatModules() {

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

    public static void registerAddon(IEventBus modEventBus) {

        if(ModCompat.loaded("firmalife")) {
            FLCompat.register(modEventBus);
            modEventBus.addListener(FLCompatEvents::addToBlockEntities);
        }
        if(ModCompat.loaded("rnr")) {
            RnRCompat.register(modEventBus);
        }
    }
}