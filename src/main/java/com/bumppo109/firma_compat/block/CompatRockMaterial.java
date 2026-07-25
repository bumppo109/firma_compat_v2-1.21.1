package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum CompatRockMaterial implements Iterable<CompatRockSet> {
    STONE(
            CompatRockSet.STONE,
            CompatRockSet.COBBLESTONE,
            CompatRockSet.MOSSY_COBBLESTONE,
            null,
            CompatRockSet.SMOOTH_STONE,
            null,
            CompatRockSet.STONE_BRICKS,
            CompatRockSet.MOSSY_STONE_BRICKS,
            CompatRockSet.CRACKED_STONE_BRICKS,
            CompatRockSet.CHISELED_STONE_BRICKS,
            null,
            null
    ),

    DEEPSLATE(
            CompatRockSet.DEEPSLATE,
            CompatRockSet.COBBLED_DEEPSLATE,
            null,
            null,
            CompatRockSet.POLISHED_DEEPSLATE,
            CompatRockSet.CHISELED_DEEPSLATE,
            CompatRockSet.DEEPSLATE_BRICKS,
            null,
            CompatRockSet.CRACKED_DEEPSLATE_BRICKS,
            null,
            CompatRockSet.DEEPSLATE_TILES,
            CompatRockSet.CRACKED_DEEPSLATE_TILES
    ),

    ANDESITE(
            CompatRockSet.ANDESITE,
            null,
            null,
            null,
            CompatRockSet.POLISHED_ANDESITE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    DIORITE(
            CompatRockSet.DIORITE,
            null,
            null,
            null,
            CompatRockSet.POLISHED_DIORITE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    GRANITE(
            CompatRockSet.GRANITE,
            null,
            null,
            null,
            CompatRockSet.POLISHED_GRANITE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    TUFF(
            CompatRockSet.TUFF,
            null,
            null,
            null,
            CompatRockSet.POLISHED_TUFF,
            CompatRockSet.CHISELED_TUFF,
            CompatRockSet.TUFF_BRICKS,
            null,
            null,
            CompatRockSet.CHISELED_TUFF_BRICKS,
            null,
            null
    ),

    CALCITE(
            CompatRockSet.CALCITE,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    DRIPSTONE(
            CompatRockSet.DRIPSTONE,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    OBSIDIAN(
            CompatRockSet.OBSIDIAN,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    CRYING_OBSIDIAN(
            CompatRockSet.CRYING_OBSIDIAN,
            null, null, null, null, null, null, null, null, null, null, null
    ),

    BASALT(
            CompatRockSet.BASALT,
            null,
            null,
            CompatRockSet.SMOOTH_BASALT,
            CompatRockSet.POLISHED_BASALT,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    ),

    BLACKSTONE(
            CompatRockSet.BLACKSTONE,
            null,
            null,
            null,
            CompatRockSet.POLISHED_BLACKSTONE,
            CompatRockSet.CHISELED_POLISHED_BLACKSTONE,
            CompatRockSet.POLISHED_BLACKSTONE_BRICKS,
            null,
            CompatRockSet.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            null,
            null,
            null
    ),

    NETHERRACK(
            CompatRockSet.NETHERRACK,
            null,
            null,
            null,
            null,
            null,
            CompatRockSet.NETHER_BRICKS,
            null,
            CompatRockSet.CRACKED_NETHER_BRICKS,
            CompatRockSet.CHISELED_NETHER_BRICKS,
            null,
            null
    ),

    END_STONE(
            CompatRockSet.END_STONE,
            null,
            null,
            null,
            null,
            null,
            CompatRockSet.END_STONE_BRICKS,
            null,
            null,
            null,
            null,
            null
    );

    private final CompatRockSet raw;
    @Nullable private final CompatRockSet cobble;
    @Nullable private final CompatRockSet mossyCobble;
    @Nullable private final CompatRockSet smooth;
    @Nullable private final CompatRockSet polished;
    @Nullable private final CompatRockSet chiseled;
    @Nullable private final CompatRockSet bricks;
    @Nullable private final CompatRockSet mossyBrick;
    @Nullable private final CompatRockSet crackedBrick;
    @Nullable private final CompatRockSet chiseledBrick;
    @Nullable private final CompatRockSet tile;
    @Nullable private final CompatRockSet crackedTile;

    CompatRockMaterial(
            CompatRockSet raw,
            @Nullable CompatRockSet cobble,
            @Nullable CompatRockSet mossyCobble,
            @Nullable CompatRockSet smooth,
            @Nullable CompatRockSet polished,
            @Nullable CompatRockSet chiseled,
            @Nullable CompatRockSet brick,
            @Nullable CompatRockSet mossyBrick,
            @Nullable CompatRockSet crackedBrick,
            @Nullable CompatRockSet chiseledBrick,
            @Nullable CompatRockSet tile,
            @Nullable CompatRockSet crackedTile
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

    public CompatRockSet raw() {
        return raw;
    }

    @Nullable
    public CompatRockSet cobble() {
        return cobble;
    }

    @Nullable
    public CompatRockSet mossyCobble() {
        return mossyCobble;
    }

    @Nullable
    public CompatRockSet smooth() {
        return smooth;
    }

    @Nullable
    public CompatRockSet polished() {
        return polished;
    }

    @Nullable
    public CompatRockSet chiseled() {
        return chiseled;
    }

    @Nullable
    public CompatRockSet brick() {
        return bricks;
    }

    @Nullable
    public CompatRockSet mossyBrick() {
        return mossyBrick;
    }

    @Nullable
    public CompatRockSet crackedBrick() {
        return crackedBrick;
    }

    @Nullable
    public CompatRockSet chiseledBrick() {
        return chiseledBrick;
    }

    @Nullable
    public CompatRockSet tile() {
        return tile;
    }

    @Nullable
    public CompatRockSet crackedTile() {
        return crackedTile;
    }

    public Stream<CompatRockSet> stream() {
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
    public @NotNull Iterator<CompatRockSet> iterator() {
        return stream().iterator();
    }
}