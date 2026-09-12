package com.bumppo109.firma_compat.world;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class RootedSoilHelper
{
    public static void replaceSoilUnderTree(WorldGenLevel level, BlockPos treePos)
    {
        BlockPos soilPos = treePos.below();

        BlockState current = level.getBlockState(soilPos);

        Block rooted = Blocks.AIR;

        for(SoilBlockType.Variant variant : SoilBlockType.Variant.values()){
            for(SoilBlockType type : SoilBlockType.VALUES){
                Block testBlock = TFCBlocks.SOIL.get(type).get(variant).get();

                if(current.getBlock().equals(testBlock)){
                    rooted = TFCBlocks.SOIL.get(SoilBlockType.ROOTED_DIRT).get(variant).get();
                }
            }
        }

        if (!rooted.equals(Blocks.AIR))
        {
            level.setBlock(
                    soilPos,
                    rooted.defaultBlockState(),
                    Block.UPDATE_CLIENTS
            );
        }
    }
}