package com.bumppo109.firma_compat.block;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

public record ModDecorationBlockHolder(ModBlocks.Id<? extends SlabBlock> slab, ModBlocks.Id<? extends StairBlock> stair, ModBlocks.Id<? extends WallBlock> wall) {
}
