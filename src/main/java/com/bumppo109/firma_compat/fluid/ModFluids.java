package com.bumppo109.firma_compat.fluid;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.fluids.FluidHolder;
import net.dries007.tfc.common.fluids.FluidId;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.dries007.tfc.common.fluids.MoltenFluid;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidType.Properties;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ModFluids {
    public static final DeferredRegister<Fluid> FLUID = DeferredRegister.create(Registries.FLUID, FirmaCompat.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, FirmaCompat.MODID);

    public static final Map<CompatMetal, FluidHolder<BaseFlowingFluid>> METALS = Helpers.mapOf(CompatMetal.class, metal -> register(
            "metal/" + metal.getSerializedName(),
            properties -> properties
                    .block(ModBlocks.METAL_FLUIDS.get(metal))
                    .bucket(ModItems.METAL_FLUID_BUCKETS.get(metal))
                    .explosionResistance(100),
            lavaLike()
                    .descriptionId("fluid.firma_compat.metal." + metal.getSerializedName())
                    .canConvertToSource(false),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    ));

    private static Properties lavaLike()
    {
        return Properties.create()
                .adjacentPathType(PathType.LAVA)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .lightLevel(15)
                .density(3000)
                .viscosity(6000)
                .temperature(1300)
                .canConvertToSource(false)
                .canDrown(false)
                .canExtinguish(false)
                .canHydrate(false)
                .canPushEntity(false)
                .canSwim(false)
                .supportsBoating(false);
    }

    private static FluidType.Properties waterLike()
    {
        return FluidType.Properties.create()
                .adjacentPathType(PathType.WATER)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .canConvertToSource(true)
                .canDrown(true)
                .canExtinguish(true)
                .canHydrate(true)
                .canPushEntity(true)
                .canSwim(true)
                .supportsBoating(true);
    }

    private static <F extends FlowingFluid> FluidHolder<F> register(String name, Consumer<BaseFlowingFluid.Properties> builder, Properties typeProperties, Function<BaseFlowingFluid.Properties, F> sourceFactory, Function<BaseFlowingFluid.Properties, F> flowingFactory)
    {
        // Names `metal/foo` to `metal/flowing_foo`
        final int index = name.lastIndexOf('/');
        final String flowingName = index == -1 ? "flowing_" + name : name.substring(0, index) + "/flowing_" + name.substring(index + 1);

        return RegistrationHelpers.registerFluid(FLUID_TYPES, FLUID, name, name, flowingName, builder, () -> new FluidType(typeProperties), sourceFactory, flowingFactory);
    }
}
