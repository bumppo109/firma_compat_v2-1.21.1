package com.bumppo109.firma_compat.world.processor;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;


public enum BlockShape
        implements StringRepresentable
{

    BLOCK,

    SLAB,

    STAIR,

    WALL;



    public static final Codec<BlockShape> CODEC =
            StringRepresentable.fromEnum(
                    BlockShape::values
            );



    private final String name;


    BlockShape()
    {
        this.name =
                name().toLowerCase();
    }



    @Override
    public String getSerializedName()
    {
        return name;
    }
}