package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.datagen.ModAccessors;
import com.google.common.base.Preconditions;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.IdHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.dries007.tfc.common.TFCTags.Blocks.*;
import static net.minecraft.tags.BlockTags.*;

public class BuiltinBlockTags extends TagsProvider<Block> implements ModAccessors
{
    private final ExistingFileHelper.IResourceType resourceType;

    public BuiltinBlockTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(event.getGenerator().getPackOutput(), Registries.BLOCK, lookup, FirmaCompat.MODID, event.getExistingFileHelper());
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        addAllCompatWoods(CompatWood.BlockType.LOG_FENCE, FENCES);
        addAllCompatWoods(CompatWood.BlockType.HORIZONTAL_SUPPORT, SUPPORT_BEAMS);
        addAllCompatWoods(CompatWood.BlockType.VERTICAL_SUPPORT, SUPPORT_BEAMS);

        for (CompatRock rock : CompatRock.VALUES) {
            for (CompatRock.BlockType blockType : CompatRock.BlockType.VALUES) {
                Block block = ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get();

                tag(MINEABLE_WITH_PICKAXE).add(block);
                if (blockType.equals(CompatRock.BlockType.HARDENED)) {
                    tag(STONES_HARDENED).add(block);
                }
                if (blockType.equals(CompatRock.BlockType.SPIKE)) {
                    tag(STONES_SPIKE).add(block);
                }
                if (blockType.equals(CompatRock.BlockType.LOOSE) || blockType.equals(CompatRock.BlockType.MOSSY_LOOSE)) {
                    tag(STONES_LOOSE).add(block);
                }
                if(blockType.equals(CompatRock.BlockType.COBBLE) || blockType.equals(CompatRock.BlockType.MOSSY_COBBLE)) {
                    tag(Tags.Blocks.COBBLESTONES_NORMAL).add(block);
                    tag(CAN_LANDSLIDE).add(block);
                }
                if(blockType.equals(CompatRock.BlockType.HARDENED_COBBLE) || blockType.equals(CompatRock.BlockType.MOSSY_HARDENED_COBBLE)) {
                    tag(Tags.Blocks.COBBLESTONES_NORMAL).add(block);
                }
            }
        }

        ModBlocks.TFC_ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                tag(MINEABLE_WITH_PICKAXE).add(blockId.get());
                tag(Tags.Blocks.COBBLESTONES_NORMAL).add(blockId.get());
            });
        });
    }

    private void addAllCompatWoods(CompatWood.BlockType type, TagKey<Block> tagKey) {
        if (tagKey == null) return;  // Skip if no tag for this type

        ModBlocks.WOODS.forEach((wood, map) -> {
            if (map.containsKey(type)) {
                tag(tagKey).add(map.get(type).get());
            }
        });
    }

    private void addAllTFCWoods(Wood.BlockType type, TagKey<Block> tagKey) {
        if (tagKey == null) return;  // Skip if no tag for this type

        TFCBlocks.WOODS.forEach((wood, map) -> {
            if (map.containsKey(type)) {
                tag(tagKey).add(map.get(type).get());
            }
        });
    }

    private void addAllCompatRock(CompatRock.BlockType type, TagKey<Block> tagKey) {
        if (tagKey == null) return;  // Skip if no tag for this type

        ModBlocks.ROCK_BLOCKS.forEach((rock, map) -> {
            if (map.containsKey(type)) {
                tag(tagKey).add(map.get(type).get());
            }
        });
    }

    @Override
    protected BlockTagAppender tag(TagKey<Block> tag)
    {
        return new BlockTagAppender(getOrCreateRawBuilder(tag));
    }

    @Override
    protected TagBuilder getOrCreateRawBuilder(TagKey<Block> tag)
    {
        if (existingFileHelper != null) existingFileHelper.trackGenerated(tag.location(), resourceType);
        return this.builders.computeIfAbsent(tag.location(), key -> new TagBuilder()
        {
            @Override
            public TagBuilder add(TagEntry entry)
            {
                Preconditions.checkArgument(!entry.getId().equals(BuiltInRegistries.BLOCK.getDefaultKey()), "Adding air to block tag");
                return super.add(entry);
            }
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    static class BlockTagAppender extends TagAppender<Block> implements ModAccessors
    {
        BlockTagAppender(TagBuilder builder)
        {
            super(builder);
        }

        BlockTagAppender add(Block... blocks)
        {
            for (Block block : blocks) add(key(block));
            return this;
        }

        BlockTagAppender add(Stream<? extends Supplier<? extends Block>> blocks)
        {
            blocks.forEach(b -> add(key(b.get())));
            return this;
        }

        @SafeVarargs
        final <T extends IdHolder<? extends Block>> BlockTagAppender add(T... blocks)
        {
            return add(Arrays.stream(blocks));
        }

        /*
        BlockTagAppender addEveryFL(Predicate<Block> predicate)
        {
            return add(ModBlocks.BLOCK.getEntries().stream().filter(e -> predicate.test(e.get())));
        }
        */

        BlockTagAppender add(Map<?, ? extends IdHolder<? extends Block>> blocks)
        {
            blocks.values().forEach(this::add);
            return this;
        }

        BlockTagAppender add2(Map<?, ? extends Map<?, ? extends IdHolder<? extends Block>>> blocks)
        {
            blocks.values().forEach(m -> m.values().forEach(this::add));
            return this;
        }

        <V> BlockTagAppender add2(Map<?, ? extends Map<?, V>> blocks, Function<V, ? extends IdHolder<? extends Block>> ap)
        {
            blocks.values().forEach(m -> m.values().forEach(v -> add(ap.apply(v))));
            return this;
        }

        BlockTagAppender add3(Map<?, ? extends Map<?, ? extends Map<?, ? extends IdHolder<? extends Block>>>> blocks)
        {
            blocks.values().forEach(m1 -> m1.values().forEach(m2 -> m2.values().forEach(this::add)));
            return this;
        }

        BlockTagAppender addAll(Map<?, DecorationBlockHolder> blocks)
        {
            blocks.values().forEach(h -> add(h.slab(), h.stair(), h.wall()));
            return this;
        }

        BlockTagAppender addAll(DecorationBlockHolder blocks)
        {
            add(blocks.slab(), blocks.stair(), blocks.wall());
            return this;
        }

        BlockTagAppender addAll2(Map<?, ? extends Map<?, DecorationBlockHolder>> blocks)
        {
            blocks.values().forEach(m -> m.values().forEach(h -> add(h.slab(), h.stair(), h.wall())));
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Block>> BlockTagAppender add(Map<T1, Map<T2, V>> blocks, T2 key)
        {
            return add(pivot(blocks, key));
        }

        <T, V extends IdHolder<? extends Block>> BlockTagAppender addOnly(Map<T, V> blocks, Predicate<T> key)
        {
            blocks.forEach((k, v) -> {if (key.test(k)) add(v);});
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Block>> BlockTagAppender addOnly2(Map<T1, Map<T2, V>> blocks, Predicate<T2> key)
        {
            blocks.values().forEach(m -> addOnly(m, key));
            return this;
        }

        @SafeVarargs
        @SuppressWarnings("unchecked")
        final <K> BlockTagAppender addTags(Function<K, TagKey<Block>> apply, K... values)
        {
            return addTags(Arrays.stream(values).map(apply).toArray(TagKey[]::new));
        }

        @Override
        public BlockTagAppender addTag(TagKey<Block> tag)
        {
            return (BlockTagAppender) super.addTag(tag);
        }

        @Override
        @SafeVarargs
        public final BlockTagAppender addTags(TagKey<Block>... values)
        {
            return (BlockTagAppender) super.addTags(values);
        }

        BlockTagAppender remove(Block... blocks)
        {
            for (Block block : blocks) remove(key(block));
            return this;
        }

        private ResourceKey<Block> key(Block block)
        {
            return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
        }
    }
}
