package com.bumppo109.firma_compat.block;

import java.util.function.Supplier;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

public interface ModRegistryWood extends StringRepresentable {
    MapColor woodColor();

    MapColor barkColor();

    boolean isFlammable();

    Supplier<Block> getBlock(CompatWood.BlockType var1);

}
