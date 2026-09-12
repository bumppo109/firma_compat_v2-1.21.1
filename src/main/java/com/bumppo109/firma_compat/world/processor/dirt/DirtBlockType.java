package com.bumppo109.firma_compat.world.processor.dirt;


import com.mojang.serialization.Codec;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;


public enum DirtBlockType implements StringRepresentable
{
    GRASS(SoilBlockType.GRASS),
    DUFF(SoilBlockType.DUFF),
    GRASS_PATH(SoilBlockType.GRASS_PATH),
    CLAY(SoilBlockType.CLAY),
    FARMLAND(SoilBlockType.FARMLAND),
    DIRT(SoilBlockType.DIRT),
    COARSE_DIRT(SoilBlockType.COARSE_DIRT),
    ROOTED_DIRT(SoilBlockType.ROOTED_DIRT),
    MUD(SoilBlockType.MUD),
    MUD_BRICKS(SoilBlockType.MUD_BRICKS);


    public static final Codec<DirtBlockType> CODEC =
            StringRepresentable.fromEnum(
                    DirtBlockType::values
            );


    private final SoilBlockType soilType;


    DirtBlockType(
            SoilBlockType soilType
    )
    {
        this.soilType = soilType;
    }


    public SoilBlockType soilType()
    {
        return soilType;
    }


    @Override
    public String getSerializedName()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}