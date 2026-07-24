package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.util.data.Support;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BuiltinSupports extends DataManagerProvider<Support>
{
    public BuiltinSupports(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Support.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add("firma_compat_horizontal_support_beam", new Support(BlockIngredient.of(
                Arrays.stream(CompatWood.values()).map(w -> ModBlocks.WOODS.get(w).get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get())),
                2, 2, 4
        ));
    }
}
