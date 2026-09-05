package com.bumppo109.firma_compat.materials;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum RockSet {
    STONE(
            () -> Blocks.STONE,
            () -> Blocks.STONE_SLAB,
            () -> Blocks.STONE_STAIRS,
            null
    ),

    COBBLESTONE(
            () -> Blocks.COBBLESTONE,
            () -> Blocks.COBBLESTONE_SLAB,
            () -> Blocks.COBBLESTONE_STAIRS,
            () -> Blocks.COBBLESTONE_WALL
    ),

    MOSSY_COBBLESTONE(
            () -> Blocks.MOSSY_COBBLESTONE,
            () -> Blocks.MOSSY_COBBLESTONE_SLAB,
            () -> Blocks.MOSSY_COBBLESTONE_STAIRS,
            () -> Blocks.MOSSY_COBBLESTONE_WALL
    ),

    SMOOTH_STONE(
            () -> Blocks.SMOOTH_STONE,
            () -> Blocks.SMOOTH_STONE_SLAB,
            null,
            null
    ),

    STONE_BRICKS(
            () -> Blocks.STONE_BRICKS,
            () -> Blocks.STONE_BRICK_SLAB,
            () -> Blocks.STONE_BRICK_STAIRS,
            () -> Blocks.STONE_BRICK_WALL
    ),

    MOSSY_STONE_BRICKS(
            () -> Blocks.MOSSY_STONE_BRICKS,
            () -> Blocks.MOSSY_STONE_BRICK_SLAB,
            () -> Blocks.MOSSY_STONE_BRICK_STAIRS,
            () -> Blocks.MOSSY_STONE_BRICK_WALL
    ),

    CRACKED_STONE_BRICKS(
            () -> Blocks.CRACKED_STONE_BRICKS,
            null,
            null,
            null
    ),

    CHISELED_STONE_BRICKS(
            () -> Blocks.CHISELED_STONE_BRICKS,
            null,
            null,
            null
    ),

    DEEPSLATE(
            () -> Blocks.DEEPSLATE,
            null,
            null,
            null
    ),

    COBBLED_DEEPSLATE(
            () -> Blocks.COBBLED_DEEPSLATE,
            () -> Blocks.COBBLED_DEEPSLATE_SLAB,
            () -> Blocks.COBBLED_DEEPSLATE_STAIRS,
            () -> Blocks.COBBLED_DEEPSLATE_WALL
    ),

    CHISELED_DEEPSLATE(
            () -> Blocks.CHISELED_DEEPSLATE,
            null,
            null,
            null
    ),

    POLISHED_DEEPSLATE(
            () -> Blocks.POLISHED_DEEPSLATE,
            () -> Blocks.POLISHED_DEEPSLATE_SLAB,
            () -> Blocks.POLISHED_DEEPSLATE_STAIRS,
            () -> Blocks.POLISHED_DEEPSLATE_WALL
    ),

    DEEPSLATE_BRICKS(
            () -> Blocks.DEEPSLATE_BRICKS,
            () -> Blocks.DEEPSLATE_BRICK_SLAB,
            () -> Blocks.DEEPSLATE_BRICK_STAIRS,
            () -> Blocks.DEEPSLATE_BRICK_WALL
    ),

    CRACKED_DEEPSLATE_BRICKS(
            () -> Blocks.CRACKED_DEEPSLATE_BRICKS,
            null,
            null,
            null
    ),

    DEEPSLATE_TILES(
            () -> Blocks.DEEPSLATE_TILES,
            () -> Blocks.DEEPSLATE_TILE_SLAB,
            () -> Blocks.DEEPSLATE_TILE_STAIRS,
            () -> Blocks.DEEPSLATE_TILE_WALL
    ),

    CRACKED_DEEPSLATE_TILES(
            () -> Blocks.CRACKED_DEEPSLATE_TILES,
            null,
            null,
            null
    ),

    ANDESITE(
            () -> Blocks.ANDESITE,
            () -> Blocks.ANDESITE_SLAB,
            () -> Blocks.ANDESITE_STAIRS,
            () -> Blocks.ANDESITE_WALL
    ),

    POLISHED_ANDESITE(
            () -> Blocks.POLISHED_ANDESITE,
            () -> Blocks.POLISHED_ANDESITE_SLAB,
            () -> Blocks.POLISHED_ANDESITE_STAIRS,
            null
    ),

    GRANITE(
            () -> Blocks.GRANITE,
            () -> Blocks.GRANITE_SLAB,
            () -> Blocks.GRANITE_STAIRS,
            () -> Blocks.GRANITE_WALL
    ),

    POLISHED_GRANITE(
            () -> Blocks.POLISHED_GRANITE,
            () -> Blocks.POLISHED_GRANITE_SLAB,
            () -> Blocks.POLISHED_GRANITE_STAIRS,
            null
    ),

    DIORITE(
            () -> Blocks.DIORITE,
            () -> Blocks.DIORITE_SLAB,
            () -> Blocks.DIORITE_STAIRS,
            () -> Blocks.DIORITE_WALL
    ),

    POLISHED_DIORITE(
            () -> Blocks.POLISHED_DIORITE,
            () -> Blocks.POLISHED_DIORITE_SLAB,
            () -> Blocks.POLISHED_DIORITE_STAIRS,
            null
    ),

    TUFF(
            () -> Blocks.TUFF,
            () -> Blocks.TUFF_SLAB,
            () -> Blocks.TUFF_STAIRS,
            () -> Blocks.TUFF_WALL
    ),

    CHISELED_TUFF(
            () -> Blocks.CHISELED_TUFF,
            null,
            null,
            null
    ),

    POLISHED_TUFF(
            () -> Blocks.POLISHED_TUFF,
            () -> Blocks.POLISHED_TUFF_SLAB,
            () -> Blocks.POLISHED_TUFF_STAIRS,
            () -> Blocks.POLISHED_TUFF_WALL
    ),

    TUFF_BRICKS(
            () -> Blocks.TUFF_BRICKS,
            () -> Blocks.TUFF_BRICK_SLAB,
            () -> Blocks.TUFF_BRICK_STAIRS,
            () -> Blocks.TUFF_BRICK_WALL
    ),

    CHISELED_TUFF_BRICKS(
            () -> Blocks.CHISELED_TUFF_BRICKS,
            null,
            null,
            null
    ),

    CALCITE(
            () -> Blocks.CALCITE,
            null,
            null,
            null
    ),

    DRIPSTONE(
            () -> Blocks.DRIPSTONE_BLOCK,
            null,
            null,
            null
    ),

    OBSIDIAN(
            () -> Blocks.OBSIDIAN,
            null,
            null,
            null
    ),

    CRYING_OBSIDIAN(
            () -> Blocks.CRYING_OBSIDIAN,
            null,
            null,
            null
    ),

    BASALT(
            () -> Blocks.BASALT,
            null,
            null,
            null
    ),

    POLISHED_BASALT(
            () -> Blocks.POLISHED_BASALT,
            null,
            null,
            null
    ),

    SMOOTH_BASALT(
            () -> Blocks.SMOOTH_BASALT,
            null,
            null,
            null
    ),

    BLACKSTONE(
            () -> Blocks.BLACKSTONE,
            () -> Blocks.BLACKSTONE_SLAB,
            () -> Blocks.BLACKSTONE_STAIRS,
            () -> Blocks.BLACKSTONE_WALL
    ),

    CHISELED_POLISHED_BLACKSTONE(
            () -> Blocks.CHISELED_POLISHED_BLACKSTONE,
            null,
            null,
            null
    ),

    POLISHED_BLACKSTONE(
            () -> Blocks.POLISHED_BLACKSTONE,
            () -> Blocks.POLISHED_BLACKSTONE_SLAB,
            () -> Blocks.POLISHED_BLACKSTONE_STAIRS,
            () -> Blocks.POLISHED_BLACKSTONE_WALL
    ),

    POLISHED_BLACKSTONE_BRICKS(
            () -> Blocks.POLISHED_BLACKSTONE_BRICKS,
            () -> Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,
            () -> Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,
            () -> Blocks.POLISHED_BLACKSTONE_BRICK_WALL
    ),

    CRACKED_POLISHED_BLACKSTONE_BRICKS(
            () -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            null,
            null,
            null
    ),

    NETHERRACK(
            () -> Blocks.NETHERRACK,
            null,
            null,
            null
    ),

    NETHER_BRICKS(
            () -> Blocks.NETHER_BRICKS,
            () -> Blocks.NETHER_BRICK_SLAB,
            () -> Blocks.NETHER_BRICK_STAIRS,
            () -> Blocks.NETHER_BRICK_WALL
    ),

    CHISELED_NETHER_BRICKS(
            () -> Blocks.CHISELED_NETHER_BRICKS,
            null,
            null,
            null
    ),

    CRACKED_NETHER_BRICKS(
            () -> Blocks.CRACKED_NETHER_BRICKS,
            null,
            null,
            null
    ),

    END_STONE(
            () -> Blocks.END_STONE,
            null,
            null,
            null
    ),

    END_STONE_BRICKS(
            () -> Blocks.END_STONE_BRICKS,
            () -> Blocks.END_STONE_BRICK_SLAB,
            () -> Blocks.END_STONE_BRICK_STAIRS,
            () -> Blocks.END_STONE_BRICK_WALL
    );

    private final Supplier<Block> base;
    @Nullable private final Supplier<Block> slab;
    @Nullable private final Supplier<Block> stairs;
    @Nullable private final Supplier<Block> wall;

    RockSet(
            Supplier<Block> base,
            @Nullable Supplier<Block> slab,
            @Nullable Supplier<Block> stairs,
            @Nullable Supplier<Block> wall
    ) {
        this.base = base;
        this.slab = slab;
        this.stairs = stairs;
        this.wall = wall;
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

    @Nullable
    public Supplier<Block> wall() {
        return wall;
    }
}