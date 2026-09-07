package com.bumppo109.firma_compat.materials;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;
import java.util.function.Supplier;

public enum SoilMaterial {
    DIRT(
            () -> Blocks.DIRT,
            () -> Blocks.GRASS_BLOCK,
            () -> Blocks.PODZOL
    );

    private final String serializedName;
    private final Supplier<Block> dirt;
    private final Supplier<Block> grass;
    private final Supplier<Block> podzol;

    SoilMaterial(Supplier<Block> dirt, Supplier<Block> grass, Supplier<Block> podzol) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.dirt = dirt;
        this.grass = grass;
        this.podzol = podzol;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public Supplier<Block> getDirt() {
        return dirt;
    }

    public Supplier<Block> getGrass() {
        return grass;
    }

    public Supplier<Block> getPodzol() {
        return podzol;
    }

    public enum SoilBlockType {
        DIRT,
        GRASS,
        PODZOL;

        private final String serializedName;

        SoilBlockType() {
            this.serializedName = this.name().toLowerCase(Locale.ROOT);
        }

        public String getSerializedName() {
            return serializedName;
        }

        public Supplier<Block> baseBlock(SoilMaterial material) {
            return switch (this) {
                case DIRT -> material.getDirt();
                case GRASS -> material.getGrass();
                case PODZOL -> material.getPodzol();
            };
        }

        public String typeName(SoilMaterial material) {
            return switch (this) {
                case DIRT -> material.getSerializedName();
                case GRASS, PODZOL -> this.getSerializedName();
            };
        }
    }

}
