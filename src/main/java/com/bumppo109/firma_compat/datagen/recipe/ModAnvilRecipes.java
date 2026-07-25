package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatMetalMaterial;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static net.dries007.tfc.common.component.forge.ForgeRule.*;

public interface ModAnvilRecipes extends ModRecipes
{
    /**
     * Forge rules are entered in the order they are displayed in the anvil.
     * As such, they should be entered in a valid order of hits.
     * Ex. if the steps are BEND_THIRD_LAST, HIT_SECOND_LAST, PUNCH_LAST, they should be entered in that order.
     * Ex. if the steps are BEND_ANY, HIT_ANY, PUNCH_LAST, they can be entered in that order, or as HIT_ANY, BEND_ANY, PUNCH_LAST
     */
    default void anvilRecipes()
    {
        final ForgeRule[] hitX3 = {HIT_THIRD_LAST, HIT_SECOND_LAST, HIT_LAST};
        final ForgeRule[] pickaxeHeadRule = {BEND_NOT_LAST, DRAW_NOT_LAST, PUNCH_LAST};
        final ForgeRule[] shovelHeadRule = {HIT_NOT_LAST, PUNCH_LAST};
        final ForgeRule[] axeHeadRule = {UPSET_THIRD_LAST, HIT_SECOND_LAST, PUNCH_LAST};
        final ForgeRule[] hoeHeadRule = {BEND_NOT_LAST, HIT_NOT_LAST, PUNCH_LAST};
        final ForgeRule[] hammerHeadRule = {SHRINK_NOT_LAST, PUNCH_LAST};
        final ForgeRule[] propickHeadRule = {DRAW_NOT_LAST, BEND_NOT_LAST, PUNCH_LAST};
        final ForgeRule[] sawBladeRule = {HIT_SECOND_LAST, HIT_LAST};
        final ForgeRule[] scytheBladeRule = {BEND_THIRD_LAST, DRAW_SECOND_LAST, HIT_LAST};
        final ForgeRule[] knifeBladeRule = {DRAW_THIRD_LAST, DRAW_SECOND_LAST, HIT_LAST};
        final ForgeRule[] chiselHeadRule = {HIT_NOT_LAST, DRAW_NOT_LAST, HIT_LAST};
        final ForgeRule[] javelinHeadRule = {DRAW_THIRD_LAST, HIT_SECOND_LAST, HIT_LAST};
        final ForgeRule[] swordBladeRule = {BEND_THIRD_LAST, BEND_SECOND_LAST, HIT_LAST};
        final ForgeRule[] maceHeadRule = {SHRINK_NOT_LAST, BEND_NOT_LAST, HIT_LAST};
        final ForgeRule[] unfinishedHelmetRule = {BEND_THIRD_LAST, BEND_SECOND_LAST, HIT_LAST};
        final ForgeRule[] unfinishedChestplateRule = {UPSET_THIRD_LAST, HIT_SECOND_LAST, HIT_LAST};
        final ForgeRule[] unfinishedGreavesRule = {BEND_ANY, DRAW_ANY, HIT_ANY};
        final ForgeRule[] unfinishedBootsRule = {SHRINK_THIRD_LAST, BEND_SECOND_LAST, BEND_LAST};
        final ForgeRule[] shieldRule = {BEND_THIRD_LAST, BEND_SECOND_LAST, UPSET_LAST};

        for (CompatMetal metal : CompatMetal.values()) {
            if (metal.isDummy()) continue;
            CompatMetalMaterial material = metal.getMetalMaterial();

            if (material != null) {
                anvil(metal, CompatMetal.ItemType.DOUBLE_INGOT, CompatMetal.ItemType.SHEET, false, hitX3);
                anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.ROD), 2), metal.tier(), false, hitX3);

                for (CompatMetal.ItemType itemType : CompatMetal.ItemType.values()) {
                    if (CompatMetal.makeItem(material, itemType)) {
                        switch (itemType) {
                            case PICKAXE_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.PICKAXE_HEAD)), metal.tier(), true, pickaxeHeadRule);
                            case SHOVEL_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SHOVEL_HEAD)), metal.tier(), true, shovelHeadRule);
                            case AXE_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.AXE_HEAD)), metal.tier(), true, axeHeadRule);
                            case HOE_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.HOE_HEAD)), metal.tier(), true, hoeHeadRule);

                            case PROPICK_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.PROPICK_HEAD)), metal.tier(), true, propickHeadRule);
                            case SAW_BLADE -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SAW_BLADE)), metal.tier(), true, sawBladeRule);
                            case KNIFE_BLADE -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.KNIFE_BLADE)), metal.tier(), true, knifeBladeRule);
                            case SCYTHE_BLADE -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SCYTHE_BLADE)), metal.tier(), true, scytheBladeRule);
                            case CHISEL_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.CHISEL_HEAD)), metal.tier(), true, chiselHeadRule);
                            case HAMMER_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.HAMMER_HEAD)), metal.tier(), true, hammerHeadRule);
                            case JAVELIN_HEAD -> anvil(Ingredient.of(material.ingot().get()), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.JAVELIN_HEAD)), metal.tier(), true, javelinHeadRule);

                            case SWORD_BLADE -> anvil(metal, CompatMetal.ItemType.DOUBLE_INGOT, CompatMetal.ItemType.SWORD_BLADE, true, swordBladeRule);
                            case MACE_HEAD -> anvil(metal, CompatMetal.ItemType.DOUBLE_INGOT, CompatMetal.ItemType.MACE_HEAD, true, maceHeadRule);

                            case UNFINISHED_HELMET -> anvil(metal, CompatMetal.ItemType.DOUBLE_SHEET, CompatMetal.ItemType.UNFINISHED_HELMET, true, unfinishedHelmetRule);
                            case UNFINISHED_CHESTPLATE -> anvil(metal, CompatMetal.ItemType.DOUBLE_SHEET, CompatMetal.ItemType.UNFINISHED_CHESTPLATE, true, unfinishedChestplateRule);
                            case UNFINISHED_LEGGINGS -> anvil(metal, CompatMetal.ItemType.DOUBLE_SHEET, CompatMetal.ItemType.UNFINISHED_LEGGINGS, true, unfinishedGreavesRule);
                            case UNFINISHED_BOOTS -> anvil(metal, CompatMetal.ItemType.SHEET, CompatMetal.ItemType.UNFINISHED_BOOTS, true, unfinishedBootsRule);
                        }
                    }
                }
            }
        }
        anvil(TFCItems.METAL_ITEMS.get(Metal.CAST_IRON).get(Metal.ItemType.INGOT).get(), Items.CHAIN, 16, 2, DRAW_NOT_LAST, HIT_ANY);

        anvil(Metal.CAST_IRON, Metal.ItemType.INGOT, ModItems.UNFINISHED_LANTERN.get(), 1, DRAW_THIRD_LAST, BEND_SECOND_LAST, BEND_LAST);
        anvil(Metal.WROUGHT_IRON, Metal.ItemType.SHEET, Items.BUCKET, 3, false, BEND_THIRD_LAST, BEND_SECOND_LAST, HIT_LAST);
    }

    private void anvil(CompatMetal metal, CompatMetal.ItemType inputType, CompatMetal.ItemType outputType, boolean applyForgingBonus, ForgeRule... rules)
    {
        anvil(ingredientOf(metal, inputType), ItemStackProvider.of(ModItems.METAL_ITEMS.get(metal).get(outputType)), metal.tier(), applyForgingBonus, rules);
    }

    private void anvil(Metal metal, Metal.ItemType inputType, Item outputItem, int minTier, boolean applyForgingBonus, ForgeRule... rules) {
        anvil(ingredientOf(metal, inputType), ItemStackProvider.of(outputItem), minTier, applyForgingBonus, rules);
    }

    private void anvil(CompatMetal metal, CompatMetal.ItemType inputType, Item outputItem, int amount, ForgeRule... rules)
    {
        anvil(ingredientOf(metal, inputType), ItemStackProvider.of(outputItem, amount), metal.tier(), false, rules);
    }

    private void anvil(Item inputItem, Item outputItem, int amount, int minTier, ForgeRule... rules)
    {
        anvil(Ingredient.of(inputItem), ItemStackProvider.of(outputItem, amount), minTier, false, rules);
    }

    private void anvil(Metal metal, Metal.ItemType inputType, Item outputItem, int amount, ForgeRule... rules)
    {
        anvil(ingredientOf(metal, inputType), ItemStackProvider.of(outputItem, amount), metal.tier(), false, rules);
    }

    private void anvil(Ingredient input, ItemStackProvider output, int minTier, boolean applyForgingBonus, ForgeRule... rules)
    {
        add(new AnvilRecipe(input, minTier, List.of(rules), applyForgingBonus, output));
    }
}
