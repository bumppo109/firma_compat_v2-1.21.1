package com.bumppo109.firma_compat.addon.everycompat.modules.woodgood;

import com.bumppo109.firma_compat.FirmaCompat;
import com.eerussianguy.firmalife.common.blockentities.BarrelPressBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import com.eerussianguy.firmalife.common.blockentities.KegBlockEntity;
import com.eerussianguy.firmalife.common.blocks.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
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
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        //TODO - limit stack size to 1
        KEG = SimpleEntrySet.builder(WoodType.class, "keg",
                        getModBlock("oak_keg"), () -> VanillaWoodTypes.OAK,
                        w -> new KegCoreBlock(ExtendedProperties.of().mapColor(w.getColor()).sound(SoundType.WOOD).noOcclusion().strength(10.0F)
                                .pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG).serverTicks(KegBlockEntity::serverTick), () -> KEG_SUB.blocks.get(w))
                )
                .requiresFromMap(KEG_SUB.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTag(modRes("compat_kegs"), Registries.ITEM, Registries.BLOCK)
                .addRecipe(modRes("crafting/firmalife/keg_oak"))
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
                .addTag(modRes("compat_food_shelves"), Registries.ITEM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
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
                .addTag(modRes("compat_hangers"), Registries.ITEM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addRecipe(modRes("crafting/firmalife/hanger_oak"))
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
                .addTag(modRes("compat_jarbnets"), Registries.ITEM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
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
                .addTag(modRes("compat_wine_shelves"), Registries.ITEM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addRecipe(modRes("crafting/firmalife/wine_shelf_oak"))
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
                .addTag(modRes("compat_stomping_barrels"), Registries.ITEM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
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
                .addTag(modRes("compat_barrel_presses"), Registries.ITEM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(BARREL_PRESS);
    }

    @Override
    public boolean isEntryAlreadyRegistered(String entrySetId, ResourceLocation blockId, BlockType blockType, Registry<?> registry) {
        return false;
    }

    @Override
    public void addDynamicClientResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {});
    }

    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for(WoodType wood : WoodTypeRegistry.INSTANCE){
                if(FOOD_SHELF.blocks.get(wood) != null){
                    generateFoodShelfRecipe(sink, wood, null);
                }
                if(JARBNET.blocks.get(wood) != null){
                    generateJarbnetRecipe(sink, wood, null);
                }
                if(STOMPING_BARREL.blocks.get(wood) != null){
                    generateStompBarrelRecipe(sink, wood, null);
                }
                if(BARREL_PRESS.blocks.get(wood) != null){
                    generateBarrelPressRecipe(sink, wood, null);
                }
            }
        });
    }

    public void generateFoodShelfRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        String lumberItem = wood.getTypeName().toLowerCase(Locale.ROOT) + "_lumber";
        String lumberNamespace = wood.getNamespace();
        Item outputItem = FOOD_SHELF.getItemOf(wood);
        Item planksItem = wood.getItemOfThis("planks");

        String lumberItemPath = FirmaCompat.MODID + "/" + lumberNamespace + "/" + lumberItem;


        assert outputItem != null;
        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", FirmaCompat.MODID + ":" + lumberItemPath);
        key.add("L", lumberKey);

        JsonObject planksKey = new JsonObject();
        assert planksItem != null;
        planksKey.addProperty("item", planksItem.toString());
        key.add("P", planksKey);

        recipe.add("key", key);

        JsonArray pattern = new JsonArray();
        pattern.add("PPP");
        pattern.add("LLL");
        pattern.add("PPP");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("id",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateJarbnetRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        String lumberItem = wood.getTypeName().toLowerCase(Locale.ROOT) + "_lumber";
        String lumberNamespace = wood.getNamespace();
        Item outputItem = JARBNET.getItemOf(wood);
        Item logItem = wood.getItemOfThis("log");

        String lumberItemPath = FirmaCompat.MODID + "/" + lumberNamespace + "/" + lumberItem;


        assert outputItem != null;
        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", FirmaCompat.MODID + ":" + lumberItemPath);
        key.add("L", lumberKey);

        JsonObject logKey = new JsonObject();
        assert logItem != null;
        logKey.addProperty("item", logItem.toString());
        key.add("P", logKey);

        JsonObject brassRodKey = new JsonObject();
        brassRodKey.addProperty("item", TFCItems.METAL_ITEMS.get(Metal.BRASS).get(Metal.ItemType.ROD).get().toString());
        key.add("X", brassRodKey);

        recipe.add("key", key);

        JsonArray pattern = new JsonArray();
        pattern.add("P  ");
        pattern.add("XLL");
        pattern.add("P  ");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("id",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateStompBarrelRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        String lumberItem = wood.getTypeName().toLowerCase(Locale.ROOT) + "_lumber";
        String lumberNamespace = wood.getNamespace();
        Item outputItem = STOMPING_BARREL.getItemOf(wood);

        String lumberItemPath = FirmaCompat.MODID + "/" + lumberNamespace + "/" + lumberItem;


        assert outputItem != null;
        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", FirmaCompat.MODID + ":" + lumberItemPath);
        key.add("L", lumberKey);

        JsonObject brassRodKey = new JsonObject();
        brassRodKey.addProperty("item", TFCItems.GLUE.get().toString());
        key.add("G", brassRodKey);

        recipe.add("key", key);

        JsonArray pattern = new JsonArray();
        pattern.add("LGL");
        pattern.add("LLL");
        pattern.add("GGG");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("id",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateBarrelPressRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        String stompBarrelItem = wood.getTypeName().toLowerCase(Locale.ROOT) + "_stomping_barrel";
        String woodNamespace = wood.getNamespace();
        Item outputItem = BARREL_PRESS.getItemOf(wood);

        String lumberItemPath = FirmaCompat.MODID + "/" + woodNamespace + "/" + stompBarrelItem;


        assert outputItem != null;
        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", FirmaCompat.MODID + ":" + lumberItemPath);
        key.add("L", lumberKey);

        JsonObject brassKey = new JsonObject();
        brassKey.addProperty("item", TFCItems.BRASS_MECHANISMS.get().toString());
        key.add("B", brassKey);

        JsonObject steelRodKey = new JsonObject();
        steelRodKey.addProperty("item", TFCItems.METAL_ITEMS.get(Metal.STEEL).get(Metal.ItemType.ROD).get().toString());
        key.add("S", steelRodKey);

        JsonObject steelSheetKey = new JsonObject();
        steelSheetKey.addProperty("item", TFCItems.METAL_ITEMS.get(Metal.STEEL).get(Metal.ItemType.SHEET).get().toString());
        key.add("H", steelSheetKey);

        recipe.add("key", key);

        JsonArray pattern = new JsonArray();
        pattern.add("LS ");
        pattern.add("HB ");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("id",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }
}
