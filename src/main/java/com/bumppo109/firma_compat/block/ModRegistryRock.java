package com.bumppo109.firma_compat.block;

import java.util.function.Supplier;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.MapColor;

public interface ModRegistryRock extends StringRepresentable {
    Block base();

    Supplier<? extends Block> getBlock(CompatRock.BlockType var1);

    Supplier<? extends Block> getAnvil();

    Supplier<? extends SlabBlock> getSlab(CompatRock.BlockType var1);

    Supplier<? extends StairBlock> getStair(CompatRock.BlockType var1);

    Supplier<? extends WallBlock> getWall(CompatRock.BlockType var1);
}
