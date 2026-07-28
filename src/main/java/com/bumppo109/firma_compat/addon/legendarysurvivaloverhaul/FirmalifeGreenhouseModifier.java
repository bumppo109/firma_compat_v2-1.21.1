package com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul;

import com.bumppo109.firma_compat.addon.ModCompat;

import com.bumppo109.firma_compat.world.climate.VanillaClimateHelper;
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

import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;

import java.util.Optional;
import java.util.Set;


public class FirmalifeGreenhouseModifier extends ModifierBase {

    private static final int SEARCH_DISTANCE = 64;

    @Override
    public float getWorldInfluence(Player player, Level level, BlockPos pos) {

        if (!ModCompat.loaded("firmalife")) return 0.0F;
        if (level.isClientSide) return 0.0F;
        if (!isInsideValidGreenhouse(level, pos)) return 0.0F;

        float currentPlayerTemp = TemperatureUtil.getPlayerTargetTemperature(player);

        return currentPlayerTemp >= 20 ? -(currentPlayerTemp - 20.0F) : currentPlayerTemp - 20.0F;
    }

    private boolean isInsideValidGreenhouse(Level level, BlockPos playerPos) {

        if(!(level instanceof ServerLevel)) return false;

        PoiManager poiManager = ((ServerLevel) level).getPoiManager();


        Optional<BlockPos> stationPosition = poiManager.findClosest(holder ->
                                holder.is(
                                        FLPOIs.CLIMATE_STATIONS.getKey()),
                                        playerPos,
                                        SEARCH_DISTANCE,
                                        PoiManager.Occupancy.ANY);


        if (stationPosition.isEmpty()) return false;

        BlockPos pos = stationPosition.get();

        BlockState state = level.getBlockState(pos);

        /*
         * Firmalife's own validation flag.
         *
         * Avoids using stale POI data where the station
         * exists but the greenhouse has failed.
         */
        if (!state.hasProperty(ClimateStationBlock.STASIS) || !state.getValue(ClimateStationBlock.STASIS)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof ClimateStationBlockEntity station)) return false;

        if (!(station instanceof ClimateStationAccess accessor)) {
            System.out.println("[ClimateStationAccessor] mixin not loaded]");
            return false;
        }

        Set<BlockPos> positions = accessor.firmaCompat$getPositions();

        if (positions.isEmpty()) return false;

        return isWithinClimateArea(positions, playerPos);
    }

    private boolean isWithinClimateArea(Set<BlockPos> positions, BlockPos playerPos) {

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for(BlockPos pos : positions) {
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