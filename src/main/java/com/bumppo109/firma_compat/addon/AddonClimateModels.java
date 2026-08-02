package com.bumppo109.firma_compat.addon;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.eclipticseasons.EclipticSeasonsClimateModel;
import io.netty.buffer.ByteBuf;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AddonClimateModels {

    // The registry key (matches what TFC uses)
    public static final ResourceKey<Registry<ClimateModelType<?>>> KEY =
            ResourceKey.createRegistryKey(Helpers.identifier("climate_model"));

    // DeferredRegister for adding entries to TFC's existing registry
    public static final DeferredRegister<ClimateModelType<?>> TYPES =
            DeferredRegister.create(KEY, FirmaCompat.MODID);

    // Your custom model entry
    public static final Id<EclipticSeasonsClimateModel> ECLIPTIC_MODEL = register("ecliptic", EclipticSeasonsClimateModel.STREAM_CODEC);

    private static <T extends ClimateModel> Id<T> register(String id, StreamCodec<ByteBuf, T> codec) {
        return new Id<>(TYPES.register(id, () -> new ClimateModelType<>(codec)));
    }

    // Holder record (unchanged)
    public static record Id<T extends ClimateModel>(
            DeferredHolder<ClimateModelType<?>, ClimateModelType<T>> holder)
            implements RegistryHolder<ClimateModelType<?>, ClimateModelType<T>> {
    }
}
