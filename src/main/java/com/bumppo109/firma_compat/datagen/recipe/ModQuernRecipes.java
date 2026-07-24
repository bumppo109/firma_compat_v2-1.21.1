package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.plant.Plant;
import net.dries007.tfc.common.recipes.QuernRecipe;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;

public interface ModQuernRecipes extends ModRecipes{

    default void quernRecipes(){
        add(Ingredient.of(ModTags.Items.MAKES_BLACK_DYE), Items.BLACK_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_WHITE_DYE), Items.WHITE_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_LIGHT_GRAY_DYE), Items.LIGHT_GRAY_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_GRAY_DYE), Items.GRAY_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_BROWN_DYE), Items.BROWN_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_RED_DYE), Items.RED_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_ORANGE_DYE), Items.ORANGE_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_YELLOW_DYE), Items.YELLOW_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_LIME_DYE), Items.LIME_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_GREEN_DYE), Items.GREEN_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_CYAN_DYE), Items.CYAN_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_LIGHT_BLUE_DYE), Items.LIGHT_BLUE_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_PURPLE_DYE), Items.PURPLE_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_MAGENTA_DYE), Items.MAGENTA_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_PINK_DYE), Items.PINK_DYE, 2);
        add(Ingredient.of(ModTags.Items.MAKES_BLUE_DYE), Items.BLUE_DYE, 2);
    }

    private Ingredient notRotten(ItemLike input)
    {
        return AndIngredient.of(Ingredient.of(input), NotRottenIngredient.INSTANCE);
    }

    /*
    private void addGrain(Food grain, Food flour)
    {
        add(notRotten(TFCItems.FOOD.get(grain)), ItemStackProvider.of(new ItemStack(TFCItems.FOOD.get(flour)), CopyFoodModifier.INSTANCE));
    }

     */

    private void addDye(ItemLike item, Plant... plants)
    {
        add(Ingredient.of(Arrays.stream(plants).map(TFCBlocks.PLANTS::get).toArray(ItemLike[]::new)), item, 2);
    }

    private void add(ItemLike input, ItemLike output, int count)
    {
        add(Ingredient.of(input), output, count);
    }

    private void add(String suffix, ItemLike input, ItemLike output, int count)
    {
        add(suffix, Ingredient.of(input), output, count);
    }

    private void add(Ingredient input, ItemLike output, int count)
    {
        add(input, ItemStackProvider.of(output, count));
    }

    private void add(String suffix, Ingredient input, ItemLike output, int count)
    {
        add(nameOf(output) + "_" + suffix, new QuernRecipe(input, ItemStackProvider.of(output, count)));
    }

    private void add(Ingredient input, ItemStackProvider output)
    {
        add(new QuernRecipe(input, output));
    }
}
