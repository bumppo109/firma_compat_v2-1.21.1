package com.bumppo109.firma_compat.world.processor;

import net.minecraft.util.StringRepresentable;

public enum ReplacementCategory
        implements StringRepresentable
{
    ROCK,
    DIRT,
    SAND,
    SANDSTONE,
    WOOD;


    private final String serializedName;


    ReplacementCategory()
    {
        this.serializedName =
                name().toLowerCase();
    }


    @Override
    public String getSerializedName()
    {
        return serializedName;
    }


    public static final StringRepresentable.EnumCodec<ReplacementCategory> CODEC =
            StringRepresentable.fromEnum(
                    ReplacementCategory::values
            );
}