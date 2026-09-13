package com.bumppo109.firma_compat.world.processor.dirt;


import com.bumppo109.firma_compat.world.processor.BlockShape;
import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementTarget;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.dries007.tfc.common.blocks.soil.SoilBlockType;


public record DirtTarget(
        DirtBlockType type,
        BlockShape shape
)
        implements ReplacementTarget
{

    public static final MapCodec<DirtTarget> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                                    DirtBlockType.CODEC
                                            .fieldOf("type")
                                            .forGetter(DirtTarget::type),

                                    BlockShape.CODEC
                                            .fieldOf("shape")
                                            .forGetter(DirtTarget::shape)
                            )
                            .apply(
                                    instance,
                                    DirtTarget::new
                            )
            );


    public DirtTarget
    {
        if (shape != BlockShape.BLOCK)
        {
            if (!hasDecoration(type))
            {
                throw new IllegalArgumentException(
                        type + " has no TFC decoration blocks"
                );
            }
        }
    }


    private static boolean hasDecoration(
            DirtBlockType type
    )
    {
        return type.soilType() == SoilBlockType.MUD_BRICKS;
    }


    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.DIRT;
    }
}