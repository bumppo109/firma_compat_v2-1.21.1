package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.block.CompatMetal;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.CastingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public interface ModCastingRecipe extends ModRecipes {
    default void castingRecipes()
    {
        add("netherite_ingot", new CastingRecipe(
                Ingredient.of(TFCItems.MOLDS.get(Metal.ItemType.INGOT)),
                SizedFluidIngredient.of(fluidOf(CompatMetal.NETHERITE), 100),
                ItemStackProvider.of(CompatMetal.NETHERITE.getMetalMaterial().ingot().get()),
                0.1f
        ));
        add("netherite_fire_ingot", new CastingRecipe(
                Ingredient.of(TFCItems.FIRE_INGOT_MOLD),
                SizedFluidIngredient.of(fluidOf(CompatMetal.NETHERITE), 100),
                ItemStackProvider.of(CompatMetal.NETHERITE.getMetalMaterial().ingot().get()),
                0.01f
        ));

        add("poor_netherite_ingot", new CastingRecipe(
                Ingredient.of(TFCItems.MOLDS.get(Metal.ItemType.INGOT)),
                SizedFluidIngredient.of(fluidOf(CompatMetal.SCRAP_NETHERITE), 100),
                ItemStackProvider.of(CompatMetal.SCRAP_NETHERITE.getMetalMaterial().ingot().get()),
                0.1f
        ));
        add("poor_netherite_fire_ingot", new CastingRecipe(
                Ingredient.of(TFCItems.FIRE_INGOT_MOLD),
                SizedFluidIngredient.of(fluidOf(CompatMetal.SCRAP_NETHERITE), 100),
                ItemStackProvider.of(CompatMetal.SCRAP_NETHERITE.getMetalMaterial().ingot().get()),
                0.01f
        ));
    }

    private void casting(String name, ItemLike item, CompatMetal metal, ItemLike result, int units, float chance)
    {
        add(name, new CastingRecipe(
                Ingredient.of(item),
                SizedFluidIngredient.of(fluidOf(metal), units),
                ItemStackProvider.of(result),
                chance
        ));
    }
}
