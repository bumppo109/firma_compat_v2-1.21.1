package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);
    public static final DeferredRegister<Block> FLUID_BLOCKS = DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);

    public static final Map<CompatWood, Map<CompatWood.BlockType, Id<Block>>> WOODS = Helpers.mapOf(CompatWood.class,
            wood -> Helpers.mapOf(
                    CompatWood.BlockType.class,
                    blockType -> blockType.hasVariant(wood),
                    blockType -> register(
                            wood.name() + "_" + blockType.name(),
                            blockType.create(wood),
                            blockType.createBlockItem(wood, new Item.Properties())
                    )
            )
    );

    public static final Map<CompatMetal, Id<LiquidBlock>> METAL_FLUIDS = Helpers.mapOf(CompatMetal.class, metal ->
            registerNoItem("fluid/metal/" + metal.name(), () -> new LiquidBlock(ModFluids.METALS.get(metal).source().get(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA).noLootTable()))
    );


    public static boolean skippedOre(Ore ore) {
        return switch (ore) {
            case BITUMINOUS_COAL, LIGNITE, HALITE -> true;
            default -> false;
        };
    }

    public static ToIntFunction<BlockState> lavaLoggedBlockEmission() {
        return (state) -> ((FluidProperty.FluidKey)state.getValue(((IFluidLoggable)state.getBlock()).getFluidProperty())).is(Fluids.LAVA) ? 15 : 0;
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(ModBlocks.BLOCKS, ModItems.ITEMS, name, blockSupplier, blockItemFactory));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    protected static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> Id<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }

    /*
    private static <T1 extends SlabBlock, T2 extends StairBlock, T3 extends WallBlock> ModDecorationBlockHolder registerDecorations(String baseName, Supplier<T1> slab, Supplier<T2> stair, Supplier<T3> wall, Item.Properties properties) {

        return new ModDecorationBlockHolder(register(baseName + "_slab", slab, (Function)((b) -> new BlockItem((Block) b, properties))), register(baseName + "_stairs", stair, (Function)((b) -> new BlockItem((Block) b, properties))), register(baseName + "_wall", wall, (Function)((b) -> new BlockItem((Block) b, properties))));
    }

     */

    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }
}