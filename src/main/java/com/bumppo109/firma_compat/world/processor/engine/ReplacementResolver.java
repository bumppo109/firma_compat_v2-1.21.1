package com.bumppo109.firma_compat.world.processor.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;


public interface ReplacementResolver<T extends ReplacementTarget>
{

    ReplacementCategory category();


    Block resolve(
            LevelReader level,
            BlockPos pos,
            T target
    );
}