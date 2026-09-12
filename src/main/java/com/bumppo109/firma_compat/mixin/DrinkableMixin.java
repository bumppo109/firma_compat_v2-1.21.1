package com.bumppo109.firma_compat.mixin;

import net.dries007.tfc.util.data.Drinkable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    private static void requireCrouching(
            Level level,
            Player player,
            boolean doDrink,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (!player.isCrouching()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}