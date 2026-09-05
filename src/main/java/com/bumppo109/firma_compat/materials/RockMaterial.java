package com.bumppo109.firma_compat.materials;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public enum RockMaterial implements Iterable<RockSet> {
    STONE(
            RockSet.STONE,
            RockSet.COBBLESTONE,
            RockSet.MOSSY_COBBLESTONE,
            null,
            RockSet.SMOOTH_STONE,
            null,
            RockSet.STONE_BRICKS,
            RockSet.MOSSY_STONE_BRICKS,
            RockSet.CRACKED_STONE_BRICKS,
            RockSet.CHISELED_STONE_BRICKS,
            null,
            null
    ),

    DEEPSLATE(
            RockSet.DEEPSLATE,
            RockSet.COBBLED_DEEPSLATE,
            null,
            null,
            RockSet.POLISHED_DEEPSLATE,
            RockSet.CHISELED_DEEPSLATE,
            RockSet.DEEPSLATE_BRICKS,
            null,
            RockSet.CRACKED_DEEPSLATE_BRICKS,
            null,
            RockSet.DEEPSLATE_TILES,
            RockSet.CRACKED_DEEPSLATE_TILES
    ),

    ANDESITE(
            RockSet.ANDESITE,
            null,
            null,
            null,
            RockSet.POLISHED_ANDESITE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    DIORITE(
            RockSet.DIORITE,
            null,
            null,
            null,
            RockSet.POLISHED_DIORITE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    GRANITE(
            RockSet.GRANITE,
            null,
            null,
            null,
            RockSet.POLISHED_GRANITE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    TUFF(
            RockSet.TUFF,
            null,
            null,
            null,
            RockSet.POLISHED_TUFF,
            RockSet.CHISELED_TUFF,
            RockSet.TUFF_BRICKS,
            null,
            null,
            RockSet.CHISELED_TUFF_BRICKS,
            null,
            null
    ),

    CALCITE(
            RockSet.CALCITE,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    DRIPSTONE(
            RockSet.DRIPSTONE,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    OBSIDIAN(
            RockSet.OBSIDIAN,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    CRYING_OBSIDIAN(
            RockSet.CRYING_OBSIDIAN,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    BASALT(
            RockSet.BASALT,
            null,
            null,
            RockSet.SMOOTH_BASALT,
            RockSet.POLISHED_BASALT,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    BLACKSTONE(
            RockSet.BLACKSTONE,
            null,
            null,
            null,
            RockSet.POLISHED_BLACKSTONE,
            RockSet.CHISELED_POLISHED_BLACKSTONE,
            RockSet.POLISHED_BLACKSTONE_BRICKS,
            null,
            RockSet.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            null,
            null,
            null
    ),

    NETHERRACK(
            RockSet.NETHERRACK,
            null,
            null,
            null,
            null,
            null,
            RockSet.NETHER_BRICKS,
            null,
            RockSet.CRACKED_NETHER_BRICKS,
            RockSet.CHISELED_NETHER_BRICKS,
            null,
            null
    ),

    END_STONE(
            RockSet.END_STONE,
            null,
            null,
            null,
            null,
            null,
            RockSet.END_STONE_BRICKS,
            null,
            null,
            null,
            null,
            null
    );

    private final RockSet raw;
    @Nullable private final RockSet cobble;
    @Nullable private final RockSet mossyCobble;
    @Nullable private final RockSet smooth;
    @Nullable private final RockSet polished;
    @Nullable private final RockSet chiseled;
    @Nullable private final RockSet bricks;
    @Nullable private final RockSet mossyBrick;
    @Nullable private final RockSet crackedBrick;
    @Nullable private final RockSet chiseledBrick;
    @Nullable private final RockSet tile;
    @Nullable private final RockSet crackedTile;

    RockMaterial(
            RockSet raw,
            @Nullable RockSet cobble,
            @Nullable RockSet mossyCobble,
            @Nullable RockSet smooth,
            @Nullable RockSet polished,
            @Nullable RockSet chiseled,
            @Nullable RockSet brick,
            @Nullable RockSet mossyBrick,
            @Nullable RockSet crackedBrick,
            @Nullable RockSet chiseledBrick,
            @Nullable RockSet tile,
            @Nullable RockSet crackedTile
    ) {
        this.raw = raw;
        this.cobble = cobble;
        this.mossyCobble = mossyCobble;
        this.smooth = smooth;
        this.polished = polished;
        this.chiseled = chiseled;
        this.bricks = brick;
        this.mossyBrick = mossyBrick;
        this.crackedBrick = crackedBrick;
        this.chiseledBrick = chiseledBrick;
        this.tile = tile;
        this.crackedTile = crackedTile;
    }

    public RockSet raw() {
        return raw;
    }

    @Nullable
    public RockSet cobble() {
        return cobble;
    }

    @Nullable
    public RockSet mossyCobble() {
        return mossyCobble;
    }

    @Nullable
    public RockSet smooth() {
        return smooth;
    }

    @Nullable
    public RockSet polished() {
        return polished;
    }

    @Nullable
    public RockSet chiseled() {
        return chiseled;
    }

    @Nullable
    public RockSet brick() {
        return bricks;
    }

    @Nullable
    public RockSet mossyBrick() {
        return mossyBrick;
    }

    @Nullable
    public RockSet crackedBrick() {
        return crackedBrick;
    }

    @Nullable
    public RockSet chiseledBrick() {
        return chiseledBrick;
    }

    @Nullable
    public RockSet tile() {
        return tile;
    }

    @Nullable
    public RockSet crackedTile() {
        return crackedTile;
    }

    public Stream<RockSet> stream() {
        return Stream.of(
                raw,
                cobble,
                mossyCobble,
                smooth,
                polished,
                chiseled,
                bricks,
                mossyBrick,
                crackedBrick,
                chiseledBrick,
                tile,
                crackedTile
        ).filter(Objects::nonNull);
    }

    @Override
    public @NotNull Iterator<RockSet> iterator() {
        return stream().iterator();
    }
}