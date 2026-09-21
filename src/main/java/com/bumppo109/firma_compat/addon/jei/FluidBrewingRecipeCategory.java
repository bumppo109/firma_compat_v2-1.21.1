package com.bumppo109.firma_compat.addon.jei;

import com.bumppo109.firma_compat.block.Glass;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipe;

import com.bumppo109.firma_compat.util.ModTags;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
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
import net.minecraft.world.item.crafting.RecipeHolder;

import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public final class FluidBrewingRecipeCategory
        implements IRecipeCategory<RecipeHolder<FluidBrewingRecipe>> {

    public static final int WIDTH = 116;
    public static final int HEIGHT = 36;

    private final RecipeType<RecipeHolder<FluidBrewingRecipe>> recipeType;
    private final IGuiHelper guiHelper;

    private final IDrawable slot;
    private final IDrawable arrow;

    public FluidBrewingRecipeCategory(
            RecipeType<RecipeHolder<FluidBrewingRecipe>> recipeType,
            IGuiHelper guiHelper
    ) {
        this.recipeType = recipeType;
        this.guiHelper = guiHelper;

        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.getRecipeArrow();
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
    public IDrawable getIcon() {
        return getBrewingContainerIcon();
    }

    private IDrawable getBrewingContainerIcon() {
        ItemStack icon = BuiltInRegistries.ITEM
                .getTag(ModTags.Items.BREWING_CONTAINERS)
                .flatMap(tag -> tag.stream().findFirst())
                .map(holder -> new ItemStack(holder.value()))
                .orElse(ItemStack.EMPTY);

        ItemStack bottle = ModItems.SPLASH_POTIONS.get(Glass.VOLCANIC).get().getDefaultInstance();

        return guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                bottle
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
         * ------------------------------------------------------------
         * INPUT CONTAINER
         * ------------------------------------------------------------
         *
         * Show all items in BREWING_CONTAINERS.
         *
         * We use the item tag here rather than enumerating containers
         * ourselves.
         */
        IRecipeSlotBuilder containerSlot =
                builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        5,
                        5
                );

        containerSlot.addItemStacks(
                getInputContainers(recipe)
        );

        /*
        containerSlot.addItemStacks(
                BuiltInRegistries.ITEM
                        .getTag(ModTags.Items.BREWING_CONTAINERS)
                        .stream()
                        .flatMap(tag -> tag.stream())
                        .map(holder -> new ItemStack(holder.value()))
                        .toList()
        );

         */

        containerSlot.setBackground(
                slot,
                -1,
                -1
        );

        /*
         * ------------------------------------------------------------
         * CATALYST
         * ------------------------------------------------------------
         */

        IRecipeSlotBuilder catalystSlot =
                builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        35,
                        5
                );

        catalystSlot.addIngredients(
                recipe.catalyst()
        );

        catalystSlot.setBackground(
                slot,
                -1,
                -1
        );

        /*
         * ------------------------------------------------------------
         * OUTPUT FLUID
         * ------------------------------------------------------------
         *
         * We display inputAmount as the representative amount.
         *
         * The actual recipe transforms the ENTIRE amount in the
         * container, so this is a minimum/representative value rather
         * than a fixed output quantity.
         */

        FluidStack output =
                new FluidStack(
                        recipe.outputFluid(),
                        recipe.inputAmount()
                );

        IRecipeSlotBuilder outputSlot =
                builder.addSlot(
                        RecipeIngredientRole.OUTPUT,
                        95,
                        5
                );

        outputSlot.addIngredient(
                JEIIntegration.FLUID_STACK,
                output
        );

        outputSlot.setFluidRenderer(
                1L,
                false,
                16,
                16
        );

        outputSlot.setBackground(
                slot,
                -1,
                -1
        );

        /*
         * ------------------------------------------------------------
         * INPUT CONTAINER TOOLTIP
         * ------------------------------------------------------------
         */

        containerSlot.addRichTooltipCallback(
                (view, tooltip) -> {
                    tooltip.add(
                            Component.translatable(
                                    "jei.firma_compat.fluid_brewing.container"
                            )
                    );

                    tooltip.add(
                            Component.translatable(
                                    "jei.firma_compat.fluid_brewing.minimum",
                                    recipe.inputAmount()
                            )
                    );
                }
        );

        /*
         * ------------------------------------------------------------
         * OUTPUT TOOLTIP
         * ------------------------------------------------------------
         */

        outputSlot.addRichTooltipCallback(
                (view, tooltip) -> {
                    tooltip.add(
                            Component.translatable(
                                    "jei.firma_compat.fluid_brewing.entire_amount"
                            )
                    );
                }
        );
    }

    @Override
    public void draw(
            RecipeHolder<FluidBrewingRecipe> recipeHolder,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics graphics,
            double mouseX,
            double mouseY
    ) {
        arrow.draw(
                graphics,
                65,
                5
        );
    }

    private static List<ItemStack> getInputContainers(
            FluidBrewingRecipe recipe
    ) {
        FluidStack fluid = new FluidStack(
                recipe.inputFluid(),
                recipe.inputAmount()
        );

        return BuiltInRegistries.ITEM
                .getTag(ModTags.Items.BREWING_CONTAINERS)
                .stream()
                .flatMap(tag -> tag.stream())
                .map(holder -> new ItemStack(holder.value()))
                .filter(stack ->
                        stack.getItem() instanceof FluidContainerItem container
                )
                .filter(stack -> {
                    FluidContainerItem container =
                            (FluidContainerItem) stack.getItem();

                    return container.containerInfo()
                            .canContainFluid(recipe.inputFluid())
                            && container.containerInfo()
                            .canContainFluid(recipe.outputFluid());
                })
                .map(stack -> {
                    stack.set(
                            TFCComponents.FLUID,
                            new FluidComponent(fluid)
                    );

                    return stack;
                })
                .toList();
    }

}
