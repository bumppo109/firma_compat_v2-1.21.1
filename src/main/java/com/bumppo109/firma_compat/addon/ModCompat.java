package com.bumppo109.firma_compat.addon;

import net.neoforged.fml.ModList;


public class ModCompat {

    private ModCompat(){}

    public static boolean loaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}