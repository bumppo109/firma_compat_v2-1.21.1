package com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.ModAttachments;

/**
 * Utility class to integrate Legendary Survival Overhaul (LSO) body temperature
 * into TFC's thirst decay system, replacing TFC's climate contribution when LSO is present.
 */
public class LSOThirstModifier {

    private static final float NEUTRAL_BODY_TEMP = 15.0F;
    private static final float THIRST_PENALTY_START = 25.0F;
    private static final float THIRST_PENALTY_MAX = 35.0F;
    private static final float MAX_PENALTY = 0.4F;

    /**
     * Retrieves the current LSO body temperature from the player's attachment.
     * Returns a neutral value if LSO is not present, on client-side, or if lookup fails.
     *
     * @param player The player to query
     * @return Current body temperature (typically ~15 neutral, higher = hotter)
     */
    public static float getCurrentBodyTemperature(Player player) {
        if (player == null || player.level().isClientSide) {
            return NEUTRAL_BODY_TEMP;
        }

        try {
            ITemperatureAttachment attachment = player.getData(ModAttachments.TEMPERATURE.get());
            if (attachment != null) {
                return attachment.getTemperatureLevel();
            }
        } catch (Exception ignored) {
            // Attachment missing, wrong type, or other lookup failure
        }

        return NEUTRAL_BODY_TEMP;
    }

    /**
     * Returns the thirst decay contribution based on LSO's current body temperature.
     * This value replaces TFC's climate contribution when LSO is loaded.
     *
     * @param player The player
     * @return Thirst penalty factor (0.0F = no extra decay, up to 0.4F = significant extra decay)
     */
    public static float getLsoTemperatureThirstContribution(Player player) {
        float bodyTemp = getCurrentBodyTemperature(player);

        // Hotter body temperature increases thirst loss
        // Mapping: linear ramp from 25°C to 35°C, capped at 0.4
        // Tune these constants (THIRST_PENALTY_START, THIRST_PENALTY_MAX, MAX_PENALTY) as needed
        return Mth.clampedMap(bodyTemp, THIRST_PENALTY_START, THIRST_PENALTY_MAX, 0.0F, MAX_PENALTY);
    }

    /**
     * Checks whether LSO is loaded and we should override TFC's climate-based thirst contribution.
     */
    public static boolean isLsoLoaded() {
        return ModList.get().isLoaded("legendarysurvivaloverhaul");
    }
}