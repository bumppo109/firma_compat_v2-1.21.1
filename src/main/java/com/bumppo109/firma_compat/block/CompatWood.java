package com.bumppo109.firma_compat.block;

import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.materials.WoodMaterial;
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
import net.dries007.tfc.common.items.ChestBlockItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

public enum CompatWood implements ModRegistryWood {
    ACACIA(MapColor.COLOR_ORANGE, MapColor.STONE),
    BIRCH(MapColor.SAND, MapColor.QUARTZ),
    CHERRY(MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_PINK),
    DARK_OAK(MapColor.COLOR_BROWN, MapColor.COLOR_BROWN),
    JUNGLE(MapColor.DIRT, MapColor.PODZOL),
    MANGROVE(MapColor.COLOR_RED, MapColor.PODZOL),
    OAK(MapColor.WOOD, MapColor.PODZOL),
    SPRUCE(MapColor.PODZOL, MapColor.COLOR_BROWN),
    BAMBOO(MapColor.COLOR_YELLOW, MapColor.COLOR_LIGHT_GREEN),
    CRIMSON(MapColor.CRIMSON_STEM, MapColor.CRIMSON_STEM, WoodMaterial.CRIMSON,false),
    WARPED(MapColor.WARPED_STEM, MapColor.WARPED_STEM, WoodMaterial.WARPED,false)
    ;

    public static final CompatWood[] VALUES = values();
    private final String serializedName;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final WoodMaterial woodMaterial;
    private final boolean isFlammable;

    private CompatWood(MapColor woodColor, MapColor barkColor) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.woodMaterial = WoodMaterial.valueOf(this.name());
        this.isFlammable = true;
    }

    private CompatWood(MapColor woodColor, MapColor barkColor, WoodMaterial woodMaterial) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.woodMaterial = woodMaterial;
        this.isFlammable = true;
    }

    private CompatWood(MapColor woodColor, MapColor barkColor, WoodMaterial woodMaterial, boolean isFlammable) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.woodMaterial = woodMaterial;
        this.isFlammable = isFlammable;
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
    public Supplier<Block> getBlock(CompatWood.BlockType type) {
        return ModBlocks.WOODS.get(this).get(type);
    }

    public WoodMaterial woodMaterial() {
        return this.woodMaterial;
    }

    public static enum BlockType {
        //BOOKSHELF((self,wood) -> new BookshelfBlock(properties(wood, self).strength(2.0F, 3.0F).enchantPower(BookshelfBlock::getEnchantPower).blockEntity(TFCBlockEntities.BOOKSHELF))),
        //LECTERN((self,wood) -> new TFCLecternBlock(properties(wood, self).noCollission().strength(2.5F).blockEntity(TFCBlockEntities.LECTERN))),
        //TRAPPED_CHEST((self, wood) -> new TFCTrappedChestBlock(properties(wood, self).strength(2.5F).blockEntity(TFCBlockEntities.TRAPPED_CHEST).clientTicks(ChestBlockEntity::lidAnimateTick), wood.getSerializedName()), (block, properties, wood) -> new ChestBlockItem(block, properties, chestBoatRes(wood))),
        //CHEST((self, wood) -> new TFCChestBlock(properties(wood, self).strength(2.5F).blockEntity(TFCBlockEntities.CHEST).clientTicks(ChestBlockEntity::lidAnimateTick), wood.getSerializedName()), (block, properties, wood) -> new ChestBlockItem(block, properties, chestBoatRes(wood))),
        //WORKBENCH((self,wood) -> new TFCCraftingTableBlock(properties(wood, self).strength(2.5F))),

        LOG_FENCE((self,wood) -> new TFCFenceBlock(properties(wood, self).strength(2.0F, 3.0F))),
        TOOL_RACK((self,wood) -> new ToolRackBlock(properties(wood, self).strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.TOOL_RACK))),
        TWIG((wood) -> GroundcoverBlock.twig(ExtendedProperties.of().strength(0.05F, 0.0F).sound(SoundType.WOOD).noCollission())),
        FALLEN_LEAVES((self, wood) -> new FallenLeavesBlock(ExtendedProperties.of().strength(0.05F, 0.0F).noOcclusion().noCollission().isViewBlocking(TFCBlocks::never).sound(SoundType.CROP), Objects.requireNonNull(wood.woodMaterial().leaves()))),
        VERTICAL_SUPPORT((self,wood) -> new VerticalSupportBlock(properties(wood, self).strength(1.0F).noOcclusion())),
        HORIZONTAL_SUPPORT((self,wood) -> new HorizontalSupportBlock(properties(wood, self).strength(1.0F).noOcclusion())),
        LOOM((self, wood) -> new TFCLoomBlock(properties(wood, self).strength(2.5F).noOcclusion().blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick), self.planksTexture(wood))),
        SLUICE((self,wood) -> new SluiceBlock(properties(wood, self).strength(3.0F).noOcclusion().blockEntity(TFCBlockEntities.SLUICE).serverTicks(SluiceBlockEntity::serverTick))),
        BARREL((self, wood) -> new BarrelBlock(properties(wood, self).strength(2.5F).noOcclusion().blockEntity(TFCBlockEntities.BARREL).serverTicks(BarrelBlockEntity::serverTick)), BarrelBlockItem::new),
        SCRIBING_TABLE((self,wood) -> new ScribingTableBlock(properties(wood, self).noOcclusion().strength(2.5F))),
        SEWING_TABLE((self,wood) -> new SewingTableBlock(properties(wood, self).noOcclusion().strength(2.5F))),
        SHELF((self,wood) -> new ShelfBlock(properties(wood, self).noOcclusion().strength(2.5F).blockEntity(TFCBlockEntities.SHELF), false)),
        AXLE((self, wood) -> new AxleBlock(properties(wood, self).noOcclusion().strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.AXLE), getBlock(wood, self.windmill()), self.planksTexture(wood))),
        BLADED_AXLE((self, wood) -> new BladedAxleBlock(properties(wood, self).noOcclusion().strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.BLADED_AXLE), getBlock(wood, self.axle()))),
        ENCASED_AXLE((self, wood) -> new EncasedAxleBlock(properties(wood, self).strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.ENCASED_AXLE))),
        CLUTCH((self, wood) -> new ClutchBlock(properties(wood, self).strength(2.5F).pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.CLUTCH), getBlock(wood, self.axle()))),
        GEAR_BOX((self, wood) -> new GearBoxBlock(properties(wood, self).strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.GEAR_BOX), getBlock(wood, self.axle()))),
        WINDMILL((self, wood) -> new WindmillBlock(properties(wood, self).strength(9.0F).noOcclusion().blockEntity(TFCBlockEntities.WINDMILL).ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick), getBlock(wood, self.axle()))),
        WATER_WHEEL((self, wood) -> new WaterWheelBlock(properties(wood, self).strength(9.0F).noOcclusion().blockEntity(TFCBlockEntities.WATER_WHEEL).ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick), getBlock(wood, self.axle()), self.waterWheelTexture(wood))),
        CRATE((self, wood) -> new CrateBlock(properties(wood, self).strength(9.0F).noOcclusion().blockEntity(TFCBlockEntities.CRATE)));

        private final BiFunction<BlockType, ModRegistryWood, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, ModRegistryWood, ? extends BlockItem> blockItemFactory;

        private static ExtendedProperties properties(ModRegistryWood wood, CompatWood.BlockType blockType) {
            ExtendedProperties properties = ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS);

            if (wood.isFlammable()) {
                return switch(blockType) {
                    case TWIG, FALLEN_LEAVES -> properties.flammableLikeWool();
                    case LOOM, BARREL, SHELF -> properties.flammableLikePlanks();
                    default -> properties.flammableLikeLogs();
                };
            } else {
                return properties;
            }
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

        public boolean hasVariant(CompatWood wood) {
            boolean hasLog = wood.woodMaterial.log() != null;
            boolean hasPlanks = wood.woodMaterial.planks() != null;
            boolean hasLeaves = wood.woodMaterial.leaves() != null;

            return switch (this) {
                case FALLEN_LEAVES -> hasLeaves;
                case TWIG, VERTICAL_SUPPORT, HORIZONTAL_SUPPORT -> hasLog;
                case LOG_FENCE -> hasLog && hasPlanks;
                default -> hasPlanks;
            };
        }

        private ResourceLocation planksTexture(ModRegistryWood wood) {
            return ResourceLocation.withDefaultNamespace("block/" + wood.getSerializedName() + "_planks");
        }

        private ResourceLocation waterWheelTexture(ModRegistryWood wood) {
            return FirmaCompatHelpers.modIdentifier("textures/entity/water_wheel/" + wood.getSerializedName() + ".png");
        }

        private static ResourceLocation chestBoatRes(ModRegistryWood wood) {
            return FirmaCompatHelpers.modIdentifier("textures/entity/chest_boat/" + wood.getSerializedName() + ".png");
        }

        private CompatWood.BlockType twig() {
            return TWIG;
        }

        private CompatWood.BlockType fallenLeaves() {
            return FALLEN_LEAVES;
        }

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