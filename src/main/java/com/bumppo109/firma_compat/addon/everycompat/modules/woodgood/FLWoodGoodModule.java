package com.bumppo109.firma_compat.addon.everycompat.modules.woodgood;

import com.bumppo109.firma_compat.FirmaCompat;
import com.eerussianguy.firmalife.common.FLTags;
import com.eerussianguy.firmalife.common.blockentities.BarrelPressBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import com.eerussianguy.firmalife.common.blockentities.KegBlockEntity;
import com.eerussianguy.firmalife.common.blocks.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.bumppo109.firma_compat.addon.everycompat.modules.woodgood.CompatWoodGoodModule.getChildRes;
import static net.mehvahdjukaar.every_compat.misc.UtilityTag.getATagOrCreateANew;

public final class FLWoodGoodModule extends EveryCompatModule {

    public final SimpleEntrySet<WoodType, Block> KEG;
    public final SimpleEntrySet<WoodType, Block> KEG_SUB;
    public final SimpleEntrySet<WoodType, Block> FOOD_SHELF;
    public final SimpleEntrySet<WoodType, Block> HANGER;
    public final SimpleEntrySet<WoodType, Block> JARBNET;
    public final SimpleEntrySet<WoodType, Block> WINE_SHELF;
    public final SimpleEntrySet<WoodType, Block> STOMPING_BARREL;
    public final SimpleEntrySet<WoodType, Block> BARREL_PRESS;

    public FLWoodGoodModule(String modId) {
        super(modId, "tfc");

        Supplier<CreativeModeTab> tab = getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        KEG_SUB = SimpleEntrySet.builder(WoodType.class, "keg_sub",
                        getModBlock("oak_keg_sub"), () -> VanillaWoodTypes.OAK,
                        w -> new KegSubBlock(ExtendedProperties.of().mapColor(w.getColor()).sound(SoundType.WOOD).noOcclusion().strength(10.0F)
                                .pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG_SUB))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS, Registries.ITEM)
                .noItem()
                .noDrops()
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(KEG_SUB);

        KEG = SimpleEntrySet.builder(WoodType.class, "keg",
                        getModBlock("oak_keg"), () -> VanillaWoodTypes.OAK,
                        w -> new KegCoreBlock(ExtendedProperties.of().mapColor(w.getColor()).sound(SoundType.WOOD).noOcclusion().strength(10.0F)
                                .pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG).serverTicks(KegBlockEntity::serverTick), () -> KEG_SUB.blocks.get(w))
                )
                .requiresFromMap(KEG_SUB.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.KEGS, Registries.BLOCK)
                .addTag(FLTags.Items.KEGS, Registries.ITEM)
                .dropSelf()
                .addTexture(modRes("block/big_barrel/oak_0"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_0_side"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_0_top"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_1"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_1_side"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_1_top"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_2"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_2_side"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_2_top"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_3"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_3_side"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("block/big_barrel/oak_3_top"), PaletteStrategies.MAIN_CHILD)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(KEG);

        FOOD_SHELF = SimpleEntrySet.builder(WoodType.class, "food_shelf",
                        getModBlock("oak_food_shelf"), () -> VanillaWoodTypes.OAK,
                        w -> new FoodShelfBlock(ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().blockEntity(FLBlockEntities.FOOD_SHELF).mapColor(w.getColor()))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.FOOD_SHELVES, Registries.BLOCK)
                .addTag(FLTags.Items.FOOD_SHELVES, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(FOOD_SHELF);

        HANGER = SimpleEntrySet.builder(WoodType.class, "hanger",
                        getModBlock("oak_hanger"), () -> VanillaWoodTypes.OAK,
                        w -> new HangerBlock(ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().blockEntity(FLBlockEntities.HANGER).mapColor(w.getColor()))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.HANGERS, Registries.BLOCK)
                .addTag(FLTags.Items.HANGERS, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(HANGER);

        JARBNET = SimpleEntrySet.builder(WoodType.class, "jarbnet",
                        getModBlock("oak_jarbnet"), () -> VanillaWoodTypes.OAK,
                        w -> new JarbnetBlock(ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().randomTicks().lightLevel((s) -> (Boolean)s.getValue(JarbnetBlock.LIT) ? 11 : 0).blockEntity(FLBlockEntities.JARBNET).mapColor(w.getColor()))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.JARBNETS, Registries.BLOCK)
                .addTag(FLTags.Items.JARBNETS, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(JARBNET);

        WINE_SHELF = SimpleEntrySet.builder(WoodType.class, "wine_shelf",
                        getModBlock("oak_wine_shelf"), () -> VanillaWoodTypes.OAK,
                        w -> new WineShelfBlock(ExtendedProperties.of().mapColor(w.getColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.WINE_SHELF))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.WINE_SHELVES, Registries.BLOCK)
                .addTag(FLTags.Items.WINE_SHELVES, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(WINE_SHELF);

        STOMPING_BARREL = SimpleEntrySet.builder(WoodType.class, "stomping_barrel",
                        getModBlock("oak_stomping_barrel"), () -> VanillaWoodTypes.OAK,
                        w -> new StompingBarrelBlock(ExtendedProperties.of().mapColor(w.getColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.STOMPING_BARREL))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.STOMPING_BARRELS, Registries.BLOCK)
                .addTag(FLTags.Items.STOMPING_BARRELS, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(STOMPING_BARREL);

        BARREL_PRESS = SimpleEntrySet.builder(WoodType.class, "barrel_press",
                        getModBlock("oak_barrel_press"), () -> VanillaWoodTypes.OAK,
                        w -> new BarrelPressBlock(ExtendedProperties.of().mapColor(w.getColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.BARREL_PRESS).ticks(BarrelPressBlockEntity::tick))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(FLTags.Blocks.BARREL_PRESSES, Registries.BLOCK)
                .addTag(FLTags.Items.BARREL_PRESSES, Registries.ITEM)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(BARREL_PRESS);
    }

    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for (var woodType : WoodTypeRegistry.INSTANCE) {
                if (woodType.getNamespace().equals("tfc") || woodType.getNamespace().equals("afc") || woodType.getNamespace().equals("minecraft")) continue;

                ResourceLocation logTag = getATagOrCreateANew("logs", "caps", woodType, sink, manager);
                ResourceLocation lumberRes = ResourceLocation.fromNamespaceAndPath("everycomp","tfc/" + woodType.getNamespace() + "/" + woodType.getTypeName() + "_lumber");
                ResourceLocation planksRes = getChildRes(woodType, "planks");
                ResourceLocation slabRes = getChildRes(woodType, "slab");
                ResourceLocation axleRes = woodType.hasChild("stripped_log") ? ResourceLocation.fromNamespaceAndPath("everycomp","tfc/" + woodType.getNamespace() + "/" + woodType.getTypeName() + "_axle") : ResourceLocation.fromNamespaceAndPath("","");
                ResourceLocation strippedLogRes = getChildRes(woodType, "stripped_log");;
                ResourceLocation logRes = getChildRes(woodType, "log");

                for (WoodGoodEntry entry : WoodGoodEntry.values()) {
                    if (!entry.isFirmaLife()) continue;

                    ResourceLocation oakEntryRes = Utils.getID(entry.oakItem().get());

                    try {
                        StaticResource recipeTemplate = StaticResource.getOrThrow(manager,
                                ResType.RECIPES.getPath(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"crafting/oak_" + entry.getSerializedName())));

                        sink.addSimilarJsonResource(
                                manager,
                                recipeTemplate,
                                text -> text
                                        .replace("firma_compat:oak_lumber", lumberRes.toString())
                                        .replace("minecraft:oak_planks", planksRes.toString())
                                        .replace("minecraft:oak_slab", slabRes.toString())
                                        .replace("minecraft:stripped_oak_log", strippedLogRes.toString())
                                        .replace("minecraft:oak_logs", logTag.toString())
                                        .replace("minecraft:oak_log", logRes.toString())
                                        .replace("firma_compat:oak_axle", axleRes.toString())
                                        .replace(oakEntryRes.toString(), "everycomp:tfc/" + woodType.getNamespace() + "/" + woodType.getTypeName() + "_" + entry.getSerializedName()),
                                path -> path.replace("oak",woodType.getNamespace() + "/" + woodType.getTypeName())
                        );
                    } catch (Exception e) {
                        FirmaCompat.LOGGER.debug("Failed to grab recipe for {}", "crafting/oak_" + entry.getSerializedName());
                    }
                }
            }
        });
    }
}
