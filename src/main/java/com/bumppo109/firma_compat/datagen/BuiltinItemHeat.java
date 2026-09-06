package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatMetal;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.data.FluidHeat;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BuiltinItemHeat extends DataManagerProvider<HeatDefinition> implements ModAccessors
{
    public static final float FLUID_HEAT_CAPACITY = 0.003f;

    public final List<MeltingRecipe> meltingRecipes = new ArrayList<>();
    private final CompletableFuture<?> before;

    public BuiltinItemHeat(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<?> before)
    {
        super(HeatCapability.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
        this.before = before;
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> beforeRun()
    {
        return before.thenCompose(v -> super.beforeRun());
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        FirmaCompatHelpers.fakeDataManager(FluidHeat.MANAGER, Map.of(
                Metal.COPPER.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.COPPER).getSource(), 1080, 0.008571429f),
                Metal.CAST_IRON.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.CAST_IRON).getSource(), 1535, 0.008571429f),
                Metal.BISMUTH_BRONZE.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.BISMUTH_BRONZE).getSource(), 985, 0.008571429f),
                Metal.BLACK_BRONZE.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.BLACK_BRONZE).getSource(), 1070, 0.008571429f),
                Metal.BLACK_STEEL.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.BLACK_STEEL).getSource(), 1485, 0.008571429f),
                Metal.BLUE_STEEL.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.BLUE_STEEL).getSource(), 1540, 0.008571429f),
                Metal.RED_STEEL.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.RED_STEEL).getSource(), 1540, 0.008571429f),
                Metal.BRONZE.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.BRONZE).getSource(), 950, 0.008571429f),
                Metal.WROUGHT_IRON.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.WROUGHT_IRON).getSource(), 1535, 0.008571429f),
                Metal.STEEL.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.STEEL).getSource(), 1540, 0.008571429f)
        ));
    }

    private void addIfPresent(Supplier<Item> supplier, CompatMetal metal, int units) {
        if (supplier != null) {
            Item item = supplier.get();
            if (item != null) {
                addAndMelt(item, metal, units);
            }
        }
    }

    private void addAndMelt(ItemLike item, CompatMetal metal, int units)
    {
        meltingRecipes.add(new MeltingRecipe(item, metal, units));
        add(nameOf(item), Ingredient.of(item), metal, units);
    }

    private void add(ItemLike item, float heatCapacity)
    {
        add(Ingredient.of(item), heatCapacity);
    }

    private void add(TagKey<Item> item, float heatCapacity)
    {
        add(Ingredient.of(item), heatCapacity);
    }

    private void add(Ingredient item, float heatCapacity)
    {
        add(nameOf(item), new HeatDefinition(item, heatCapacity, 0f, 0f));
    }

    private void add(CompatMetal metal, CompatMetal.ItemType type)
    {
        add(metal.getSerializedName() + "/" + type.name().toLowerCase(Locale.ROOT), ingredientOf(metal, type), metal, units(type));
    }

    /* TODO - for metal blocks
    private void add(CompatMetal metal, Metal.BlockType type)
    {
        add(metal.getSerializedName() + "/" + type.name().toLowerCase(Locale.ROOT), ingredientOf(metal, type), metal, units(type));
    }
     */

    private void add(CompatMetal metal, String typeName, Ingredient ingredient, int units)
    {
        add(metal.getSerializedName() + "/" + typeName.toLowerCase(Locale.ROOT), ingredient, metal, units);
    }

    private void add(String name, Ingredient ingredient, Metal metal, int units)
    {
        add(name, Helpers.identifier(metal.getSerializedName()), ingredient, units);
    }

    private void add(String name, Ingredient ingredient, CompatMetal metal, int units)
    {
        add(name, FirmaCompatHelpers.modIdentifier(metal.getSerializedName()), ingredient, units);
    }

    public void add(String name, ResourceLocation metalSerializedName, Ingredient ingredient, int units)
    {
        if (FluidHeat.MANAGER.getValues().isEmpty())
        {
            FirmaCompat.LOGGER.error("FluidHeat manager has not been loaded.");
            return;
        }
        final FluidHeat fluidHeat = FluidHeat.MANAGER.getOrThrow(metalSerializedName);
        add(name, new HeatDefinition(
                ingredient,
                (fluidHeat.specificHeatCapacity() / FLUID_HEAT_CAPACITY) * (units / 100f),
                fluidHeat.meltTemperature() * 0.6f,
                fluidHeat.meltTemperature() * 0.8f));
    }
    record MeltingRecipe(ItemLike item, CompatMetal metal, int units) {}
}