package com.bumppo109.firma_compat.world.processor.sandstone;

import net.dries007.tfc.common.blocks.soil.SandBlockType;

import javax.annotation.Nullable;

public enum SandstoneBlockType {
    RAW(net.dries007.tfc.common.blocks.SandstoneBlockType.RAW),
    SMOOTH(net.dries007.tfc.common.blocks.SandstoneBlockType.SMOOTH),
    CUT(net.dries007.tfc.common.blocks.SandstoneBlockType.CUT),
    CHISELED(null);

    private final @Nullable net.dries007.tfc.common.blocks.SandstoneBlockType sandstoneBlockType;

    SandstoneBlockType(net.dries007.tfc.common.blocks.SandstoneBlockType sandstoneBlockType) {
        this.sandstoneBlockType = sandstoneBlockType;
    }

    public net.dries007.tfc.common.blocks.SandstoneBlockType tfcSandStoneType() {
        return this.sandstoneBlockType;
    }
}
