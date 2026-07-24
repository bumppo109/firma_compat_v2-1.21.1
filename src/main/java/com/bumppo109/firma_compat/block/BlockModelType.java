package com.bumppo109.firma_compat.block;

import java.util.Set;

public enum BlockModelType {

    CUBE_ALL(
            Set.of(BlockTextureSlot.SIDE)
    ),

    CUBE_BOTTOM_TOP(
            Set.of(
                    BlockTextureSlot.SIDE,
                    BlockTextureSlot.BOTTOM,
                    BlockTextureSlot.TOP
            )
    ),

    COLUMN(
            Set.of(
                    BlockTextureSlot.SIDE,
                    BlockTextureSlot.END
            )
    ),

    ROTATED_PILLAR(
            Set.of(
                    BlockTextureSlot.SIDE,
                    BlockTextureSlot.END
            )
    );

    private final Set<BlockTextureSlot> requiredTextures;

    BlockModelType(Set<BlockTextureSlot> requiredTextures) {
        this.requiredTextures = requiredTextures;
    }

    public Set<BlockTextureSlot> requiredTextures() {
        return requiredTextures;
    }
}