package com.bumppo109.firma_compat.world.processor.rock;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.RockData;
import net.dries007.tfc.world.settings.RockSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves TFC terrain rock information.
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
     * Gets the terrain RockSettings.
     *
     * Returns null if terrain data is unavailable.
     */
    /*
    @Nullable
    public static RockSettings settings(
            LevelReader level,
            BlockPos pos
    )
    {
        ChunkData chunkData =
                ChunkData.get(level, pos);

        if (chunkData == ChunkData.EMPTY
                || chunkData.status() == ChunkData.Status.INVALID)
        {
            return null;
        }

        RockData rockData =
                chunkData.getRockData();

        return rockData.getRock(pos);
    }

     */
    @Nullable
    public static RockSettings settings(
            LevelReader level,
            BlockPos pos
    )
    {
        final ChunkData chunkData = ChunkData.get(level, pos);

        if (chunkData == null
                || chunkData == ChunkData.EMPTY
                || chunkData.status() != ChunkData.Status.FULL)
        {
            return null;
        }

        try
        {
            return chunkData.getRockData().getRock(pos);
        }
        catch (NullPointerException e)
        {
            // Non-TFC chunk generator.
            return null;
        }
    }

    @Nullable
    public static Rock rock(
            LevelReader level,
            BlockPos pos
    )
    {
        RockSettings settings = settings(level, pos);

        if (settings == null)
        {
            return null;
        }

        return rock(settings);
    }

    @Nullable
    public static Rock rock(RockSettings settings)
    {
        initialize();
        return RAW_LOOKUP.get(settings.raw());
    }

    /**
     * Builds raw block -> Rock lookup.
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
                    TFCBlocks.ROCK_BLOCKS.get(rock);

            if (blocks == null)
            {
                continue;
            }

            TFCBlocks.Id<Block> raw =
                    blocks.get(Rock.BlockType.RAW);

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