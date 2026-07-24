package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.dries007.tfc.common.recipes.JamPotRecipe;
import net.dries007.tfc.common.recipes.PotRecipe;
import net.dries007.tfc.common.recipes.SimplePotRecipe;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.LacksTraitIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.AddTraitModifier;
import net.dries007.tfc.common.recipes.outputs.CopyInputModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface ModPotRecipes extends ModRecipes{
    default void potRecipes()
    {
        //jam(Items.SWEET_BERRIES, ModItems.GLOW_BERRIES_JAR_UNSEALED.get(), ModItems.SWEET_BERRIES_JAR.get());
        //jam(Items.GLOW_BERRIES, ModItems.GLOW_BERRIES_JAR_UNSEALED.get(), ModItems.GLOW_BERRIES_JAR.get());
    }

    private void pot(ItemLike item, int count, Fluid outputFluid, int duration, float temperature, boolean usesAllFluid)
    {
        add(nameOf(outputFluid), new SimplePotRecipe(new PotRecipe(
                Collections.nCopies(count, Ingredient.of(item)),
                SizedFluidIngredient.of(Fluids.WATER, 1000),
                duration, temperature),
                new FluidStack(outputFluid, 1000), List.of(),
                usesAllFluid
        ));
    }

    private void jam(Item input, Item unsealedItem, Item sealedItem)
    {
        final String name = BuiltInRegistries.ITEM.getKey(input).getPath().toLowerCase(Locale.ROOT);
        for (int n = 2; n <= 4; n++)
            add("jam_" + name + "_" + n, new JamPotRecipe(new PotRecipe(
                    Helpers.immutableAdd(Collections.nCopies(n,
                                    AndIngredient.of(Ingredient.of(input), NotRottenIngredient.INSTANCE)),
                            Ingredient.of(TFCTags.Items.SWEETENERS)),
                    SizedFluidIngredient.of(Fluids.WATER, 100),
                    500, 300f
            ), new ItemStack(unsealedItem, n), new ItemStack(sealedItem, n), FirmaCompatHelpers.modIdentifier("block/" + name + "_jar")));
        for (int n = 1; n <= 5; n++)
        {
            final Ingredient ingredient = AndIngredient.of(Ingredient.of(new ItemStack(sealedItem)), LacksTraitIngredient.of(FoodTraits.CANNED), NotRottenIngredient.INSTANCE);
            add("jam_" + name + "_canning_" + n, new SimplePotRecipe(new PotRecipe(
                    Collections.nCopies(n, ingredient),
                    SizedFluidIngredient.of(Fluids.WATER, 100 * n),
                    300, 300f),
                    FluidStack.EMPTY,
                    Collections.nCopies(n, ItemStackProvider.of(CopyInputModifier.INSTANCE, AddTraitModifier.of(FoodTraits.CANNED))),
                    false
            ));
        }
    }
}
