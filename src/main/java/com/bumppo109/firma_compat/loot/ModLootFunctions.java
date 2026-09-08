package com.bumppo109.firma_compat.loot;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModLootFunctions
{
    public static final DeferredRegister<LootItemFunctionType<?>> FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, FirmaCompat.MODID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<CopyLampStateFunction>> COPY_LAMP_STATE =
            FUNCTIONS.register("copy_lamp_state",
                    () -> new LootItemFunctionType<>(CopyLampStateFunction.CODEC));

}