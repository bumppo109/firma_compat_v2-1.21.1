package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.common.collect.ImmutableMap;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.data.FluidHeat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public interface ModAccessors
{
    default Ingredient ingredientOf(CompatMetal metal, CompatMetal.ItemType type)
    {
        return type.isCommonTagPart()
                ? Ingredient.of(commonTagOf(metal, type))
                : Ingredient.of(ModItems.METAL_ITEMS.get(metal).get(type).get());
    }

    default Ingredient ingredientOf(Metal metal, Metal.ItemType type)
    {
        return type.isCommonTagPart()
                ? Ingredient.of(commonTagOf(metal, type))
                : Ingredient.of(ModItems.METAL_ITEMS.get(metal).get(type).get());
    }

    default Ingredient ingredientOf(Ingredient... values)
    {
        return CompoundIngredient.of(values);
    }

    default <T> TagKey<T> logsTagOf(ResourceKey<Registry<T>> registry, CompatWood wood)
    {
        return TagKey.create(registry, Helpers.identifier(wood.getSerializedName() + "_logs"));
    }

    default <T> TagKey<T> vanillaLogsTag(ResourceKey<Registry<T>> registry, CompatWood wood)
    {
        return TagKey.create(registry, ResourceLocation.withDefaultNamespace(wood.getSerializedName() + "_logs"));
    }

    default TagKey<Item> commonTagOf(CompatMetal metal, CompatMetal.ItemType type)
    {
        assert type.isCommonTagPart() : "Non-typical use of tag for " + metal.getSerializedName() + " / " + type.name();
        assert type.has(metal) : "Non-typical use of " + metal.getSerializedName() + " / " + type.name();
        return commonTagOf(Registries.ITEM, type.name() + "s/" + metal.name());
    }

    default TagKey<Item> commonTagOf(Metal metal, Metal.ItemType type)
    {
        assert type.isCommonTagPart() : "Non-typical use of tag for " + metal.getSerializedName() + " / " + type.name();
        assert type.has(metal) : "Non-typical use of " + metal.getSerializedName() + " / " + type.name();
        return commonTagOf(Registries.ITEM, type.name() + "s/" + metal.name());
    }

    default TagKey<Block> oreBlockTagOf(Ore ore, @Nullable Ore.Grade grade)
    {
        return commonTagOf(Registries.BLOCK, "ores/" + (ore.isGraded() ? ore.metal().name() : ore.name()) + (grade == null ? "" : "/" + grade.name()));
    }

    default <T> TagKey<T> commonTagOf(ResourceKey<Registry<T>> key, String name)
    {
        return TagKey.create(key, ResourceLocation.fromNamespaceAndPath("c", name.toLowerCase(Locale.ROOT)));
    }

    default Item dyeOf(DyeColor color)
    {
        return itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_dye"));
    }

    default Item dyedOf(DyeColor color, String suffix)
    {
        return itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_" + suffix));
    }

    default Item itemOf(ResourceLocation name)
    {
        assert BuiltInRegistries.ITEM.containsKey(name) : "No item '" + name + "'";
        return BuiltInRegistries.ITEM.get(name);
    }

    default Fluid fluidOf(DyeColor color)
    {
        return TFCFluids.COLORED_FLUIDS.get(color).getSource();
    }

    default Fluid fluidOf(SimpleFluid fluid)
    {
        return TFCFluids.SIMPLE_FLUIDS.get(fluid).getSource();
    }

    default Fluid fluidOf(CompatMetal metal)
    {
        return ModFluids.METALS.get(metal).getSource();
    }

    default Fluid fluidOf(Metal metal)
    {
        return TFCFluids.METALS.get(metal).getSource();
    }

    default String nameOf(Ingredient ingredient)
    {
        if (ingredient.getCustomIngredient() instanceof CompoundIngredient ing) return nameOf(ing.children().get(0));
        final Ingredient.Value value = ingredient.getValues()[0];
        if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) return tag.location().getPath();
        if (value instanceof Ingredient.ItemValue(ItemStack item)) return nameOf(item.getItem());
        throw new AssertionError("Unknown ingredient value");
    }

    default String nameOf(Fluid fluid)
    {
        assert fluid != Fluids.EMPTY : "Should never get name of Items.AIR";
        return BuiltInRegistries.FLUID.getKey(fluid).getPath();
    }

    default String nameOf(ItemLike item)
    {
        assert item.asItem() != Items.AIR : "Should never get name of Items.AIR";
        assert item.asItem() != Items.BARRIER : "Should never get name of Items.BARRIER";
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    default int units(CompatMetal.ItemType type)
    {
        return switch (type)
        {
            case NUGGET -> 5;
            case ROD -> 50;
            case DOUBLE_INGOT, SHEET, SWORD_BLADE, MACE_HEAD, MACE, UNFINISHED_BOOTS -> 200;
            case DOUBLE_SHEET, UNFINISHED_HELMET, UNFINISHED_CHESTPLATE, UNFINISHED_GREAVES, TUYERE -> 400;
            default -> 100;
        };
    }

    default float temperatureOf(CompatMetal metal)
    {
        return FluidHeat.MANAGER.getOrThrow(FirmaCompatHelpers.modIdentifier(metal.getSerializedName())).meltTemperature();
    }

    default float temperatureOf(Metal metal)
    {
        return FluidHeat.MANAGER.getOrThrow(Helpers.identifier(metal.getSerializedName())).meltTemperature();
    }

    default int hours(int hours)
    {
        return hours * ICalendar.CALENDAR_TICKS_IN_HOUR;
    }

    /**
     * Given a {@code Map<T1, Map<T2, V1>>}, and a key {@code T2}, constructs a map of all the mappings of {@code T1} to maps which contain
     * an entry for the given key {@code T2}
     * @return An immutable map, with iteration order given by iteration order of the input map
     */
    default <T1, T2, V> Map<T1, V> pivot(Map<T1, Map<T2, V>> map, T2 key)
    {
        // This method must maintain a consistent, deterministic ordering, so we can't collect into a typical
        // hash map - we must use an order-preserving map here - immutable map is the easiest way to do that
        final ImmutableMap.Builder<T1, V> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<T1, Map<T2, V>> entry : map.entrySet())
            if (entry.getValue().containsKey(key))
                builder.put(entry.getKey(), entry.getValue().get(key));
        return builder.build();
    }

    default BlockGetter empty()
    {
        return new BlockGetter() {
            @Nullable
            @Override
            public BlockEntity getBlockEntity(BlockPos pos)
            {
                return null;
            }

            @Override
            public BlockState getBlockState(BlockPos pos)
            {
                return Blocks.AIR.defaultBlockState();
            }

            @Override
            public FluidState getFluidState(BlockPos pos)
            {
                return Fluids.EMPTY.defaultFluidState();
            }

            @Override
            public int getHeight()
            {
                return 0;
            }

            @Override
            public int getMinBuildHeight()
            {
                return 0;
            }
        };
    }
}