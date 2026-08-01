package com.bumppo109.firma_compat.world.processor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class ReplacementEngine
{

    private ReplacementEngine()
    {
    }


    public static BlockState apply(
            ServerLevel level,
            BlockPos pos,
            BlockState original,
            ReplacementRule rule
    )
    {
        if (!rule.matches(original.getBlock()))
        {
            return original;
        }


        return resolve(
                level,
                pos,
                rule.target()
        )
                .defaultBlockState();
    }



    private static Block resolve(
            ServerLevel level,
            BlockPos pos,
            ReplacementTarget target
    )
    {
        ReplacementResolver resolver =
                ReplacementResolverRegistry.get(
                        target.category()
                );


        return resolver.resolve(
                level,
                pos,
                target
        );
    }
}