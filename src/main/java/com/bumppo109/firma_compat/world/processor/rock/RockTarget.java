package com.bumppo109.firma_compat.world.processor.rock;

import com.bumppo109.firma_compat.world.processor.BlockShape;
import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementTarget;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


public record RockTarget(
        RockBlockType type,
        BlockShape shape
)
        implements ReplacementTarget
{


    public static final MapCodec<RockTarget> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(

                                    RockBlockType.CODEC
                                            .fieldOf("type")
                                            .forGetter(
                                                    RockTarget::type
                                            ),


                                    BlockShape.CODEC
                                            .fieldOf("shape")
                                            .forGetter(
                                                    RockTarget::shape
                                            )

                            )
                            .apply(
                                    instance,
                                    RockTarget::new
                            )
            );



    public RockTarget
    {
        if (shape != BlockShape.BLOCK)
        {
            if (type.tfcType() == null)
            {
                throw new IllegalArgumentException(
                        type +
                                " has no TFC mapping"
                );
            }


            if (!type.tfcType().hasVariants())
            {
                throw new IllegalArgumentException(
                        type +
                                " cannot be decorated"
                );
            }
        }
    }



    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.ROCK;
    }
}