package com.bumppo109.firma_compat.datagen;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.util.data.Drinkable;
import net.dries007.tfc.util.data.Drinkable.Effect;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BuiltinDrinkables extends DataManagerProvider<Drinkable> implements ModAccessors
{
    public BuiltinDrinkables(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Drinkable.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        /*
        add("milk",
                FluidIngredient.of(NeoForgeMod.MILK.get()),
                false,
                FoodData.ofDrink(10, 0).dairy(1f));

         */
    }

    private void add(String name, FluidIngredient fluid, boolean mayDrinkWhenFull, FoodData food, Effect... effects)
    {
        add(name, new Drinkable(fluid, 0, mayDrinkWhenFull, food, List.of(effects)));
    }
}
