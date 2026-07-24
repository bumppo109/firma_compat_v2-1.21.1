package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public final class BlockAssets {

    private static final Map<ResourceLocation, BlockAsset> ASSETS = new HashMap<>();
    private static boolean initialized = false;

    private BlockAssets() {}

    /**
     * Registers all vanilla/special-case assets.
     * Safe to call multiple times.
     */
    public static void bootstrap() {
        if (initialized) {
            return;
        }

        initialized = true;

        registerVanilla();
    }

    /**
     * register anything not a cube_all block
     */
    private static void registerVanilla() {

        register(Blocks.BASALT, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/basalt_side"),
                ResourceLocation.withDefaultNamespace("block/basalt_top")));
        register(Blocks.POLISHED_BASALT, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/polished_basalt_side"),
                ResourceLocation.withDefaultNamespace("block/polished_basalt_top")));
        register(Blocks.DEEPSLATE, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/deepslate"),
                ResourceLocation.withDefaultNamespace("block/deepslate_top")));
        register(Blocks.CHISELED_TUFF, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/chiseled_tuff"),
                ResourceLocation.withDefaultNamespace("block/chiseled_tuff_top")));
        register(Blocks.CHISELED_TUFF_BRICKS, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/chiseled_tuff_bricks"),
                ResourceLocation.withDefaultNamespace("block/chiseled_tuff_bricks_top")));
        register(Blocks.DRIPSTONE_BLOCK, BlockAsset.cubeAll(
                ResourceLocation.withDefaultNamespace("block/dripstone_block")));
        register(Blocks.QUARTZ_PILLAR, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/quartz_pillar"),
                ResourceLocation.withDefaultNamespace("block/quartz_pillar_top")));
        register(Blocks.CHISELED_QUARTZ_BLOCK, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/chiseled_quartz_block"),
                ResourceLocation.withDefaultNamespace("block/chiseled_quartz_block_top")));
        register(Blocks.QUARTZ_BLOCK, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/quartz_block_side"),
                ResourceLocation.withDefaultNamespace("block/quartz_block_top")));
        register(Blocks.SMOOTH_QUARTZ, BlockAsset.cubeAll(
                ResourceLocation.withDefaultNamespace("block/quartz_block_bottom")));
        register(Blocks.PURPUR_PILLAR, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/purpur_pillar"),
                ResourceLocation.withDefaultNamespace("block/purpur_pillar_top")));
        register(Blocks.BONE_BLOCK, BlockAsset.column(
                ResourceLocation.withDefaultNamespace("block/bone_block_side"),
                ResourceLocation.withDefaultNamespace("block/bone_block_top")));
        register(Blocks.SMOOTH_SANDSTONE, BlockAsset.cubeAll(
                ResourceLocation.withDefaultNamespace("block/sandstone_top")));
        register(Blocks.SMOOTH_RED_SANDSTONE, BlockAsset.cubeAll(
                ResourceLocation.withDefaultNamespace("block/red_sandstone_top")));
        register(Blocks.SNOW_BLOCK, BlockAsset.cubeAll(
                ResourceLocation.withDefaultNamespace("block/snow")));
        register(Blocks.DIRT_PATH, BlockAsset.cubeBottomTop(
                ResourceLocation.withDefaultNamespace("block/dirt_path_side"),
                ResourceLocation.withDefaultNamespace("block/dirt"),
                ResourceLocation.withDefaultNamespace("block/dirt_path_top")));
        register(Blocks.GRASS_BLOCK, BlockAsset.cubeBottomTop(
                ResourceLocation.withDefaultNamespace("block/grass_block_side"),
                ResourceLocation.withDefaultNamespace("block/dirt"),
                ResourceLocation.withDefaultNamespace("block/grass_block_top")));
        register(Blocks.PODZOL, BlockAsset.cubeBottomTop(
                ResourceLocation.withDefaultNamespace("block/podzol_side"),
                ResourceLocation.withDefaultNamespace("block/dirt"),
                ResourceLocation.withDefaultNamespace("block/podzol_top")));
        register(Blocks.MYCELIUM, BlockAsset.cubeBottomTop(
                ResourceLocation.withDefaultNamespace("block/mycelium_side"),
                ResourceLocation.withDefaultNamespace("block/dirt"),
                ResourceLocation.withDefaultNamespace("block/mycelium_top")));
        register(Blocks.WARPED_NYLIUM, BlockAsset.cubeBottomTop(
                ResourceLocation.withDefaultNamespace("block/warped_nylium_side"),
                ResourceLocation.withDefaultNamespace("block/netherrack"),
                ResourceLocation.withDefaultNamespace("block/warped_nylium_top")));
        register(Blocks.CRIMSON_NYLIUM, BlockAsset.cubeBottomTop(
                ResourceLocation.withDefaultNamespace("block/crimson_nylium_side"),
                ResourceLocation.withDefaultNamespace("block/netherrack"),
                ResourceLocation.withDefaultNamespace("block/crimson_nylium_top")));
    }

    public static void register(Block block, BlockAsset asset) {
        ASSETS.put(
                BuiltInRegistries.BLOCK.getKey(block),
                asset
        );
    }

    public static void registerNovumCubeAll(Block block) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ASSETS.put(
                blockRes,
                BlockAsset.cubeAll(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + blockRes.getPath()))
        );
    }

    public static BlockAsset getColumn(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

        return ASSETS.getOrDefault(
                BuiltInRegistries.BLOCK.getKey(block),
                BlockAsset.column(
                        ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()),
                        ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath() + "_top")
                )
        );
    }

    public static BlockAsset get(Block block) {
        return ASSETS.getOrDefault(
                BuiltInRegistries.BLOCK.getKey(block),
                BlockAsset.cubeAll(block)
        );
    }
}