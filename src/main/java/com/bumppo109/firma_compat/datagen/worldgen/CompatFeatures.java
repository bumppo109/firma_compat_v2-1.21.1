package com.bumppo109.firma_compat.datagen.worldgen;

import net.dries007.tfc.common.blocks.GroundcoverBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.minecraft.world.level.block.Block;

public enum CompatFeatures {

    CLAM("clam", TFCBlocks.GROUNDCOVER.get(GroundcoverBlockType.CLAM).get(),5,15,1)
    ;

    public final String name;
    public final Block groundcoverBlock;
    public final int patchTries;
    public final int xzSpread;
    public final int ySpread;

    CompatFeatures(String name, Block groundcoverBlock, Integer patchTries, Integer xzSpread, Integer ySpread){
        this.name = name;
        this.groundcoverBlock = groundcoverBlock;
        this.patchTries = patchTries;
        this.xzSpread = xzSpread;
        this.ySpread = ySpread;
    }
}
