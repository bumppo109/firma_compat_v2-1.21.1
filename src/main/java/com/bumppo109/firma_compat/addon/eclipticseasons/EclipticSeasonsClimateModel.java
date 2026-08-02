package com.bumppo109.firma_compat.addon.eclipticseasons;

import com.bumppo109.firma_compat.addon.AddonClimateModels;
import com.bumppo109.firma_compat.world.climate.ModClimateModels;
import io.netty.buffer.ByteBuf;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.LevelReader;

public class EclipticSeasonsClimateModel implements ClimateModel {
    public static final EclipticSeasonsClimateModel INSTANCE = new EclipticSeasonsClimateModel();
    public static final StreamCodec<ByteBuf, EclipticSeasonsClimateModel> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public ClimateModelType type() {
        return AddonClimateModels.ECLIPTIC_MODEL.get();
    }

    @Override
    public float getAverageTemperature(LevelReader levelReader, BlockPos pos) {
        return EclipticClimateHelper.getTemperature(levelReader, pos);
    }

    @Override
    public float getAverageRainfall(LevelReader levelReader, BlockPos pos) {
        return EclipticClimateHelper.getRainfall(levelReader, pos);
    }
}