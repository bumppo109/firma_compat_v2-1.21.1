package com.bumppo109.firma_compat.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;


/**
 * Defines a single block replacement rule.
 */
public record ReplacementRule(
        ReplacementSource source,
        ReplacementTarget target
)
{

    public static final Codec<ReplacementRule> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            ReplacementSourceCodec.CODEC
                                    .fieldOf("source")
                                    .forGetter(ReplacementRule::source),

                            ReplacementTargetCodec.CODEC
                                    .fieldOf("target")
                                    .forGetter(ReplacementRule::target)

                    ).apply(
                            instance,
                            ReplacementRule::new
                    )
            );


    public boolean matches(Block block)
    {
        return source.matches(block);
    }
}