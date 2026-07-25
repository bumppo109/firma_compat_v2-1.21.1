package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatMetalSet;
import com.bumppo109.firma_compat.block.CompatMetalWeathered;
import com.bumppo109.firma_compat.item.ModItems;
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
import net.minecraft.world.item.Items;
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
    public static List<String> copperType = List.of("copper", "cut_copper", "cut_copper_stairs", "cut_copper_slab", "chiseled_copper", "copper_bulb", "copper_door", "copper_grate", "copper_trapdoor");

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
                Metal.GOLD.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.GOLD).getSource(), 1060, 0.008571429f)
        ));

        //food
        add(Items.KELP, 1.0f);

        add("chain", Ingredient.of(Items.CHAIN), Metal.CAST_IRON, 6);
        add("iron_nugget", Ingredient.of(Items.IRON_NUGGET), Metal.CAST_IRON, 10);
        add("gold_nugget", Ingredient.of(Items.GOLD_NUGGET), Metal.GOLD, 10);

        ModItems.METAL_ITEMS.forEach((metal, items) -> {
            if (metal.isDummy()) return;

            add(metal, CompatMetal.ItemType.DOUBLE_INGOT);
            add(metal, CompatMetal.ItemType.SHEET);
            add(metal, CompatMetal.ItemType.DOUBLE_SHEET);
            add(metal, CompatMetal.ItemType.ROD);

            for (int amount : new int[]{50, 100, 200, 400, 600, 800, 1200}) {
                final ItemLike[] parts = Arrays.stream(CompatMetal.ItemType.values())
                        .filter(type -> !type.isCommonTagPart() && units(type) == amount)
                        .map(items::get)
                        .filter(Objects::nonNull)
                        .toArray(ItemLike[]::new);

                if (parts.length > 0) {
                    add(metal, "parts_" + amount, Ingredient.of(parts), amount);
                }
            }
        });

        for (CompatMetalWeathered weathered : CompatMetalWeathered.values()) {
            for (CompatMetalSet set : weathered) {
                set.parts().forEach(part ->
                        add(
                                part.id(),
                                Ingredient.of(part.block()),
                                Metal.COPPER,
                                part.amount()
                        )
                );
            }
        }

        addAndMelt(Items.NETHERITE_SCRAP, CompatMetal.SCRAP_NETHERITE, 25);
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
