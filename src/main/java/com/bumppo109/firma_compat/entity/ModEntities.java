package com.bumppo109.firma_compat.entity;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.ENTITY_TYPE,
                    FirmaCompat.MODID
            );

    public static final Supplier<EntityType<FluidPotionProjectile>>
            FLUID_SPLASH_POTION =
            ENTITY_TYPES.register(
                    "fluid_splash_potion",
                    () -> EntityType.Builder
                            .<FluidPotionProjectile>of(
                                    FluidPotionProjectile::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("fluid_splash_potion")
            );

    public static final Supplier<EntityType<FluidPotionProjectile>>
            FLUID_LINGERING_POTION =
            ENTITY_TYPES.register(
                    "fluid_lingering_potion",
                    () -> EntityType.Builder
                            .<FluidPotionProjectile>of(
                                    FluidPotionProjectile::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("fluid_lingering_potion")
            );

    private ModEntities() {
    }
}
