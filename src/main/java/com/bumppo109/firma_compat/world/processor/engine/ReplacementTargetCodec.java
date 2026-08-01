package com.bumppo109.firma_compat.world.processor.engine;

import com.bumppo109.firma_compat.world.processor.RockTarget;

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
            case ROCK ->
                    RockTarget.CODEC;


            case DIRT ->
                    throw new UnsupportedOperationException(
                            "Dirt replacement codec not implemented"
                    );


            case WOOD ->
                    throw new UnsupportedOperationException(
                            "Wood replacement codec not implemented"
                    );
        };
    }
}