package com.bumppo109.firma_compat.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(
                    Registries.RECIPE_SERIALIZER,
                    com.bumppo109.firma_compat.FirmaCompat.MODID
            );

    public static final DeferredHolder<
            RecipeSerializer<?>,
            RecipeSerializer<FluidBrewingRecipe>
            > FLUID_BREWING =
            RECIPE_SERIALIZERS.register(
                    "fluid_brewing",
                    () -> new RecipeSerializer<>() {

                        @Override
                        public MapCodec<FluidBrewingRecipe> codec() {
                            return FluidBrewingRecipe.MAP_CODEC;
                        }

                        @Override
                        public StreamCodec<
                                RegistryFriendlyByteBuf,
                                FluidBrewingRecipe
                                > streamCodec() {
                            return FluidBrewingRecipe.STREAM_CODEC;
                        }
                    }
            );

    private ModRecipeSerializers() {
    }
}
