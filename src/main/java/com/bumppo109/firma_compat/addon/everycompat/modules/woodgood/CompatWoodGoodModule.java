package com.bumppo109.firma_compat.addon.everycompat.modules.woodgood;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.EveryCompatHelper;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.gson.*;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.LoomBlockEntity;
import net.dries007.tfc.common.blockentities.SluiceBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blockentities.rotation.WindmillBlockEntity;
import net.dries007.tfc.common.blocks.CrateBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.ShelfBlock;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rotation.*;
import net.dries007.tfc.common.blocks.wood.*;
import net.dries007.tfc.common.items.BarrelBlockItem;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.mehvahdjukaar.every_compat.misc.UtilityTag.getATagOrCreateANew;

public final class CompatWoodGoodModule extends EveryCompatModule {

    public final ItemOnlyEntrySet<WoodType, Item> LUMBER;
    public final ItemOnlyEntrySet<WoodType, Item> SUPPORT;
    public final SimpleEntrySet<WoodType, Block> TWIG;
    public final SimpleEntrySet<WoodType, Block> LOG_FENCE;
    public final SimpleEntrySet<WoodType, Block> VERTICAL_SUPPORT;
    public final SimpleEntrySet<WoodType, Block> HORIZONTAL_SUPPORT;
    public final SimpleEntrySet<WoodType, Block> TOOL_RACK;
    public final SimpleEntrySet<WoodType, Block> LOOM;
    public final SimpleEntrySet<WoodType, Block> SLUICE;
    public final SimpleEntrySet<WoodType, Block> BARREL;
    public final SimpleEntrySet<WoodType, Block> SCRIBING_TABLE;
    public final SimpleEntrySet<WoodType, Block> SEWING_TABLE;
    public final SimpleEntrySet<WoodType, Block> SHELF;
    public SimpleEntrySet<WoodType, Block> AXLE;
    public final SimpleEntrySet<WoodType, Block> BLADED_AXLE;
    public final SimpleEntrySet<WoodType, Block> ENCASED_AXLE;
    public final SimpleEntrySet<WoodType, Block> CLUTCH;
    public final SimpleEntrySet<WoodType, Block> GEAR_BOX;
    public SimpleEntrySet<WoodType, Block> WINDMILL;
    public final SimpleEntrySet<WoodType, Block> WATER_WHEEL;
    public final SimpleEntrySet<WoodType, Block> CRATE;

    public CompatWoodGoodModule(String modId) {
        super(modId, "tfc");

        Supplier<CreativeModeTab> tab = getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        LUMBER = ItemOnlyEntrySet.builder(WoodType.class, "lumber",
                        getModItem("oak_lumber"), () -> VanillaWoodTypes.OAK,
                        w -> new Item(new Item.Properties())
                )
                .requiresChildren("planks")
                .addTexture(modRes("item/oak_lumber"), PaletteStrategies.MAIN_CHILD)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc","lumber"), Registries.ITEM)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(LUMBER);

        TWIG = SimpleEntrySet.builder(WoodType.class, "twig",
                        getModBlock("oak_twig"), () -> VanillaWoodTypes.OAK,
                        w -> GroundcoverBlock.twig(ExtendedProperties.of().strength(0.05F, 0.0F).sound(SoundType.WOOD).noCollission().flammableLikeWool())
                )
                .requiresChildren("log")
                .addTexture(modRes("item/oak_twig"), PaletteStrategies.MAIN_CHILD)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc", "twigs"), Registries.ITEM)
                .addTag(Tags.Items.RODS_WOODEN, Registries.ITEM)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(TWIG);

        LOG_FENCE = SimpleEntrySet.builder(WoodType.class, "log_fence",
                        getModBlock("oak_log_fence"), () -> VanillaWoodTypes.OAK,
                        w -> new TFCFenceBlock(ExtendedProperties.of().strength(2.0F, 3.0F).flammableLikeLogs())
                )
                .requiresChildren("planks", "log")
                .addTag(ItemTags.WOODEN_FENCES, Registries.ITEM)
                .addTag(BlockTags.WOODEN_FENCES, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(LOG_FENCE);

        VERTICAL_SUPPORT = SimpleEntrySet.builder(WoodType.class, "vertical_support",
                        getModBlock("oak_vertical_support"), () -> VanillaWoodTypes.OAK,
                        w -> new VerticalSupportBlock(ExtendedProperties.of().strength(1.0F).noOcclusion().flammableLikeLogs())
                )
                .requiresChildren("log")
                .addTag(TFCTags.Blocks.SUPPORT_BEAMS, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS, Registries.ITEM)
                .noItem()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(VERTICAL_SUPPORT);

        HORIZONTAL_SUPPORT = SimpleEntrySet.builder(WoodType.class, "horizontal_support",
                        getModBlock("oak_horizontal_support"), () -> VanillaWoodTypes.OAK,
                        w -> new HorizontalSupportBlock(ExtendedProperties.of().strength(1.0F).noOcclusion().flammableLikeLogs())
                )
                .requiresChildren("log")
                .addTag(TFCTags.Blocks.SUPPORT_BEAMS, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS, Registries.ITEM)
                .noItem()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(HORIZONTAL_SUPPORT);

        SUPPORT = ItemOnlyEntrySet.builder(WoodType.class, "support",
                        getModItem("oak_support"), () -> VanillaWoodTypes.OAK,
                        w -> new StandingAndWallBlockItem(VERTICAL_SUPPORT.blocks.get(w), HORIZONTAL_SUPPORT.blocks.get(w), new Item.Properties(), Direction.DOWN)
                )
                .requiresFromMap(VERTICAL_SUPPORT.blocks)
                .requiresFromMap(HORIZONTAL_SUPPORT.blocks)
                .requiresChildren("log")
                .addTag(TFCTags.Items.SUPPORT_BEAMS, Registries.ITEM)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SUPPORT);

        TOOL_RACK = SimpleEntrySet.builder(WoodType.class, "tool_rack",
                        getModBlock("oak_tool_rack"), () -> VanillaWoodTypes.OAK,
                        w -> new ToolRackBlock(ExtendedProperties.of().strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.TOOL_RACK))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.TOOL_RACKS, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(TOOL_RACK);

        LOOM = SimpleEntrySet.builder(WoodType.class, "loom",
                        getModBlock("oak_loom"), () -> VanillaWoodTypes.OAK,
                        w -> new TFCLoomBlock(ExtendedProperties.of().strength(2.5F).noOcclusion().flammableLikePlanks()
                                .blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick), getPlanksTextureId(w))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.LOOMS, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(LOOM);

        SLUICE = SimpleEntrySet.builder(WoodType.class, "sluice",
                        getModBlock("oak_sluice"), () -> VanillaWoodTypes.OAK,
                        w -> new SluiceBlock(ExtendedProperties.of().strength(3F).noOcclusion().flammableLikeLogs().blockEntity(TFCBlockEntities.SLUICE).serverTicks(SluiceBlockEntity::serverTick))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.SLUICES, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SLUICE);

        BARREL = SimpleEntrySet.builder(WoodType.class, "barrel",
                        getModBlock("oak_barrel"), () -> VanillaWoodTypes.OAK,
                        w -> new BarrelBlock(ExtendedProperties.of().strength(2.5f).flammableLikePlanks().noOcclusion()
                                .blockEntity(TFCBlockEntities.BARREL).serverTicks(BarrelBlockEntity::serverTick))
                )
                .requiresChildren("planks")
                .addTextureM(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "entity/chest/horse/oak_barrel"), ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "template/entity/chest/barrel_mask"))
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.BARRELS, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .addCustomItem((wood, block, itemProperties) -> new BarrelBlockItem(block, itemProperties))
                .copyParentDrop()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(BARREL);

        SCRIBING_TABLE = SimpleEntrySet.builder(WoodType.class, "scribing_table",
                        getModBlock("oak_scribing_table"), () -> VanillaWoodTypes.OAK,
                        w -> new ScribingTableBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammable(20, 30))
                )
                .requiresChildren("planks", "slab")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.SCRIBING_TABLES, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SCRIBING_TABLE);

        SEWING_TABLE = SimpleEntrySet.builder(WoodType.class, "sewing_table",
                        getModBlock("oak_sewing_table"), () -> VanillaWoodTypes.OAK,
                        w -> new SewingTableBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammable(20, 30))
                )
                .requiresChildren("planks", "log")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.SEWING_TABLES, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SEWING_TABLE);

        SHELF = SimpleEntrySet.builder(WoodType.class, "shelf",
                        getModBlock("oak_shelf"), () -> VanillaWoodTypes.OAK,
                        w -> new ShelfBlock(ExtendedProperties.of().noOcclusion().strength(2.5f).flammableLikePlanks().blockEntity(TFCBlockEntities.SHELF), false)
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHELF);

        AXLE = SimpleEntrySet.builder(WoodType.class, "axle",
                        () -> getModBlock("oak_axle").get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new AxleBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.AXLE),
                                () -> (WindmillBlock) WINDMILL.blocks.get(w), getPlanksTextureId(w))
                )
                .requiresChildren("stripped_log")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.AXLES, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();

        this.addEntry(AXLE);

        WINDMILL = SimpleEntrySet.builder(WoodType.class, "windmill",
                        () -> getModBlock("oak_windmill").get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new WindmillBlock(ExtendedProperties.of().strength(9f).noOcclusion().blockEntity(TFCBlockEntities.WINDMILL).ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick),
                                () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .noItem()
                .copyParentDrop()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(WINDMILL);

        BLADED_AXLE = SimpleEntrySet.builder(WoodType.class, "bladed_axle",
                        () -> getModBlock("oak_bladed_axle").get(), () -> VanillaWoodTypes.OAK,
                        w -> new BladedAxleBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.BLADED_AXLE), () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(BLADED_AXLE);

        ENCASED_AXLE = SimpleEntrySet.builder(WoodType.class, "encased_axle",
                        getModBlock("oak_encased_axle"), () -> VanillaWoodTypes.OAK,
                        w -> new EncasedAxleBlock(ExtendedProperties.of().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.ENCASED_AXLE))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(ENCASED_AXLE);

        CLUTCH = SimpleEntrySet.builder(WoodType.class, "clutch",
                        () -> getModBlock("oak_clutch").get(), () -> VanillaWoodTypes.OAK,
                        w -> new ClutchBlock(ExtendedProperties.of().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.CLUTCH), () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.CLUTCHES, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(CLUTCH);

        GEAR_BOX = SimpleEntrySet.builder(WoodType.class, "gear_box",
                        () -> getModBlock("oak_gear_box").get(), () -> VanillaWoodTypes.OAK,
                        w -> new GearBoxBlock(ExtendedProperties.of().strength(2f).noOcclusion().blockEntity(TFCBlockEntities.GEAR_BOX), () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.GEAR_BOXES, Registries.ITEM)
                .setRenderType(RenderLayer.CUTOUT)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(GEAR_BOX);

        WATER_WHEEL = SimpleEntrySet.builder(WoodType.class, "water_wheel",
                        () -> getModBlock("oak_water_wheel").get(), () -> VanillaWoodTypes.OAK,
                        w -> new WaterWheelBlock(ExtendedProperties.of().strength(9f).noOcclusion()
                                .blockEntity(TFCBlockEntities.WATER_WHEEL).ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick),
                                () -> (AxleBlock) AXLE.blocks.get(w),
                                ResourceLocation.fromNamespaceAndPath("everycomp", "textures/entity/tfc/" + w.getNamespace()+ "/" + w.getTypeName() + "_water_wheel.png"))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(TFCTags.Items.WATER_WHEELS, Registries.ITEM)
                .addTexture(modRes("item/oak_water_wheel"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("entity/oak_water_wheel"), PaletteStrategies.MAIN_CHILD)
                .setRenderType(RenderLayer.CUTOUT)
                .dropSelf()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(WATER_WHEEL);

        CRATE = SimpleEntrySet.builder(WoodType.class, "crate",
                        () -> getModBlock("oak_crate").get(), () -> VanillaWoodTypes.OAK,
                        w -> new CrateBlock(ExtendedProperties.of().sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).strength(9.0F).noOcclusion()
                                .blockEntity(TFCBlockEntities.CRATE))
                )
                .addTexture(modRes("block/oak_crate"), PaletteStrategies.MAIN_CHILD)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .copyParentDrop()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(CRATE);
    }

    private ResourceLocation getPlanksTextureId(WoodType wood) {
        String namespace = wood.getNamespace();
        String typeName = wood.getTypeName();

        if ("minecraft".equals(namespace)) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + typeName + "_planks");
        } else if ("tfc".equals(namespace)) {
            return ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/planks/" + typeName);
        } else {
            // Fallback for other mods — adjust if you support more
            FirmaCompat.LOGGER.warn("Unknown wood namespace for plank texture: {}", namespace);
            return ResourceLocation.fromNamespaceAndPath(namespace, "block/" + typeName + "_planks");
        }
    }
}