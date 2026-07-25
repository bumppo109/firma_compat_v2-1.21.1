package com.bumppo109.firma_compat.datagen.tags;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.datagen.ModAccessors;
import net.dries007.tfc.common.fluids.FluidHolder;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.bumppo109.firma_compat.util.ModTags.Fluids.WATERLOGGING_WATER;

public class BuiltinFluidTags extends TagsProvider<Fluid> implements ModAccessors
{
    private final ExistingFileHelper.IResourceType resourceType;

    final CompletableFuture<?> before;

    public BuiltinFluidTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<?> before)
    {
        super(event.getGenerator().getPackOutput(), Registries.FLUID, lookup, FirmaCompat.MODID, event.getExistingFileHelper());
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
        this.before = before;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(WATERLOGGING_WATER)
                .add(Fluids.WATER)
                .add(Fluids.FLOWING_WATER)
                .add(TFCFluids.RIVER_WATER.get())
                .add(TFCFluids.SALT_WATER.getSource())
                .add(TFCFluids.SALT_WATER.getFlowing())
                .add(TFCFluids.SPRING_WATER.getSource())
                .add(TFCFluids.SPRING_WATER.getFlowing())
        ;
    }

    @Override
    protected FluidTagAppender tag(TagKey<Fluid> tag)
    {
        return new FluidTagAppender(getOrCreateRawBuilder(tag), modId);
    }

    @SuppressWarnings("UnusedReturnValue")
    static class FluidTagAppender extends TagAppender<Fluid> implements ModAccessors
    {
        FluidTagAppender(TagBuilder builder, String modId)
        {
            super(builder);
        }

        FluidTagAppender add(Fluid... fluids) { return add(Arrays.stream(fluids)); }
        FluidTagAppender add(Stream<Fluid> fluids) { fluids.forEach(b -> add(key(b))); return this; }
        FluidTagAppender add(Map<?, ? extends FluidHolder<? extends Fluid>> fluids) { fluids.values().forEach(v -> add(v.getSource())); return this; }
        FluidTagAppender add(FluidIngredient ingredient)
        {
            switch (ingredient)
            {
                case TagFluidIngredient tag -> addTag(tag.tag());
                case SingleFluidIngredient item -> add(item.fluid().value());
                case CompoundFluidIngredient comp -> comp.children().forEach(this::add);
                default -> throw new AssertionError("Unhandled ingredient type: " + ingredient);
            }
            return this;
        }

        @Override public FluidTagAppender addTag(TagKey<Fluid> tag) { return (FluidTagAppender) super.addTag(tag); }

        private ResourceKey<Fluid> key(Fluid fluid)
        {
            return BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow();
        }
    }
}
