package com.bumppo109.firma_compat.entity;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.ENTITY_TYPE,
                    FirmaCompat.MODID
            );

    public static final DeferredHolder<
            EntityType<?>,
            EntityType<ThrownFluidSplashPotion>
            > THROWN_FLUID_SPLASH_POTION =
            ENTITY_TYPES.register(
                    "thrown_fluid_splash_potion",
                    () -> EntityType.Builder
                            .<ThrownFluidSplashPotion>of(
                                    ThrownFluidSplashPotion::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("thrown_fluid_splash_potion")
            );

    public static final DeferredHolder<
                EntityType<?>,
                EntityType<ThrownFluidLingeringPotion>
                > THROWN_FLUID_LINGERING_POTION =
            ENTITY_TYPES.register(
                    "thrown_fluid_lingering_potion",
                    () -> EntityType.Builder
                            .<ThrownFluidLingeringPotion>of(
                                    ThrownFluidLingeringPotion::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("thrown_fluid_lingering_potion")
            );

    private ModEntities() {
    }
}
