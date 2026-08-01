package com.bumppo109.firma_compat.world.processor;

import com.mojang.serialization.Codec;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Represents a logical TFC rock block type.
 *
 * This closely mirrors TFC's Rock.BlockType enum while also exposing
 * terrain-only block types (Sand/Sandstone) that come from RockSettings.
 */
public enum RockBlockType implements StringRepresentable
{
    RAW(Rock.BlockType.RAW),
    HARDENED(Rock.BlockType.HARDENED),
    COBBLE(Rock.BlockType.COBBLE),
    MOSSY_COBBLE(Rock.BlockType.MOSSY_COBBLE),

    BRICKS(Rock.BlockType.BRICKS),
    CRACKED_BRICKS(Rock.BlockType.CRACKED_BRICKS),
    MOSSY_BRICKS(Rock.BlockType.MOSSY_BRICKS),

    SMOOTH(Rock.BlockType.SMOOTH),
    CHISELED(Rock.BlockType.CHISELED),

    PRESSURE_PLATE(Rock.BlockType.PRESSURE_PLATE),
    BUTTON(Rock.BlockType.BUTTON),

    GRAVEL(Rock.BlockType.GRAVEL);

    private final Rock.BlockType tfcType;

    public static final Codec<RockBlockType> CODEC =
            StringRepresentable.fromEnum(
                    RockBlockType::values
            );

    RockBlockType(@Nullable Rock.BlockType tfcType)
    {
        this.tfcType = tfcType;
    }

    @Override
    public String getSerializedName()
    {
        return name().toLowerCase(Locale.ROOT);
    }

    @Nullable
    public Rock.BlockType tfcType()
    {
        return tfcType;
    }
}