package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.Deposit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class BuiltinDeposits extends DataManagerProvider<Deposit>
{
    public BuiltinDeposits(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Deposit.MANAGER, output, lookup, TerraFirmaCraft.MOD_ID);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        ModBlocks.ORE_DEPOSITS.forEach((oreDeposit, blockId) -> {
            ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(blockId.get());

            add("%s".formatted(blockRes.getPath()), new Deposit(
                    Ingredient.of(blockId),
                    ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "deposit/" + blockRes.getPath())),
                    List.of(
                            Helpers.identifier("item/pan/" + oreDeposit.name().toLowerCase(Locale.ROOT) + "/andesite_full"),
                            Helpers.identifier("item/pan/" + oreDeposit.name().toLowerCase(Locale.ROOT) + "/andesite_half"),
                            Helpers.identifier("item/pan/" + oreDeposit.name().toLowerCase(Locale.ROOT) + "/result")
                    )
            ));
        });
    }
}