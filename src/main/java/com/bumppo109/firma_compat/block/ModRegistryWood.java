package com.bumppo109.firma_compat.block;

import java.util.function.Supplier;

import com.bumppo109.firma_compat.materials.WoodMaterial;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public interface ModRegistryWood extends StringRepresentable {
    MapColor woodColor();

    MapColor barkColor();

    boolean isFlammable();

    Supplier<Block> getBlock(CompatWood.BlockType type);

    WoodMaterial woodMaterial();

}