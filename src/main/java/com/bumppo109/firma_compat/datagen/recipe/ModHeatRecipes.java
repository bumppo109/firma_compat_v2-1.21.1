package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatMetalMaterial;
import com.bumppo109.firma_compat.block.CompatMetalSet;
import com.bumppo109.firma_compat.block.CompatMetalWeathered;
import com.bumppo109.firma_compat.item.ModItems;
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

        ModItems.METAL_ITEMS.forEach((compatMetal, itemTypeItemIdMap) -> {
            if (compatMetal.isDummy()) return;
            itemTypeItemIdMap.forEach((itemType, itemId) -> {
                if (!new ItemStack(itemId.get()).isDamageableItem()) return;
                ResourceLocation fluid = BuiltInRegistries.FLUID.getKey(meltFluidFor(compatMetal));
                FirmaCompat.LOGGER.debug("Fluid for {} = {}", compatMetal, fluid);
                FirmaCompat.LOGGER.debug("ModHeatRecipe: {}", itemId);
                switch (itemType) {
                    case MACE, TUYERE, PROPICK, CHISEL, HAMMER, SAW, SCYTHE, JAVELIN, KNIFE -> add(compatMetal.getSerializedName() + "_" +itemType.name().toLowerCase(Locale.ROOT),
                            new HeatingRecipe(Ingredient.of(itemId.get()),
                                    ItemStackProvider.empty(),
                                    new FluidStack(meltFluidFor(compatMetal), units(itemType)),
                                    temperatureOf(compatMetal), new ItemStack(itemId.get()).isDamageableItem()));
                }
            });
        });

        for (CompatMetal metal : CompatMetal.values()) {
            if (metal.isDummy()) continue;
            CompatMetalMaterial material = metal.getMetalMaterial();
            for (CompatMetalMaterial.MetalPart partType : CompatMetalMaterial.MetalPart.values()) {
                Supplier<?> supplier = material.parts().get(partType);

                if (supplier == null) continue;

                int units = switch (partType) {
                    case SWORD -> 200;
                    case BOOTS, SHIELD -> 400;
                    case LEGGINGS, HELMET -> 600;
                    case CHESTPLATE -> 800;
                    case HORSE_ARMOR -> 1200;
                    default -> 100;
                };

                add(metal.getSerializedName() + "_" + partType.name().toLowerCase(Locale.ROOT),
                        new HeatingRecipe(Ingredient.of((ItemLike) supplier.get()),
                                ItemStackProvider.empty(),
                                new FluidStack(meltFluidFor(metal), units),
                                temperatureOf(metal), new ItemStack((ItemLike) supplier.get()).isDamageableItem()));
            }
        }

        add(Ingredient.of(ModItems.UNFINISHED_LANTERN),
                new FluidStack(meltFluidFor(Metal.CAST_IRON),100),
                1535
        );

        for (CompatMetalWeathered weathered : CompatMetalWeathered.values()) {
            for (CompatMetalSet set : weathered) {
                set.parts().forEach(part ->
                    add(Ingredient.of(part.block()),
                        new FluidStack(meltFluidFor(Metal.COPPER), part.amount()),
                        1060
                    )
                );
            }
        }

        List.of("", "waxed").forEach(waxed -> {
            List.of("", "exposed", "weathered", "oxidized").forEach(state -> {
                List.of("copper_door", "copper_trapdoor").forEach(door -> {
                    String idStr = waxed.isEmpty() ? state.isEmpty() ? door : state + "_" + door : state.isEmpty() ? waxed + "_" + door : waxed + "_" + state + "_" + door;
                    Item item = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(idStr));
                    add(Ingredient.of(item),
                            new FluidStack(meltFluidFor(Metal.COPPER),200),
                            1060
                    );
                });
            });
        });
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
