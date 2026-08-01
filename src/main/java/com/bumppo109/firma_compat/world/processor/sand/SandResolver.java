package com.bumppo109.firma_compat.world.processor.sand;

import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementResolver;
import com.bumppo109.firma_compat.world.processor.rock.RockLookup;

import net.dries007.tfc.world.settings.RockSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;


public final class SandResolver
        implements ReplacementResolver<SandTarget>
{

    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.SAND;
    }


    @Override
    public Block resolve(
            LevelReader level,
            BlockPos pos,
            SandTarget target
    )
    {
        Block fallback =
                level.getBlockState(pos)
                        .getBlock();


        RockSettings settings =
                RockLookup.settings(
                        level,
                        pos
                );


        if (settings == null)
        {
            return fallback;
        }


        return settings.sand();
    }
}