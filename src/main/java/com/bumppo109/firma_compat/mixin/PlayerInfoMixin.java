package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.addon.ModCompat;
import com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul.LSOThirstModifier;
import net.dries007.tfc.common.player.PlayerInfo;
import net.dries007.tfc.config.TFCConfig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Replaces TFC's climate temperature contribution in thirst decay with LSO body temperature
 * when LSO is loaded, preventing double-counting of environmental effects.
 */
@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin {

    /**
     * Redirects the call to getThirstContributionFromTemperature() **inside tick()**.
     * - LSO loaded → return our LSO-based contribution instead
     * - LSO not loaded → call original method (normal TFC behavior)
     */
    @Redirect(
            method = "tick(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/common/player/PlayerInfo;getThirstContributionFromTemperature()F"
            )
    )
    private float redirectTemperatureContribution(PlayerInfo instance, Player player) {
        if (ModCompat.loaded("legendarysurvivaloverhaul")) {
            if (TFCConfig.SERVER.enableThirstOverheating.get()) {
                return LSOThirstModifier.getLsoTemperatureThirstContribution(player);
            } else {
                return 0.0F;
            }
        }

        // Fallback: original TFC logic
        return instance.getThirstContributionFromTemperature();
    }
}