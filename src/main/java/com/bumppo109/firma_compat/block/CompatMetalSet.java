package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.datagen.BuiltinRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum CompatMetalSet {
    COPPER(
            () -> Blocks.COPPER_BLOCK,
            null,
            null
    ),
    EXPOSED_COPPER(
            () -> Blocks.EXPOSED_COPPER,
            null,
            null
    ),
    WEATHERED_COPPER(
            () -> Blocks.WEATHERED_COPPER,
            null,
            null
    ),
    OXIDIZED_COPPER(
            () -> Blocks.OXIDIZED_COPPER,
            null,
            null
    ),
    WAXED_COPPER(
            () -> Blocks.WAXED_COPPER_BLOCK,
            null,
            null
    ),
    WAXED_EXPOSED_COPPER(
            () -> Blocks.WAXED_EXPOSED_COPPER,
            null,
            null
    ),
    WAXED_WEATHERED_COPPER(
            () -> Blocks.WAXED_WEATHERED_COPPER,
            null,
            null
    ),
    WAXED_OXIDIZED_COPPER(
            () -> Blocks.WAXED_OXIDIZED_COPPER,
            null,
            null
    ),

    CHISELED_COPPER(
            () -> Blocks.CHISELED_COPPER,
            null,
            null
    ),
    EXPOSED_CHISELED_COPPER(
            () -> Blocks.EXPOSED_CHISELED_COPPER,
            null,
            null
    ),
    WEATHERED_CHISELED_COPPER(
            () -> Blocks.WEATHERED_CHISELED_COPPER,
            null,
            null
    ),
    OXIDIZED_CHISELED_COPPER(
            () -> Blocks.OXIDIZED_CHISELED_COPPER,
            null,
            null
    ),
    WAXED_CHISELED_COPPER(
            () -> Blocks.WAXED_CHISELED_COPPER,
            null,
            null
    ),
    WAXED_EXPOSED_CHISELED_COPPER(
            () -> Blocks.WAXED_EXPOSED_CHISELED_COPPER,
            null,
            null
    ),
    WAXED_WEATHERED_CHISELED_COPPER(
            () -> Blocks.WAXED_WEATHERED_CHISELED_COPPER,
            null,
            null
    ),
    WAXED_OXIDIZED_CHISELED_COPPER(
            () -> Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
            null,
            null
    ),

    COPPER_GRATE(
            () -> Blocks.COPPER_GRATE,
            null,
            null
    ),
    EXPOSED_COPPER_GRATE(
            () -> Blocks.EXPOSED_COPPER_GRATE,
            null,
            null
    ),
    WEATHERED_COPPER_GRATE(
            () -> Blocks.WEATHERED_COPPER_GRATE,
            null,
            null
    ),
    OXIDIZED_COPPER_GRATE(
            () -> Blocks.OXIDIZED_COPPER_GRATE,
            null,
            null
    ),
    WAXED_COPPER_GRATE(
            () -> Blocks.WAXED_COPPER_GRATE,
            null,
            null
    ),
    WAXED_EXPOSED_COPPER_GRATE(
            () -> Blocks.WAXED_EXPOSED_COPPER_GRATE,
            null,
            null
    ),
    WAXED_WEATHERED_COPPER_GRATE(
            () -> Blocks.WAXED_WEATHERED_COPPER_GRATE,
            null,
            null
    ),
    WAXED_OXIDIZED_COPPER_GRATE(
            () -> Blocks.WAXED_OXIDIZED_COPPER_GRATE,
            null,
            null
    ),

    CUT_COPPER(
            () -> Blocks.CUT_COPPER,
            () -> Blocks.CUT_COPPER_SLAB,
            () -> Blocks.CUT_COPPER_STAIRS
    ),
    EXPOSED_CUT_COPPER(
            () -> Blocks.EXPOSED_CUT_COPPER,
            () -> Blocks.EXPOSED_CUT_COPPER_SLAB,
            () -> Blocks.EXPOSED_CUT_COPPER_STAIRS
    ),
    WEATHERED_CUT_COPPER(
            () -> Blocks.WEATHERED_CUT_COPPER,
            () -> Blocks.WEATHERED_CUT_COPPER_SLAB,
            () -> Blocks.WEATHERED_CUT_COPPER_STAIRS
    ),
    OXIDIZED_CUT_COPPER(
            () -> Blocks.OXIDIZED_CUT_COPPER,
            () -> Blocks.OXIDIZED_CUT_COPPER_SLAB,
            () -> Blocks.OXIDIZED_CUT_COPPER_STAIRS
    ),
    WAXED_CUT_COPPER(
            () -> Blocks.WAXED_CUT_COPPER,
            () -> Blocks.WAXED_CUT_COPPER_SLAB,
            () -> Blocks.WAXED_CUT_COPPER_STAIRS
    ),
    WAXED_EXPOSED_CUT_COPPER(
            () -> Blocks.WAXED_EXPOSED_CUT_COPPER,
            () -> Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
            () -> Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS
    ),
    WAXED_WEATHERED_CUT_COPPER(
            () -> Blocks.WAXED_WEATHERED_CUT_COPPER,
            () -> Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
            () -> Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS
    ),
    WAXED_OXIDIZED_CUT_COPPER(
            () -> Blocks.WAXED_OXIDIZED_CUT_COPPER,
            () -> Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
            () -> Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS
    ),

    ;

    private final Supplier<Block> base;
    @Nullable private final Supplier<Block> slab;
    @Nullable private final Supplier<Block> stairs;

    CompatMetalSet(
            Supplier<Block> base,
            @Nullable Supplier<Block> slab,
            @Nullable Supplier<Block> stairs
    ) {
        this.base = base;
        this.slab = slab;
        this.stairs = stairs;
    }

    public Supplier<Block> base() {
        return base;
    }

    @Nullable
    public Supplier<Block> slab() {
        return slab;
    }

    @Nullable
    public Supplier<Block> stairs() {
        return stairs;
    }

    private static String id(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public record MetalPart(String id, Block block, int amount) {}

    public Stream<MetalPart> parts() {
        Stream.Builder<MetalPart> builder = Stream.builder();

        Block baseBlock = base.get();
        builder.add(new MetalPart(id(baseBlock), baseBlock, 100));

        if (slab != null) {
            Block slabBlock = slab.get();
            builder.add(new MetalPart(id(slabBlock), slabBlock, 50));
        }

        if (stairs != null) {
            Block stairBlock = stairs.get();
            builder.add(new MetalPart(id(stairBlock), stairBlock, 75));
        }

        return builder.build();
    }
}