package com.bumppo109.firma_compat.block;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public enum CompatMetalWeathered implements Iterable<CompatMetalSet> {
    COPPER(
            CompatMetalSet.COPPER,
            CompatMetalSet.EXPOSED_COPPER,
            CompatMetalSet.WEATHERED_COPPER,
            CompatMetalSet.OXIDIZED_COPPER,
            CompatMetalSet.WAXED_COPPER,
            CompatMetalSet.WAXED_EXPOSED_COPPER,
            CompatMetalSet.WAXED_WEATHERED_COPPER,
            CompatMetalSet.WAXED_OXIDIZED_COPPER
    ),
    CHISELED_COPPER(
            CompatMetalSet.CHISELED_COPPER,
            CompatMetalSet.EXPOSED_CHISELED_COPPER,
            CompatMetalSet.WEATHERED_CHISELED_COPPER,
            CompatMetalSet.OXIDIZED_CHISELED_COPPER,
            CompatMetalSet.WAXED_CHISELED_COPPER,
            CompatMetalSet.WAXED_EXPOSED_CHISELED_COPPER,
            CompatMetalSet.WAXED_WEATHERED_CHISELED_COPPER,
            CompatMetalSet.WAXED_OXIDIZED_CHISELED_COPPER
    ),
    COPPER_GRATE(
            CompatMetalSet.COPPER_GRATE,
            CompatMetalSet.EXPOSED_COPPER_GRATE,
            CompatMetalSet.WEATHERED_COPPER_GRATE,
            CompatMetalSet.OXIDIZED_COPPER_GRATE,
            CompatMetalSet.WAXED_COPPER_GRATE,
            CompatMetalSet.WAXED_EXPOSED_COPPER_GRATE,
            CompatMetalSet.WAXED_WEATHERED_COPPER_GRATE,
            CompatMetalSet.WAXED_OXIDIZED_COPPER_GRATE
    ),
    CUT_COPPER(
            CompatMetalSet.CUT_COPPER,
            CompatMetalSet.EXPOSED_CUT_COPPER,
            CompatMetalSet.WEATHERED_CUT_COPPER,
            CompatMetalSet.OXIDIZED_CUT_COPPER,
            CompatMetalSet.WAXED_CUT_COPPER,
            CompatMetalSet.WAXED_EXPOSED_CUT_COPPER,
            CompatMetalSet.WAXED_WEATHERED_CUT_COPPER,
            CompatMetalSet.WAXED_OXIDIZED_CUT_COPPER
    )
    ;

    private final CompatMetalSet baseSet;
    @Nullable private final CompatMetalSet baseSetExposed;
    @Nullable private final CompatMetalSet baseSetWeathered;
    @Nullable private final CompatMetalSet baseSetOxidized;
    @Nullable private final CompatMetalSet baseSetWaxed;
    @Nullable private final CompatMetalSet baseSetExposedWaxed;
    @Nullable private final CompatMetalSet baseSetWeatheredWaxed;
    @Nullable private final CompatMetalSet baseSetOxidizedWaxed;

    CompatMetalWeathered(
            CompatMetalSet baseSet,
            @Nullable CompatMetalSet baseSetExposed,
            @Nullable CompatMetalSet baseSetWeathered,
            @Nullable CompatMetalSet baseSetOxidized,
            @Nullable CompatMetalSet baseSetWaxed,
            @Nullable CompatMetalSet baseSetExposedWaxed,
            @Nullable CompatMetalSet baseSetWeatheredWaxed,
            @Nullable CompatMetalSet baseSetOxidizedWaxed
    ) {
        this.baseSet = baseSet;
        this.baseSetExposed = baseSetExposed;
        this.baseSetWeathered = baseSetWeathered;
        this.baseSetOxidized = baseSetOxidized;
        this.baseSetWaxed = baseSetWaxed;
        this.baseSetExposedWaxed = baseSetExposedWaxed;
        this.baseSetWeatheredWaxed = baseSetWeatheredWaxed;
        this.baseSetOxidizedWaxed = baseSetOxidizedWaxed;
    }

    public CompatMetalSet baseSet() {
        return baseSet;
    }

    @Nullable
    public CompatMetalSet baseSetExposed() {
        return baseSetExposed;
    }

    @Nullable
    public CompatMetalSet baseSetWeathered() {
        return baseSetWeathered;
    }

    @Nullable
    public CompatMetalSet baseSetOxidized() {
        return baseSetOxidized;
    }

    @Nullable
    public CompatMetalSet baseSetWaxed() {
        return baseSetWaxed;
    }

    @Nullable
    public CompatMetalSet baseSetExposedWaxed() {
        return baseSetExposedWaxed;
    }

    @Nullable
    public CompatMetalSet baseSetWeatheredWaxed() {
        return baseSetWeatheredWaxed;
    }

    @Nullable
    public CompatMetalSet baseSetOxidizedWaxed() {
        return baseSetOxidizedWaxed;
    }

    public Stream<CompatMetalSet> stream() {
        return Stream.of(
                baseSet,
                baseSetExposed,
                baseSetWeathered,
                baseSetOxidized,
                baseSetWaxed,
                baseSetExposedWaxed,
                baseSetWeatheredWaxed,
                baseSetOxidizedWaxed
        ).filter(Objects::nonNull);
    }

    @Override
    public Iterator<CompatMetalSet> iterator() {
        return stream().iterator();
    }
}