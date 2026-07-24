package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.fluid.ModFluids;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.data.FluidHeat;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class BuiltinFluidHeat extends DataManagerProvider<FluidHeat> implements ModAccessors
{
    public static final float HEAT_CAPACITY = 0.003f;

    public BuiltinFluidHeat(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(FluidHeat.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(CompatMetal.NETHERITE, 0.35f, 1485);
        add(CompatMetal.POOR_NETHERITE, 0.35f, 1450);
    }

    private void add(CompatMetal metal, float baseHeatCapacity, float meltTemperature)
    {
        add(metal.getSerializedName(), new FluidHeat(ModFluids.METALS.get(metal).getSource(), meltTemperature, HEAT_CAPACITY / baseHeatCapacity));
    }

}
