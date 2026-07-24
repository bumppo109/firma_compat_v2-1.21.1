package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatMetalMaterial;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Map;

import static net.dries007.tfc.common.recipes.WeldingRecipe.Behavior.COPY_BEST;
import static net.dries007.tfc.common.recipes.WeldingRecipe.Behavior.IGNORE;

public interface ModWeldingRecipes extends ModRecipes {

    default void weldingRecipes(){

        for (CompatMetal metal : CompatMetal.values()) {
            CompatMetalMaterial material = metal.getMetalMaterial();

            if (material != null) {
                add(new WeldingRecipe(Ingredient.of(material.ingot().get()), Ingredient.of(material.ingot().get()),
                        metal.tier() - 1,
                        ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.DOUBLE_INGOT)),
                        IGNORE
                ));

                for (CompatMetal.ItemType itemType : CompatMetal.ItemType.values()) {
                    if (CompatMetal.makeItem(material, itemType)) {
                        switch (itemType) {
                            case DOUBLE_SHEET -> weld(metal, CompatMetal.ItemType.SHEET, CompatMetal.ItemType.SHEET, ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.DOUBLE_SHEET), IGNORE);
                            case UNFINISHED_HELMET -> weld(metal, CompatMetal.ItemType.UNFINISHED_HELMET, CompatMetal.ItemType.SHEET, material.helmet().get(), COPY_BEST);
                            case UNFINISHED_CHESTPLATE -> weld(metal, CompatMetal.ItemType.UNFINISHED_CHESTPLATE, CompatMetal.ItemType.DOUBLE_SHEET, material.chestplate().get(), COPY_BEST);
                            case UNFINISHED_GREAVES -> weld(metal, CompatMetal.ItemType.UNFINISHED_GREAVES, CompatMetal.ItemType.SHEET, material.leggings().get(), COPY_BEST);
                            case UNFINISHED_BOOTS -> weld(metal, CompatMetal.ItemType.UNFINISHED_BOOTS, CompatMetal.ItemType.SHEET, material.boots().get(), COPY_BEST);

                        }
                    }
                }
            }
        }
    }

    private void weld(CompatMetal metal, CompatMetal.ItemType input1, CompatMetal.ItemType input2, ItemLike output, WeldingRecipe.Behavior behavior)
    {
        add(new WeldingRecipe(
                ingredientOf(metal, input1),
                ingredientOf(metal, input2),
                metal.tier() - 1,
                ItemStackProvider.of(output),
                behavior
        ));
    }
}
