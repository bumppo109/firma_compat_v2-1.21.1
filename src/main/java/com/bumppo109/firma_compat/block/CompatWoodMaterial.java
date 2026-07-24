package com.bumppo109.firma_compat.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum CompatWoodMaterial {

    ACACIA(
            WoodType.ACACIA,
            BlockSetType.ACACIA,
            () -> Blocks.ACACIA_LOG,
            () -> Blocks.STRIPPED_ACACIA_LOG,
            () -> Blocks.ACACIA_WOOD,
            () -> Blocks.STRIPPED_ACACIA_WOOD,
            () -> Blocks.ACACIA_PLANKS,
            () -> Blocks.ACACIA_STAIRS,
            () -> Blocks.ACACIA_SLAB,
            () -> Blocks.ACACIA_DOOR,
            () -> Blocks.ACACIA_TRAPDOOR,
            () -> Blocks.ACACIA_FENCE,
            () -> Blocks.ACACIA_FENCE_GATE,
            () -> Blocks.ACACIA_SIGN,
            () -> Blocks.ACACIA_HANGING_SIGN,
            () -> Blocks.ACACIA_PRESSURE_PLATE,
            () -> Blocks.ACACIA_BUTTON
    ),
    BIRCH(
            WoodType.BIRCH,
            BlockSetType.BIRCH,
            () -> Blocks.BIRCH_LOG,
            () -> Blocks.STRIPPED_BIRCH_LOG,
            () -> Blocks.BIRCH_WOOD,
            () -> Blocks.STRIPPED_BIRCH_WOOD,
            () -> Blocks.BIRCH_PLANKS,
            () -> Blocks.BIRCH_STAIRS,
            () -> Blocks.BIRCH_SLAB,
            () -> Blocks.BIRCH_DOOR,
            () -> Blocks.BIRCH_TRAPDOOR,
            () -> Blocks.BIRCH_FENCE,
            () -> Blocks.BIRCH_FENCE_GATE,
            () -> Blocks.BIRCH_SIGN,
            () -> Blocks.BIRCH_HANGING_SIGN,
            () -> Blocks.BIRCH_PRESSURE_PLATE,
            () -> Blocks.BIRCH_BUTTON
    ),
    CHERRY(
            WoodType.CHERRY,
            BlockSetType.CHERRY,
            () -> Blocks.CHERRY_LOG,
            () -> Blocks.STRIPPED_CHERRY_LOG,
            () -> Blocks.CHERRY_WOOD,
            () -> Blocks.STRIPPED_CHERRY_WOOD,
            () -> Blocks.CHERRY_PLANKS,
            () -> Blocks.CHERRY_STAIRS,
            () -> Blocks.CHERRY_SLAB,
            () -> Blocks.CHERRY_DOOR,
            () -> Blocks.CHERRY_TRAPDOOR,
            () -> Blocks.CHERRY_FENCE,
            () -> Blocks.CHERRY_FENCE_GATE,
            () -> Blocks.CHERRY_SIGN,
            () -> Blocks.CHERRY_HANGING_SIGN,
            () -> Blocks.CHERRY_PRESSURE_PLATE,
            () -> Blocks.CHERRY_BUTTON
    ),
    DARK_OAK(
            WoodType.DARK_OAK,
            BlockSetType.DARK_OAK,
            () -> Blocks.DARK_OAK_LOG,
            () -> Blocks.STRIPPED_DARK_OAK_LOG,
            () -> Blocks.DARK_OAK_WOOD,
            () -> Blocks.STRIPPED_DARK_OAK_WOOD,
            () -> Blocks.DARK_OAK_PLANKS,
            () -> Blocks.DARK_OAK_STAIRS,
            () -> Blocks.DARK_OAK_SLAB,
            () -> Blocks.DARK_OAK_DOOR,
            () -> Blocks.DARK_OAK_TRAPDOOR,
            () -> Blocks.DARK_OAK_FENCE,
            () -> Blocks.DARK_OAK_FENCE_GATE,
            () -> Blocks.DARK_OAK_SIGN,
            () -> Blocks.DARK_OAK_HANGING_SIGN,
            () -> Blocks.DARK_OAK_PRESSURE_PLATE,
            () -> Blocks.DARK_OAK_BUTTON
    ),
    JUNGLE(
            WoodType.JUNGLE,
            BlockSetType.JUNGLE,
            () -> Blocks.JUNGLE_LOG,
            () -> Blocks.STRIPPED_JUNGLE_LOG,
            () -> Blocks.JUNGLE_WOOD,
            () -> Blocks.STRIPPED_JUNGLE_WOOD,
            () -> Blocks.JUNGLE_PLANKS,
            () -> Blocks.JUNGLE_STAIRS,
            () -> Blocks.JUNGLE_SLAB,
            () -> Blocks.JUNGLE_DOOR,
            () -> Blocks.JUNGLE_TRAPDOOR,
            () -> Blocks.JUNGLE_FENCE,
            () -> Blocks.JUNGLE_FENCE_GATE,
            () -> Blocks.JUNGLE_SIGN,
            () -> Blocks.JUNGLE_HANGING_SIGN,
            () -> Blocks.JUNGLE_PRESSURE_PLATE,
            () -> Blocks.JUNGLE_BUTTON
    ),
    MANGROVE(
            WoodType.MANGROVE,
            BlockSetType.MANGROVE,
            () -> Blocks.MANGROVE_LOG,
            () -> Blocks.STRIPPED_MANGROVE_LOG,
            () -> Blocks.MANGROVE_WOOD,
            () -> Blocks.STRIPPED_MANGROVE_WOOD,
            () -> Blocks.MANGROVE_PLANKS,
            () -> Blocks.MANGROVE_STAIRS,
            () -> Blocks.MANGROVE_SLAB,
            () -> Blocks.MANGROVE_DOOR,
            () -> Blocks.MANGROVE_TRAPDOOR,
            () -> Blocks.MANGROVE_FENCE,
            () -> Blocks.MANGROVE_FENCE_GATE,
            () -> Blocks.MANGROVE_SIGN,
            () -> Blocks.MANGROVE_HANGING_SIGN,
            () -> Blocks.MANGROVE_PRESSURE_PLATE,
            () -> Blocks.MANGROVE_BUTTON
    ),
    OAK(
            WoodType.OAK,
            BlockSetType.OAK,
            () -> Blocks.OAK_LOG,
            () -> Blocks.STRIPPED_OAK_LOG,
            () -> Blocks.OAK_WOOD,
            () -> Blocks.STRIPPED_OAK_WOOD,
            () -> Blocks.OAK_PLANKS,
            () -> Blocks.OAK_STAIRS,
            () -> Blocks.OAK_SLAB,
            () -> Blocks.OAK_DOOR,
            () -> Blocks.OAK_TRAPDOOR,
            () -> Blocks.OAK_FENCE,
            () -> Blocks.OAK_FENCE_GATE,
            () -> Blocks.OAK_SIGN,
            () -> Blocks.OAK_HANGING_SIGN,
            () -> Blocks.OAK_PRESSURE_PLATE,
            () -> Blocks.OAK_BUTTON
    ),
    SPRUCE(
            WoodType.SPRUCE,
            BlockSetType.SPRUCE,
            () -> Blocks.SPRUCE_LOG,
            () -> Blocks.STRIPPED_SPRUCE_LOG,
            () -> Blocks.SPRUCE_WOOD,
            () -> Blocks.STRIPPED_SPRUCE_WOOD,
            () -> Blocks.SPRUCE_PLANKS,
            () -> Blocks.SPRUCE_STAIRS,
            () -> Blocks.SPRUCE_SLAB,
            () -> Blocks.SPRUCE_DOOR,
            () -> Blocks.SPRUCE_TRAPDOOR,
            () -> Blocks.SPRUCE_FENCE,
            () -> Blocks.SPRUCE_FENCE_GATE,
            () -> Blocks.SPRUCE_SIGN,
            () -> Blocks.SPRUCE_HANGING_SIGN,
            () -> Blocks.SPRUCE_PRESSURE_PLATE,
            () -> Blocks.SPRUCE_BUTTON
    ),
    CRIMSON(
            WoodType.CRIMSON,
            BlockSetType.CRIMSON,
            () -> Blocks.CRIMSON_STEM,
            () -> Blocks.STRIPPED_CRIMSON_STEM,
            () -> Blocks.CRIMSON_HYPHAE,
            () -> Blocks.STRIPPED_CRIMSON_HYPHAE,
            () -> Blocks.CRIMSON_PLANKS,
            () -> Blocks.CRIMSON_STAIRS,
            () -> Blocks.CRIMSON_SLAB,
            () -> Blocks.CRIMSON_DOOR,
            () -> Blocks.CRIMSON_TRAPDOOR,
            () -> Blocks.CRIMSON_FENCE,
            () -> Blocks.CRIMSON_FENCE_GATE,
            () -> Blocks.CRIMSON_SIGN,
            () -> Blocks.CRIMSON_HANGING_SIGN,
            () -> Blocks.CRIMSON_PRESSURE_PLATE,
            () -> Blocks.CRIMSON_BUTTON
    ),
    WARPED(
            WoodType.WARPED,
            BlockSetType.WARPED,
            () -> Blocks.WARPED_STEM,
            () -> Blocks.STRIPPED_WARPED_STEM,
            () -> Blocks.WARPED_HYPHAE,
            () -> Blocks.STRIPPED_WARPED_HYPHAE,
            () -> Blocks.WARPED_PLANKS,
            () -> Blocks.WARPED_STAIRS,
            () -> Blocks.WARPED_SLAB,
            () -> Blocks.WARPED_DOOR,
            () -> Blocks.WARPED_TRAPDOOR,
            () -> Blocks.WARPED_FENCE,
            () -> Blocks.WARPED_FENCE_GATE,
            () -> Blocks.WARPED_SIGN,
            () -> Blocks.WARPED_HANGING_SIGN,
            () -> Blocks.WARPED_PRESSURE_PLATE,
            () -> Blocks.WARPED_BUTTON
    ),
    BAMBOO(
            WoodType.BAMBOO,
            BlockSetType.BAMBOO,
            () -> Blocks.BAMBOO_BLOCK,
            () -> Blocks.STRIPPED_BAMBOO_BLOCK,
            null,
            null,
            () -> Blocks.BAMBOO_PLANKS,
            () -> Blocks.BAMBOO_STAIRS,
            () -> Blocks.BAMBOO_SLAB,
            () -> Blocks.BAMBOO_DOOR,
            () -> Blocks.BAMBOO_TRAPDOOR,
            () -> Blocks.BAMBOO_FENCE,
            () -> Blocks.BAMBOO_FENCE_GATE,
            () -> Blocks.BAMBOO_SIGN,
            () -> Blocks.BAMBOO_HANGING_SIGN,
            () -> Blocks.BAMBOO_PRESSURE_PLATE,
            () -> Blocks.BAMBOO_BUTTON
    ),
    ;

    @Nullable
    private final WoodType woodType;
    @Nullable
    private final BlockSetType blockSetType;

    @Nullable
    private final Supplier<Block> log;
    @Nullable
    private final Supplier<Block> strippedLog;
    @Nullable
    private final Supplier<Block> wood;
    @Nullable
    private final Supplier<Block> strippedWood;
    @Nullable
    private final Supplier<Block> planks;
    @Nullable
    private final Supplier<Block> plankStairs;
    @Nullable
    private final Supplier<Block> plankSlab;

    @Nullable
    private final Supplier<Block> door;
    @Nullable
    private final Supplier<Block> trapdoor;
    @Nullable
    private final Supplier<Block> fence;
    @Nullable
    private final Supplier<Block> fenceGate;
    @Nullable
    private final Supplier<Block> sign;
    @Nullable
    private final Supplier<Block> hangingSign;
    @Nullable
    private final Supplier<Block> pressurePlate;
    @Nullable
    private final Supplier<Block> button;

    CompatWoodMaterial(
            @Nullable WoodType woodType,
            @Nullable BlockSetType blockSetType,
            @Nullable Supplier<Block> log,
            @Nullable Supplier<Block> strippedLog,
            @Nullable Supplier<Block> wood,
            @Nullable Supplier<Block> strippedWood,
            @Nullable Supplier<Block> planks,
            @Nullable Supplier<Block> plankStairs,
            @Nullable Supplier<Block> plankSlab,
            @Nullable Supplier<Block> door,
            @Nullable Supplier<Block> trapdoor,
            @Nullable Supplier<Block> fence,
            @Nullable Supplier<Block> fenceGate,
            @Nullable Supplier<Block> sign,
            @Nullable Supplier<Block> hangingSign,
            @Nullable Supplier<Block> pressurePlate,
            @Nullable Supplier<Block> button
    ) {
        this.woodType = woodType;
        this.blockSetType = blockSetType;
        this.log = log;
        this.strippedLog = strippedLog;
        this.wood = wood;
        this.strippedWood = strippedWood;
        this.planks = planks;
        this.plankStairs = plankStairs;
        this.plankSlab = plankSlab;
        this.door = door;
        this.trapdoor = trapdoor;
        this.fence = fence;
        this.fenceGate = fenceGate;
        this.sign = sign;
        this.hangingSign = hangingSign;
        this.pressurePlate = pressurePlate;
        this.button = button;
    }

    public @Nullable WoodType woodType() {
        return woodType;
    }

    public @Nullable BlockSetType blockSetType() {
        return blockSetType;
    }

    public @Nullable Block log() {
        return log == null ? null : log.get();
    }

    public @Nullable Block strippedLog() {
        return strippedLog == null ? null : strippedLog.get();
    }

    public @Nullable Block wood() {
        return wood == null ? null : wood.get();
    }

    public @Nullable Block strippedWood() {
        return strippedWood == null ? null : strippedWood.get();
    }

    public @Nullable Block planks() {
        return planks == null ? null : planks.get();
    }

    public @Nullable Block plankStairs() {
        return plankStairs == null ? null : plankStairs.get();
    }

    public @Nullable Block plankSlab() {
        return plankSlab == null ? null : plankSlab.get();
    }

    public @Nullable Block door() {
        return door == null ? null : door.get();
    }

    public @Nullable Block trapdoor() {
        return trapdoor == null ? null : trapdoor.get();
    }

    public @Nullable Block fence() {
        return fence == null ? null : fence.get();
    }

    public @Nullable Block fenceGate() {
        return fenceGate == null ? null : fenceGate.get();
    }

    public @Nullable Block sign() {
        return sign == null ? null : sign.get();
    }

    public @Nullable Block hangingSign() {
        return hangingSign == null ? null : hangingSign.get();
    }

    public @Nullable Block pressurePlate() {
        return pressurePlate == null ? null : pressurePlate.get();
    }

    public @Nullable Block button() {
        return button == null ? null : button.get();
    }
}