package com.bumppo109.firma_compat.world.processor.sandstone;

import com.bumppo109.firma_compat.world.processor.BlockShape;
import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementTarget;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


public record SandstoneTarget(
        SandstoneType type,
        BlockShape shape
)
        implements ReplacementTarget
{

    public static final MapCodec<SandstoneTarget> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(

                                    SandstoneType.CODEC
                                            .fieldOf("type")
                                            .forGetter(
                                                    SandstoneTarget::type
                                            ),

                                    BlockShape.CODEC
                                            .fieldOf("shape")
                                            .forGetter(
                                                    SandstoneTarget::shape
                                            )

                            )
                            .apply(
                                    instance,
                                    SandstoneTarget::new
                            )
            );


    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.SANDSTONE;
    }
}