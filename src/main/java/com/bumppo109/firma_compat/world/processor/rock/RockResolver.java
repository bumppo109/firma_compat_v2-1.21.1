package com.bumppo109.firma_compat.world.processor.rock;

import com.bumppo109.firma_compat.world.processor.BlockShape;
import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementResolver;

import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;

import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Resolves logical rock replacements into TFC rock blocks.
 *
 * Uses the terrain rock at the structure position.
 *
 * If terrain data is unavailable, Granite is used as a safe fallback.
 */
public final class RockResolver
        implements ReplacementResolver<RockTarget>
{
    /**
     * Used when terrain rock cannot be determined.
     */
    private static final Rock FALLBACK_ROCK =
            Rock.GRANITE;

    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.ROCK;
    }

    @Override
    @Nullable
    public Block resolve(
            LevelReader level,
            BlockPos pos,
            RockTarget material
    )
    {
        RockSettings settings =
                RockLookup.settings(level, pos);

        // No TFC terrain information.
        if (settings == null)
        {
            return null;
        }

        Rock rock =
                RockLookup.rock(settings);

        // Terrain exists, but couldn't resolve the rock.
        if (rock == null)
        {
            rock = FALLBACK_ROCK;
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
            case BlockShape.BLOCK ->
                    resolveBlock(
                            rock,
                            material.type()
                    );

            case BlockShape.SLAB ->
                    resolveSlab(
                            rock,
                            material.type()
                    );

            case BlockShape.STAIRS ->
                    resolveStair(
                            rock,
                            material.type()
                    );

            case BlockShape.WALL ->
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
        ).slab().get();
    }

    private Block resolveStair(
            Rock rock,
            RockBlockType type
    )
    {
        return getDecoration(
                rock,
                type
        ).stair().get();
    }

    private Block resolveWall(
            Rock rock,
            RockBlockType type
    )
    {
        return getDecoration(
                rock,
                type
        ).wall().get();
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