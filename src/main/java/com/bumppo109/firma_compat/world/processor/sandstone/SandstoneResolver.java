package com.bumppo109.firma_compat.world.processor.sandstone;

import com.bumppo109.firma_compat.world.processor.ReplacementCategory;
import com.bumppo109.firma_compat.world.processor.ReplacementResolver;
import com.bumppo109.firma_compat.world.processor.rock.RockLookup;

import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.world.settings.RockSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

import java.util.Map;


public final class SandstoneResolver
        implements ReplacementResolver<SandstoneTarget>
{

    @Override
    public ReplacementCategory category()
    {
        return ReplacementCategory.SANDSTONE;
    }


    @Override
    public Block resolve(
            LevelReader level,
            BlockPos pos,
            SandstoneTarget target
    )
    {
        RockSettings settings =
                RockLookup.settings(
                        level,
                        pos
                );


        if (settings == null)
        {
            return level.getBlockState(pos)
                    .getBlock();
        }


        SandBlockType sand =
                findSandType(
                        settings.sand()
                );


        return resolve(
                sand,
                target
        );
    }


    private Block resolve(
            SandBlockType sand,
            SandstoneTarget target
    )
    {
        SandstoneBlockType type =
                target.type()
                        .tfcType();


        return switch (target.shape())
        {
            case BLOCK ->
                    resolveBlock(
                            sand,
                            type
                    );

            case SLAB ->
                    resolveDecoration(
                            sand,
                            type
                    )
                            .slab()
                            .get();

            case STAIRS ->
                    resolveDecoration(
                            sand,
                            type
                    )
                            .stair()
                            .get();

            case WALL ->
                    resolveDecoration(
                            sand,
                            type
                    )
                            .wall()
                            .get();
        };
    }


    private Block resolveBlock(
            SandBlockType sand,
            SandstoneBlockType type
    )
    {
        Map<SandstoneBlockType, TFCBlocks.Id<Block>> blocks =
                TFCBlocks.SANDSTONE.get(
                        sand
                );


        if (blocks == null)
        {
            throw new IllegalStateException(
                    "Missing sandstone registry for "
                            + sand
            );
        }


        TFCBlocks.Id<Block> block =
                blocks.get(type);


        if (block == null)
        {
            throw new IllegalStateException(
                    "Missing sandstone "
                            + type
                            + " for "
                            + sand
            );
        }


        return block.get();
    }


    private DecorationBlockHolder resolveDecoration(
            SandBlockType sand,
            SandstoneBlockType type
    )
    {
        Map<SandstoneBlockType, DecorationBlockHolder> decorations =
                TFCBlocks.SANDSTONE_DECORATIONS.get(
                        sand
                );


        if (decorations == null)
        {
            throw new IllegalStateException(
                    "Missing sandstone decorations for "
                            + sand
            );
        }


        DecorationBlockHolder holder =
                decorations.get(type);


        if (holder == null)
        {
            throw new IllegalStateException(
                    "Missing sandstone decoration "
                            + type
                            + " for "
                            + sand
            );
        }


        return holder;
    }


    private SandBlockType findSandType(
            Block sand
    )
    {
        for (SandBlockType type : SandBlockType.values())
        {
            if (TFCBlocks.SAND.get(type).get() == sand)
            {
                return type;
            }
        }


        throw new IllegalStateException(
                "Unable to determine sand type for "
                        + sand
        );
    }
}