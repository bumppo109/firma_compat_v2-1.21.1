package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.block.CompatRockMaterial;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.items.HideItemType;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.HeatIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.AddHeatModifier;
import net.dries007.tfc.common.recipes.outputs.CopyInputModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.function.Function;

public interface ModBarrelRecipes extends ModRecipes {

    default void barrelRecipes() {

        barrel()
                .input(Items.PRISMARINE_BRICKS)
                .input(fluidOf(DyeColor.BLACK), 25)
                .output(Items.DARK_PRISMARINE)
                .sealed(hours(1));
        barrel()
                .input(Items.NETHER_BRICKS)
                .input(fluidOf(DyeColor.RED), 25)
                .output(Items.RED_NETHER_BRICKS)
                .sealed(hours(1));
        barrel()
                .input(ModBlocks.AQUEDUCTS.get(CompatRockMaterial.NETHERRACK).asItem())
                .input(fluidOf(DyeColor.RED), 25)
                .output(ModBlocks.RED_NETHER_BRICK_AQUEDUCT.get())
                .sealed(hours(1));
    }

    private void alcohol(Ingredient item, SimpleFluid fluid)
    {
        barrel()
                .input(notRotten(item))
                .input(Fluids.WATER, 500)
                .output(fluidOf(fluid), 500)
                .sealed(hours(72));
    }

    private void cooling(Fluid fluid, float amount)
    {
        barrel(BuiltInRegistries.FLUID.getKey(fluid).getPath() + "_cooling")
                .input(HeatIngredient.min(1f))
                .input(fluid, 1)
                .output(ItemStackProvider.of(
                        CopyInputModifier.INSTANCE,
                        AddHeatModifier.of(amount)
                ))
                .sound(SoundEvents.FIRE_EXTINGUISH)
                .instant();
    }

    private void removeDye(TagKey<Item> input, ItemLike output)
    {
        barrel("bleaching_" + nameOf(output).replace("white_", "").replace("/white", ""))
                .input(input)
                .input(fluidOf(SimpleFluid.LYE), 25)
                .output(output)
                .sealed(hours(1));
    }

    private void removeDye(String prefix, TagKey<Item> input, ItemLike output)
    {
        barrel("bleaching_" + prefix + "_" + nameOf(output).replace("white_", "").replace("/white", ""))
                .input(input)
                .input(fluidOf(SimpleFluid.LYE), 25)
                .output(output)
                .sealed(hours(1));
    }

    private void dye(ItemLike input, String baseName)
    {
        dye(input, color -> itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_" + baseName)));
    }

    private void dye(ItemLike input, Function<DyeColor, ItemLike> output)
    {
        for (DyeColor color : DyeColor.values())
            if (input.asItem() != output.apply(color).asItem())
                barrel()
                        .input(fluidOf(color), 25)
                        .input(input)
                        .output(output.apply(color))
                        .sealed(hours(1));
    }

    private void musicDisc(DyeColor color, ItemLike output)
    {
        barrel()
                .input(fluidOf(color), 25)
                .input(TFCItems.BLANK_DISC)
                .output(output)
                .sealed(hours(1));
    }

    private TFCItems.ItemId itemOf(HideItemType type, HideItemType.Size size)
    {
        return TFCItems.HIDES.get(type).get(size);
    }

    private SizedIngredient notRotten(Ingredient item)
    {
        return new SizedIngredient(AndIngredient.of(item, NotRottenIngredient.INSTANCE), 1);
    }

    private BarrelRecipe.Builder barrel()
    {
        return new BarrelRecipe.Builder(r -> {
            if (!r.getResultItem().isEmpty()) add("barrel", nameOf(r.getResultItem().getItem()), r);
            else if (!r.getOutputFluid().isEmpty()) add("barrel", nameOf(r.getOutputFluid().getFluid()), r);
            else throw new IllegalStateException("Barrel recipe requires a custom name!");
        });
    }

    private BarrelRecipe.Builder barrel(String name)
    {
        return new BarrelRecipe.Builder(r -> add("barrel", name, r));
    }

}
