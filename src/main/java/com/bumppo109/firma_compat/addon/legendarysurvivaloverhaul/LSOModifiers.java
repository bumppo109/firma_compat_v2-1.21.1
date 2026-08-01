package com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul;

import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;

public class LSOModifiers {
    public static void register() {
        TemperatureModifierRegistry.MODIFIERS.register("test_modifier", TestModifier::new);
    }
}