package com.bumppo109.firma_compat.fluid;

import java.util.Locale;

public enum CompatFluid {
    AWKWARD(-12618012),
    THICK(-12618012),
    MUNDANE(-12618012),
    DRAGONS_BREATH(14914302)
    ;

    private final String serializedName;
    private final int color;

    CompatFluid(int color) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.color = color;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public int getColor() {
        return color;
    }
}
