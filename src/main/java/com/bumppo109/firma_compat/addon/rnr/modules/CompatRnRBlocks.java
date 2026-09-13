package com.bumppo109.firma_compat.addon.rnr.modules;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.therighthon.rnr.common.block.*;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompatRnRBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);

    //Tamped
    public static final ModBlocks.Id<Block> TAMPED_DIRT = register("tamped_dirt",
            () -> new TampedSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));

    public static final ModBlocks.Id<Block> TAMPED_MUD = register("tamped_mud",
            () -> new TampedMudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD)));

    //Wood
    public static final Map<CompatWood, ModBlocks.Id<Block>> WOOD_SHINGLE_ROOFS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingles",
            () -> switch(wood){
                case CRIMSON, WARPED -> new Block(ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties());
                default -> new Block(ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().flammableLikeLogs().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties());
            }));

    public static final Map<CompatWood, ModBlocks.Id<Block>> WOOD_SHINGLE_ROOF_SLABS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingles_slab",
            () -> switch(wood){
                case CRIMSON, WARPED -> new SlabBlock(ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties());
                default -> new SlabBlock(ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().flammableLikeLogs().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties());
            }));

    public static final Map<CompatWood, ModBlocks.Id<Block>> WOOD_SHINGLE_ROOF_STAIRS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingles_stairs",
            () -> switch(wood){
                case CRIMSON, WARPED -> new StairBlock(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get().defaultBlockState(), ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties());
                default -> new StairBlock(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get().defaultBlockState(), ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().flammableLikeLogs().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties());
            }));

    //Flagstone, Sett, Cobbled
    public static final Map<CompatRock, Map<CompatRnR, ModBlocks.Id<Block>>> ROCK_BLOCKS = Helpers.mapOf(CompatRock.class, (rock) ->
            Helpers.mapOf(CompatRnR.class, CompatRnR::hasVariants, (type) -> register(rock.name() + "_" + type.name(), () -> type.create(rock))));;
    public static final Map<CompatRock, Map<CompatRnR, ModBlocks.Id<Block>>> ROCK_SLABS = Helpers.mapOf(CompatRock.class, (rock) ->
            Helpers.mapOf(CompatRnR.class, CompatRnR::hasVariants, (type) -> register(rock.name() + "_" + type.name() + "_slab", () -> type.createRockSlab(rock, type))));;
    public static final Map<CompatRock, Map<CompatRnR, ModBlocks.Id<Block>>> ROCK_STAIRS = Helpers.mapOf(CompatRock.class, (rock) ->
            Helpers.mapOf(CompatRnR.class, CompatRnR::hasVariants, (type) -> register(rock.name() + "_" + type.name() + "_stairs", () -> type.createPathStairs(rock, type))));;

    //Gravel
    public static final ModBlocks.Id<Block> GRAVEL_ROAD = register("gravel_road",
            () -> new PathHeightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final ModBlocks.Id<Block> GRAVEL_ROAD_STAIRS = register("gravel_road_stairs",
            () -> new PathStairBlock(() -> CompatRnRBlocks.GRAVEL_ROAD.get().defaultBlockState() ,BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final ModBlocks.Id<Block> GRAVEL_ROAD_SLAB = register("gravel_road_slab",
            () -> new PathSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));

    public static final ModBlocks.Id<Block> OVER_HEIGHT_GRAVEL = register("overheight_gravel",
            () -> new OverHeightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));

    public static final ModBlocks.Id<Block> MACADAM_ROAD = register("macadam_road",
            () -> new PathHeightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final ModBlocks.Id<Block> MACADAM_ROAD_STAIRS = register("macadam_road_stairs",
            () -> new PathStairBlock(() -> CompatRnRBlocks.MACADAM_ROAD.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final ModBlocks.Id<Block> MACADAM_ROAD_SLAB = register("macadam_road_slab",
            () -> new PathSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));

    private static <T extends Block> ModBlocks.Id<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

    protected static <T extends Block> ModBlocks.Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> ModBlocks.Id<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> ModBlocks.Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new ModBlocks.Id<>(RegistrationHelpers.registerBlock(ModBlocks.BLOCKS, ModItems.ITEMS, name, blockSupplier, blockItemFactory));
    }
}
