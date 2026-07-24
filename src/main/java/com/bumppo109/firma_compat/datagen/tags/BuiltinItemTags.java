package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.datagen.ModAccessors;
import com.google.common.base.Preconditions;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.RockCategory;
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

import static net.dries007.tfc.common.TFCTags.Blocks.*;
import static net.dries007.tfc.common.TFCTags.Blocks.CAN_LANDSLIDE;
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
