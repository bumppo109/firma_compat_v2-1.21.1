package com.bumppo109.firma_compat.blockentity;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    FirmaCompat.MODID
            );

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<FluidBrewingStandBlockEntity>
            > FLUID_BREWING_STAND =
            BLOCK_ENTITIES.register(
                    "fluid_brewing_stand",
                    () -> BlockEntityType.Builder.of(
                            FluidBrewingStandBlockEntity::new,
                            ModBlocks.FLUID_BREWING_STAND.get()
                    ).build(null)
            );

    private ModBlockEntities() {}
}
