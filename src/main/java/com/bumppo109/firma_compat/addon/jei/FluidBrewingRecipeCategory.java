package com.bumppo109.firma_compat.addon.jei;

import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipe;
import com.bumppo109.firma_compat.util.ModTags;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.dries007.tfc.common.items.TFCItems;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;

import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public final class FluidBrewingRecipeCategory
        implements IRecipeCategory<RecipeHolder<FluidBrewingRecipe>> {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;

    private final RecipeType<RecipeHolder<FluidBrewingRecipe>> recipeType;
    private final IGuiHelper guiHelper;
    private final IDrawable background;

    public FluidBrewingRecipeCategory(
            RecipeType<RecipeHolder<FluidBrewingRecipe>> recipeType,
            IGuiHelper guiHelper
    ) {
        this.recipeType = recipeType;
        this.guiHelper = guiHelper;

        /*
         * Use the actual vanilla Brewing Stand GUI as the JEI
         * category background.
         */
        this.background = guiHelper.createDrawable(
                ResourceLocation.withDefaultNamespace(
                        "textures/gui/container/brewing_stand.png"
                ),
                0,
                0,
                WIDTH,
                HEIGHT
        );
    }

    @Override
    public RecipeType<RecipeHolder<FluidBrewingRecipe>> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
                "jei.firma_compat.fluid_brewing"
        );
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon =
                TFCItems.SILICA_GLASS_BOTTLE
                        .get()
                        .getDefaultInstance();

        return guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                icon
        );
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            RecipeHolder<FluidBrewingRecipe> recipeHolder,
            IFocusGroup focuses
    ) {
        FluidBrewingRecipe recipe = recipeHolder.value();

        /*
         * ============================================================
         * BLAZE POWDER / FUEL
         * ============================================================
         *
         * This is purely visual.
         *
         * FluidBrewingRecipe does not actually consume blaze powder,
         * but the real Fluid Brewing Stand uses a fuel slot and the
         * JEI category is intentionally styled after vanilla.
         */
        IRecipeSlotBuilder fuelSlot =
                builder.addSlot(
                        RecipeIngredientRole.RENDER_ONLY,
                        17,
                        17
                );

        fuelSlot.addItemStack(
                new ItemStack(Items.BLAZE_POWDER)
        );

        /*
         * ============================================================
         * CATALYST
         * ============================================================
         *
         * This is the actual recipe ingredient.
         *
         * Position matches the vanilla Brewing Stand ingredient slot.
         */
        IRecipeSlotBuilder catalystSlot =
                builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        79,
                        17
                );

        catalystSlot.addIngredients(
                recipe.catalyst()
        );

        /*
         * ============================================================
         * INPUT FLUID
         * ============================================================
         *
         * One container containing the recipe's input fluid.
         *
         * This is deliberately shown separately from the three
         * output bottle slots.
         */
        IRecipeSlotBuilder inputSlot =
                builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        102,
                        17
                );

        inputSlot.addItemStacks(
                getInputContainers(recipe)
        );

        inputSlot.addRichTooltipCallback(
                (view, tooltip) -> tooltip.add(
                        Component.translatable(
                                "jei.firma_compat.fluid_brewing.minimum",
                                recipe.inputAmount()
                        )
                )
        );

        /*
         * ============================================================
         * OUTPUT BOTTLE 0
         * ============================================================
         */
        IRecipeSlotBuilder output0 =
                builder.addSlot(
                        RecipeIngredientRole.OUTPUT,
                        56,
                        51
                );

        output0.addItemStacks(
                getOutputContainers(recipe)
        );

        /*
         * ============================================================
         * OUTPUT BOTTLE 1
         * ============================================================
         */
        IRecipeSlotBuilder output1 =
                builder.addSlot(
                        RecipeIngredientRole.OUTPUT,
                        79,
                        58
                );

        output1.addItemStacks(
                getOutputContainers(recipe)
        );

        /*
         * ============================================================
         * OUTPUT BOTTLE 2
         * ============================================================
         */
        IRecipeSlotBuilder output2 =
                builder.addSlot(
                        RecipeIngredientRole.OUTPUT,
                        102,
                        51
                );

        output2.addItemStacks(
                getOutputContainers(recipe)
        );

        /*
         * The recipe transforms the entire amount of fluid in the
         * container rather than consuming exactly inputAmount.
         */
        output0.addRichTooltipCallback(
                (view, tooltip) -> tooltip.add(
                        Component.translatable(
                                "jei.firma_compat.fluid_brewing.entire_amount"
                        )
                )
        );

        output1.addRichTooltipCallback(
                (view, tooltip) -> tooltip.add(
                        Component.translatable(
                                "jei.firma_compat.fluid_brewing.entire_amount"
                        )
                )
        );

        output2.addRichTooltipCallback(
                (view, tooltip) -> tooltip.add(
                        Component.translatable(
                                "jei.firma_compat.fluid_brewing.entire_amount"
                        )
                )
        );
    }

    /**
     * No custom drawing is required.
     *
     * The vanilla Brewing Stand texture supplies the GUI artwork,
     * including the brewing stand, bottle outlines, fuel area, etc.
     */
    @Override
    public void draw(
            RecipeHolder<FluidBrewingRecipe> recipeHolder,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics graphics,
            double mouseX,
            double mouseY
    ) {
    }

    /**
     * Gets containers capable of holding both the input and output
     * fluids.
     */
    private static List<ItemStack> getCompatibleContainers(
            FluidBrewingRecipe recipe
    ) {
        return BuiltInRegistries.ITEM
                .getTag(ModTags.Items.BREWING_CONTAINERS)
                .stream()
                .flatMap(tag -> tag.stream())
                .map(holder -> new ItemStack(holder.value()))
                .filter(stack ->
                        stack.getItem() instanceof FluidContainerItem
                )
                .filter(stack -> {
                    FluidContainerItem container =
                            (FluidContainerItem) stack.getItem();

                    return container.containerInfo()
                            .canContainFluid(recipe.inputFluid())
                            &&
                            container.containerInfo()
                                    .canContainFluid(recipe.outputFluid());
                })
                .toList();
    }

    /**
     * Creates the input-container representations.
     *
     * The item itself is preserved, but its TFC fluid component is
     * changed to the recipe's input fluid.
     */
    private static List<ItemStack> getInputContainers(
            FluidBrewingRecipe recipe
    ) {
        FluidStack fluid =
                new FluidStack(
                        recipe.inputFluid(),
                        recipe.inputAmount()
                );

        return getCompatibleContainers(recipe)
                .stream()
                .map(container -> {
                    ItemStack result = container.copy();

                    result.set(
                            TFCComponents.FLUID,
                            new FluidComponent(fluid)
                    );

                    return result;
                })
                .toList();
    }

    /**
     * Creates the output-container representations.
     *
     * JEI displays inputAmount as the representative amount.
     *
     * The actual recipe is capable of transforming the entire
     * quantity contained by the player's container.
     */
    private static List<ItemStack> getOutputContainers(
            FluidBrewingRecipe recipe
    ) {
        FluidStack fluid =
                new FluidStack(
                        recipe.outputFluid(),
                        recipe.inputAmount()
                );

        return getCompatibleContainers(recipe)
                .stream()
                .map(container -> {
                    ItemStack result = container.copy();

                    result.set(
                            TFCComponents.FLUID,
                            new FluidComponent(fluid)
                    );

                    return result;
                })
                .toList();
    }
}
