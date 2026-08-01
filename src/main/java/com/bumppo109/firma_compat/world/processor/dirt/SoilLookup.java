package com.bumppo109.firma_compat.world.processor.dirt;

import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;


public final class SoilLookup
{
    private SoilLookup()
    {
    }


    public static SoilBlockType.Variant variant(
            LevelReader level,
            BlockPos pos
    )
    {
        try
        {
            ChunkData data =
                    ChunkData.get(
                            level,
                            pos
                    );


            float groundwater =
                    data.getBaseGroundwater(
                            pos.getX(),
                            pos.getZ()
                    );


            float rainfall =
                    data.getAverageRainfall(
                            pos.getX(),
                            pos.getZ()
                    );


            float temperature =
                    data.getAverageSeaLevelTemp(
                            pos.getX(),
                            pos.getZ()
                    );


            /*
             * Flood plains / river deposits.
             */
            if (groundwater > 25.0F)
            {
                return SoilBlockType.Variant.FLUVISOL;
            }


            /*
             * Hot and dry.
             */
            if (temperature > 18.0F
                    && rainfall < 200.0F)
            {
                return SoilBlockType.Variant.ARIDISOL;
            }


            /*
             * Tropical weathered soils.
             */
            if (temperature > 20.0F
                    && rainfall > 500.0F)
            {
                return SoilBlockType.Variant.OXISOL;
            }


            /*
             * Cool wet forests.
             */
            if (temperature < 10.0F
                    && rainfall > 400.0F)
            {
                return SoilBlockType.Variant.PODZOL;
            }


            /*
             * Warm grassland / prairie.
             */
            if (temperature >= 10.0F
                    && temperature <= 18.0F
                    && rainfall >= 300.0F
                    && rainfall <= 700.0F)
            {
                return SoilBlockType.Variant.MOLLISOL;
            }


            /*
             * Temperate productive soils.
             */
            if (rainfall >= 250.0F)
            {
                return SoilBlockType.Variant.ALFISOL;
            }


            /*
             * Safe generic fallback.
             */
            return SoilBlockType.Variant.ENTISOL;
        }
        catch (Exception ignored)
        {
            return SoilBlockType.Variant.ENTISOL;
        }
    }
}