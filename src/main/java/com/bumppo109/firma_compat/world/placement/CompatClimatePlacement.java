package com.bumppo109.firma_compat.world.placement;

import com.bumppo109.firma_compat.world.climate.VanillaClimateHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class CompatClimatePlacement extends PlacementModifier {

    public static final MapCodec<CompatClimatePlacement> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("min_temperature", Float.NEGATIVE_INFINITY).forGetter(c -> c.minTemp),
                    Codec.FLOAT.optionalFieldOf("max_temperature", Float.POSITIVE_INFINITY).forGetter(c -> c.maxTemp),
                    Codec.FLOAT.optionalFieldOf("min_groundwater", Float.NEGATIVE_INFINITY).forGetter(c -> c.minGroundwater),
                    Codec.FLOAT.optionalFieldOf("max_groundwater", Float.POSITIVE_INFINITY).forGetter(c -> c.maxGroundwater),
                    Codec.INT.optionalFieldOf("min_elevation", Integer.MIN_VALUE).forGetter(c -> c.minElevation),
                    Codec.INT.optionalFieldOf("max_elevation", Integer.MAX_VALUE).forGetter(c -> c.maxElevation)
            ).apply(instance, CompatClimatePlacement::new)
    );

    private final float minTemp;
    private final float maxTemp;
    private final float minGroundwater;
    private final float maxGroundwater;
    private final int minElevation;
    private final int maxElevation;

    public CompatClimatePlacement(
            float minTemp, float maxTemp,
            float minGroundwater, float maxGroundwater,
            int minElevation, int maxElevation
    ) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minGroundwater = minGroundwater;
        this.maxGroundwater = maxGroundwater;
        this.minElevation = minElevation;
        this.maxElevation = maxElevation;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacement.COMPAT_CLIMATE_PLACEMENT.get();
    }

    public boolean isValid(WorldGenLevel level, BlockPos pos, RandomSource random) {

        float temp = VanillaClimateHelper.getTemperatureWorldgen(
                level, pos
        );

        float rain = VanillaClimateHelper.getRainfallWorldgen(
                level, pos
        );

        int elevation = pos.getY();

        return elevation >= minElevation && elevation <= maxElevation
                && temp >= minTemp && temp <= maxTemp
                && rain >= minGroundwater && rain <= maxGroundwater;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        WorldGenLevel level = context.getLevel();
        if (isValid(level, pos, random)) {
            return Stream.of(pos);
        }
        return Stream.empty();
    }
}