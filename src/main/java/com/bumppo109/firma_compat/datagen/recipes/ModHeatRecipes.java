package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.materials.MetalMaterial;
import com.bumppo109.firma_compat.materials.MetalSet;
import com.bumppo109.firma_compat.materials.MetalWeathered;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public interface ModHeatRecipes extends ModRecipes
{
    default void heatRecipes()
    {
        addFood(Items.KELP, Items.DRIED_KELP);
        burnFood("kelp", Ingredient.of(Items.DRIED_KELP), 700);
        add(Ingredient.of(Items.CHAIN), new FluidStack(meltFluidFor(Metal.CAST_IRON), 6), 1535);
        add(Ingredient.of(Items.IRON_NUGGET), new FluidStack(meltFluidFor(Metal.CAST_IRON), 10), 1535);
        add(Ingredient.of(Items.GOLD_NUGGET), new FluidStack(meltFluidFor(Metal.GOLD), 10), 1060);

        add(Items.WET_SPONGE, Items.SPONGE, 500);

        add(ModItems.UNFIRED_POT, Items.DECORATED_POT, 1399);

        add(Ingredient.of(ModItems.UNFINISHED_LANTERN),
                new FluidStack(meltFluidFor(Metal.CAST_IRON),100),
                1535
        );

        ModItems.METAL_ITEMS.forEach((metal, items) -> items.forEach((type, item) -> add(nameOf(item),
                new HeatingRecipe(
                ingredientOf(metal, type),
                ItemStackProvider.empty(),
                new FluidStack(meltFluidFor(metal), units(type)),
                temperatureOf(metal), new ItemStack(item).isDamageableItem()))));

        for (MetalWeathered weathered : MetalWeathered.values()) {
            for (MetalSet metalSet : weathered) {
                if (metalSet.base() != null) {
                    add(nameOf(metalSet.base().get().asItem()), new HeatingRecipe(Ingredient.of(metalSet.base().get().asItem()),
                            ItemStackProvider.empty(),
                            new FluidStack(meltFluidFor(Metal.COPPER), 100),
                            temperatureOf(Metal.COPPER), false));
                }

                if (metalSet.stairs() != null) {
                    add(nameOf(metalSet.stairs().get().asItem()), new HeatingRecipe(Ingredient.of(metalSet.stairs().get().asItem()),
                            ItemStackProvider.empty(),
                            new FluidStack(meltFluidFor(Metal.COPPER),75),
                            temperatureOf(Metal.COPPER),false));
                }

                if (metalSet.slab() != null) {
                    add(nameOf(metalSet.slab().get().asItem()), new HeatingRecipe(Ingredient.of(metalSet.slab().get().asItem()),
                            ItemStackProvider.empty(),
                            new FluidStack(meltFluidFor(Metal.COPPER),50),
                            temperatureOf(Metal.COPPER),false));
                }
            }
        }

        add(Ingredient.of(Items.COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.EXPOSED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.WEATHERED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.OXIDIZED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.WAXED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.WAXED_EXPOSED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.WAXED_WEATHERED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
        add(Ingredient.of(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR), new FluidStack(meltFluidFor(Metal.COPPER),200), temperatureOf(Metal.COPPER));
    }

    private void metalHeat(CompatMetal metal, Item item, int units) {

    }

    private Fluid meltFluidFor(CompatMetal metal)
    {
        return fluidOf(switch (metal)
        {
            default -> metal;
        });
    }

    private Fluid meltFluidFor(Metal metal)
    {
        return fluidOf(switch (metal)
        {
            default -> metal;
        });
    }

    private Ingredient notRotten(ItemLike input)
    {
        return AndIngredient.of(Ingredient.of(input), NotRottenIngredient.INSTANCE);
    }

    private void addFood(Food input, Food output)
    {
        add(notRotten(TFCItems.FOOD.get(input)), ItemStackProvider.of(new ItemStack(TFCItems.FOOD.get(output)), CopyFoodModifier.INSTANCE), 200);
    }

    private void addFood(Item input, Item output)
    {
        add(notRotten(input), ItemStackProvider.of(new ItemStack(output), CopyFoodModifier.INSTANCE), 200);
    }

    private void burnFood(String name, Ingredient input, float temperature)
    {
        add("burn_" + name, new HeatingRecipe(input, ItemStackProvider.empty(), FluidStack.EMPTY, temperature, false));
    }

    private void add(ItemLike input, ItemLike output, float temperature)
    {
        add(Ingredient.of(input), ItemStackProvider.of(output), temperature);
    }

    private void add(Ingredient input, FluidStack output, float temperature)
    {
        add(nameOf(input), new HeatingRecipe(input, ItemStackProvider.empty(), output, temperature, false));
    }

    private void add(String suffix, Ingredient input, ItemStackProvider output, float temperature)
    {
        add(nameOf(output.getEmptyStack().getItem()) + "_" + suffix, new HeatingRecipe(input, output, FluidStack.EMPTY, temperature, false));
    }

    private void add(Ingredient input, ItemStackProvider output, float temperature)
    {
        add(new HeatingRecipe(input, output, FluidStack.EMPTY, temperature, false));
    }
}