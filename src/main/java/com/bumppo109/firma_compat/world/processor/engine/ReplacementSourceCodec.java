package com.bumppo109.firma_compat.world.processor.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public final class ReplacementSourceCodec
{
    private ReplacementSourceCodec()
    {
    }


    public static final MapCodec<ReplacementSource> MAP_CODEC =
            Codec.STRING
                    .dispatchMap(
                            "type",
                            ReplacementSourceCodec::type,
                            ReplacementSourceCodec::codecForType
                    );


    public static final Codec<ReplacementSource> CODEC =
            MAP_CODEC.codec();



    private static String type(
            ReplacementSource source
    )
    {
        return switch (source)
        {
            case ReplacementSource.BlockSource ignored ->
                    "block";

            case ReplacementSource.TagSource ignored ->
                    "tag";

            case ReplacementSource.ListSource ignored ->
                    "list";
        };
    }



    private static MapCodec<? extends ReplacementSource> codecForType(
            String type
    )
    {
        return switch(type)
        {
            case "block" ->
                    blockCodec();

            case "tag" ->
                    tagCodec();

            case "list" ->
                    listCodec();

            default ->
                    throw new IllegalArgumentException(
                            "Unknown replacement source type: " + type
                    );
        };
    }



    private static MapCodec<ReplacementSource.BlockSource> blockCodec()
    {
        return RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                                BuiltInRegistries.BLOCK
                                        .byNameCodec()
                                        .fieldOf("block")
                                        .forGetter(
                                                ReplacementSource.BlockSource::block
                                        )
                        )
                        .apply(
                                instance,
                                ReplacementSource.BlockSource::new
                        )
        );
    }



    private static MapCodec<ReplacementSource.TagSource> tagCodec()
    {
        return RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                                ResourceLocation.CODEC
                                        .fieldOf("tag")
                                        .forGetter(
                                                source ->
                                                        source.tag()
                                                                .location()
                                        )
                        )
                        .apply(
                                instance,
                                id ->
                                        new ReplacementSource.TagSource(
                                                TagKey.create(
                                                        Registries.BLOCK,
                                                        id
                                                )
                                        )
                        )
        );
    }



    private static MapCodec<ReplacementSource.ListSource> listCodec()
    {
        return RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                                BuiltInRegistries.BLOCK
                                        .byNameCodec()
                                        .listOf()
                                        .fieldOf("blocks")
                                        .forGetter(
                                                ReplacementSource.ListSource::blocks
                                        )
                        )
                        .apply(
                                instance,
                                ReplacementSource.ListSource::new
                        )
        );
    }
}