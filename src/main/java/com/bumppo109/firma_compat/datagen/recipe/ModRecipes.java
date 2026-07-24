package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.datagen.ModAccessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

public interface ModRecipes extends ModAccessors
{
    HolderLookup.Provider lookup();

    default String nameOf(Recipe<?> recipe)
    {
        return nameOf(recipe.getResultItem(lookup()).getItem());
    }

    default void add(Recipe<?> recipe)
    {
        add(nameOf(recipe), recipe);
    }

    default void add(String name, Recipe<?> recipe)
    {
        add(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()), "No recipe type").getPath(), name, recipe);
    }

    void add(String prefix, String name, Recipe<?> recipe);

    void remove(String... names);

    void removeTFC(String... names);

    void replace(String name, Recipe<?> recipe);

}
