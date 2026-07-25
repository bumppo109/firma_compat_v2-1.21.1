package com.bumppo109.firma_compat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.dries007.tfc.client.overworld.LevelRendererExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRendererExtension.class)
public class LevelRendererExtensionMixin {

    // Shrink the background square to almost nothing
    @ModifyExpressionValue(
            method = "renderSky",
            at = @At(value = "CONSTANT", args = "floatValue=-7.5f")
    )
    private float firmaCompat$shrinkBackground(float original) {
        return 0.05f;        // Very small — adjust between 0.01f and 0.2f
    }

    @ModifyExpressionValue(
            method = "renderSky",
            at = @At(value = "CONSTANT", args = "floatValue=7.5f")
    )
    private float firmaCompat$shrinkBackground2(float original) {
        return 0.05f;
    }

    // Optional: Also reduce alpha of the background square to match the texture better
    @ModifyExpressionValue(
            method = "renderSky",
            at = @At(value = "CONSTANT", args = "floatValue=1.0f", ordinal = 2)
    )
    private float firmaCompat$reduceBackgroundAlpha(float original) {
        return 0.0f;   // Makes background fully transparent (recommended)
    }
}