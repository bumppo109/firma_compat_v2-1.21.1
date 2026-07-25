package com.bumppo109.firma_compat.datagen.recipe;

import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.world.item.DyeColor;

import java.util.List;

public interface ModRemoveRecipes extends ModRecipes
{
    default void removeRecipes() {
        remove(
                "bread",
                "crafter",
                "sugar_from_sugar_cane",
                "sugar_from_honey_bottle",
                "raw_iron_block",
                "raw_iron",
                "raw_copper",
                "raw_copper_block",
                "raw_gold",
                "raw_gold_block",
                "iron_ingot_from_nuggets",
                "gold_ingot_from_nuggets",
                "gold_ingot_from_gold_block",
                "pumpkin_pie",
                "cake",
                "cookie",
                "copper_block",
                "waxed_copper_block_from_honeycomb",
                "netherite_sword_smithing",
                "netherite_ingot",
                "netherite_axe",
                "netherite_pickaxe",
                "netherite_shovel",
                "netherite_hoe",
                "netherite_axe_smithing",
                "netherite_pickaxe_smithing",
                "netherite_shovel_smithing",
                "netherite_hoe_smithing",
                "netherite_helmet",
                "netherite_chestplate",
                "netherite_leggings",
                "netherite_boots",
                "netherite_helmet_smithing",
                "netherite_chestplate_smithing",
                "netherite_leggings_smithing",
                "netherite_boots_smithing",
                "netherite_upgrade_smithing_template",
                "iron_block",
                "iron_ingot_from_iron_block",
                "iron_bars",
                "iron_nugget",
                "gold_nugget",
                "chain",
                "gold_block",
                "emerald_block",
                "emerald",
                "copper_ingot",
                "copper_ingot_from_waxed_copper_block",
                "lapis_block",
                "lapis_lazuli",
                "diamond_block",
                "netherite_block",
                "netherite_ingot_from_netherite_block",
                "diamond",
                "quartz_bricks_from_quartz_block_stonecutting",
                "quartz_pillar",
                "quartz_bricks",
                "quartz_block",
                "chiseled_quartz_block",
                "nether_bricks",
                "chiseled_nether_bricks",
                "red_nether_bricks",
                "purpur_pillar",
                "stone_bricks",
                "cobblestone",
                "smooth_stone",
                "cracked_stone_bricks",
                "chiseled_stone_bricks",
                "granite",
                "polished_granite",
                "andesite",
                "polished_andesite",
                "diorite",
                "polished_diorite",
                "stone_brick_slab_from_stone_stonecutting",
                "stone_brick_stairs_from_stone_stonecutting",
                "stone_brick_walls_from_stone_stonecutting",
                "chiseled_stone_bricks_from_stone_stonecutting",
                "stone_bricks_from_stone_stonecutting",
                "stone_pressure_plate",
                "stone_button",
                "tuff_bricks",
                "tuff_bricks_from_tuff_stonecutting",
                "tuff_bricks_from_polished_tuff_stonecutting",
                "tuff_brick_stairs_from_tuff_stonecutting",
                "tuff_brick_slab_from_tuff_stonecutting",
                "tuff_brick_wall_from_tuff_stonecutting",
                "chiseled_tuff",
                "chiseled_tuff_from_tuff_stonecutting",
                "chiseled_tuff_bricks",
                "chiseled_tuff_bricks_from_tuff_stonecutting",
                "polished_tuff",
                "deepslate_bricks",
                "cobbled_deepslate",
                "chiseled_deepslate",
                "polished_deepslate",
                "cracked_deepslate_bricks",
                "cracked_deepslate_tiles",
                "deepslate_brick_slab_from_cobbled_deepslate_stonecutting",
                "deepslate_brick_stairs_from_cobbled_deepslate_stonecutting",
                "deepslate_brick_walls_from_cobbled_deepslate_stonecutting",
                "deepslate_bricks_from_cobbled_deepslate_stonecutting",
                "deepslate_tiles_from_cobbled_deepslate_stonecutting",
                "sandstone",
                "smooth_sandstone",
                "cut_sandstone",
                "chiseled_sandstone",
                "red_sandstone",
                "smooth_red_sandstone",
                "cut_red_sandstone",
                "chiseled_red_sandstone",
                "prismarine",
                "prismarine_bricks",
                "dark_prismarine",
                "smooth_basalt",
                "polished_basalt",
                "chiseled_polished_blackstone",
                "polished_blackstone",
                "polished_blackstone_bricks",
                "cracked_polished_blackstone_bricks",
                "polished_blackstone_pressure_plate",
                "polished_blackstone_button",
                "polished_blackstone_bricks_from_blackstone_stonecutting",
                "polished_blackstone_brick_slab_from_blackstone_stonecutting",
                "polished_blackstone_brick_stairs_from_blackstone_stonecutting",
                "polished_blackstone_brick_wall_from_blackstone_stonecutting",
                "chiseled_polished_blackstone_from_blackstone_stonecutting",
                "end_stone_bricks",
                "stone_button",
                "stone_pressure_plate",
                "polished_blackstone_button",
                "polished_blackstone_pressure_plate",
                "stone_button",
                "stone_pressure_plate",
                "polished_blackstone_button",
                "polished_blackstone_pressure_plate",
                "packed_mud",
                "mud_bricks",
                "bone_block",
                "bone_meal_from_bone_block",
                "decorated_pot_simple",
                "stonecutter",
                "smithing_table",
                "blast_furnace",
                "cauldron",
                "chiseled_bookshelf",
                "tripwire_hook",
                "piston",
                "daylight_detector",
                "hopper",
                "observer",
                "detector_rail",
                "activator_rail",
                "minecart",
                "tnt",
                "shears",
                "carrot_on_a_stick",
                "warped_fungus_on_a_stick",
                "crossbow",
                "honey_bottle",
                "yellow_dye_from_dandelion",
                "red_dye_from_poppy",
                "light_blue_dye_from_blue_orchid",
                "magenta_dye_from_allium",
                "light_gray_dye_from_azure_bluet",
                "red_dye_from_tulip",
                "orange_dye_from_orange_tulip",
                "light_gray_dye_from_white_tulip",
                "pink_dye_from_pink_tulip",
                "light_gray_dye_from_oxeye_daisy",
                "blue_dye_from_cornflower",
                "white_dye_from_lily_of_the_valley",
                "orange_dye_from_torchflower",
                "black_dye_from_wither_rose",
                "pink_dye_from_pink_petals",
                "yellow_dye_from_sunflower",
                "magenta_dye_from_lilac",
                "red_dye_from_rose_bush",
                "pink_dye_from_peony",
                "cyan_dye_from_pitcher_plant",

                "bamboo_mosaic",
                "bamboo_door",
                "bamboo_trapdoor",
                "bamboo_fence_gate",
                "bamboo_fence",
                "bamboo_pressure_plate",

                "acacia_planks",
                "acacia_door",
                "acacia_trapdoor",
                "acacia_fence",
                "acacia_fence_gate",
                "acacia_pressure_plate",
                "acacia_sign",
                "acacia_hanging_sign",

                "birch_planks",
                "birch_door",
                "birch_trapdoor",
                "birch_fence",
                "birch_fence_gate",
                "birch_pressure_plate",
                "birch_sign",
                "birch_hanging_sign",

                "cherry_planks",
                "cherry_door",
                "cherry_trapdoor",
                "cherry_fence",
                "cherry_fence_gate",
                "cherry_pressure_plate",
                "cherry_sign",
                "cherry_hanging_sign",

                "crimson_planks",
                "crimson_door",
                "crimson_trapdoor",
                "crimson_fence",
                "crimson_fence_gate",
                "crimson_pressure_plate",
                "crimson_sign",
                "crimson_hanging_sign",

                "warped_planks",
                "warped_door",
                "warped_trapdoor",
                "warped_fence",
                "warped_fence_gate",
                "warped_pressure_plate",
                "warped_sign",
                "warped_hanging_sign",

                "dark_oak_planks",
                "dark_oak_door",
                "dark_oak_trapdoor",
                "dark_oak_fence",
                "dark_oak_fence_gate",
                "dark_oak_pressure_plate",
                "dark_oak_sign",
                "dark_oak_hanging_sign",

                "jungle_planks",
                "jungle_door",
                "jungle_trapdoor",
                "jungle_fence",
                "jungle_fence_gate",
                "jungle_pressure_plate",
                "jungle_sign",
                "jungle_hanging_sign",

                "mangrove_planks",
                "mangrove_door",
                "mangrove_trapdoor",
                "mangrove_fence",
                "mangrove_fence_gate",
                "mangrove_pressure_plate",
                "mangrove_sign",
                "mangrove_hanging_sign",

                "oak_planks",
                "oak_door",
                "oak_trapdoor",
                "oak_fence",
                "oak_fence_gate",
                "oak_pressure_plate",
                "oak_sign",
                "oak_hanging_sign",

                "spruce_planks",
                "spruce_door",
                "spruce_trapdoor",
                "spruce_fence",
                "spruce_fence_gate",
                "spruce_pressure_plate",
                "spruce_sign",
                "spruce_hanging_sign",
                "chiseled_stone_bricks_stone_from_stonecutting",
                "deepslate_bricks_from_polished_deepslate_stonecutting",
                "deepslate_brick_wall_from_cobbled_deepslate_stonecutting",
                "deepslate_tile_slab_from_cobbled_deepslate_stonecutting",
                "deepslate_tile_stairs_from_cobbled_deepslate_stonecutting",
                "deepslate_tile_wall_from_cobbled_deepslate_stonecutting",
                "chiseled_deepslate_from_cobbled_deepslate_stonecutting",
                "end_stone_bricks_from_end_stone_stonecutting",
                "polished_blackstone_bricks_from_polished_blackstone_stonecutting",
                "sandstone_stairs",
                "sandstone_slab",
                "red_sandstone_stairs",
                "red_sandstone_slab"
        );

        //Remove crafted Dye
        for(DyeColor color : DyeColor.values()){
            String woolRecipe = "dye_" + color.getSerializedName() + "_wool";
            String carpetRecipe = "dye_" + color.getSerializedName() + "_carpet";
            String glazedTerracottaRecipe = color.getSerializedName() + "_glazed_terracotta";
            String candleRecipe = color.getSerializedName() + "_candle";

            remove(
                    woolRecipe,
                    carpetRecipe,
                    glazedTerracottaRecipe,
                    candleRecipe
            );
        }

        for(String suffix : List.of("copper","chiseled_copper", "cut_copper", "copper_bulb", "copper_door", "copper_trapdoor", "copper_grate")){
            if(suffix.equals("cut_copper")){
                remove(
                        "waxed_" + suffix,
                        "waxed_" + suffix + "_from_waxed_copper_block_stonecutting",
                        "waxed_" + suffix + "_from_honeycomb",
                        "waxed_" + suffix + "_stairs",
                        "waxed_" + suffix + "_stairs_from_waxed_" + suffix + "_stonecutting",
                        "waxed_" + suffix + "_slab",
                        "waxed_" + suffix + "_slab_from_waxed_" + suffix + "_stonecutting",
                        "waxed_" + suffix + "_stairs_from_honeycomb",
                        "waxed_" + suffix + "_slab_from_honeycomb",
                        "waxed_" + suffix + "_stairs_from_waxed_copper_block_stonecutting",
                        "waxed_" + suffix + "_slab_from_waxed_copper_block_stonecutting"
                );
            } else {
                remove(
                        suffix,
                        suffix + "_from_copper_block_stonecutting",
                        "waxed_" + suffix,
                        "waxed_" + suffix + "_from_honeycomb",
                        "waxed_" + suffix + "_from_waxed_copper_block_stonecutting",
                        "waxed_" + suffix + "_from_waxed_cut_copper_stonecutting"
                );
            }
            for(String weather : List.of("exposed", "weathered", "oxidized")){
                if(suffix.equals("cut_copper")){
                    remove(
                            "waxed_" + weather + "_" + suffix,
                            "waxed_" + weather + "_" + suffix + "_from_honeycomb",
                            "waxed_" + weather + "_" + suffix + "_from_waxed_" + weather + "_copper_stonecutting",
                            "waxed_" + weather + "_" + suffix + "_from_waxed_" + weather + "_cut_copper_stonecutting",
                            "waxed_" + weather + "_" + suffix + "_stairs",
                            "waxed_" + weather + "_" + suffix + "_slab",
                            "waxed_" + weather + "_" + suffix + "_stairs_from_honeycomb",
                            "waxed_" + weather + "_" + suffix + "_slab_from_honeycomb",
                            "waxed_" + weather + "_" + suffix + "_stairs_from_waxed_" + weather + "_copper_block_stonecutting",
                            "waxed_" + weather + "_" + suffix + "_stairs_from_waxed_" + weather + "_cut_copper_stonecutting",
                            "waxed_" + weather + "_" + suffix + "_slab_from_waxed_" + weather + "_copper_stairs_stonecutting",
                            "waxed_" + weather + "_" + suffix + "_slab_from_waxed_" + weather + "_cut_copper_stonecutting"
                    );
                } else {
                    remove(
                            weather + "_" + suffix,
                            "waxed_" + weather + "_" + suffix,
                            "waxed_" + weather + "_" + suffix + "_from_honeycomb",
                            "waxed_" + weather + "_" + suffix + "_from_waxed_" + weather + "_copper_stonecutting"
                    );
                }
                if(suffix.equals("chiseled_copper")){
                    remove(
                            "waxed_" + weather + "_" + suffix + "_from_waxed_" + weather + "_copper_block_stonecutting",
                            "waxed_" + weather + "_" + suffix + "_from_waxed_" + weather + "_cut_copper_stonecutting"
                    );
                }
            }
        }

        //Remove TFC
        for(Rock rock : Rock.values()) {
            removeTFC("crafting/rock/cobble/" + rock.getSerializedName() + "_slab");
            removeTFC("crafting/rock/cobble/" + rock.getSerializedName() + "_stairs");
            removeTFC("crafting/rock/cobble/" + rock.getSerializedName() + "_wall");
            removeTFC("chisel/rock/cobble/" + rock.getSerializedName() + "_slab");
            removeTFC("chisel/rock/cobble/" + rock.getSerializedName() + "_stairs");
            removeTFC("crafting/rock/mossy_cobble/" + rock.getSerializedName() + "_slab");
            removeTFC("crafting/rock/mossy_cobble/" + rock.getSerializedName() + "_stairs");
            removeTFC("crafting/rock/mossy_cobble/" + rock.getSerializedName() + "_wall");
            removeTFC("chisel/rock/mossy_cobble/" + rock.getSerializedName() + "_slab");
            removeTFC("chisel/rock/mossy_cobble/" + rock.getSerializedName() + "_stairs");
        }
        for(Wood wood : Wood.values()) {
            removeTFC("crafting/wood/workbench/" + wood.getSerializedName());
            removeTFC("crafting/wood/lectern/" + wood.getSerializedName());
            removeTFC("crafting/wood/bookshelf/" + wood.getSerializedName());
            removeTFC("crafting/wood/chest/" + wood.getSerializedName());
            removeTFC("crafting/wood/trapped_chest/" + wood.getSerializedName());
            removeTFC("crafting/wood/workbench/" + wood.getSerializedName());
            removeTFC("crafting/wood/chest_minecart/" + wood.getSerializedName());
        }
        removeTFC("crafting/metal/block/gold");
    }
}



