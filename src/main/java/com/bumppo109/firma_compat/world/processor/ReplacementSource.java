package com.bumppo109.firma_compat.world.processor;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;


public sealed interface ReplacementSource
        permits ReplacementSource.BlockSource,
        ReplacementSource.TagSource,
        ReplacementSource.ListSource
{

    boolean matches(Block block);



    record BlockSource(
            Block block
    )
            implements ReplacementSource
    {

        @Override
        public boolean matches(Block other)
        {
            return block == other;
        }
    }



    record TagSource(
            TagKey<Block> tag
    )
            implements ReplacementSource
    {

        @Override
        public boolean matches(Block block)
        {
            return block.builtInRegistryHolder()
                    .is(tag);
        }
    }



    record ListSource(
            List<Block> blocks
    )
            implements ReplacementSource
    {

        @Override
        public boolean matches(Block block)
        {
            return blocks.contains(block);
        }
    }
}