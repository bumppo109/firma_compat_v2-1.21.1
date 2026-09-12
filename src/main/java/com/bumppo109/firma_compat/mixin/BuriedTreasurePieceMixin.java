package com.bumppo109.firma_compat.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasurePieces;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuriedTreasurePieces.BuriedTreasurePiece.class)
public class BuriedTreasurePieceMixin {

    /**
     * Normalize the block BELOW before vanilla compares it.
     * This is the ONLY stable injection point in this method.
     */
    @Redirect(
            method = "postProcess",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/WorldGenLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 1
            )
    )
    private BlockState redirectBelowState(
            net.minecraft.world.level.WorldGenLevel level,
            net.minecraft.core.BlockPos pos
    ) {

        BlockState state = level.getBlockState(pos);

        // Normalize sandstone-like blocks via tags
        if (state.is(Tags.Blocks.SANDSTONE_BLOCKS)) {
            return Blocks.SANDSTONE.defaultBlockState();
        }
        // Normalize stone-like blocks via tags
        if (state.is(Tags.Blocks.STONES)) {
            return Blocks.STONE.defaultBlockState();
        }

        return state;
    }
}