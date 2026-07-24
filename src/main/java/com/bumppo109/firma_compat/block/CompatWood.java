package com.bumppo109.firma_compat.block;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.LoomBlockEntity;
import net.dries007.tfc.common.blockentities.SluiceBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blockentities.rotation.WindmillBlockEntity;
import net.dries007.tfc.common.blocks.*;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rotation.*;
import net.dries007.tfc.common.blocks.wood.*;
import net.dries007.tfc.common.items.BarrelBlockItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;

import javax.annotation.Nullable;

public enum CompatWood implements ModRegistryWood {
    ACACIA(MapColor.COLOR_ORANGE, MapColor.STONE,true),
    BIRCH(MapColor.SAND, MapColor.QUARTZ,true),
    CHERRY(MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_PINK,true),
    DARK_OAK(MapColor.COLOR_BROWN, MapColor.COLOR_BROWN,true),
    JUNGLE(MapColor.DIRT, MapColor.PODZOL,true),
    MANGROVE(MapColor.COLOR_RED, MapColor.PODZOL,true),
    OAK(MapColor.WOOD, MapColor.PODZOL,true),
    SPRUCE(MapColor.PODZOL, MapColor.COLOR_BROWN,true),
    BAMBOO(MapColor.COLOR_YELLOW, MapColor.COLOR_LIGHT_GREEN,true),
    CRIMSON(MapColor.CRIMSON_STEM, MapColor.CRIMSON_STEM, false),
    WARPED(MapColor.WARPED_STEM, MapColor.WARPED_STEM, false)
    ;

    public static final CompatWood[] VALUES = values();
    private final String serializedName;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final boolean isFlammable;

    private CompatWood(MapColor woodColor, MapColor barkColor, boolean isFlammable) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.isFlammable = isFlammable;
    }

    public CompatWoodMaterial compatWoodMaterial() {
        return CompatWoodMaterial.valueOf(this.name());
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public MapColor woodColor() {
        return this.woodColor;
    }

    public MapColor barkColor() {
        return this.barkColor;
    }

    public boolean isFlammable() {
        return this.isFlammable;
    }

    @Override
    public Supplier<Block> getBlock(BlockType type) {
        return ModBlocks.WOODS.get(this).get(type);
    }

    public static enum BlockType {
        //BOOKSHELF((wood) -> new BookshelfBlock(properties(wood, Flammable.LOGS).strength(2.0F, 3.0F).enchantPower(BookshelfBlock::getEnchantPower).blockEntity(TFCBlockEntities.BOOKSHELF))),
        LOG_FENCE((wood) -> new TFCFenceBlock(properties(wood, Flammable.LOGS).strength(2.0F, 3.0F))),
        TOOL_RACK((wood) -> new ToolRackBlock(properties(wood, Flammable.LOGS).strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.TOOL_RACK))),
        TWIG((wood) -> GroundcoverBlock.twig(properties(wood, Flammable.WOOL).strength(0.05F, 0.0F).sound(SoundType.WOOD).noCollission())),
        //FALLEN_LEAVES((self, wood) -> new FallenLeavesBlock(properties(wood, Flammable.WOOL).strength(0.05F, 0.0F).noOcclusion().noCollission().isViewBlocking(TFCBlocks::never).sound(SoundType.CROP), CompatWood.getBlock(self.leaves()))),
        VERTICAL_SUPPORT((wood) -> new VerticalSupportBlock(properties(wood, Flammable.LOGS).strength(1.0F).noOcclusion())),
        HORIZONTAL_SUPPORT((wood) -> new HorizontalSupportBlock(properties(wood, Flammable.LOGS).strength(1.0F).noOcclusion())),
        //WORKBENCH((wood) -> new TFCCraftingTableBlock(properties(wood, Flammable.LOGS).strength(2.5F))),
        //TRAPPED_CHEST((self, wood) -> new TFCTrappedChestBlock(properties(wood, Flammable.LOGS).strength(2.5F).blockEntity(TFCBlockEntities.TRAPPED_CHEST).clientTicks(ChestBlockEntity::lidAnimateTick), CompatWood.getSerializedName()), ChestBlockItem::new),
        //CHEST((self, wood) -> new TFCChestBlock(properties(wood, Flammable.LOGS).strength(2.5F).blockEntity(TFCBlockEntities.CHEST).clientTicks(ChestBlockEntity::lidAnimateTick), CompatWood.getSerializedName()), ChestBlockItem::new),
        LOOM((self, wood) -> new TFCLoomBlock(properties(wood, Flammable.PLANKS).strength(2.5F).noOcclusion().blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick), self.planksTexture(wood))),
        SLUICE((wood) -> new SluiceBlock(properties(wood, Flammable.LOGS).strength(3.0F).noOcclusion().blockEntity(TFCBlockEntities.SLUICE).serverTicks(SluiceBlockEntity::serverTick))),
        BARREL((self, wood) -> new BarrelBlock(properties(wood, Flammable.PLANKS).strength(2.5F).noOcclusion().blockEntity(TFCBlockEntities.BARREL).serverTicks(BarrelBlockEntity::serverTick)), BarrelBlockItem::new),
        //LECTERN((wood) -> new TFCLecternBlock(properties(wood, Flammable.LOGS).noCollission().strength(2.5F).blockEntity(TFCBlockEntities.LECTERN))),
        SCRIBING_TABLE((wood) -> new ScribingTableBlock(properties(wood, Flammable.PLANKS).noOcclusion().strength(2.5F))),
        SEWING_TABLE((wood) -> new SewingTableBlock(properties(wood, Flammable.PLANKS).noOcclusion().strength(2.5F))),
        SHELF((wood) -> new ShelfBlock(properties(wood, Flammable.PLANKS).noOcclusion().strength(2.5F).blockEntity(TFCBlockEntities.SHELF), false)),
        AXLE((self, wood) -> new AxleBlock(properties(wood, Flammable.LOGS).noOcclusion().strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.AXLE), getBlock(wood, self.windmill()), self.planksTexture(wood))),
        BLADED_AXLE((self, wood) -> new BladedAxleBlock(properties(wood, Flammable.LOGS).noOcclusion().strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.BLADED_AXLE), getBlock(wood, self.axle()))),
        ENCASED_AXLE((self, wood) -> new EncasedAxleBlock(properties(wood, Flammable.LOGS).strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.ENCASED_AXLE))),
        CLUTCH((self, wood) -> new ClutchBlock(properties(wood, Flammable.LOGS).strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.CLUTCH), getBlock(wood, self.axle()))),
        GEAR_BOX((self, wood) -> new GearBoxBlock(properties(wood, Flammable.LOGS).strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.GEAR_BOX), getBlock(wood, self.axle()))),
        WINDMILL((self, wood) -> new WindmillBlock(properties(wood, Flammable.LOGS).strength(9.0F).noOcclusion().blockEntity(TFCBlockEntities.WINDMILL).ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick), getBlock(wood, self.axle()))),
        WATER_WHEEL((self, wood) -> new WaterWheelBlock(properties(wood, Flammable.LOGS).strength(9.0F).noOcclusion().blockEntity(TFCBlockEntities.WATER_WHEEL).ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick), getBlock(wood, self.axle()), self.waterWheelTexture(wood))),
        CRATE((self, wood) -> new CrateBlock(properties(wood, Flammable.LOGS).strength(9.0F).noOcclusion().blockEntity(TFCBlockEntities.CRATE)));

        private final BiFunction<BlockType, ModRegistryWood, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, ModRegistryWood, ? extends BlockItem> blockItemFactory;

        private static ExtendedProperties properties(ModRegistryWood wood, Flammable flammableType)
        {
            if(flammableType.equals(Flammable.LOGS) && wood.isFlammable()) {
                return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).flammableLikeLogs();
            } else if (flammableType.equals(Flammable.PLANKS) && wood.isFlammable()) {
                return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).flammableLikePlanks();
            } else if (flammableType.equals(Flammable.WOOL) && wood.isFlammable()) {
                return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).flammableLikeWool();
            } else {
                return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS);
            }
        }

        public enum Flammable {
            LOGS,
            PLANKS,
            WOOL
        }

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(ModRegistryWood wood, BlockType type)
        {
            return (Supplier<? extends B>) wood.getBlock(type);
        }

        BlockType(Function<ModRegistryWood, Block> blockFactory)
        {
            this((self, wood) -> blockFactory.apply(wood));
        }

        BlockType(BiFunction<BlockType, ModRegistryWood, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        BlockType(BiFunction<BlockType, ModRegistryWood, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this(blockFactory, (block, properties, self) -> blockItemFactory.apply(block, properties));
        }

        BlockType(BiFunction<BlockType, ModRegistryWood, Block> blockFactory, TriFunction<Block, Item.Properties, ModRegistryWood, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(ModRegistryWood wood, Item.Properties properties)
        {
            return this.needsItem() ? (block) -> (BlockItem)this.blockItemFactory.apply(block, properties, wood) : null;
            //return needsItem() ? block -> blockItemFactory.apply(block, properties, wood) : null;
        }

        public String nameFor(ModRegistryWood wood) {
            return "%s_%s".formatted(wood.getSerializedName(), this.name().toLowerCase(Locale.ROOT));
        }

        public boolean needsItem()
        {
            return switch (this)
            {
                case VERTICAL_SUPPORT, HORIZONTAL_SUPPORT, WINDMILL -> false;
                default -> true;
            };
        }

        private ResourceLocation planksTexture(ModRegistryWood wood) {
            return ResourceLocation.withDefaultNamespace("block/" + wood.getSerializedName() + "_planks");
        }

        private ResourceLocation waterWheelTexture(ModRegistryWood wood) {
            return Helpers.identifier("textures/entity/water_wheel/" + wood.getSerializedName() + ".png");
        }

        private CompatWood.BlockType twig() {
            return TWIG;
        }

        /*
        private CompatWood.BlockType fallenLeaves() {
            return FALLEN_LEAVES;
        }

        private CompatWood.BlockType leaves() {
            return LEAVES;
        }
         */

        private CompatWood.BlockType axle() {
            return AXLE;
        }

        private CompatWood.BlockType windmill() {
            return WINDMILL;
        }

        public Supplier<Block> create(ModRegistryWood wood) {
            return () -> (Block)this.blockFactory.apply(this, wood);
        }
    }
}
