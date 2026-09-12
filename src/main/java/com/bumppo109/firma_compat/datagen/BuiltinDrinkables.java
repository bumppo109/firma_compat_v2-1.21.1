package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.fluid.Potion;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.util.data.Drinkable;
import net.dries007.tfc.util.data.Drinkable.Effect;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BuiltinDrinkables
        extends DataManagerProvider<Drinkable>
        implements ModAccessors
{
    public BuiltinDrinkables(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookup
    ) {
        super(
                Drinkable.MANAGER,
                output,
                lookup,
                TerraFirmaCraft.MOD_ID
        );
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        for (Potion potion : Potion.values()) {

            Potion.EffectData[] potionEffects =
                    potion.effects();

            Effect[] effects =
                    new Effect[potionEffects.length];

            for (int i = 0; i < potionEffects.length; i++) {

                Potion.EffectData potionEffect =
                        potionEffects[i];

                effects[i] = new Effect(
                        potionEffect.effect(),
                        potionEffect.duration(),
                        potionEffect.level(),
                        1
                );
            }

            add(
                    potion.serializedName() + "_potion",
                    FluidIngredient.of(
                            ModFluids.POTIONS
                                    .get(potion)
                                    .getSource()
                    ),
                    true,
                    FoodData.ofDrink(1, 0),
                    effects
            );
        }
    }

    private void add(
            String name,
            FluidIngredient fluid,
            boolean mayDrinkWhenFull,
            FoodData food,
            Effect... effects
    ) {
        add(
                name,
                new Drinkable(
                        fluid,
                        0,
                        mayDrinkWhenFull,
                        food,
                        List.of(effects)
                )
        );
    }
}
