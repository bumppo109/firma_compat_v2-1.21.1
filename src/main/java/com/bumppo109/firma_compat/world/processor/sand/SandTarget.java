package com.bumppo109.firma_compat.world.processor.sand;

import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementTarget;

import com.mojang.serialization.MapCodec;


public record SandTarget()
        implements ReplacementTarget
{

    public static final MapCodec<SandTarget> CODEC =
            MapCodec.unit(
                    new SandTarget()
            );


    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.SAND;
    }
}