package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.block.ModDecorationBlockHolder;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidContentIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.MealModifier;
import net.dries007.tfc.util.Metal;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Optional;

import static net.dries007.tfc.util.DataGenerationHelpers.Builder;

public interface ModCraftingRecipes extends ModRecipes {

    default void craftingRecipes() {
        for (CompatWood wood : CompatWood.VALUES) {
            final var blockMap = ModBlocks.WOODS.get(wood);
            final var lumberMap = ModItems.LUMBER.get(wood);
            final var material = wood.woodMaterial();

            if (material.planks() != null) {
                Block planks = material.planks().get();

                recipe("from_planks")
                        .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                        .input(planks)
                        .damageInputs()
                        .shapeless(lumberMap.get(), 4);
                recipe()
                        .input('L', lumberMap.asItem())
                        .pattern("L L", "L L", "LLL")
                        .shaped(planks);
            }
        }
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output.
     */
    private Builder recipe()
    {
        return new Builder((name, r) -> {
            if (name != null) add(name, r);
            else add(r);
        });
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output, plus a suffix. The suffix should not start with an underscore.
     */
    private Builder recipe(String suffix)
    {
        return new Builder((name, r) -> {
            assert !suffix.startsWith("_") : "recipe(String suffix) shouldn't start with an '_', it is added for you!";
            assert name == null : "Cannot use a named recipe and recipe(String suffix) at the same time!";
            add(nameOf(r.getResultItem(lookup()).getItem()) + "_" + suffix, r);
        });
    }

    /**
     * @return A builder for a recipe that will replace a vanilla recipe at {@code name}. Checks for conflicts with removals or other replacements.
     */
    private Builder replace(String name)
    {
        return new Builder((name1, r) -> {
            assert name1 == null : "Cannot used replace() with a named recipe!";
            replace(name, r);
        });
    }

    private void makeTool(Item toolHead, Item tool) {
        recipe()
                .input('H', toolHead)
                .input('R', Tags.Items.RODS_WOODEN)
                .pattern("H  ", "R  ")
                .shaped(tool);
    }

    private void addDecorations(ItemLike input, ModDecorationBlockHolder output)
    {
        recipe()
                .input('#', input)
                .pattern("###")
                .shaped(output.slab(), 6);
        recipe()
                .input('#', input)
                .pattern("#  ", "## ", "###")
                .shaped(output.stair(), 8);
        recipe()
                .input('#', input)
                .pattern("###", "###")
                .shaped(output.wall(), 6);
    }

    private void addGrains(Food crop, Food grain, Food flour, Food dough, Food bread, Food sandwich, Food jamSandwich)
    {
        final var meal = new MealModifier(
                FoodData.ofFood(1f, 0.5f, 4.5f),
                List.of(
                        // For a 3-ingredient sandwich, average nutritional value is 0.75, matching salads
                        new MealModifier.MealPortion(Optional.of(Ingredient.of(TFCItems.FOOD.get(bread))), 0.675f, 0.5f, 0.5f),
                        new MealModifier.MealPortion(Optional.empty(), 0.8f, 0.8f, 0.8f)
                ));

        recipe()
                .input(notRotten(crop))
                .inputIsPrimary(TFCTags.Items.TOOLS_KNIFE)
                .damageInputs()
                .copyFood()
                .extraProduct(TFCItems.STRAW)
                .shapeless(TFCItems.FOOD.get(grain));

        // Non-jam sandwiches
        for (String pattern : List.of("SSS", "SS ", " SS", "S S", "S  ", " S ", "  S"))
        {
            recipe(pattern.replace(" ", "x").toLowerCase())
                    .input('K', TFCTags.Items.TOOLS_KNIFE)
                    .input('B', notRotten(bread))
                    .input('S', notRotten(Ingredient.of(TFCTags.Items.USABLE_IN_SANDWICH)))
                    .pattern("KB ", pattern, " B ")
                    .damageInputs()
                    .addOutputModifier(meal)
                    .shaped(TFCItems.FOOD.get(sandwich), 1);
        }

        // Two and three ingredient jam sandwiches
        for (String pattern : List.of("JSS", "SJS", "SSJ", "JS ", "SJ ", " JS", " SJ", "S J", "J S"))
        {
            String variant = "_jar";
            for (TagKey<Item> tag : List.of(TFCTags.Items.PRESERVES, TFCTags.Items.JAM))
            {
                recipe("" + pattern.replace(" ", "x").toLowerCase() + variant)
                        .input('K', TFCTags.Items.TOOLS_KNIFE)
                        .input('B', notRotten(bread))
                        .input('S', notRotten(Ingredient.of(TFCTags.Items.USABLE_IN_JAM_SANDWICH)))
                        .input('J', notRotten(Ingredient.of(tag)))
                        .pattern("KB ", pattern, " B ")
                        .damageInputs()
                        .addOutputModifier(meal)
                        .shaped(TFCItems.FOOD.get(jamSandwich));
                variant = "_jam";
            }
        }

        // One item jam sandwiches
        for (String pattern : List.of(" J ", "J  ", "  J"))
        {
            String variant = "_jar";
            for (TagKey<Item> tag : List.of(TFCTags.Items.PRESERVES, TFCTags.Items.JAM))
            {
                recipe("" + pattern.replace(" ", "x").toLowerCase() + variant)
                        .input('K', TFCTags.Items.TOOLS_KNIFE)
                        .input('B', notRotten(bread))
                        .input('J', notRotten(Ingredient.of(tag)))
                        .pattern("KB ", pattern, " B ")
                        .damageInputs()
                        .addOutputModifier(meal)
                        .shaped(TFCItems.FOOD.get(jamSandwich));
                variant = "_jam";
            }
        }

        for (int n = 1; n <= 8; n++)
            recipe("" + n)
                    .inputIsPrimary(FluidContentIngredient.of(Fluids.WATER, 100))
                    .input(notRotten(flour), n)
                    .copyOldestFood()
                    .shapeless(TFCItems.FOOD.get(dough), n);
    }

    private void addTools(Metal.ItemType input, Metal.ItemType output)
    {
        for (Metal metal : Metal.values())
            if (metal.allParts())
                recipe()
                        .input('S', Tags.Items.RODS_WOODEN)
                        .input('X', TFCItems.METAL_ITEMS.get(metal).get(input))
                        .pattern("X", "S")
                        .copyForging()
                        .source(0, 0)
                        .shaped(TFCItems.METAL_ITEMS.get(metal).get(output));
    }

    private void addTools(RockCategory.ItemType input, RockCategory.ItemType output)
    {
        for (RockCategory type : RockCategory.values())
            recipe()
                    .input('S', Tags.Items.RODS_WOODEN)
                    .input('X', TFCItems.ROCK_TOOLS.get(type).get(input))
                    .pattern("X", "S")
                    .shaped(TFCItems.ROCK_TOOLS.get(type).get(output));
    }

    private Ingredient notRotten(Food food)
    {
        return notRotten(Ingredient.of(TFCItems.FOOD.get(food)));
    }

    private Ingredient notRotten(Ingredient food)
    {
        return AndIngredient.of(food, NotRottenIngredient.INSTANCE);
    }
}