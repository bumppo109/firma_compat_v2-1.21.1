package com.bumppo109.firma_compat.materials;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public enum MetalWeathered implements Iterable<MetalSet> {
    COPPER(
            MetalSet.COPPER,
            MetalSet.EXPOSED_COPPER,
            MetalSet.WEATHERED_COPPER,
            MetalSet.OXIDIZED_COPPER,
            MetalSet.WAXED_COPPER,
            MetalSet.WAXED_EXPOSED_COPPER,
            MetalSet.WAXED_WEATHERED_COPPER,
            MetalSet.WAXED_OXIDIZED_COPPER
    ),
    CHISELED_COPPER(
            MetalSet.CHISELED_COPPER,
            MetalSet.EXPOSED_CHISELED_COPPER,
            MetalSet.WEATHERED_CHISELED_COPPER,
            MetalSet.OXIDIZED_CHISELED_COPPER,
            MetalSet.WAXED_CHISELED_COPPER,
            MetalSet.WAXED_EXPOSED_CHISELED_COPPER,
            MetalSet.WAXED_WEATHERED_CHISELED_COPPER,
            MetalSet.WAXED_OXIDIZED_CHISELED_COPPER
    ),
    COPPER_GRATE(
            MetalSet.COPPER_GRATE,
            MetalSet.EXPOSED_COPPER_GRATE,
            MetalSet.WEATHERED_COPPER_GRATE,
            MetalSet.OXIDIZED_COPPER_GRATE,
            MetalSet.WAXED_COPPER_GRATE,
            MetalSet.WAXED_EXPOSED_COPPER_GRATE,
            MetalSet.WAXED_WEATHERED_COPPER_GRATE,
            MetalSet.WAXED_OXIDIZED_COPPER_GRATE
    ),
    CUT_COPPER(
            MetalSet.CUT_COPPER,
            MetalSet.EXPOSED_CUT_COPPER,
            MetalSet.WEATHERED_CUT_COPPER,
            MetalSet.OXIDIZED_CUT_COPPER,
            MetalSet.WAXED_CUT_COPPER,
            MetalSet.WAXED_EXPOSED_CUT_COPPER,
            MetalSet.WAXED_WEATHERED_CUT_COPPER,
            MetalSet.WAXED_OXIDIZED_CUT_COPPER
    )
    ;

    private final MetalSet baseSet;
    @Nullable private final MetalSet baseSetExposed;
    @Nullable private final MetalSet baseSetWeathered;
    @Nullable private final MetalSet baseSetOxidized;
    @Nullable private final MetalSet baseSetWaxed;
    @Nullable private final MetalSet baseSetExposedWaxed;
    @Nullable private final MetalSet baseSetWeatheredWaxed;
    @Nullable private final MetalSet baseSetOxidizedWaxed;

    MetalWeathered(
            MetalSet baseSet,
            @Nullable MetalSet baseSetExposed,
            @Nullable MetalSet baseSetWeathered,
            @Nullable MetalSet baseSetOxidized,
            @Nullable MetalSet baseSetWaxed,
            @Nullable MetalSet baseSetExposedWaxed,
            @Nullable MetalSet baseSetWeatheredWaxed,
            @Nullable MetalSet baseSetOxidizedWaxed
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

    public MetalSet baseSet() {
        return baseSet;
    }

    @Nullable
    public MetalSet baseSetExposed() {
        return baseSetExposed;
    }

    @Nullable
    public MetalSet baseSetWeathered() {
        return baseSetWeathered;
    }

    @Nullable
    public MetalSet baseSetOxidized() {
        return baseSetOxidized;
    }

    @Nullable
    public MetalSet baseSetWaxed() {
        return baseSetWaxed;
    }

    @Nullable
    public MetalSet baseSetExposedWaxed() {
        return baseSetExposedWaxed;
    }

    @Nullable
    public MetalSet baseSetWeatheredWaxed() {
        return baseSetWeatheredWaxed;
    }

    @Nullable
    public MetalSet baseSetOxidizedWaxed() {
        return baseSetOxidizedWaxed;
    }

    public Stream<MetalSet> stream() {
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
    public Iterator<MetalSet> iterator() {
        return stream().iterator();
    }
}