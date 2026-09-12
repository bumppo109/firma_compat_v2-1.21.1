package com.bumppo109.firma_compat.recipe;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(
                    Registries.RECIPE_TYPE,
                    FirmaCompat.MODID
            );

    public static final DeferredHolder<
            RecipeType<?>,
            RecipeType<FluidBrewingRecipe>
            > FLUID_BREWING =
            RECIPE_TYPES.register(
                    "fluid_brewing",
                    () -> RecipeType.simple(
                            ResourceLocation.fromNamespaceAndPath(
                                    FirmaCompat.MODID,
                                    "fluid_brewing"
                            )
                    )
            );

    public static final DeferredHolder<
            RecipeSerializer<?>,
            RecipeSerializer<FluidBrewingRecipe>
            > FLUID_BREWING_SERIALIZER =
            ModRecipeSerializers.FLUID_BREWING;

    private ModRecipes() {
    }
}
