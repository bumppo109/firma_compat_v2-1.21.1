package com.bumppo109.firma_compat.block;

import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.Locale;
import java.util.function.Supplier;

public enum Glass implements StringRepresentable {

    VOLCANIC(TFCItems.VOLCANIC_GLASS_BOTTLE),
    HEMATITIC(TFCItems.HEMATITIC_GLASS_BOTTLE),
    OLIVINE(TFCItems.OLIVINE_GLASS_BOTTLE),
    SILICA(TFCItems.SILICA_GLASS_BOTTLE);

    private final String serializedName;
    private final Supplier<Item> glassBottle;

    Glass(Supplier<Item> glassBottle) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.glassBottle = glassBottle;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public Supplier<Item> getGlassBottle() {
        return glassBottle;
    }
}
