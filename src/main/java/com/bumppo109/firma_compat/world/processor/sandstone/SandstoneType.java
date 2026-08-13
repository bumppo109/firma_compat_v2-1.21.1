package com.bumppo109.firma_compat.world.processor.sandstone;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;


public enum SandstoneType
        implements StringRepresentable
{
    RAW(SandstoneBlockType.RAW),
    SMOOTH(SandstoneBlockType.SMOOTH),
    CUT(SandstoneBlockType.CUT),
    CHISELED(SandstoneBlockType.CHISELED);


    public static final Codec<SandstoneType> CODEC =
            StringRepresentable.fromEnum(
                    SandstoneType::values
            );


    private final String name;
    private final SandstoneBlockType tfcType;


    SandstoneType(
            SandstoneBlockType tfcType
    )
    {
        this.name =
                name().toLowerCase();

        this.tfcType =
                tfcType;
    }


    @Override
    public String getSerializedName()
    {
        return name;
    }


    public SandstoneBlockType tfcType()
    {
        return tfcType;
    }
}