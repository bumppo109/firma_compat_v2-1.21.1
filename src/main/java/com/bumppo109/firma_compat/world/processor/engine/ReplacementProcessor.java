package com.bumppo109.firma_compat.world.processor.engine;


import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.world.ModStructureProcessors;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;


public final class ReplacementProcessor
        extends StructureProcessor
{

    public static final MapCodec<ReplacementProcessor> CODEC =
            ReplacementRule.CODEC
                    .listOf()
                    .fieldOf("rules")
                    .xmap(
                            ReplacementProcessor::new,
                            processor -> processor.rules
                    );


    private final List<ReplacementRule> rules;



    public ReplacementProcessor(
            List<ReplacementRule> rules
    )
    {
        this.rules =
                List.copyOf(rules);
    }



    @Override
    @Nullable
    public StructureTemplate.StructureBlockInfo process(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo original,
            StructureTemplate.StructureBlockInfo transformed,
            StructurePlaceSettings settings,
            @Nullable StructureTemplate template
    )
    {
        Block current =
                transformed.state()
                        .getBlock();


        FirmaCompat.LOGGER.debug(
                "[ReplacementProcessor] checking {}",
                current
        );


        for (ReplacementRule rule : rules)
        {
            if (!rule.matches(current))
            {
                continue;
            }


            Block replacement =
                    resolve(
                            level,
                            transformed.pos(),
                            rule.target()
                    );


            FirmaCompat.LOGGER.debug(
                    "[ReplacementProcessor] {} -> {}",
                    current,
                    replacement
            );


            if (replacement == current)
            {
                return transformed;
            }


            return new StructureTemplate.StructureBlockInfo(
                    transformed.pos(),
                    replacement.defaultBlockState(),
                    transformed.nbt()
            );
        }


        return transformed;
    }



    private static <T extends ReplacementTarget> Block resolve(
            LevelReader level,
            BlockPos pos,
            T target
    )
    {
        ReplacementResolver<T> resolver =
                ReplacementResolverRegistry.get(
                        target.category()
                );

        return resolver.resolve(
                level,
                pos,
                target
        );
    }



    @Override
    protected StructureProcessorType<?> getType()
    {
        return ModStructureProcessors.REPLACEMENT.get();
    }
}