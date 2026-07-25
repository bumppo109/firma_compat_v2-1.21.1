package com.bumppo109.firma_compat.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;

import java.util.function.Supplier;

public class CompatBrushableBlock extends BrushableBlock
{
    private final Supplier<Block> target;

    public CompatBrushableBlock(
            Supplier<Block> target,
            SoundEvent brushSound,
            SoundEvent brushCompleteSound,
            Properties properties)
    {
        super(Blocks.AIR, brushSound, brushCompleteSound, properties);
        this.target = target;
    }

    @Override
    public Block getTurnsInto()
    {
        return target.get();
    }
}