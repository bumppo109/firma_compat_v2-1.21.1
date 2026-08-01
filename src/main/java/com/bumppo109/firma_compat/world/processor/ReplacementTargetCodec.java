package com.bumppo109.firma_compat.world.processor;

import com.bumppo109.firma_compat.world.processor.dirt.DirtTarget;
import com.bumppo109.firma_compat.world.processor.rock.RockTarget;

import com.bumppo109.firma_compat.world.processor.sand.SandTarget;
import com.bumppo109.firma_compat.world.processor.sandstone.SandstoneTarget;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;


public final class ReplacementTargetCodec
{

    private ReplacementTargetCodec()
    {
    }



    public static final MapCodec<ReplacementTarget> MAP_CODEC =
            ReplacementCategory.CODEC
                    .dispatchMap(
                            "category",
                            ReplacementTarget::category,
                            ReplacementTargetCodec::codecForCategory
                    );


    public static final Codec<ReplacementTarget> CODEC =
            MAP_CODEC.codec();



    private static MapCodec<? extends ReplacementTarget> codecForCategory(
            ReplacementCategory category
    )
    {
        return switch (category)
        {
            case ROCK -> RockTarget.CODEC;
            case DIRT -> DirtTarget.CODEC;
            case SAND -> SandTarget.CODEC;
            case SANDSTONE -> SandstoneTarget.CODEC;
            case WOOD ->
                    throw new UnsupportedOperationException(
                            "Wood replacement codec not implemented"
                    );
        };
    }
}