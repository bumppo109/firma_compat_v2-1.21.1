package com.bumppo109.firma_compat.addon.everycompat.modules.stonezone;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.addon.firmalife.modules.FLVein;
import com.bumppo109.firma_compat.block.CompatRock;
import com.eerussianguy.firmalife.common.items.FLItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.util.collections.Weighted;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.api.StoneZoneModule;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FLStoneZoneModule extends StoneZoneModule {

    public final SimpleEntrySet<StoneType, Block> POOR_CHORMITE;
    public final SimpleEntrySet<StoneType, Block> NORMAL_CHROMITE;
    public final SimpleEntrySet<StoneType, Block> RICH_CHROMITE;

    public final Map<String, SimpleEntrySet<StoneType, Block>> ORE_ENTRY_SETS = new HashMap<>();

    public FLStoneZoneModule(String modId){
        super(modId,"tfc");

        Supplier<CreativeModeTab> tab = getTab(ResourceLocation.withDefaultNamespace("building_blocks"));

        POOR_CHORMITE = SimpleEntrySet.builder(StoneType.class, "chromite_ore", "poor",
                        getModBlock("poor_stone_chromite_ore"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                .copyParentDrop()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(POOR_CHORMITE);

        NORMAL_CHROMITE = SimpleEntrySet.builder(StoneType.class, "chromite_ore", "normal",
                        getModBlock("normal_stone_chromite_ore"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                .copyParentDrop()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(NORMAL_CHROMITE);

        RICH_CHROMITE = SimpleEntrySet.builder(StoneType.class, "chromite_ore", "rich",
                        getModBlock("rich_stone_chromite_ore"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                .copyParentDrop()
                .setRenderType(RenderLayer.CUTOUT)
                .setTab(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(RICH_CHROMITE);
    }

    @Override
    public void addDynamicClientResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                //poor
                String rawPath = Utils.getID(stone.stone).getPath();
                String rawNamespace = Utils.getID(stone.stone).getNamespace();
                String rawTexture = rawNamespace + ":block/" + rawPath;
                ResourceLocation poorOrePath = Utils.getID(POOR_CHORMITE.blocks.get(stone));

                JsonObject poorOreModelJson = new JsonObject();
                JsonObject poorTextureObj = new JsonObject();

                poorTextureObj.addProperty("all", rawTexture);
                poorTextureObj.addProperty("overlay", "firmalife:block/ore/poor_chromite");

                poorOreModelJson.addProperty("parent", "tfc:block/ore");
                poorOreModelJson.add("textures", poorTextureObj);
                sink.addJson(poorOrePath, poorOreModelJson, ResType.BLOCK_MODELS);
                //normal
                ResourceLocation normalOrePath = Utils.getID(NORMAL_CHROMITE.blocks.get(stone));

                JsonObject normalOreModelJson = new JsonObject();
                JsonObject normalTextureObj = new JsonObject();

                normalTextureObj.addProperty("all", rawTexture);
                normalTextureObj.addProperty("overlay", "firmalife:block/ore/normal_chromite");

                normalOreModelJson.addProperty("parent", "tfc:block/ore");
                normalOreModelJson.add("textures", normalTextureObj);
                sink.addJson(normalOrePath, normalOreModelJson, ResType.BLOCK_MODELS);
                //rich
                ResourceLocation richOrePath = Utils.getID(RICH_CHROMITE.blocks.get(stone));

                JsonObject richOreModelJson = new JsonObject();
                JsonObject richTextureObj = new JsonObject();

                richTextureObj.addProperty("all", rawTexture);
                richTextureObj.addProperty("overlay", "firmalife:block/ore/rich_chromite");

                richOreModelJson.addProperty("parent", "tfc:block/ore");
                richOreModelJson.add("textures", richTextureObj);
                sink.addJson(richOrePath, richOreModelJson, ResType.BLOCK_MODELS);
            }
        });
    }


    @Override
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {

        });
    }

    private JsonObject createRule(String targetId, List<Pair<BlockState, Double>> weights) {
        JsonObject rule = new JsonObject();

        JsonArray replaceArray = new JsonArray();
        replaceArray.add(targetId);
        rule.add("replace", replaceArray);

        JsonArray withArray = new JsonArray();
        for (Pair<BlockState, Double> pair : weights) {
            String oreId = BuiltInRegistries.BLOCK.getKey(pair.getFirst().getBlock()).toString();

            JsonObject entry = new JsonObject();
            entry.addProperty("block", oreId);

            double weight = pair.getSecond();
            if (Math.abs(weight - 1.0) > 0.001) {
                entry.addProperty("weight", weight);
            }

            withArray.add(entry);
        }

        rule.add("with", withArray);
        return rule;
    }

    private List<Pair<BlockState, Double>> getOreWeightsForStone(FLVein vein, StoneType stoneType) {
        String oreName = "chromite";

        // Graded ore
        SimpleEntrySet<StoneType, Block> poorSet   = ORE_ENTRY_SETS.get("poor_"   + oreName);
        SimpleEntrySet<StoneType, Block> normalSet = ORE_ENTRY_SETS.get("normal_" + oreName);
        SimpleEntrySet<StoneType, Block> richSet   = ORE_ENTRY_SETS.get("rich_"   + oreName);

        if (poorSet == null || normalSet == null || richSet == null) return null;

        Block poor   = poorSet.blocks.get(stoneType);
        Block normal = normalSet.blocks.get(stoneType);
        Block rich   = richSet.blocks.get(stoneType);

        if (poor == null || normal == null || rich == null ||
                poor == Blocks.AIR || normal == Blocks.AIR || rich == Blocks.AIR) {
            return null;
        }

        // Reuse your original weighting logic
        return switch (vein.gradedVeinClass) {  // ← note: you used String "normal"/"surface" here
            case SURFACE -> List.of(
                    Pair.of(poor.defaultBlockState(),   70.0),
                    Pair.of(normal.defaultBlockState(), 25.0),
                    Pair.of(rich.defaultBlockState(),    5.0)
            );
            case RICH -> List.of(
                    Pair.of(poor.defaultBlockState(),   15.0),
                    Pair.of(normal.defaultBlockState(), 25.0),
                    Pair.of(rich.defaultBlockState(),   60.0)
            );
            case NORMAL -> List.of(
                    Pair.of(poor.defaultBlockState(),   35.0),
                    Pair.of(normal.defaultBlockState(), 40.0),
                    Pair.of(rich.defaultBlockState(),   25.0));
        };
    }

    private JsonObject heightProviderJson(int minY, int maxY) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:uniform"); // or "absolute" if you prefer
        JsonObject min = new JsonObject();
        min.addProperty("absolute", minY);
        JsonObject max = new JsonObject();
        max.addProperty("absolute", maxY);
        obj.add("min_inclusive", min);
        obj.add("max_inclusive", max);
        return obj;
    }

    //Graded BlockMap
    private static Map<Block, IWeighted<BlockState>> buildReplacementMap(FLVein vein) {
        Map<Block, IWeighted<BlockState>> map = new HashMap<>();

        int added = 0;
        int skipped = 0;

        for (CompatRock rock : CompatRock.VALUES) {
            Block target = rock.rockMaterial().raw().base().get();
            if (target == null || target == Blocks.AIR) {
                skipped++;
                continue;
            }

            var oreMap = CompatFLBlocks.CHROMITE_ORES.get(rock);
            if (oreMap == null) {
                skipped++;
                continue;
            }
            /*
            var gradeMap = oreMap.get(vein.ore);
            if (gradeMap == null) {
                skipped++;
                continue;
            }

             */

            var poorId   = oreMap.get(Ore.Grade.POOR);
            var normalId = oreMap.get(Ore.Grade.NORMAL);
            var richId   = oreMap.get(Ore.Grade.RICH);

            if (poorId == null || normalId == null || richId == null) {
                skipped++;
                continue;
            }

            Block poor   = poorId.get();
            Block normal = normalId.get();
            Block rich   = richId.get();

            if (poor == null || normal == null || rich == null) {
                skipped++;
                continue;
            }

            List<Pair<BlockState, Double>> weights = switch(vein.gradedVeinClass){
                case SURFACE -> List.of(
                        Pair.of(poor.defaultBlockState(),   70.0),
                        Pair.of(normal.defaultBlockState(), 25.0),
                        Pair.of(rich.defaultBlockState(),    5.0));
                case NORMAL -> List.of(
                        Pair.of(poor.defaultBlockState(),   35.0),
                        Pair.of(normal.defaultBlockState(), 40.0),
                        Pair.of(rich.defaultBlockState(),   25.0));
                case RICH -> List.of(
                        Pair.of(poor.defaultBlockState(),   15.0),
                        Pair.of(normal.defaultBlockState(), 25.0),
                        Pair.of(rich.defaultBlockState(),   60.0));
            };

            map.put(target, new Weighted<>(weights));
            added++;
        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

    // Simple deterministic seed – same as TFC mostly uses
    private static long veinSeedFromName(String name) {
        long hash = 0;
        for (char c : name.toCharArray()) {
            hash = 31 * hash + c;
        }
        return hash & 0x7FFFFFFFFFFFFFFFL; // positive
    }
}
