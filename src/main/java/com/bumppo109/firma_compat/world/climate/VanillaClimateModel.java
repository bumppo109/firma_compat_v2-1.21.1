package com.bumppo109.firma_compat.world.climate;

import io.netty.buffer.ByteBuf;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.LevelReader;

public class VanillaClimateModel implements ClimateModel {
    public static final VanillaClimateModel INSTANCE = new VanillaClimateModel();

    public static final StreamCodec<ByteBuf, VanillaClimateModel> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public ClimateModelType type() {
        return ModClimateModels.VANILLA_MODEL.get();
    }

    @Override
    public float getAverageTemperature(LevelReader levelReader, BlockPos blockPos) {
        return VanillaClimateHelper.getTemperature(levelReader, blockPos);
    }

    @Override
    public float getAverageRainfall(LevelReader levelReader, BlockPos blockPos) {
        return VanillaClimateHelper.getRainfall(levelReader, blockPos);
    }
}