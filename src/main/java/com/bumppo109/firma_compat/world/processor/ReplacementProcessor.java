package com.bumppo109.firma_compat.world.processor;


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



    private static <T extends ReplacementTarget> @Nullable Block resolve(
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
        Block current = transformed.state().getBlock();

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

            // No terrain information available.
            if (replacement == null)
            {
                return transformed;
            }

            if (replacement == current)
            {
                return transformed;
            }

            return new StructureTemplate.StructureBlockInfo(
                    transformed.pos(),
                    replacement.withPropertiesOf(transformed.state()),
                    transformed.nbt()
            );
        }

        return transformed;
    }

    @Override
    protected StructureProcessorType<?> getType()
    {
        return ModStructureProcessors.REPLACEMENT.get();
    }
}