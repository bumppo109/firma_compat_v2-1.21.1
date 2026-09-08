package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.materials.food.Food;
import com.bumppo109.firma_compat.materials.food.FoodIngredient;
import com.bumppo109.firma_compat.materials.food.FoodIngredients;
import com.bumppo109.firma_compat.materials.food.Foods;
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
        for (FoodIngredient ingredient : FoodIngredients.values()) {
            int hunger = ingredient.hunger();
            float saturation = ingredient.saturation();
            float water = ingredient.water();
            float decayModifier = ingredient.decayModifier();
            float grain = ingredient.grain();
            float vegetables = ingredient.vegetables();
            float protein = ingredient.protein();
            float fruit = ingredient.fruit();
            float dairy = ingredient.dairy();

            FoodData foodData;

            if (hunger != 4) {
                foodData = addFood(saturation, water, decayModifier, grain, vegetables, protein, fruit, dairy);
            } else {
                foodData = addFood(hunger, saturation, water, decayModifier, grain, vegetables, protein, fruit, dairy);
            }

            add(ingredient.foodItem().get(), foodData);
        }

        for (Food food : Foods.values()) {

            int hunger = food.hunger();
            float saturation = food.saturation();
            float water = food.water();
            float decayModifier = food.decayModifier();
            float grain = 0.0f;
            float vegetables = 0.0f;
            float protein = 0.0f;
            float fruit = 0.0f;
            float dairy = 0.0f;

            float avgIngHunger = 0.0f;
            float ingCount = 0.0f;

            for (FoodIngredient ingredient : food.ingredients()) {
                grain += ingredient.grain();
                vegetables += ingredient.vegetables();
                protein += ingredient.protein();
                fruit += ingredient.fruit();
                dairy += ingredient.dairy();

                avgIngHunger += ingredient.hunger();
                ingCount += 1;
            }

            avgIngHunger /= ingCount;

            FoodData foodData = addFood(hunger, saturation, water, decayModifier,
                    calculateCompoundNutrient(grain, avgIngHunger, hunger),
                    calculateCompoundNutrient(vegetables, avgIngHunger, hunger),
                    calculateCompoundNutrient(protein, avgIngHunger, hunger),
                    calculateCompoundNutrient(fruit, avgIngHunger, hunger),
                    calculateCompoundNutrient(dairy, avgIngHunger, hunger)
            );

            add(food.item().get(), foodData);
        }
    }

    private float calculateCompoundNutrient(float nutrientTotal, float averageIngredientHunger, float foodhunger) {
        return Math.round((((nutrientTotal / averageIngredientHunger) * foodhunger) * 0.8f) * 100.0f) / 100.0f;
    }

    private FoodData addFood(float saturation, float water, float decayModifier, float grain, float vegetables, float protein, float fruit, float dairy) {
        return addFood(4, saturation, water, decayModifier, grain, vegetables, protein, fruit, dairy);
    }

    private FoodData addFood(int hunger, float saturation, float water, float decayModifier, float grain, float vegetables, float protein, float fruit, float dairy) {
        FoodData foodData = ofFood(
                hunger,
                saturation,
                water,
                decayModifier
        );

        if (grain != 0.0f) {
            foodData.grain(grain);
        }
        if (vegetables != 0.0f) {
            foodData.vegetables(vegetables);
        }
        if (protein != 0.0f) {
            foodData.protein(protein);
        }
        if (fruit != 0.0f) {
            foodData.fruit(fruit);
        }
        if (dairy != 0.0f) {
            foodData.dairy(dairy);
        }
        return foodData;
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