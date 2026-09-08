package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.block.CompatWood;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.data.Fuel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BuiltinFuels extends DataManagerProvider<Fuel> implements ModAccessors
{
    public BuiltinFuels(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Fuel.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(CompatWood.ACACIA, 650, 1000, 0.95f);
        add(CompatWood.BIRCH, 652, 1750, 0.95f);
        add(CompatWood.CHERRY, 650, 1200, 0.8f);
        add(CompatWood.DARK_OAK, 762, 2000, 0.8f);
        add(CompatWood.JUNGLE, 645, 1000, 0.95f);
        add(CompatWood.MANGROVE, 655, 1000, 0.95f);
        add(CompatWood.OAK, 728, 2250, 1f);
        add(CompatWood.SPRUCE, 608, 1500, 0.6f);

        add("bamboo_block", Ingredient.of(Blocks.BAMBOO_BLOCK), 800, 650, 0.8f);
        add("stripped_bamboo_block", Ingredient.of(Blocks.STRIPPED_BAMBOO_BLOCK), 800, 650, 0.8f);
        add("bamboo_planks", Ingredient.of(Blocks.BAMBOO_PLANKS), 360, 700, 0.8f);
        add("bamboo_mosaic", Ingredient.of(Blocks.BAMBOO_MOSAIC), 360, 700, 0.8f);
    }

    private void add(CompatWood wood, float temperature, int duration, float purity)
    {
        //final Map<CompatWood.BlockType, ModBlocks.Id<Block>> blocks = ModBlocks.WOODS.get(wood);
        add(wood.getSerializedName() + "_logs", Ingredient.of(vanillaLogsTagOf(Registries.ITEM, wood)), duration, temperature, purity);
        add(wood.getSerializedName() + "_planks", Ingredient.of(getVanillaPlank(wood)), (int) (duration * 0.4f), temperature + 50f, purity);
    }

    private void add(ItemLike item, int duration, float temperature, float purity)
    {
        add(nameOf(item), new Fuel(Ingredient.of(item), duration, temperature, purity));
    }

    private void add(String name, Ingredient item, int duration, float temperature, float purity)
    {
        add(name, new Fuel(item, duration, temperature, purity));
    }

    private <T> TagKey<T> vanillaLogsTagOf(ResourceKey<Registry<T>> registry, CompatWood wood)
    {
        String tagName = wood.getSerializedName() + "_logs";
        ResourceLocation id = ResourceLocation.withDefaultNamespace(tagName);
        return TagKey.create(registry, id);
    }

    private Item getVanillaPlank(CompatWood wood) {
        String name = wood.getSerializedName() + "_planks";
        ResourceLocation id = ResourceLocation.withDefaultNamespace(name);
        return BuiltInRegistries.ITEM.get(id);
    }
}