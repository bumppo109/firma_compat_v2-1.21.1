package com.bumppo109.firma_compat.world.processor;


import com.bumppo109.firma_compat.world.processor.engine.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.engine.ReplacementResolver;

import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

import java.util.Map;



/**
 * Resolves logical rock replacements into TFC rock blocks.
 *
 * Uses the terrain rock at the structure position.
 *
 * BLOCK:
 *      TFCBlocks.ROCK_BLOCKS
 *
 * SLAB:
 *      TFCBlocks.ROCK_DECORATIONS -> slab()
 *
 * STAIR:
 *      TFCBlocks.ROCK_DECORATIONS -> stair()
 *
 * WALL:
 *      TFCBlocks.ROCK_DECORATIONS -> wall()
 */
public final class RockResolver
        implements ReplacementResolver<RockTarget>
{


    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.ROCK;
    }



    @Override
    public Block resolve(
            LevelReader level,
            BlockPos pos,
            RockTarget material
    )
    {
        // Resolve terrain rock.
        // If TFC chunk data is unavailable, fall back to a default rock.
        Rock rock = RockLookup.rock(level, pos);

        if (rock == null)
        {
            rock = Rock.PHYLLITE; // or your configured fallback
        }

        return resolve(
                rock,
                material
        );
    }



    public Block resolve(
            Rock rock,
            RockTarget material
    )
    {
        return switch (material.shape())
        {
            case BLOCK ->
                    resolveBlock(
                            rock,
                            material.type()
                    );


            case SLAB ->
                    resolveSlab(
                            rock,
                            material.type()
                    );


            case STAIR ->
                    resolveStair(
                            rock,
                            material.type()
                    );


            case WALL ->
                    resolveWall(
                            rock,
                            material.type()
                    );
        };
    }



    private Block resolveBlock(
            Rock rock,
            RockBlockType type
    )
    {
        Rock.BlockType tfcType =
                requireTfcType(type);


        Map<Rock.BlockType, TFCBlocks.Id<Block>> blocks =
                TFCBlocks.ROCK_BLOCKS.get(rock);


        if (blocks == null)
        {
            throw new IllegalStateException(
                    "Missing TFC rock block registry for "
                            + rock
            );
        }


        TFCBlocks.Id<Block> id =
                blocks.get(tfcType);


        if (id == null)
        {
            throw new IllegalStateException(
                    "Missing TFC rock block "
                            + tfcType
                            + " for "
                            + rock
            );
        }


        return id.get();
    }



    private Block resolveSlab(
            Rock rock,
            RockBlockType type
    )
    {
        return getDecoration(
                rock,
                type
        )
                .slab()
                .get();
    }



    private Block resolveStair(
            Rock rock,
            RockBlockType type
    )
    {
        return getDecoration(
                rock,
                type
        )
                .stair()
                .get();
    }



    private Block resolveWall(
            Rock rock,
            RockBlockType type
    )
    {
        return getDecoration(
                rock,
                type
        )
                .wall()
                .get();
    }



    private DecorationBlockHolder getDecoration(
            Rock rock,
            RockBlockType type
    )
    {
        Rock.BlockType tfcType =
                requireTfcType(type);


        Map<Rock.BlockType, DecorationBlockHolder> decorations =
                TFCBlocks.ROCK_DECORATIONS.get(rock);


        if (decorations == null)
        {
            throw new IllegalStateException(
                    "Missing TFC rock decoration registry for "
                            + rock
            );
        }


        DecorationBlockHolder holder =
                decorations.get(tfcType);


        if (holder == null)
        {
            throw new IllegalStateException(
                    "Rock type "
                            + tfcType
                            + " has no decorations for "
                            + rock
            );
        }


        return holder;
    }



    private Rock.BlockType requireTfcType(
            RockBlockType type
    )
    {
        Rock.BlockType tfcType =
                type.tfcType();


        if (tfcType == null)
        {
            throw new IllegalArgumentException(
                    "Rock type "
                            + type
                            + " has no TFC Rock.BlockType mapping"
            );
        }


        return tfcType;
    }

}