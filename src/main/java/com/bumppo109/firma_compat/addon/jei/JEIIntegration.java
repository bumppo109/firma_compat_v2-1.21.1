package com.bumppo109.firma_compat.addon.jei;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipe;
import com.bumppo109.firma_compat.recipe.ModRecipes;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

@JeiPlugin
public final class JEIIntegration implements IModPlugin {

    public static final IIngredientType<FluidStack> FLUID_STACK =
            NeoForgeTypes.FLUID_STACK;

    public static final RecipeType<
            RecipeHolder<FluidBrewingRecipe>
            > FLUID_BREWING =
            RecipeType.createRecipeHolderType(
                    ResourceLocation.fromNamespaceAndPath(
                            FirmaCompat.MODID,
                            "fluid_brewing"
                    )
            );

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                "jei"
        );
    }

    @Override
    public void registerCategories(
            IRecipeCategoryRegistration registration
    ) {
        IGuiHelper gui =
                registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new FluidBrewingRecipeCategory(
                        FLUID_BREWING,
                        gui
                )
        );
    }

    @Override
    public void registerRecipes(
            IRecipeRegistration registration
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        List<RecipeHolder<FluidBrewingRecipe>> recipes =
                minecraft.level.getRecipeManager()
                        .getAllRecipesFor(
                                ModRecipes.FLUID_BREWING.get()
                        );

        registration.addRecipes(
                FLUID_BREWING,
                recipes
        );
    }


    private static List<RecipeHolder<FluidBrewingRecipe>> getRecipes() {
        /*
         * This is the client-side recipe manager.
         *
         * JEI's recipe registration happens on the client,
         * so get the current client level.
         */
        var level = net.minecraft.client.Minecraft
                .getInstance()
                .level;

        if (level == null) {
            return List.of();
        }

        return level.getRecipeManager()
                .getAllRecipesFor(
                        ModRecipes.FLUID_BREWING.get()
                );
    }
}
