package com.bumppo109.firma_compat.world.processor.dirt;

import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementResolver;

import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;


public final class DirtResolver
        implements ReplacementResolver<DirtTarget>
{
    private static final SoilBlockType.Variant FALLBACK_VARIANT =
            SoilBlockType.Variant.ENTISOL;


    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.DIRT;
    }


    @Override
    public Block resolve(
            LevelReader level,
            BlockPos pos,
            DirtTarget target
    )
    {
        Block fallback =
                level.getBlockState(pos)
                        .getBlock();

        try
        {
            return resolveTarget(
                    level,
                    pos,
                    target
            );
        }
        catch (RuntimeException ignored)
        {
            return fallback;
        }
    }


    private Block resolveTarget(
            LevelReader level,
            BlockPos pos,
            DirtTarget target
    )
    {
        SoilBlockType.Variant variant =
                SoilLookup.variant(
                        level,
                        pos
                );


        return switch (target.shape())
        {
            case BLOCK ->
                    resolveBlock(
                            target.type(),
                            variant
                    );

            case SLAB ->
                    resolveDecoration(
                            target.type(),
                            variant
                    )
                            .slab()
                            .get();

            case STAIRS ->
                    resolveDecoration(
                            target.type(),
                            variant
                    )
                            .stair()
                            .get();

            case WALL ->
                    resolveDecoration(
                            target.type(),
                            variant
                    )
                            .wall()
                            .get();
        };
    }


    private Block resolveBlock(
            DirtBlockType type,
            SoilBlockType.Variant variant
    )
    {
        Map<SoilBlockType.Variant, TFCBlocks.Id<Block>> variants =
                TFCBlocks.SOIL.get(
                        type.soilType()
                );


        if (variants == null)
        {
            throw new IllegalStateException(
                    "Missing soil registry for "
                            + type
            );
        }


        Supplier<Block> block =
                variants.get(
                        variant
                );


        /*
         * Some soil families may not have every climate variant.
         * Match TFC's behavior and fall back to ENTISOL.
         */
        if (block == null)
        {
            block =
                    variants.get(
                            FALLBACK_VARIANT
                    );
        }


        if (block == null)
        {
            throw new IllegalStateException(
                    "Missing soil variants for "
                            + type
            );
        }


        return block.get();
    }


    private DecorationBlockHolder resolveDecoration(
            DirtBlockType type,
            SoilBlockType.Variant variant
    )
    {
        if (type.soilType() != SoilBlockType.MUD_BRICKS)
        {
            throw new IllegalArgumentException(
                    type
                            + " does not support decorations"
            );
        }


        DecorationBlockHolder holder =
                TFCBlocks.MUD_BRICK_DECORATIONS.get(
                        variant
                );


        if (holder == null)
        {
            holder =
                    TFCBlocks.MUD_BRICK_DECORATIONS.get(
                            FALLBACK_VARIANT
                    );
        }


        if (holder == null)
        {
            throw new IllegalStateException(
                    "Missing mud brick decorations"
            );
        }


        return holder;
    }
}