package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.loot.loot_modifiers.ItemRemoverLootModifier;
import com.bumppo109.firma_compat.loot.loot_modifiers.ItemSwapModifier;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;

import java.util.concurrent.CompletableFuture;

public class BuiltinLootModifier extends GlobalLootModifierProvider {

    // Reusable empty conditions
    private static final LootItemCondition[] NO_CONDITIONS = new LootItemCondition[0];

    public BuiltinLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, FirmaCompat.MODID);
    }

    @Override
    protected void start() {
        //chest
        swap(Items.CHEST, ModBlocks.COMPAT_CHEST.get().asItem());
        swap(Items.TRAPPED_CHEST, ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem());
        swap(Items.CHEST_MINECART, ModItems.COMPAT_CHEST_MINECART.get());
        //straw
        addStrawFromGrassesModifier();
        //food
        remove(Items.WHEAT_SEEDS);
        remove(Items.MELON_SEEDS);
        remove(Items.PUMPKIN_SEEDS);
        remove(Items.POTATO);
        remove(Items.CARROT);
        remove(Items.BEETROOT);
        remove(Items.APPLE);
        remove(Items.BAKED_POTATO);
        remove(Items.BREAD);
        remove(Items.CHICKEN);
        remove(Items.COOKED_CHICKEN);
        remove(Items.COD);
        remove(Items.COOKED_COD);
        remove(Items.MUTTON);
        remove(Items.COOKED_MUTTON);
        remove(Items.PORKCHOP);
        remove(Items.COOKED_PORKCHOP);
        remove(Items.RABBIT);
        remove(Items.COOKED_RABBIT);
        remove(Items.SALMON);
        remove(Items.COOKED_SALMON);
        remove(Items.MELON_SLICE);
        remove(Items.PUFFERFISH);
        remove(Items.BEEF);
        remove(Items.COOKED_BEEF);
        remove(Items.TROPICAL_FISH);

        //misc
        swap(Items.BUCKET, TFCItems.WOODEN_BUCKET.get());
        //Candle
        swap(Items.CANDLE, TFCBlocks.CANDLE.get().asItem());
        for(DyeColor color : DyeColor.values()){
            Item tfcCandle = TFCBlocks.DYED_CANDLE.get(color).get().asItem();
            Item vanillaCandle = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_candle"));

            swap(vanillaCandle, tfcCandle);
        }
        //Natural
        swap(Items.FARMLAND, Items.FARMLAND);
        //Ore
        swap(Items.DIAMOND, TFCItems.GEMS.get(Ore.DIAMOND).get().asItem());
        swap(Items.EMERALD, TFCItems.GEMS.get(Ore.EMERALD).get().asItem());
        swap(Items.LAPIS_LAZULI, TFCItems.GEMS.get(Ore.LAPIS_LAZULI).get().asItem());
        //Metal
        swap(Items.IRON_INGOT, TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.INGOT).get());
        swap(Items.IRON_BLOCK, TFCBlocks.METALS.get(Metal.WROUGHT_IRON).get(Metal.BlockType.BLOCK).get().asItem());
        swap(Items.GOLD_INGOT, TFCItems.METAL_ITEMS.get(Metal.GOLD).get(Metal.ItemType.INGOT).get());
        swap(Items.GOLD_BLOCK, TFCBlocks.METALS.get(Metal.GOLD).get(Metal.BlockType.BLOCK).get().asItem());
        swap(Items.COPPER_INGOT, TFCItems.METAL_ITEMS.get(Metal.COPPER).get(Metal.ItemType.INGOT).get());
        //Equipment

    }
    /**
     * Registers the loot modifier that adds straw drops from short/tall grass and ferns.
     */
    private void addStrawFromGrassesModifier() {
        // Build the AnyOf condition for grass/fern blocks
        AnyOfCondition.Builder conditionBuilder = AnyOfCondition.anyOf(
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SHORT_GRASS),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.FERN),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.LARGE_FERN)
        );

        // Build the array of conditions
        LootItemCondition[] conditions = new LootItemCondition[]{conditionBuilder.build()};

        // The loot table that defines the straw drop(s)
        ResourceLocation strawTableLocation = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "blocks/straw_modifier");
        ResourceKey<LootTable> strawTable =
                ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, strawTableLocation);

        // Register the add_table modifier
        add("straw_from_grasses", new AddTableLootModifier(conditions, strawTable));
    }

    /**
     * Registers a loot modifier that swaps all instances of one item with another.
     * The modifier name is automatically generated as:
     * "swap_[from_namespace]_[from_path]_to_[to_namespace]_[to_path]"
     * No conditions → always applies.
     *
     * @param from The item to replace
     * @param to   The replacement item
     */
    protected void swap(Item from, Item to) {
        ResourceLocation fromId = BuiltInRegistries.ITEM.getKey(from);
        ResourceLocation toId = BuiltInRegistries.ITEM.getKey(to);

        // Clean name: swap_minecraft_black_candle_to_tfc_candle_black
        String name = normalizeModifierName("swap_" +
                fromId.getNamespace() + "_" + fromId.getPath() +
                "_to_" +
                toId.getNamespace() + "_" + toId.getPath());

        add(name, new ItemSwapModifier(NO_CONDITIONS, from, to));
    }

    protected void swap(Item from, Item to, ICondition... conditions) {
        ResourceLocation fromId = BuiltInRegistries.ITEM.getKey(from);
        ResourceLocation toId = BuiltInRegistries.ITEM.getKey(to);

        // Clean name: swap_minecraft_black_candle_to_tfc_candle_black
        String name = normalizeModifierName("swap_" +
                fromId.getNamespace() + "_" + fromId.getPath() +
                "_to_" +
                toId.getNamespace() + "_" + toId.getPath());

        add(name, new ItemSwapModifier(NO_CONDITIONS, from, to), conditions);
    }
    /**
     * Registers a loot modifier that removes all instances of a specific item.
     * The modifier name is automatically generated as:
     * "remove_[namespace]_[path]"
     * No conditions → always removes.
     *
     * @param item The item to remove
     */
    protected void remove(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);

        // Clean name: remove_minecraft_bucket
        String name = normalizeModifierName("remove_" + itemId.getNamespace() + "_" + itemId.getPath());

        add(name, new ItemRemoverLootModifier(NO_CONDITIONS, item));
    }

    protected String normalizeModifierName(String rawName) {
        if (rawName == null || rawName.isEmpty()) {
            return "modifier_" + System.currentTimeMillis(); // Fallback
        }

        // Replace path separators and invalid chars
        String normalized = rawName
                .replace("/", "_")
                .replace("\\", "_")
                .replace(":", "_")
                .replace("*", "_")
                .replace("?", "_")
                .replace("\"", "_")
                .replace("<", "_")
                .replace(">", "_")
                .replace("|", "_");

        // Remove leading/trailing underscores and collapse multiples
        normalized = normalized.replaceAll("^_+|_+$", "")
                .replaceAll("_{2,}", "_");

        return normalized.isEmpty() ? "unnamed_modifier" : normalized;
    }
}