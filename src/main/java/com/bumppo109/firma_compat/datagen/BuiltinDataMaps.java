package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.data.ModDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class BuiltinDataMaps extends DataMapProvider
{
    public BuiltinDataMaps(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(output, lookup);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        addHardened(Blocks.STONE, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.DIRT, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.DEEPSLATE, ModBlocks.ROCK_BLOCKS.get(CompatRock.DEEPSLATE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.ANDESITE, ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.DIORITE, ModBlocks.ROCK_BLOCKS.get(CompatRock.DIORITE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.GRANITE, ModBlocks.ROCK_BLOCKS.get(CompatRock.GRANITE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.TUFF, ModBlocks.ROCK_BLOCKS.get(CompatRock.TUFF).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.CALCITE, ModBlocks.ROCK_BLOCKS.get(CompatRock.CALCITE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.DRIPSTONE_BLOCK, ModBlocks.ROCK_BLOCKS.get(CompatRock.DRIPSTONE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.NETHERRACK, ModBlocks.ROCK_BLOCKS.get(CompatRock.NETHERRACK).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.BLACKSTONE, ModBlocks.ROCK_BLOCKS.get(CompatRock.BLACKSTONE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.BASALT, ModBlocks.ROCK_BLOCKS.get(CompatRock.BASALT).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.END_STONE, ModBlocks.ROCK_BLOCKS.get(CompatRock.END_STONE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.SAND, Blocks.SANDSTONE);
        addHardened(Blocks.RED_SAND, Blocks.RED_SANDSTONE);
        addHardened(Blocks.GRAVEL, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get());
        addHardened(Blocks.GRASS_BLOCK, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get());

        //copper, gold, iron
        addOreReplace(Blocks.COPPER_ORE, Blocks.STONE);
        addOreReplace(Blocks.GOLD_ORE, Blocks.STONE);
        addOreReplace(Blocks.IRON_ORE, Blocks.STONE);
        addOreReplace(Blocks.COAL_ORE, Blocks.STONE);
        addOreReplace(Blocks.REDSTONE_ORE, Blocks.STONE);
        addOreReplace(Blocks.EMERALD_ORE, Blocks.STONE);
        addOreReplace(Blocks.DIAMOND_ORE, Blocks.STONE);
        addOreReplace(Blocks.LAPIS_ORE, Blocks.STONE);
        addOreReplace(Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE);
        addOreReplace(Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE);

    }

    private void addHardened(Block raw, Block hardened) {
        // Option A – cleanest: use ResourceKey / Holder + ResourceLocation value
        builder(ModDataMaps.HARDENED_ROCK_REPLACEMENT).add(
                raw.builtInRegistryHolder(),                           // Holder<Block>
                hardened.builtInRegistryHolder().key().location(),      // ResourceLocation of the target block
                false                                                   // replace = false
        );
    }

    private void addOreReplace(Block ore, Block replace) {
        // Option A – cleanest: use ResourceKey / Holder + ResourceLocation value
        builder(ModDataMaps.VANILLA_ORE_REPLACEMENT).add(
                ore.builtInRegistryHolder(),                           // Holder<Block>
                replace.builtInRegistryHolder().key().location(),      // ResourceLocation of the target block
                false                                                   // replace = false
        );
    }
}
