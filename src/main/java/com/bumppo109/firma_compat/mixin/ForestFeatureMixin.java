package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.world.RootedSoilHelper;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.feature.tree.ForestConfig;
import net.dries007.tfc.world.feature.tree.ForestFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForestFeature.class)
public abstract class ForestFeatureMixin
{
    @Inject(
            method = "placeTree",
            at = @At("RETURN")
    )
    private void firmaCompat$placeRootedSoil(
            WorldGenLevel level,
            ChunkGenerator generator,
            RandomSource random,
            BlockPos chunkBlockPos,
            ForestConfig config,
            ChunkData data,
            BlockPos.MutableBlockPos mutablePos,
            ForestType typeConfig,
            CallbackInfoReturnable<Boolean> cir
    )
    {
        if (cir.getReturnValue())
        {
            RootedSoilHelper.replaceSoilUnderTree(
                    level,
                    mutablePos.immutable()
            );
        }
    }
}