package com.bumppo109.firma_compat.data;

import com.bumppo109.firma_compat.FirmaCompat;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents
{
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, FirmaCompat.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIT =
            COMPONENTS.registerComponentType(
                    "lit",
                    builder -> builder
                            .persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
            );
}
