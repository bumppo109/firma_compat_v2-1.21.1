package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.fluid.CompatFluid;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.fluid.Potion;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public interface ModPotionRecipes extends ModRecipes {

    default void potionRecipes() {

        /*
         * ====================================================================
         * AWKWARD POTION -> BASIC POTIONS
         * ====================================================================
         */

        add(
                "water_to_awkward",
                Fluids.WATER,
                Items.NETHER_WART,
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource()
        );

        add(
                "awkward_to_night_vision",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.GOLDEN_CARROT,
                ModFluids.POTIONS.get(Potion.NIGHT_VISION).getSource()
        );

        add(
                "awkward_to_leaping",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.RABBIT_FOOT,
                ModFluids.POTIONS.get(Potion.LEAPING).getSource()
        );

        add(
                "awkward_to_fire_resistance",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.MAGMA_CREAM,
                ModFluids.POTIONS.get(Potion.FIRE_RESISTANCE).getSource()
        );

        add(
                "awkward_to_swiftness",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.SUGAR,
                ModFluids.POTIONS.get(Potion.SWIFTNESS).getSource()
        );

        add(
                "awkward_to_water_breathing",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.PUFFERFISH,
                ModFluids.POTIONS.get(Potion.WATER_BREATHING).getSource()
        );

        add(
                "awkward_to_healing",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.GLISTERING_MELON_SLICE,
                ModFluids.POTIONS.get(Potion.HEALING).getSource()
        );

        add(
                "awkward_to_poison",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.SPIDER_EYE,
                ModFluids.POTIONS.get(Potion.POISON).getSource()
        );

        add(
                "awkward_to_regeneration",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.GHAST_TEAR,
                ModFluids.POTIONS.get(Potion.REGENERATION).getSource()
        );

        add(
                "awkward_to_strength",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.BLAZE_POWDER,
                ModFluids.POTIONS.get(Potion.STRENGTH).getSource()
        );

        add(
                "awkward_to_weakness",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.FERMENTED_SPIDER_EYE,
                ModFluids.POTIONS.get(Potion.WEAKNESS).getSource()
        );

        add(
                "awkward_to_slow_falling",
                ModFluids.FLUIDS.get(CompatFluid.AWKWARD).getSource(),
                Items.PHANTOM_MEMBRANE,
                ModFluids.POTIONS.get(Potion.SLOW_FALLING).getSource()
        );

        /*
         * ====================================================================
         * EXTENDED POTIONS
         * ====================================================================
         */

        add("night_vision_to_extended",
                Potion.NIGHT_VISION,
                Items.REDSTONE,
                Potion.NIGHT_VISION_EXT);

        add("fire_resistance_to_extended",
                Potion.FIRE_RESISTANCE,
                Items.REDSTONE,
                Potion.FIRE_RESISTANCE_EXT);

        add("swiftness_to_extended",
                Potion.SWIFTNESS,
                Items.REDSTONE,
                Potion.SWIFTNESS_EXT);

        add("water_breathing_to_extended",
                Potion.WATER_BREATHING,
                Items.REDSTONE,
                Potion.WATER_BREATHING_EXT);

        add("poison_to_extended",
                Potion.POISON,
                Items.REDSTONE,
                Potion.POISON_EXT);

        add("regeneration_to_extended",
                Potion.REGENERATION,
                Items.REDSTONE,
                Potion.REGENERATION_EXT);

        add("strength_to_extended",
                Potion.STRENGTH,
                Items.REDSTONE,
                Potion.STRENGTH_EXT);

        add("weakness_to_extended",
                Potion.WEAKNESS,
                Items.REDSTONE,
                Potion.WEAKNESS_EXT);

        add("leaping_to_extended",
                Potion.LEAPING,
                Items.REDSTONE,
                Potion.LEAPING_EXT);

        add("slow_falling_to_extended",
                Potion.SLOW_FALLING,
                Items.REDSTONE,
                Potion.SLOW_FALLING_EXT);

        /*
         * ====================================================================
         * LEVEL II
         * ====================================================================
         */

        add("leaping_to_level_ii",
                Potion.LEAPING,
                Items.GLOWSTONE_DUST,
                Potion.LEAPING_II);

        add("swiftness_to_level_ii",
                Potion.SWIFTNESS,
                Items.GLOWSTONE_DUST,
                Potion.SWIFTNESS_II);

        add("slowness_to_level_ii",
                Potion.SLOWNESS,
                Items.GLOWSTONE_DUST,
                Potion.SLOWNESS_II);

        add("healing_to_level_ii",
                Potion.HEALING,
                Items.GLOWSTONE_DUST,
                Potion.HEALING_II);

        add("harming_to_level_ii",
                Potion.HARMING,
                Items.GLOWSTONE_DUST,
                Potion.HARMING_II);

        add("poison_to_level_ii",
                Potion.POISON,
                Items.GLOWSTONE_DUST,
                Potion.POISON_II);

        add("regeneration_to_level_ii",
                Potion.REGENERATION,
                Items.GLOWSTONE_DUST,
                Potion.REGENERATION_II);

        add("strength_to_level_ii",
                Potion.STRENGTH,
                Items.GLOWSTONE_DUST,
                Potion.STRENGTH_II);

        /*
         * ====================================================================
         * CORRUPTION
         * ====================================================================
         */

        add("night_vision_to_invisibility",
                Potion.NIGHT_VISION,
                Items.FERMENTED_SPIDER_EYE,
                Potion.INVISIBILITY);

        add("night_vision_extended_to_invisibility_extended",
                Potion.NIGHT_VISION_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.INVISIBILITY_EXT);

        add("invisibility_to_night_vision",
                Potion.INVISIBILITY,
                Items.FERMENTED_SPIDER_EYE,
                Potion.NIGHT_VISION);

        add("invisibility_extended_to_night_vision_extended",
                Potion.INVISIBILITY_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.NIGHT_VISION_EXT);

        add("leaping_to_slowness",
                Potion.LEAPING,
                Items.FERMENTED_SPIDER_EYE,
                Potion.SLOWNESS);

        add("leaping_extended_to_slowness_extended",
                Potion.LEAPING_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.SLOWNESS_EXT);

        add("swiftness_to_slowness",
                Potion.SWIFTNESS,
                Items.FERMENTED_SPIDER_EYE,
                Potion.SLOWNESS);

        add("swiftness_extended_to_slowness_extended",
                Potion.SWIFTNESS_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.SLOWNESS_EXT);

        add("poison_to_harming",
                Potion.POISON,
                Items.FERMENTED_SPIDER_EYE,
                Potion.HARMING);

        add("poison_extended_to_harming_extended",
                Potion.POISON_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.HARMING);

        add("poison_ii_to_harming_ii",
                Potion.POISON_II,
                Items.FERMENTED_SPIDER_EYE,
                Potion.HARMING_II);

        add("healing_to_harming",
                Potion.HEALING,
                Items.FERMENTED_SPIDER_EYE,
                Potion.HARMING);

        add("healing_ii_to_harming_ii",
                Potion.HEALING_II,
                Items.FERMENTED_SPIDER_EYE,
                Potion.HARMING_II);

        add("fire_resistance_to_slowness",
                Potion.FIRE_RESISTANCE,
                Items.FERMENTED_SPIDER_EYE,
                Potion.SLOWNESS);

        add("fire_resistance_extended_to_slowness_extended",
                Potion.FIRE_RESISTANCE_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.SLOWNESS_EXT);

        add("water_breathing_to_invisibility",
                Potion.WATER_BREATHING,
                Items.FERMENTED_SPIDER_EYE,
                Potion.INVISIBILITY);

        add("water_breathing_extended_to_invisibility_extended",
                Potion.WATER_BREATHING_EXT,
                Items.FERMENTED_SPIDER_EYE,
                Potion.INVISIBILITY_EXT);
    }

    private void add(
            String name,
            Potion input,
            Item catalyst,
            Potion output
    ) {
        add(
                "fluid_brewing",
                name,
                new FluidBrewingRecipe(
                        ModFluids.POTIONS.get(input).getSource(),
                        FluidBrewingRecipe.DEFAULT_INPUT_AMOUNT,
                        Ingredient.of(catalyst),
                        ModFluids.POTIONS.get(output).getSource()
                )
        );
    }

    private void add(
            String name,
            Fluid input,
            Item catalyst,
            Fluid output
    ) {
        add(
                "fluid_brewing",
                name,
                new FluidBrewingRecipe(
                        input,
                        FluidBrewingRecipe.DEFAULT_INPUT_AMOUNT,
                        Ingredient.of(catalyst),
                        output
                )
        );
    }
}
