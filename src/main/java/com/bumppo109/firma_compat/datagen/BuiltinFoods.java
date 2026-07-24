package com.bumppo109.firma_compat.datagen;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

import static net.dries007.tfc.common.component.food.FoodData.ofFood;

public class BuiltinFoods extends DataManagerProvider<FoodDefinition> implements ModAccessors
{
    public BuiltinFoods(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(FoodCapability.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(Items.SWEET_BERRIES, ofFood(0.3f, 5, 4.9f).fruit(0.8f));
        add(Items.GLOW_BERRIES, ofFood(0.3f, 5, 4.9f).fruit(0.8f));
        add(Items.DRIED_KELP, ofFood(0f, 0, 2.5f).vegetables(0.5f));
        add(Items.BROWN_MUSHROOM, ofFood(0f, 0, 4.9f).protein(0.5f));
        add(Items.RED_MUSHROOM, ofFood(0f, 0, 4.9f).protein(0.5f));
        add(Items.MUSHROOM_STEW, ofFood(2f, 10, 4.0f).protein(0.8f));
        add(Items.RABBIT_STEW, ofFood(2f, 10, 4.0f).protein(1.5f).vegetables(1.6f));
        add(Items.BEETROOT_SOUP, ofFood(2f, 10, 4.0f).vegetables(2.4f));
    }

    private void add(ItemLike item, FoodData food)
    {
        add(item, food, true);
    }

    private void add(ItemLike item, FoodData food, boolean edible)
    {
        add(nameOf(item).replace("food/", ""), new FoodDefinition(Ingredient.of(item), food, edible));
    }

    private void add(TagKey<Item> tag, FoodData food, boolean edible)
    {
        add(tag.location().getPath().replace("foods/", ""), new FoodDefinition(Ingredient.of(tag), food, edible));
    }

}
