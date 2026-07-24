package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.block.CompatMetal;
import net.dries007.tfc.common.recipes.AlloyRecipe;
import net.dries007.tfc.util.AlloyRange;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

public interface ModAlloyRecipes extends ModRecipes {

    default void alloyRecipes()
    {

    }

    private AlloyRange rangeOf(Metal metal, double min, double max)
    {
        return new AlloyRange(fluidOf(metal), min, max);
    }
    private AlloyRange rangeOf(CompatMetal metal, double min, double max)
    {
        return new AlloyRange(fluidOf(metal), min, max);
    }

    private void alloy(CompatMetal metal, AlloyRange... ranges)
    {
        final AlloyRecipe recipe = new AlloyRecipe(List.of(ranges), fluidOf(metal));
        add(BuiltInRegistries.FLUID.getKey(recipe.result()).getPath().split("/")[1], recipe);
    }
}
