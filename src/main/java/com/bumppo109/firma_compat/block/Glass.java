package com.bumppo109.firma_compat.block;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum Glass implements StringRepresentable {

    VOLCANIC,
    HEMATITIC,
    OLIVINE,
    SILICA;

    private final String serializedName;

    Glass() {
        this.serializedName =
                name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }
}
