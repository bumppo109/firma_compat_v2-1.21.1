package com.bumppo109.firma_compat.world;

import com.bumppo109.firma_compat.data.ModDataMaps;
import com.mojang.serialization.Codec;
import net.dries007.tfc.common.entities.misc.TFCFallingBlockEntity;
import net.dries007.tfc.common.recipes.LandslideRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CompatErosion extends Feature<NoneFeatureConfiguration> {
    public CompatErosion(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        ChunkAccess chunk = level.getChunk(pos);
        ChunkPos chunkPos = new ChunkPos(pos);
        int chunkX = chunkPos.getMinBlockX();
        int chunkZ = chunkPos.getMinBlockZ();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int minY = context.chunkGenerator().getMinY();

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                int baseHeight = level.getHeight(Types.WORLD_SURFACE_WG, chunkX + x, chunkZ + z);
                boolean prevBlockCanLandslide = false;
                int lastSafeY = baseHeight;
                Block prevBlockHardened = null;
                mutablePos.set(chunkX + x, baseHeight, chunkZ + z);

                for(int y = baseHeight; y >= minY; --y) {
                    mutablePos.setY(y);
                    BlockState stateAt = chunk.getBlockState(mutablePos);
                    BlockState stateAbove = chunk.getBlockState(mutablePos.setY(y + 1));
                    LandslideRecipe recipe = stateAt.isAir() ? null : LandslideRecipe.getRecipe(stateAt);
                    boolean stateAtIsFragile = stateAt.isAir() || TFCFallingBlockEntity.canFallThrough(level, mutablePos, stateAt);

                    //Ore replacement
                    Block oreRep = getOreReplacement(stateAt.getBlock());
                    if (oreRep != null) {
                        this.setBlock(level, chunk, mutablePos, oreRep.defaultBlockState());
                    }

                    if (prevBlockCanLandslide) {
                        if (recipe == null) {
                            if (stateAtIsFragile) {
                                if (lastSafeY > y + 2) {
                                    mutablePos.setY(y + 1);
                                    if(getHardenedReplacement(stateAbove.getBlock()) != null){
                                        this.setBlock(level, chunk, mutablePos, getHardenedReplacement(stateAbove.getBlock()).defaultBlockState());
                                    }
                                } else {
                                    //TODO - add aquifier check
                                    //FirmaCompat.LOGGER.error("aquifer placement at - {}", mutablePos.setY(y + 2));

                                    mutablePos.setY(y + 1);
                                    if(getHardenedReplacement(stateAbove.getBlock()) != null) {
                                        this.setBlock(level, chunk, mutablePos, getHardenedReplacement(stateAbove.getBlock()).defaultBlockState());
                                    }

                                    /*
                                    mutablePos.setY(y + 2);
                                    boolean blockAboveIsAir = chunk.getBlockState(mutablePos).isAir();
                                    mutablePos.setY(y + 1);
                                    BlockState airOrLiquidState = aquifer.computeSubstance(point, (double)-1.0F);
                                    if (blockAboveIsAir && airOrLiquidState != null) {
                                        this.setBlock(level, chunk, mutablePos, getHardenedReplacement(stateAbove.getBlock()).defaultBlockState());
                                    } else {
                                        mutablePos.setY(y + 1);
                                        this.setBlock(level, chunk, mutablePos, getHardenedReplacement(stateAbove.getBlock()).defaultBlockState());
                                    }
                                     */
                                }
                            }

                            prevBlockCanLandslide = false;
                            lastSafeY = y;
                        }
                    } else if (recipe == null) {
                        lastSafeY = y;
                    } else {
                        prevBlockCanLandslide = true;
                    }

                    if (stateAtIsFragile) {
                        if (prevBlockHardened != null) {
                            mutablePos.setY(y + 1);
                            this.setBlock(level, chunk, mutablePos, prevBlockHardened.defaultBlockState());
                        }

                        prevBlockHardened = null;
                    } else {
                        prevBlockHardened = getHardenedReplacement(stateAt.getBlock());
                    }
                }
            }
        }

        return true;
    }

    private Block getHardenedReplacement(Block original) {
        if (original == null) {
            //System.out.println("[DEBUG] Input block is null");
            return null;
        }

        //ResourceLocation id = BuiltInRegistries.BLOCK.getKey(original);
        //System.out.println("[DEBUG] Input block ID: " + id);  // should print minecraft:deepslate

        var keyOpt = BuiltInRegistries.BLOCK.getResourceKey(original);
        if (keyOpt.isEmpty()) {
            //System.out.println("[DEBUG] No ResourceKey for block: " + id);
            return null;
        }
        ResourceKey<Block> key = keyOpt.get();

        Holder<Block> holder = BuiltInRegistries.BLOCK.getHolder(key).orElse(null);
        if (holder == null) {
            //System.out.println("[DEBUG] No Holder found for key: " + key);
            return null;
        }

        ResourceLocation repId = holder.getData(ModDataMaps.HARDENED_ROCK_REPLACEMENT);

        Block rep = BuiltInRegistries.BLOCK.get(repId);

        return (rep != null && rep != Blocks.AIR) ? rep : null;
    }

    private Block getOreReplacement(Block original) {
        if (original == null) {
            //System.out.println("[DEBUG] Input block is null");
            return null;
        }

        //ResourceLocation id = BuiltInRegistries.BLOCK.getKey(original);
        //System.out.println("[DEBUG] Input block ID: " + id);  // should print minecraft:deepslate

        var keyOpt = BuiltInRegistries.BLOCK.getResourceKey(original);
        if (keyOpt.isEmpty()) {
            //System.out.println("[DEBUG] No ResourceKey for block: " + id);
            return null;
        }
        ResourceKey<Block> key = keyOpt.get();

        Holder<Block> holder = BuiltInRegistries.BLOCK.getHolder(key).orElse(null);
        if (holder == null) {
            //System.out.println("[DEBUG] No Holder found for key: " + key);
            return null;
        }

        ResourceLocation repId = holder.getData(ModDataMaps.VANILLA_ORE_REPLACEMENT);

        Block rep = BuiltInRegistries.BLOCK.get(repId);

        return (rep != null && rep != Blocks.AIR) ? rep : null;
    }

    private void setBlock(WorldGenLevel level, ChunkAccess chunk, BlockPos pos, BlockState state) {
        BlockState prevState = chunk.setBlockState(pos, state, false);
        if (prevState != null && prevState.hasBlockEntity()) {
            chunk.removeBlockEntity(pos);
        }

        if (state.hasPostProcess(level, pos)) {
            chunk.markPosForPostprocessing(pos);
        }

    }
}

