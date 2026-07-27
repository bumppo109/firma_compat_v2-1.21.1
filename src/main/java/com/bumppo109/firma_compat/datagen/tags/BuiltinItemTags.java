package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.firmalife.FLCompat;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.datagen.ModAccessors;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.util.ModTags;
import com.eerussianguy.firmalife.common.FLTags;
import com.google.common.base.Preconditions;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.IdHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.bumppo109.firma_compat.util.ModTags.Items.*;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_BLUE_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_BROWN_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_CYAN_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_GREEN_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_LIGHT_BLUE_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_LIME_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_MAGENTA_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_ORANGE_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_PINK_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_RED_DYE;
import static com.bumppo109.firma_compat.util.ModTags.Items.MAKES_YELLOW_DYE;
import static com.eerussianguy.firmalife.common.FLTags.Blocks.*;
import static com.eerussianguy.firmalife.common.FLTags.Blocks.KEGS;
import static net.dries007.tfc.common.TFCTags.Blocks.*;
import static net.dries007.tfc.common.TFCTags.Blocks.CAN_LANDSLIDE;
import static net.dries007.tfc.common.TFCTags.Items.*;
import static net.dries007.tfc.common.TFCTags.Items.LAMPS;
import static net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE;
import static net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE;

public class BuiltinItemTags extends TagsProvider<Item> implements ModAccessors
{
    private final ExistingFileHelper.IResourceType resourceType;
    private final CompletableFuture<TagLookup<Block>> blockTags;
    private final Map<TagKey<Block>, TagKey<Item>> tagsToCopy = new HashMap<>();

    public BuiltinItemTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags)
    {
        super(event.getGenerator().getPackOutput(), Registries.ITEM, lookup, FirmaCompat.MODID, event.getExistingFileHelper());
        this.blockTags = blockTags;
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
    //Util
        tag(PREVENT_INTERACTION)
                .add(Items.WHEAT_SEEDS)
                .add(Items.CARROT)
                .add(Items.POTATO)
                .add(Items.BEETROOT)
                .add(Items.SUGAR_CANE);

    //Wood
        tag(Tags.Items.CHESTS_WOODEN)
                .add(ModBlocks.COMPAT_CHEST.get().asItem())
                .add(ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem());

        tag(MINECARTS)
                .add(ModItems.COMPAT_CHEST_MINECART);

        tag(CARRIED_BY_HORSE)
                .add(ModBlocks.COMPAT_CHEST.get().asItem())
                .add(ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem());

        tag(TANNIN_LOGS)
                .add(Blocks.BIRCH_LOG.asItem())
                .add(Blocks.BIRCH_WOOD.asItem())
                .add(Blocks.OAK_LOG.asItem())
                .add(Blocks.OAK_WOOD.asItem())
                .add(Blocks.DARK_OAK_LOG.asItem())
                .add(Blocks.DARK_OAK_WOOD.asItem())
        ;

        addAllCompatWoods(CompatWood.BlockType.TOOL_RACK, TFCTags.Items.TOOL_RACKS);
        addAllCompatWoods(CompatWood.BlockType.TWIG, TFCTags.Items.TWIGS);
        addAllCompatWoods(CompatWood.BlockType.LOOM, TFCTags.Items.LOOMS);
        addAllCompatWoods(CompatWood.BlockType.SLUICE, TFCTags.Items.SLUICES);
        addAllCompatWoods(CompatWood.BlockType.BARREL, TFCTags.Items.BARRELS);
        addAllCompatWoods(CompatWood.BlockType.SCRIBING_TABLE, TFCTags.Items.SCRIBING_TABLES);
        addAllCompatWoods(CompatWood.BlockType.SEWING_TABLE, TFCTags.Items.SEWING_TABLES);
        addAllCompatWoods(CompatWood.BlockType.AXLE, TFCTags.Items.AXLES);
        addAllCompatWoods(CompatWood.BlockType.GEAR_BOX, TFCTags.Items.GEAR_BOXES);
        addAllCompatWoods(CompatWood.BlockType.CLUTCH, TFCTags.Items.CLUTCHES);
        addAllCompatWoods(CompatWood.BlockType.WATER_WHEEL, TFCTags.Items.WATER_WHEELS);
        addAllCompatWoods(CompatWood.BlockType.LOG_FENCE, Tags.Items.FENCES_WOODEN);
        ModItems.SUPPORTS.forEach((compatWood, itemId) -> tag(TFCTags.Items.SUPPORT_BEAMS).add(itemId.get()));
        ModItems.LUMBER.forEach((compatWood, itemId) -> tag(TFCTags.Items.LUMBER).add(itemId.get()));


        for (CompatRock rock : CompatRock.VALUES) {
            for (CompatRock.BlockType blockType : CompatRock.BlockType.VALUES) {
                Item item = ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get().asItem();

                if (blockType.equals(CompatRock.BlockType.HARDENED)) {
                    tag(TFCTags.Items.STONES_HARDENED).add(item);
                }
                if (blockType.equals(CompatRock.BlockType.LOOSE) || blockType.equals(CompatRock.BlockType.MOSSY_LOOSE)) {
                    tag(TFCTags.Items.STONES_LOOSE).add(item);
                    tag(TFCTags.Items.STONES_LOOSE_CATEGORY.get(RockCategory.METAMORPHIC)).add(item);
                }
                if(blockType.equals(CompatRock.BlockType.COBBLE) || blockType.equals(CompatRock.BlockType.MOSSY_COBBLE)
                    || blockType.equals(CompatRock.BlockType.HARDENED_COBBLE) || blockType.equals(CompatRock.BlockType.MOSSY_HARDENED_COBBLE)) {
                    tag(Tags.Items.COBBLESTONES_NORMAL).add(item);
                }
            }
        }

    //Metal
        for (CompatMetal metal : CompatMetal.values()) {
            if (metal.isDummy()) continue;
            metalTag(metal, CompatMetal.ItemType.DOUBLE_INGOT, TFCTags.Items.DOUBLE_INGOTS);
            metalTag(metal, CompatMetal.ItemType.SHEET, TFCTags.Items.DOUBLE_INGOTS);
            metalTag(metal, CompatMetal.ItemType.DOUBLE_SHEET, TFCTags.Items.DOUBLE_INGOTS);
            metalTag(metal, CompatMetal.ItemType.ROD, Tags.Items.RODS);

            if(metal.allParts()) {
                metalTag(metal, CompatMetal.ItemType.JAVELIN, Tags.Items.TOOLS_SPEAR);
                metalTag(metal, CompatMetal.ItemType.JAVELIN, Tags.Items.RANGED_WEAPON_TOOLS);
                metalTag(metal, CompatMetal.ItemType.MACE, Tags.Items.TOOLS_MACE);
                metalTag(metal, CompatMetal.ItemType.HAMMER, TFCTags.Items.TOOLS_HAMMER);
                metalTag(metal, CompatMetal.ItemType.SAW, TFCTags.Items.TOOLS_SAW);
                metalTag(metal, CompatMetal.ItemType.SCYTHE, TFCTags.Items.TOOLS_SCYTHE);
                metalTag(metal, CompatMetal.ItemType.PROPICK, TFCTags.Items.TOOLS_PROPICK);
                metalTag(metal, CompatMetal.ItemType.KNIFE, TFCTags.Items.TOOLS_KNIFE);
                metalTag(metal, CompatMetal.ItemType.CHISEL, TFCTags.Items.TOOLS_CHISEL);
            }
        }

        tag(ModTags.Items.TOOLS_NETHERITE)
                .add(Items.NETHERITE_PICKAXE)
                .add(Items.NETHERITE_AXE)
                .add(Items.NETHERITE_SHOVEL)
                .add(Items.NETHERITE_HOE)
                .add(Items.NETHERITE_SWORD)
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.TUYERE))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.PROPICK))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.CHISEL))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.HAMMER))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SAW))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.JAVELIN))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.MACE))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.KNIFE))
                .add(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SCYTHE))
        ;

        tag(LAMPS).add(ModBlocks.LANTERN.get().asItem());

        for(Metal metal : Metal.values()) {
            if(metal.allParts()){
                tag(LAMPS).add(ModBlocks.COMPAT_LANTERNS.get(metal).get().asItem());
            }
        }

    //Earthen
        tag(ORE_DEPOSITS)
                .add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get().asItem())
                .add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get().asItem())
                .add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get().asItem())
                .add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get().asItem())
        ;

        tag(MUD_BRICK_ITEMS).add(ModItems.MUD_BRICK);

    //Dye
        tag(MAKES_WHITE_DYE)
                .add(Items.LILY_OF_THE_VALLEY)
                .add(Items.WHITE_TULIP)
                .add(Items.BONE_MEAL);

        tag(MAKES_BLACK_DYE)
                .add(Items.WITHER_ROSE);

        tag(MAKES_BROWN_DYE)
                .add(Items.COCOA_BEANS);

        tag(MAKES_RED_DYE)
                .add(Items.ROSE_BUSH)
                .add(Items.BEETROOT)
                .add(Items.RED_TULIP)
                .add(Items.POPPY);

        tag(MAKES_ORANGE_DYE)
                .add(Items.TORCHFLOWER)
                .add(Items.ORANGE_TULIP);

        tag(MAKES_YELLOW_DYE)
                .add(Items.SUNFLOWER)
                .add(Items.DANDELION);

        tag(MAKES_LIME_DYE)
                .add(Items.SEA_PICKLE);

        tag(MAKES_GREEN_DYE)
                .add(Items.CACTUS);

        tag(MAKES_CYAN_DYE)
                .add(Items.PITCHER_PLANT);

        tag(MAKES_LIGHT_BLUE_DYE)
                .add(Items.BLUE_ORCHID);

        tag(MAKES_BLUE_DYE)
                .add(Items.CORNFLOWER);

        tag(MAKES_MAGENTA_DYE)
                .add(Items.LILAC)
                .add(Items.ALLIUM);

        tag(MAKES_PINK_DYE)
                .add(Items.PEONY)
                .add(Items.PINK_PETALS)
                .add(Items.PINK_TULIP);

    //Compostables
        tag(COMPOST_BROWNS_LOW)
                .add(Items.MOSS_BLOCK)
                .add(Items.MOSS_CARPET)
                .add(Items.VINE);

        tag(COMPOST_GREENS_HIGH)
                .add(Items.PUMPKIN)
                .add(Items.MELON);

        tag(COMPOST_GREENS_LOW)
                .addTag(ItemTags.FLOWERS)
                .addTag(ItemTags.SAPLINGS)
                .addTag(Tags.Items.MUSHROOMS)
                .add(Items.SHORT_GRASS)
                .add(Items.TALL_GRASS)
                .add(Items.FERN)
                .add(Items.LARGE_FERN)
                .add(Items.BIG_DRIPLEAF)
                .add(Items.SMALL_DRIPLEAF)
                .add(Items.LILY_PAD);

    //Misc
        tag(Tags.Items.TOOLS_IGNITER).add(TFCItems.FLINT_AND_PYRITE);

    // ============== Firmalife ============
        for (CompatWood wood : CompatWood.VALUES) {
            ResourceLocation foodShelf = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.FOOD_SHELVES.get(wood).get());
            ResourceLocation hanger = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.HANGERS.get(wood).get());
            ResourceLocation jarbnet = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.JARBNETS.get(wood).get());
            ResourceLocation keg = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.KEGS.get(wood).get());
            ResourceLocation stompBarrel = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.STOMPING_BARRELS.get(wood).get());
            ResourceLocation barrelPress = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.BARREL_PRESSES.get(wood).get());
            ResourceLocation wineShelf = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.WINE_SHELVES.get(wood).get());

            tag(FLTags.Items.FOOD_SHELVES).addOptional(foodShelf);
            tag(FLTags.Items.WINE_SHELVES).addOptional(wineShelf);
            tag(FLTags.Items.HANGERS).addOptional(hanger);
            tag(FLTags.Items.JARBNETS).addOptional(jarbnet);
            tag(FLTags.Items.STOMPING_BARRELS).addOptional(stompBarrel);
            tag(FLTags.Items.BARREL_PRESSES).addOptional(barrelPress);
            tag(FLTags.Items.KEGS).addOptional(keg);
        }
    }

    private void metalTag(CompatMetal metal, CompatMetal.ItemType type, TagKey<Item> baseTag)
    {
        final TagKey<Item> commonTag = commonTagOf(metal, type);
        tag(commonTag).add(ModItems.METAL_ITEMS.get(metal).get(type));
        tag(baseTag).addTag(commonTag);
    }


    private void addAllCompatWoods(CompatWood.BlockType type, TagKey<Item> tagKey) {
        ModBlocks.WOODS.forEach((wood, map) -> {
            if (map.containsKey(type)) {
                tag(tagKey).add(map.get(type).get().asItem());
            }
        });
    }
    private void addAllCompatRocks(CompatRock.BlockType type, TagKey<Item> tagKey) {
        ModBlocks.ROCK_BLOCKS.forEach((rock, map) -> {
            if (map.containsKey(type)) {
                tag(tagKey).add(map.get(type).get().asItem());
            }
        });
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> createContentsProvider()
    {
        return super.createContentsProvider().thenCombine(blockTags, (lookup, tagLookup) -> {
            tagsToCopy.forEach((blockTag, itemTag) -> {
                tagLookup.apply(blockTag)
                        .map(TagBuilder::build)
                        .filter(e -> !e.isEmpty())
                        .ifPresentOrElse(content -> {
                            // N.B. Only copy the tag if the original is non-empty. We do this since we copy all vanilla tags by default,
                            // and we only really want to include the ones that we are adding to
                            final TagBuilder builder = getOrCreateRawBuilder(itemTag);
                            content.forEach(builder::add);
                        }, () -> {
                            // Throw an error if we try and copy a TFC tag that didn't exist
                            if (blockTag.location().getNamespace().equals("tfc")) throw new IllegalArgumentException("Copying empty or missing tag " + blockTag.location());
                        });
            });
            return lookup;
        });
    }

    @Override
    protected ItemTagAppender tag(TagKey<Item> tag)
    {
        return new ItemTagAppender(getOrCreateRawBuilder(tag));
    }

    @Override
    protected TagBuilder getOrCreateRawBuilder(TagKey<Item> tag)
    {
        if (existingFileHelper != null) existingFileHelper.trackGenerated(tag.location(), resourceType);
        return this.builders.computeIfAbsent(tag.location(), key -> new TagBuilder()
        {
            @Override
            public TagBuilder add(TagEntry entry)
            {
                Preconditions.checkArgument(!entry.getId().equals(BuiltInRegistries.ITEM.getDefaultKey()), "Adding air to item tag");
                return super.add(entry);
            }
        });
    }

    private void copy(TagKey<Block> blockTag, TagKey<Item> itemTag)
    {
        this.tagsToCopy.put(blockTag, itemTag);
    }

    static class ItemTagAppender extends TagAppender<Item> implements ModAccessors
    {
        ItemTagAppender(TagBuilder builder)
        {
            super(builder);
        }

        ItemTagAppender add(Item... items)
        {
            for (Item item : items) add(key(item));
            return this;
        }

        ItemTagAppender add(Stream<? extends Supplier<? extends Item>> items)
        {
            items.forEach(b -> add(key(b.get())));
            return this;
        }

        @SafeVarargs
        final <T extends IdHolder<? extends Item>> ItemTagAppender add(T... items)
        {
            return add(Arrays.stream(items));
        }

        /*
        ItemTagAppender addEveryFL(Predicate<Item> predicate)
        {
            return add(FLItems.ITEM.getEntries().stream().filter(e -> predicate.test(e.get())));
        }

         */

        ItemTagAppender add(Map<?, ? extends IdHolder<? extends Item>> items)
        {
            items.values().forEach(this::add);
            return this;
        }

        ItemTagAppender add2(Map<?, ? extends Map<?, ? extends IdHolder<? extends Item>>> items)
        {
            items.values().forEach(m -> m.values().forEach(this::add));
            return this;
        }

        <V> ItemTagAppender add2(Map<?, ? extends Map<?, V>> items, Function<V, ? extends IdHolder<? extends Item>> ap)
        {
            items.values().forEach(m -> m.values().forEach(v -> add(ap.apply(v))));
            return this;
        }

        ItemTagAppender add3(Map<?, ? extends Map<?, ? extends Map<?, ? extends IdHolder<? extends Item>>>> items)
        {
            items.values().forEach(m1 -> m1.values().forEach(m2 -> m2.values().forEach(this::add)));
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Item>> ItemTagAppender add(Map<T1, Map<T2, V>> items, T2 key)
        {
            return add(pivot(items, key));
        }

        <T, V extends IdHolder<? extends Item>> ItemTagAppender addOnly(Map<T, V> items, Predicate<T> key)
        {
            items.forEach((k, v) -> {if (key.test(k)) add(v);});
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Item>> ItemTagAppender addOnly2(Map<T1, Map<T2, V>> items, Predicate<T2> key)
        {
            items.values().forEach(m -> addOnly(m, key));
            return this;
        }

        @SafeVarargs
        @SuppressWarnings("unchecked")
        final <K> ItemTagAppender addTags(Function<K, TagKey<Item>> apply, K... values)
        {
            return addTags(Arrays.stream(values).map(apply).toArray(TagKey[]::new));
        }

        @Override
        public ItemTagAppender addTag(TagKey<Item> tag)
        {
            return (ItemTagAppender) super.addTag(tag);
        }

        @Override
        @SafeVarargs
        public final ItemTagAppender addTags(TagKey<Item>... values)
        {
            return (ItemTagAppender) super.addTags(values);
        }

        ItemTagAppender remove(Item... items)
        {
            for (Item item : items) remove(key(item));
            return this;
        }

        private ResourceKey<Item> key(Item item)
        {
            return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
        }
    }
}
