package com.bumppo109.firma_compat.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;


public record ReplacementRuleSet(
        List<ReplacementRule> rules
)
{

    public static final Codec<ReplacementRuleSet> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            ReplacementRule.CODEC
                                    .listOf()
                                    .fieldOf("rules")
                                    .forGetter(
                                            ReplacementRuleSet::rules
                                    )

                    ).apply(
                            instance,
                            ReplacementRuleSet::new
                    )
            );



    public ReplacementRuleSet
    {
        if (rules.isEmpty())
        {
            throw new IllegalArgumentException(
                    "ReplacementRuleSet cannot be empty"
            );
        }
    }
}