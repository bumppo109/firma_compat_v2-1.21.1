package com.bumppo109.firma_compat.addon;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.GemsRealmCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.StoneZoneCompat;
import com.bumppo109.firma_compat.addon.everycompat.loaders.WoodGoodCompat;
import com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul.LSOModifiers;
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
            com.bumppo109.firma_compat.addon.firmalife.FLCompat.register(modEventBus);
            modEventBus.addListener(com.bumppo109.firma_compat.addon.firmalife.FLCompatEvents::addToBlockEntities);
        }
        if(ModCompat.loaded("rnr")) {
            com.bumppo109.firma_compat.addon.rnr.RnRCompat.register(modEventBus);
        }
    }

    public static void registerLSOModifiers() {
        if (ModCompat.loaded("legendarysurvivaloverhaul")) {
            FirmaCompat.LOGGER.debug("Registering LSO greenhouse modifier");
            LSOModifiers.register();
        }
    }
}