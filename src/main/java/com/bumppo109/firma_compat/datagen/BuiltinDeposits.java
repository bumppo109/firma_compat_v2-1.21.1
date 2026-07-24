package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.Deposit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
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

        add("%s".formatted("cassiterite_gravel_deposit"), new Deposit(
                Ingredient.of(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT),
                ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "deposit/cassiterite_gravel_deposit")),
                List.of(
                        Helpers.identifier("item/pan/cassiterite/andesite_full"),
                        Helpers.identifier("item/pan/cassiterite/andesite_half"),
                        Helpers.identifier("item/pan/cassiterite/result")
                )
        ));
        add("%s".formatted("native_silver_gravel_deposit"), new Deposit(
                Ingredient.of(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT),
                ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "deposit/native_silver_gravel_deposit")),
                List.of(
                        Helpers.identifier("item/pan/native_silver/andesite_full"),
                        Helpers.identifier("item/pan/native_silver/andesite_half"),
                        Helpers.identifier("item/pan/native_silver/result")
                )
        ));
        add("%s".formatted("native_copper_gravel_deposit"), new Deposit(
                Ingredient.of(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT),
                ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "deposit/native_copper_gravel_deposit")),
                List.of(
                        Helpers.identifier("item/pan/native_copper/andesite_full"),
                        Helpers.identifier("item/pan/native_copper/andesite_half"),
                        Helpers.identifier("item/pan/native_copper/result")
                )
        ));
        add("%s".formatted("native_gold_gravel_deposit"), new Deposit(
                Ingredient.of(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT),
                ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "deposit/native_gold_gravel_deposit")),
                List.of(
                        Helpers.identifier("item/pan/native_gold/andesite_full"),
                        Helpers.identifier("item/pan/native_gold/andesite_half"),
                        Helpers.identifier("item/pan/native_gold/result")
                )
        ));
    }
}
