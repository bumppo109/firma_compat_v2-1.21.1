package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatMetalMaterial;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
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
                Metal.GOLD.getSerializedName(), new FluidHeat(TFCFluids.METALS.get(Metal.GOLD).getSource(), 1060, 0.008571429f)
        ));

        //food
        add(Items.KELP, 1.0f);
        add(Items.DRIED_KELP, 1.0f);

        //misc
        add(Items.WET_SPONGE, 1.0f);
        add(ModItems.UNFIRED_POT, 2.5f);

        add("raw_iron_block", Ingredient.of(Items.RAW_IRON_BLOCK), Metal.CAST_IRON, 100);
        add("raw_iron", Ingredient.of(Items.RAW_IRON), Metal.CAST_IRON, 10);
        add("raw_copper_block", Ingredient.of(Items.RAW_COPPER_BLOCK), Metal.COPPER, 100);
        add("raw_copper", Ingredient.of(Items.RAW_COPPER), Metal.COPPER, 10);
        add("raw_gold_block", Ingredient.of(Items.RAW_GOLD_BLOCK), Metal.GOLD, 100);
        add("raw_gold", Ingredient.of(Items.RAW_GOLD), Metal.GOLD, 10);
        add("chain", Ingredient.of(Items.CHAIN), Metal.CAST_IRON, 6);
        add("iron_nugget", Ingredient.of(Items.IRON_NUGGET), Metal.CAST_IRON, 10);
        add("gold_nugget", Ingredient.of(Items.GOLD_NUGGET), Metal.GOLD, 10);

        ModItems.METAL_ITEMS.forEach((compatMetal, itemTypeItemIdMap) -> {
            itemTypeItemIdMap.forEach((itemType, itemId) -> {
                if (compatMetal.isDummy()) return;
                switch (itemType) {
                    case DOUBLE_SHEET, UNFINISHED_HELMET, UNFINISHED_LEGGINGS, UNFINISHED_CHESTPLATE -> addAndMelt(itemId.get(), compatMetal, 400);
                    case DOUBLE_INGOT, SHEET, SWORD_BLADE, MACE_HEAD,
                         UNFINISHED_BOOTS -> addAndMelt(itemId.get(), compatMetal, 200);
                    case PICKAXE_HEAD, AXE_HEAD, SHOVEL_HEAD, HOE_HEAD, PROPICK_HEAD,
                         CHISEL_HEAD, HAMMER_HEAD, SAW_BLADE, KNIFE_BLADE, SCYTHE_BLADE, JAVELIN_HEAD-> addAndMelt(itemId.get(), compatMetal, 100);
                    case ROD -> addAndMelt(itemId.get(), compatMetal, 50);
                    case MACE -> add(compatMetal.getSerializedName() + "_mace", Ingredient.of(itemId.get()), compatMetal, 200);
                    case TUYERE -> add(compatMetal.getSerializedName() + "_tuyere", Ingredient.of(itemId.get()), compatMetal, 400);
                    default -> add(compatMetal.getSerializedName() + "_" + itemType.name().toLowerCase(Locale.ROOT),
                            Ingredient.of(itemId.get()), compatMetal, 100);
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
                        Ingredient.of((ItemLike) supplier.get()), metal, units);
            }
        }
        addAndMelt(Items.NETHERITE_SCRAP, CompatMetal.SCRAP_NETHERITE, 25);
        add("unfinished_lantern", Ingredient.of(ModItems.UNFINISHED_LANTERN), Metal.CAST_IRON, 100);

        for (CompatMetalWeathered weathered : CompatMetalWeathered.values()) {
            for (CompatMetalSet set : weathered) {
                set.parts().forEach(part ->
                        add(part.id(),
                                Ingredient.of(part.block()),
                                Metal.COPPER,
                                part.amount()
                        )
                );
            }
        }

        List.of("", "waxed").forEach(waxed -> {
            List.of("", "exposed", "weathered", "oxidized").forEach(state -> {
                List.of("copper_door", "copper_trapdoor").forEach(door -> {
                    String idStr = waxed.isEmpty() ? state.isEmpty() ? door : state + "_" + door : state.isEmpty() ? waxed + "_" + door : waxed + "_" + state + "_" + door;
                    Item item = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(idStr));
                    add(idStr,
                            Ingredient.of(item),
                            Metal.COPPER,
                            200
                    );
                });
            });
        });
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
