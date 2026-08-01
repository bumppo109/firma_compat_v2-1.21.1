package com.bumppo109.firma_compat.world.processor;


import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.RockData;
import net.dries007.tfc.world.settings.RockSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;


import java.util.HashMap;
import java.util.Map;



/**
 * Resolves TFC terrain rock information.
 *
 * Structure replacement flow:
 *
 * BlockPos
 *    |
 *    v
 * ChunkData
 *    |
 *    v
 * RockSettings
 *    |
 *    v
 * Rock
 *
 *
 * This intentionally uses terrain data rather than the structure block.
 * A structure may be placed above, beside, or through terrain and should
 * inherit the local geology.
 */
public final class RockLookup
{

    private static final Map<Block, Rock> RAW_LOOKUP =
            new HashMap<>();


    private static boolean initialized;



    private RockLookup()
    {
    }



    /**
     * Gets the TFC rock at a world position.
     */
    public static Rock rock(LevelReader level, BlockPos pos)
    {
        ChunkData chunkData = ChunkData.get(level, pos);

        // Fallback if chunk data is unavailable.
        if (chunkData == ChunkData.EMPTY
                || chunkData.status() == ChunkData.Status.INVALID)
        {
            return null;
        }

        RockData rockData = chunkData.getRockData();

        return rockData.getRock(pos);
    }



    /**
     * Gets the TFC RockSettings at a position.
     */
    public static RockSettings settings(
            LevelReader level,
            BlockPos pos
    )
    {
        return ChunkData
                .get(
                        level,
                        pos
                )
                .getRockData()
                .getRock(
                        pos
                );
    }



    /**
     * Converts RockSettings into the TFC Rock enum.
     */
    public static Rock rock(
            RockSettings settings
    )
    {
        initialize();


        Rock rock =
                RAW_LOOKUP.get(
                        settings.raw()
                );


        if (rock == null)
        {
            throw new IllegalStateException(
                    "Unable to resolve TFC rock for "
                            + settings.raw()
            );
        }


        return rock;
    }



    /**
     * Builds raw-block lookup table.
     *
     * Example:
     *
     * tfc:granite
     *      ->
     * Rock.GRANITE
     */
    private static synchronized void initialize()
    {
        if (initialized)
        {
            return;
        }


        for (Rock rock : Rock.values())
        {
            Map<Rock.BlockType, TFCBlocks.Id<Block>> blocks =
                    TFCBlocks.ROCK_BLOCKS.get(
                            rock
                    );


            if (blocks == null)
            {
                continue;
            }


            TFCBlocks.Id<Block> raw =
                    blocks.get(
                            Rock.BlockType.RAW
                    );


            if (raw != null)
            {
                RAW_LOOKUP.put(
                        raw.get(),
                        rock
                );
            }
        }


        initialized = true;
    }
}