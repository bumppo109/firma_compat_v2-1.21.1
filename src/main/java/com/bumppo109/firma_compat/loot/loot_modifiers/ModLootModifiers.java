package com.bumppo109.firma_compat.loot.loot_modifiers;

import com.bumppo109.firma_compat.FirmaCompat;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final String MODID = FirmaCompat.MODID;

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    public static final Supplier<MapCodec<ItemSwapModifier>> ITEM_SWAP =
            SERIALIZERS.register("item_swap", () -> ItemSwapModifier.CODEC);

    public static final Supplier<MapCodec<ItemRemoverLootModifier>> ITEM_REMOVER =
            SERIALIZERS.register("item_remover", () -> ItemRemoverLootModifier.CODEC);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

}
