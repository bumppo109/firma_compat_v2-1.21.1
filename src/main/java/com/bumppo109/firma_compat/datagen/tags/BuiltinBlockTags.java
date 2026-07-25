package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.datagen.ModAccessors;
import com.bumppo109.firma_compat.util.ModTags;
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

import static com.bumppo109.firma_compat.util.ModTags.Blocks.PREVENT_INTERACTION;
import static com.bumppo109.firma_compat.util.ModTags.Blocks.TWIGS;
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
    protected void addTags(HolderLookup.Provider provider) {
    //Util
        tag(PREVENT_INTERACTION)
                .add(Blocks.BLAST_FURNACE)
                .add(Blocks.SMOKER)
                .add(Blocks.COMPOSTER)
                .add(Blocks.CAMPFIRE)
                .add(Blocks.SOUL_CAMPFIRE)
                .add(Blocks.FURNACE);

        tag(CONSUMES_TOOL_DURABILITY)
                .add(Blocks.TALL_GRASS)
                .add(Blocks.SHORT_GRASS)
                .add(Blocks.FERN)
                .add(Blocks.LARGE_FERN)
        ;

        tag(HEAT_PASSABLE)
                .add(Blocks.COPPER_GRATE)
                .add(Blocks.EXPOSED_COPPER_GRATE)
                .add(Blocks.WEATHERED_COPPER_GRATE)
                .add(Blocks.OXIDIZED_COPPER_GRATE)
                .add(Blocks.WAXED_COPPER_GRATE)
                .add(Blocks.WAXED_EXPOSED_COPPER_GRATE)
                .add(Blocks.WAXED_WEATHERED_COPPER_GRATE)
                .add(Blocks.WAXED_OXIDIZED_COPPER_GRATE)
        ;

        tag(Tags.Blocks.CHESTS_WOODEN)
                .add(ModBlocks.COMPAT_CHEST.get())
                .add(ModBlocks.COMPAT_TRAPPED_CHEST.get());

        tag(PET_SITS_ON)
                .add(ModBlocks.COMPAT_CHEST.get())
                .add(ModBlocks.COMPAT_TRAPPED_CHEST.get());
    //Wood
        tag(ModTags.Blocks.CHISELED_BOOKSHELVES).add(Blocks.CHISELED_BOOKSHELF);
        for (Wood wood : Wood.VALUES) {
            tag(ModTags.Blocks.CHISELED_BOOKSHELVES)
                    .add(TFCBlocks.WOODS.get(wood).get(Wood.BlockType.BOOKSHELF).get());
            tag(ModTags.Blocks.TWIGS)
                    .add(TFCBlocks.WOODS.get(wood).get(Wood.BlockType.TWIG).get());
        }

        ModBlocks.WOODS.forEach((compatWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> tag(MINEABLE_WITH_AXE).add(blockId.get()));
        });
        addAllCompatWoods(CompatWood.BlockType.TWIG, TWIGS);
        addAllCompatWoods(CompatWood.BlockType.LOG_FENCE, FENCES);
        addAllCompatWoods(CompatWood.BlockType.HORIZONTAL_SUPPORT, SUPPORT_BEAMS);
        addAllCompatWoods(CompatWood.BlockType.VERTICAL_SUPPORT, SUPPORT_BEAMS);

    //Rock
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
            if (rock.canMakeAnvil()) {
                tag(MINEABLE_WITH_PICKAXE).add(ModBlocks.ROCK_ANVILS.get(rock).get());
                tag(ModTags.Blocks.MAKES_ROCK_ANVIL).add(rock.rockMaterial().raw().base().get());
            }
        }

        ModBlocks.TFC_ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                tag(MINEABLE_WITH_PICKAXE).add(blockId.get());
                tag(Tags.Blocks.COBBLESTONES_NORMAL).add(blockId.get());
            });
        });

        ModBlocks.ORES.forEach((rock, oreMap) -> {
            oreMap.forEach((ore, blockId) -> {
                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockId.get());
                tag(Tags.Blocks.ORES).add(blockId.get());
            });
        });

        // Graded ores
        ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> {
            oreMap.forEach((ore, gradeMap) -> {
                gradeMap.forEach((grade, blockId) -> {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockId.get());
                    tag(Tags.Blocks.ORES).add(blockId.get());
                });
            });
        });

        ModBlocks.AQUEDUCTS.forEach((compatRockSets, blockId) -> tag(MINEABLE_WITH_PICKAXE).add(blockId));

        tag(MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.RED_NETHER_BRICK_AQUEDUCT)
                .add(ModBlocks.PRISMARINE_BRICK_AQUEDUCT)
                .add(ModBlocks.QUARTZ_BRICK_AQUEDUCT)
                .add(ModBlocks.BRICK_AQUEDUCT)
        ;

        tag(STONES_RAW)
                .add(Blocks.STONE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.ANDESITE)
                .add(Blocks.DIORITE)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.GRANITE)
                .add(Blocks.TUFF)
                .add(Blocks.CALCITE)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.END_STONE)
                .add(Blocks.NETHERRACK);

    //Metal
        tag(LAMPS).add(ModBlocks.LANTERN.get());
        tag(MINEABLE_WITH_PICKAXE).add(ModBlocks.LANTERN.get());

        for(Metal metal : Metal.values()) {
            if(metal.allParts()){
                tag(LAMPS).add(ModBlocks.COMPAT_LANTERNS.get(metal).get());
                tag(MINEABLE_WITH_PICKAXE).add(ModBlocks.COMPAT_LANTERNS.get(metal).get());
            }
        }

    //Earthen
        tag(CAN_LANDSLIDE)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.ROOTED_DIRT)
                .add(Blocks.PODZOL)
                .add(Blocks.MYCELIUM)
                .add(Blocks.MUD)
                .add(Blocks.PACKED_MUD)
                .add(Blocks.RED_SAND)
                .add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.CLAY_DIRT.get())
                .add(ModBlocks.CLAY_PODZOL.get())
                .add(ModBlocks.CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.KAOLIN_CLAY_DIRT.get())
                .add(ModBlocks.KAOLIN_CLAY_PODZOL.get())
                .add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.COMPAT_FARMLAND.get())
                .add(Blocks.FARMLAND)
        ;

        tag(BlockTags.DIRT)
                .add(ModBlocks.CLAY_DIRT.get())
                .add(ModBlocks.CLAY_PODZOL.get())
                .add(ModBlocks.CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.KAOLIN_CLAY_DIRT.get())
                .add(ModBlocks.KAOLIN_CLAY_PODZOL.get())
                .add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.COMPAT_FARMLAND.get())
                .add(TFCBlocks.PEAT.get())
        ;

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.CLAY_DIRT.get())
                .add(ModBlocks.CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.KAOLIN_CLAY_DIRT.get())
                .add(ModBlocks.KAOLIN_CLAY_PODZOL.get())
                .add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.COMPAT_FARMLAND.get())
        ;

        tag(FARMLANDS).add(ModBlocks.COMPAT_FARMLAND.get());
        tag(NORMAL_FARMLAND).add(ModBlocks.COMPAT_FARMLAND.get());
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
