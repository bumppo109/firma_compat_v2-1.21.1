package com.bumppo109.firma_compat.addon.rnr.modules;

import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModRegistryRock;
import com.therighthon.rnr.common.block.PathHeightBlock;
import com.therighthon.rnr.common.block.PathSlabBlock;
import com.therighthon.rnr.common.block.PathStairBlock;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum CompatRnR implements StringRepresentable {
    FLAGSTONES((rock, self) -> new PathHeightBlock(properties(rock)), true),
    SETT_ROAD((rock, self) -> new PathHeightBlock(properties(rock)), true),
    COBBLED_ROAD((rock, self) -> new PathHeightBlock(properties(rock)), true);

    public static final CompatRnR[] VALUES = values();
    private final boolean variants;
    private final BiFunction<ModRegistryRock, CompatRnR, Block> blockFactory;
    private final String serializedName;

    /*
    public static CompatRnR valueOf(int i) {
        return i >= 0 && i < VALUES.length ? VALUES[i] : GRAVEL_ROAD;
    }
     */

    private static Properties properties(ModRegistryRock rock) {
        return BlockBehaviour.Properties.ofFullCopy(rock.base());
    }

    private CompatRnR(BiFunction<ModRegistryRock, CompatRnR, Block> blockFactory, boolean variants) {
        this.blockFactory = blockFactory;
        this.variants = variants;
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
    }

    public boolean hasVariants() {
        return this.variants;
    }

    public Block create(ModRegistryRock rock) {
        return this.blockFactory.apply(rock, this);
    }

    public Block createRockSlab(ModRegistryRock rock, CompatRnR type) {
            Properties properties = BlockBehaviour.Properties.ofFullCopy(rock.base());
            return new PathSlabBlock(properties);
    }

    public PathStairBlock createPathStairs(CompatRock rock, CompatRnR type) {
        Supplier<BlockState> state = () -> (rock.rockMaterial().raw().base().get()).defaultBlockState();
            Properties properties = BlockBehaviour.Properties.ofFullCopy(rock.base());
            return new PathStairBlock(state, properties);
    }

    public String getSerializedName() {
        return this.serializedName;
    }
}
