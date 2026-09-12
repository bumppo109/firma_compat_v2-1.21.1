package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.FirmaCompatConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.dries007.tfc.world.biome.BiomeNoise;
import net.dries007.tfc.world.noise.Noise2D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BiomeNoise.class, remap = false)
public class BiomeNoiseMixin {

    @ModifyReturnValue(
            method = "ocean",
            at = @At("RETURN")
    )
    private static Noise2D scaleOceanDepth(Noise2D original)
    {
        double scale = FirmaCompatConfig.COMMON.oceanDepthScale.get();

        return (x, z) -> {
            double h = original.noise(x, z);
            return 63.0 + (h - 63.0) * scale;
        };
    }
}