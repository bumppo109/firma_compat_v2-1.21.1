package com.bumppo109.firma_compat.mixin;

import net.dries007.tfc.common.effect.TFCEffects;
import net.dries007.tfc.util.data.Drinkable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Drinkable.class)
public abstract class DrinkableMixin {

    @Inject(
            method = "attemptDrink(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Z)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void requireCrouchAndHandleSaltWaterThirst(
            Level level,
            Player player,
            boolean doDrink,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        // First: force crouch requirement everywhere
        if (!player.isCrouching()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
            return;
        }

        // We only care about thirst penalty on server side & when actually drinking
        if (level.isClientSide || !doDrink) {
            return;
        }

        // Get the hit position from the same ray-trace TFC uses
        BlockHitResult hit = net.minecraft.world.item.Item.getPlayerPOVHitResult(
                level,
                player,
                net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY
        );

        if (hit.getType() != net.minecraft.world.phys.HitResult.Type.BLOCK) {
            return;
        }

        BlockPos pos = hit.getBlockPos();

        // Get current biome (works on both client & server, but we already checked server)
        Holder<Biome> biomeHolder = level.getBiome(pos);

        // Check for any ocean biome
        if (isOceanBiome(biomeHolder)) {
            player.addEffect(new MobEffectInstance(
                    (Holder<MobEffect>) TFCEffects.THIRST.holder(),
                    600,
                    1,
                    false,
                    true,
                    true
            ));
        }
    }

    private static boolean isOceanBiome(Holder<Biome> biomeHolder) {
        return biomeHolder.is(Tags.Biomes.IS_OCEAN) || biomeHolder.is(Tags.Biomes.IS_BEACH);
    }
}