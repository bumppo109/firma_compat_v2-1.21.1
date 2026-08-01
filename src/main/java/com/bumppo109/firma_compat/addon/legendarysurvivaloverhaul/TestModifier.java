package com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul;

import com.bumppo109.firma_compat.addon.ModCompat;
import com.eerussianguy.firmalife.common.blockentities.ClimateStationBlockEntity;
import com.eerussianguy.firmalife.common.blocks.greenhouse.ClimateStationBlock;
import com.eerussianguy.firmalife.common.misc.FLPOIs;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Optional;
import java.util.Set;

public class TestModifier extends ModifierBase {

    private static final int SEARCH_DISTANCE = 64;
    private static final float NORMAL_TEMPERATURE = 20.0F;

    @Override
    public float getWorldInfluence(Player player, Level level, BlockPos pos) {

        if (!ModCompat.loaded("firmalife")) {
            return 0.0F;
        }

        if (level.isClientSide()) {
            return 0.0F;
        }

        if (!isInsideValidGreenhouse(level, pos)) {
            return 0.0F;
        }

        /*
         * Return a correction, not an absolute temperature.
         *
         * If the outside temperature is 9:
         * 20 - 9 = +11
         *
         * LSO adds +11 and the player reaches normal temperature.
         */
        float currentTemperature = getEnvironmentalTemperature(level, pos);

        return NORMAL_TEMPERATURE - currentTemperature;
    }

    private float getEnvironmentalTemperature(Level level, BlockPos pos) {
        // Replace this with whatever environmental temperature source you want.
        // This example assumes TFC is the source of the cold.
        if (LegendarySurvivalOverhaul.terraFirmaCraftLoaded
                && Config.Baked.tfcTemperatureMultiplier != 0.0F) {

            return net.dries007.tfc.util.climate.Climate.getInstantTemperature(level, pos)
                    * (float) Config.Baked.tfcTemperatureMultiplier;
        }

        return NORMAL_TEMPERATURE;
    }

    private boolean isInsideValidGreenhouse(Level level, BlockPos playerPos) {

        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }

        PoiManager poiManager = serverLevel.getPoiManager();

        Optional<BlockPos> stationPosition = poiManager.findClosest(
                holder -> holder.is(FLPOIs.CLIMATE_STATIONS.getKey()),
                playerPos,
                SEARCH_DISTANCE,
                PoiManager.Occupancy.ANY
        );

        if (stationPosition.isEmpty()) {
            return false;
        }

        BlockPos stationPos = stationPosition.get();

        BlockState state = level.getBlockState(stationPos);

        if (!state.hasProperty(ClimateStationBlock.STASIS)
                || !state.getValue(ClimateStationBlock.STASIS)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(stationPos);

        if (!(blockEntity instanceof ClimateStationBlockEntity station)) {
            return false;
        }

        if (!(station instanceof ClimateStationAccess accessor)) {
            return false;
        }

        Set<BlockPos> positions = accessor.firmaCompat$getPositions();

        return !positions.isEmpty()
                && isWithinClimateArea(positions, playerPos);
    }

    private boolean isWithinClimateArea(Set<BlockPos> positions, BlockPos playerPos) {

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (BlockPos pos : positions) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());

            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }

        return playerPos.getX() >= minX
                && playerPos.getX() <= maxX
                && playerPos.getY() >= minY - 1
                && playerPos.getY() <= maxY + 2
                && playerPos.getZ() >= minZ
                && playerPos.getZ() <= maxZ;
    }
}