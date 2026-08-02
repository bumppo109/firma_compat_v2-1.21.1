package com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul;

import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;

public class LSOModifiers {
    public static void register() {
        TemperatureModifierRegistry.MODIFIERS.register("firmalife_greenhouse", GreenhouseModifier::new);
    }
}