package com.bumppo109.firma_compat.util;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> PREVENT_INTERACTION = createTag("prevent_interaction");
        public static final TagKey<Block> TWIGS = createTag("twigs");
        public static final TagKey<Block> UNLIT_BLOCKS = createTag("unlit_blocks");

        public static final TagKey<Block> CHISELED_BOOKSHELVES = createTag("chiseled_bookshelves");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> PREVENT_INTERACTION = createTag("prevent_interaction");
        //Dye
        public static final TagKey<Item> MAKES_WHITE_DYE = createTag("makes_white");
        public static final TagKey<Item> MAKES_LIGHT_GRAY_DYE = createTag("makes_light_gray_dye");
        public static final TagKey<Item> MAKES_GRAY_DYE = createTag("makes_gray_dye");
        public static final TagKey<Item> MAKES_BLACK_DYE = createTag("makes_black_dye");
        public static final TagKey<Item> MAKES_BROWN_DYE = createTag("makes_brown_dye");
        public static final TagKey<Item> MAKES_RED_DYE = createTag("makes_red_dye");
        public static final TagKey<Item> MAKES_ORANGE_DYE = createTag("makes_orange_dye");
        public static final TagKey<Item> MAKES_YELLOW_DYE = createTag("makes_yellow_dye");
        public static final TagKey<Item> MAKES_LIME_DYE = createTag("makes_lime_dye");
        public static final TagKey<Item> MAKES_GREEN_DYE = createTag("makes_green_dye");
        public static final TagKey<Item> MAKES_CYAN_DYE = createTag("makes_cyan_dye");
        public static final TagKey<Item> MAKES_LIGHT_BLUE_DYE = createTag("makes_light_blue_dye");
        public static final TagKey<Item> MAKES_BLUE_DYE = createTag("makes_blue_dye");
        public static final TagKey<Item> MAKES_PURPLE_DYE = createTag("makes_purple_dye");
        public static final TagKey<Item> MAKES_MAGENTA_DYE = createTag("makes_magenta_dye");
        public static final TagKey<Item> MAKES_PINK_DYE = createTag("makes_pink_dye");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class PlacedFeatures {

        public static final TagKey<PlacedFeature> TWIG_PATCHES = createTag("twig_patches");
        public static final TagKey<PlacedFeature> COMPAT_VEINS = createTag("compat_veins");

        private static TagKey<PlacedFeature> createTag(String name) {
            return TagKey.create(
                    Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name)
            );
        }
    }

    public static class Biomes {

        public static final TagKey<Biome> FORESTED_BIOMES = createTag("forested_biomes");

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name)
            );
        }
    }

    public static class Fluids {
        private static TagKey<Fluid> createTag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }

        public static final TagKey<Fluid> WATERLOGGING_WATER = createTag("waterlogging_water");

    }

    public static class Entities {
        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }

        public static final TagKey<EntityType<?>> REMOVE_VANILLA_MOBS = createTag("remove_vanilla_mobs");
        public static final TagKey<EntityType<?>> COMPAT_TFC_ENTITIES = createTag("compat_tfc_entities");
        public static final TagKey<EntityType<?>> CAN_SURVIVE_SALT_WATER = createTag("can_survive_salt_water");

    }
}
