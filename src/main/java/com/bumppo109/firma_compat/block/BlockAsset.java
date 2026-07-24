package com.bumppo109.firma_compat.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public record BlockAsset(
        BlockModelType model,
        Map<BlockTextureSlot, ResourceLocation> textures
) {

    public static BlockAsset cubeAll(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

        return new BlockAsset(
                BlockModelType.CUBE_ALL,
                Map.of(
                        BlockTextureSlot.SIDE,
                        id.withPrefix("block/")
                )
        );
    }

    public static BlockAsset cubeAll(ResourceLocation texture) {
        return new BlockAsset(
                BlockModelType.CUBE_ALL,
                Map.of(
                        BlockTextureSlot.SIDE,
                        texture
                )
        );
    }

    public static BlockAsset cubeBottomTop(ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return new BlockAsset(
                BlockModelType.CUBE_BOTTOM_TOP,
                Map.of(
                        BlockTextureSlot.SIDE, side,
                        BlockTextureSlot.BOTTOM, bottom,
                        BlockTextureSlot.TOP, top
                )
        );
    }

    public static BlockAsset column(ResourceLocation side, ResourceLocation end) {
        return new BlockAsset(
                BlockModelType.COLUMN,
                Map.of(
                        BlockTextureSlot.SIDE, side,
                        BlockTextureSlot.END, end
                )
        );
    }
}