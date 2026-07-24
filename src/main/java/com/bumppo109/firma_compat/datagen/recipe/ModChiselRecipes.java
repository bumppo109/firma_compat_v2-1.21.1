package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatRockMaterial;
import com.bumppo109.firma_compat.block.CompatRockSet;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public interface ModChiselRecipes extends ModRecipes
{
    default void chiselRecipes()
    {
        for (CompatRock rock : CompatRock.VALUES) {
            CompatRockMaterial material = rock.rockMaterial();

            if (material == null) {
                FirmaCompat.LOGGER.debug("Missing CompatRockMaterial for {}", rock);
                continue;
            }

            CompatRockSet rawSet = material.raw();
            CompatRockSet polishedSet = material.polished();
            CompatRockSet chiseledSet = material.chiseled();
            CompatRockSet brickSet = material.brick();
            CompatRockSet chiseledBrickSet = material.chiseledBrick();

            if (rawSet != null) {
                if (polishedSet != null) {
                    chiselSmooth(List.of(rawSet.base()), polishedSet.base());

                    if (chiseledSet != null) {
                        chiselSmooth(List.of(polishedSet.base()), chiseledSet.base());
                    }
                } else {
                    if (chiseledSet != null) {
                        chiselSmooth(List.of(rawSet.base()), chiseledSet.base());
                    }
                }
            }

            if (brickSet != null && chiseledBrickSet != null) {
                chiselSmooth(List.of(brickSet.base()), chiseledBrickSet.base());
            }

            for (CompatRockSet set : material) {
                if (set.slab() != null) {
                    chiselSlab(List.of(set.base()), set.slab());
                }
                if (set.stairs() != null) {
                    chiselStair(List.of( set.base()), set.stairs());
                }
            }
        }

        ModBlocks.ROCK_DECORATIONS.forEach((rock, decoMap) -> {
            Supplier<Block> base = rock.rockMaterial().raw().base();

            decoMap.forEach((blockType, modDecorationBlockHolder) -> {
                chiselSlab(List.of(base), decoMap.get(blockType).slab());
                chiselStair(List.of(base), decoMap.get(blockType).stair());
            });
        });

        for(Rock rock : Rock.values()) {
            var rockMap = ModBlocks.TFC_ROCK_BLOCKS.get(rock);
            var decorationMap = TFCBlocks.ROCK_DECORATIONS.get(rock);

            chiselStair(List.of(rockMap.get(Rock.BlockType.COBBLE)), decorationMap.get(Rock.BlockType.COBBLE).stair());
            chiselSlab(List.of(rockMap.get(Rock.BlockType.COBBLE)), decorationMap.get(Rock.BlockType.COBBLE).slab());
            chiselStair(List.of(rockMap.get(Rock.BlockType.MOSSY_COBBLE)), decorationMap.get(Rock.BlockType.MOSSY_COBBLE).stair());
            chiselSlab(List.of(rockMap.get(Rock.BlockType.MOSSY_COBBLE)), decorationMap.get(Rock.BlockType.MOSSY_COBBLE).slab());
        }
    }

    private void chiselSmooth(List<? extends Supplier<? extends Block>> input, Supplier<? extends Block> output)
    {
        chisel(input, output, ChiselMode.SMOOTH, ItemStackProvider.empty());
    }
    private void chiselStair(List<? extends Supplier<? extends Block>> input, Supplier<? extends Block> output)
    {
        chisel(input, output, ChiselMode.STAIR, ItemStackProvider.empty());
    }
    private void chiselSlab(List<? extends Supplier<? extends Block>> input, Supplier<? extends Block> output)
    {
        chisel(input, output, ChiselMode.SLAB, ItemStackProvider.of(output.get().asItem()));
    }

    private void chisel(List<? extends Supplier<? extends Block>> input, Supplier<? extends Block> output, Holder<ChiselMode> mode, ItemStackProvider outputItem)
    {
        add(new ChiselRecipe(
                BlockIngredient.of(input.stream().map(Supplier::get)),
                output.get().defaultBlockState(),
                mode.value(),
                outputItem
        ));
    }
}