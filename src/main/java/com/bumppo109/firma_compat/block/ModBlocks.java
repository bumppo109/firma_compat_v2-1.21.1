package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.common.base.Suppliers;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockAnvilBlock;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.common.blocks.wood.TFCChestBlock;
import net.dries007.tfc.common.blocks.wood.TFCTrappedChestBlock;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.common.items.ChestBlockItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);
    public static final DeferredRegister<Block> FLUID_BLOCKS = DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);

//Wood
    public static final Map<CompatWood, Map<CompatWood.BlockType, Id<Block>>> WOODS = Helpers.mapOf(CompatWood.class, (wood) ->
            Helpers.mapOf(CompatWood.BlockType.class, (type) ->
                    register(type.nameFor(wood), type.create(wood), type.createBlockItem(wood, new Item.Properties()))
            )
    );


    public static final Id<Block> COMPAT_CHEST = register(
            "compat_chest",
            () -> new TFCChestBlock(
                    ExtendedProperties.of()
                            .strength(2.5F)
                            .flammableLikeLogs()
                            .blockEntity(TFCBlockEntities.CHEST)
                            .clientTicks(ChestBlockEntity::lidAnimateTick),
                    "compat_chest"  // texture identifier (folder or key for entity textures)
            ),
            block -> new ChestBlockItem(
                    block,
                    new Item.Properties(),
                    ResourceLocation.withDefaultNamespace("textures/entity/chest_boat/oak.png")  // required third param; dummy/custom path
            )
    );

    public static final Id<Block> COMPAT_TRAPPED_CHEST = register(
            "compat_trapped_chest",
            () -> new TFCTrappedChestBlock(
                    ExtendedProperties.of()
                            .strength(2.5F)
                            .flammableLikeLogs()
                            .blockEntity(TFCBlockEntities.TRAPPED_CHEST)
                            .clientTicks(ChestBlockEntity::lidAnimateTick),
                    "compat_trapped_chest"  // texture identifier
            ),
            block -> new ChestBlockItem(
                    block,
                    new Item.Properties(),
                    ResourceLocation.withDefaultNamespace("textures/entity/chest_boat/oak.png")  // or same as above
            )
    );

//Rock
    public static final Map<CompatRock, Map<CompatRock.BlockType, Id<Block>>> ROCK_BLOCKS = Helpers.mapOf(CompatRock.class, rock ->
            Helpers.mapOf(CompatRock.BlockType.class, type -> {
                final String name = switch (type) {
                    case HARDENED -> "hardened_" + rock.name();
                    case HARDENED_COBBLE -> "hardened_" + rock.name() + "_cobble";
                    case MOSSY_HARDENED_COBBLE -> "mossy_hardened_" + rock.name() + "_cobble";
                    case COBBLE -> rock.name() + "_cobble";
                    case MOSSY_COBBLE -> "mossy_" + rock.name() + "_cobble";
                    case MOSSY_LOOSE -> "mossy_" + rock.name() + "_loose";
                    default -> rock.name() + "_" + type.name();
                };

                return type.needsItem()
                        ? register(name, () -> type.create(rock), rock.createItemProperties())
                        : registerNoItem(name, () -> type.create(rock));
            })
    );

    public static final Map<CompatRock, Map<CompatRock.BlockType, ModDecorationBlockHolder>> ROCK_DECORATIONS = Helpers.mapOf(CompatRock.class, (rock) ->
            Helpers.mapOf(CompatRock.BlockType.class, CompatRock.BlockType::hasVariants, (type) ->
                    registerDecorations(createRockDecorationName(rock, type), () -> type.createSlab(rock), () -> type.createStairs(rock), () -> type.createWall(rock), rock.createItemProperties())));


    public static final Map<CompatRock, Id<Block>> ROCK_ANVILS = Helpers.mapOf(CompatRock.class, CompatRock::canMakeAnvil, (rock) ->
            register(rock.name() + "_anvil", () -> new RockAnvilBlock(ExtendedProperties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F, 10.0F).requiresCorrectToolForDrops().cloneItem((ItemLike)rock.base()).blockEntity(TFCBlockEntities.ANVIL)), (Function)((b) -> new BlockItem((Block) b, rock.createItemProperties()))));

    public static final Map<CompatRock, Map<Ore, Id<Block>>> ORES = Helpers.mapOf(CompatRock.class, (rock) ->
            Helpers.mapOf(Ore.class, (ore) -> !ore.isGraded() && ore.hasBlock() && !skippedOre(ore), (ore) ->
                    register(rock.name() + "_" + ore.name() + "_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(rock.rockMaterial().raw().base().get())))));

    public static final Map<CompatRock, Map<Ore, Map<Ore.Grade, Id<Block>>>> GRADED_ORES = Helpers.mapOf(CompatRock.class, (rock) ->
            Helpers.mapOf(Ore.class, Ore::isGraded, (ore) ->
                    Helpers.mapOf(Ore.Grade.class, (grade) ->
                            register(grade.name() + "_" + rock.name() + "_" + ore.name() + "_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(rock.rockMaterial().raw().base().get()))))));

    //adds hardened cobble variants for TFC
    public static final Map<Rock, Map<Rock.BlockType, Id<Block>>> TFC_ROCK_BLOCKS = Helpers.mapOf(Rock.class, rock ->
            Helpers.mapOf(Rock.BlockType.class, ModBlocks::hasTFCVariants, type -> {
                final String name = switch (type) {
                    case COBBLE -> "tfc_hardened_" + rock.getSerializedName() + "_cobble";
                    case MOSSY_COBBLE -> "tfc_mossy_hardened_" + rock.getSerializedName() + "_cobble";
                    default -> rock.getSerializedName() + "_" + type.name();
                };
                return register(name, () -> new Block(BlockBehaviour.Properties.of().mapColor(rock.color()).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(rock.category().hardness(5.5F), 10.0F).requiresCorrectToolForDrops()));
            })
    );

    //Aqueduct
    public static final Map<CompatRockMaterial, Id<Block>> AQUEDUCTS = Helpers.mapOf(CompatRockMaterial.class,material ->
            material.brick() != null,material -> {
                CompatRockSet brick = Objects.requireNonNull(material.brick());

                return register(getAqueductPath(brick.base().get()), () -> new AqueductBlock(BlockBehaviour.Properties.ofFullCopy(brick.base().get())));
            }
    );
    public static final Id<Block> BRICK_AQUEDUCT = register("brick_aqueduct",
            () -> new AqueductBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)));
    public static final Id<Block> PRISMARINE_BRICK_AQUEDUCT = register("prismarine_brick_aqueduct",
            () -> new AqueductBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_BRICKS)));
    public static final Id<Block> RED_NETHER_BRICK_AQUEDUCT = register("red_nether_brick_aqueduct",
            () -> new AqueductBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static final Id<Block> QUARTZ_BRICK_AQUEDUCT = register("quartz_brick_aqueduct",
            () -> new AqueductBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BRICKS)));

//Metal

    public static final Map<CompatMetal, Id<LiquidBlock>> METAL_FLUIDS = Helpers.mapOf(CompatMetal.class, metal ->
            registerNoItem("fluid/metal/" + metal.name(), () -> new LiquidBlock(ModFluids.METALS.get(metal).source().get(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA).noLootTable()))
    );

//Earthen
    //gravel deposit
    public static final Id<Block> CASSITERITE_GRAVEL_DEPOSIT = register("cassiterite_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final Id<Block> NATIVE_COPPER_GRAVEL_DEPOSIT = register("native_copper_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final Id<Block> NATIVE_GOLD_GRAVEL_DEPOSIT = register("native_gold_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final Id<Block> NATIVE_SILVER_GRAVEL_DEPOSIT = register("native_silver_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));

    public static final Id<Block> DRYING_MUD_BRICK = register("drying_mud_brick",
            () -> new DryingBricksBlock(ExtendedProperties.of(MapColor.DIRT).noCollission().noOcclusion().instabreak().sound(SoundType.STEM).randomTicks().blockEntity(TFCBlockEntities.TICK_COUNTER), ModItems.MUD_BRICK));
    public static final Id<Block> COMPAT_FARMLAND = register("compat_farmland",
            () -> new FarmlandBlock(ExtendedProperties.of(MapColor.DIRT).requiresCorrectToolForDrops().strength(1.3F).sound(SoundType.GRAVEL).randomTicks().isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(TFCBlockEntities.FARMLAND).serverTicks(FarmlandBlockEntity::serverTick), Suppliers.ofInstance(Blocks.DIRT)));
    public static final Id<Block> CLAY_GRASS_BLOCK = register("clay_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));
    public static final Id<Block> CLAY_DIRT = register("clay_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final Id<Block> CLAY_PODZOL = register("clay_podzol",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PODZOL)));
    public static final Id<Block> KAOLIN_CLAY_GRASS_BLOCK = register("kaolin_clay_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));
    public static final Id<Block> KAOLIN_CLAY_DIRT = register("kaolin_clay_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final Id<Block> KAOLIN_CLAY_PODZOL = register("kaolin_clay_podzol",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PODZOL)));

    public static boolean skippedOre(Ore ore) {
        return switch (ore) {
            case BITUMINOUS_COAL, LIGNITE, HALITE -> true;
            default -> false;
        };
    }

    public static boolean hasTFCVariants(Rock.BlockType blockType) {
        return switch (blockType) {
            case COBBLE, MOSSY_COBBLE -> true;
            default -> false;
        };
    }

    public static String createRockDecorationName(CompatRock rock, CompatRock.BlockType blockType) {
        return switch (blockType) {
            case HARDENED_COBBLE -> "hardened_" + rock.getSerializedName() + "_cobble";
            case MOSSY_HARDENED_COBBLE -> "mossy_hardened_" + rock.getSerializedName() + "_cobble";
            default -> rock.name() + "_" + blockType.name();
        };
    }

    private static String getAqueductPath(Block block) {
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        if (path.endsWith("s")) {
            path = path.substring(0, path.length() - 1);
        }
        return path + "_aqueduct";
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

    private static <T1 extends SlabBlock, T2 extends StairBlock, T3 extends WallBlock> ModDecorationBlockHolder registerDecorations(String baseName, Supplier<T1> slab, Supplier<T2> stair, Supplier<T3> wall, Item.Properties properties) {

        return new ModDecorationBlockHolder(register(baseName + "_slab", slab, (Function)((b) -> new BlockItem((Block) b, properties))), register(baseName + "_stairs", stair, (Function)((b) -> new BlockItem((Block) b, properties))), register(baseName + "_wall", wall, (Function)((b) -> new BlockItem((Block) b, properties))));
    }

    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }
}
