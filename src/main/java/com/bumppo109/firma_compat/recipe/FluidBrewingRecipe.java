package com.bumppo109.firma_compat.recipe;

import com.bumppo109.firma_compat.FirmaCompat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidBrewingRecipe(
        net.minecraft.world.level.material.Fluid inputFluid,
        int inputAmount,
        Ingredient catalyst,
        net.minecraft.world.level.material.Fluid outputFluid
) implements Recipe<FluidBrewingRecipeInput> {

    /**
     * Minimum amount of input fluid required for the recipe to match.
     *
     * This is only a threshold.
     *
     * The recipe transforms the ENTIRE amount of fluid in the
     * container rather than consuming only inputAmount.
     *
     * Example:
     *
     * 100 mB  -> does not match
     * 250 mB  -> 250 mB output
     * 500 mB  -> 500 mB output
     * 1000 mB -> 1000 mB output
     */
    public static final int DEFAULT_INPUT_AMOUNT = 250;

    private static final MapCodec<Integer> INPUT_AMOUNT_CODEC =
            Codec.INT.optionalFieldOf(
                    "input_amount",
                    DEFAULT_INPUT_AMOUNT
            );

    public static final MapCodec<FluidBrewingRecipe> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BuiltInRegistries.FLUID.byNameCodec()
                            .fieldOf("input_fluid")
                            .forGetter(FluidBrewingRecipe::inputFluid),

                    INPUT_AMOUNT_CODEC
                            .forGetter(FluidBrewingRecipe::inputAmount),

                    Ingredient.CODEC
                            .fieldOf("catalyst")
                            .forGetter(FluidBrewingRecipe::catalyst),

                    BuiltInRegistries.FLUID.byNameCodec()
                            .fieldOf("output_fluid")
                            .forGetter(FluidBrewingRecipe::outputFluid)

            ).apply(instance, FluidBrewingRecipe::new));

    public static final Codec<FluidBrewingRecipe> CODEC =
            MAP_CODEC.codec();


    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            FluidBrewingRecipe
            > STREAM_CODEC = StreamCodec.composite(

            ByteBufCodecs.registry(BuiltInRegistries.FLUID.key()),
            FluidBrewingRecipe::inputFluid,

            ByteBufCodecs.INT,
            FluidBrewingRecipe::inputAmount,

            Ingredient.CONTENTS_STREAM_CODEC,
            FluidBrewingRecipe::catalyst,

            ByteBufCodecs.registry(BuiltInRegistries.FLUID.key()),
            FluidBrewingRecipe::outputFluid,

            FluidBrewingRecipe::new
    );

    public static final DataManager<FluidBrewingRecipe> MANAGER =
            new DataManager<>(
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"fluid_brewing"),
                    CODEC
            );

    public FluidBrewingRecipe {
        if (inputAmount <= 0) {
            throw new IllegalArgumentException(
                    "inputAmount must be greater than zero"
            );
        }
    }

    /**
     * Determines whether the supplied ItemStack is a TFC fluid
     * container containing enough of the recipe's input fluid.
     *
     * The actual container item is preserved by the recipe.
     */
    public boolean matchesContainer(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        System.out.println(
                "[FirmaCompat] Container item: " +
                        BuiltInRegistries.ITEM.getKey(stack.getItem())
        );

        if (!(stack.getItem() instanceof FluidContainerItem containerItem)) {
            System.out.println("[FirmaCompat] NOT a FluidContainerItem");
            return false;
        }

        FluidStack contained =
                FluidHelpers.getContainedFluidInTank(stack);

        if (contained.isEmpty()) {
            System.out.println("[FirmaCompat] Container has no fluid");
            return false;
        }

        System.out.println(
                "[FirmaCompat] Fluid: " +
                        BuiltInRegistries.FLUID.getKey(contained.getFluid()) +
                        " amount=" +
                        contained.getAmount()
        );

        System.out.println(
                "[FirmaCompat] Expected: " +
                        BuiltInRegistries.FLUID.getKey(inputFluid)
        );

        if (contained.getFluid() != inputFluid) {
            System.out.println("[FirmaCompat] Wrong fluid");
            return false;
        }

        if (contained.getAmount() < inputAmount) {
            System.out.println(
                    "[FirmaCompat] Not enough fluid: " +
                            contained.getAmount() +
                            " < " +
                            inputAmount
            );
            return false;
        }

        boolean canContain =
                containerItem.containerInfo().canContainFluid(outputFluid);

        System.out.println(
                "[FirmaCompat] Can contain output: " + canContain
        );

        return canContain;
    }


    /**
     * Creates the transformed container.
     *
     * The container item and its unrelated item components are
     * preserved.
     *
     * The entire amount of the input fluid is transformed.
     */
    public ItemStack assembleContainer(ItemStack container) {
        if (!matchesContainer(container)) {
            return ItemStack.EMPTY;
        }

        if (!(container.getItem() instanceof FluidContainerItem fluidContainer)) {
            return ItemStack.EMPTY;
        }

        FluidStack contained = FluidHelpers.getContainedFluidInTank(container);

        if (contained.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int amount = contained.getAmount();

        /*
         * Defensive capacity check.
         *
         * The fluid is already inside this container, so normally this
         * will always pass.
         */
        if (amount > fluidContainer.containerInfo().fluidCapacity()) {
            return ItemStack.EMPTY;
        }

        /*
         * Confirm that this exact container can hold the output fluid.
         */
        if (!fluidContainer.containerInfo().canContainFluid(outputFluid)) {
            return ItemStack.EMPTY;
        }

        /*
         * FluidStack has no setFluid() method in the NeoForge version
         * being used.
         *
         * Create a new FluidStack with:
         *
         * - the recipe's output fluid
         * - the complete amount of the original fluid
         *
         * We deliberately do not copy the input FluidStack's components
         * because components may be specific to the input fluid.
         */
        FluidStack resultFluid =
                new FluidStack(outputFluid, amount);

        /*
         * Copy the original container so that the exact container item
         * and its unrelated item components are retained.
         */
        ItemStack result = container.copyWithCount(1);

        /*
         * TFC stores the container's fluid in the FLUID data component.
         */
        result.set(
                TFCComponents.FLUID,
                new FluidComponent(resultFluid)
        );

        return result;
    }

    @Override
    public boolean matches(
            FluidBrewingRecipeInput input,
            Level level
    ) {
        return matchesContainer(input.container())
                && catalyst.test(input.catalyst());
    }

    @Override
    public ItemStack assemble(
            FluidBrewingRecipeInput input,
            HolderLookup.Provider registries
    ) {
        return assembleContainer(input.container());
    }

    @Override
    public boolean canCraftInDimensions(
            int width,
            int height
    ) {
        return true;
    }

    /**
     * The result is dynamic because the recipe preserves the
     * container supplied by the player.
     */
    @Override
    public ItemStack getResultItem(
            HolderLookup.Provider registries
    ) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLUID_BREWING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FLUID_BREWING.get();
    }
}
