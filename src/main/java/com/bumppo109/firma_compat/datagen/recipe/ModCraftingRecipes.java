package com.bumppo109.firma_compat.datagen.recipe;

import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidContentIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.MealModifier;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.dries007.tfc.util.DataGenerationHelpers.Builder;

public interface ModCraftingRecipes extends ModRecipes {

    default void craftingRecipes() {
        //Food
        recipe()
                .input('W', TFCItems.FOOD.get(Food.BEET).get())
                .input('G', Items.BOWL)
                .pattern("WWW", "G  ")
                .shaped(Items.BEETROOT_SOUP);
        recipe()
                .input('R', TFCItems.FOOD.get(Food.COOKED_RABBIT).get())
                .input('C', TFCItems.FOOD.get(Food.CARROT).get())
                .input('B', TFCItems.FOOD.get(Food.BAKED_POTATO).get())
                .input('M', Tags.Items.MUSHROOMS)
                .input('G', Items.BOWL)
                .pattern(" R ", "CBM", " G ")
                .shaped(Items.RABBIT_STEW);
        recipe()
                .input('M', Tags.Items.MUSHROOMS)
                .input('G', Items.BOWL)
                .pattern("MM ", "G  ")
                .shaped(Items.MUSHROOM_STEW);

        for (CompatWood wood : CompatWood.values()) {
            final var blocks = ModBlocks.WOODS.get(wood);
            final var lumber = ModItems.LUMBER.get(wood);
            CompatWoodMaterial material = wood.compatWoodMaterial();

            recipe()
                    .input('W', Objects.requireNonNull(material.strippedLog()))
                    .input('G', TFCItems.GLUE)
                    .pattern("WGW")
                    .shaped(blocks.get(CompatWood.BlockType.AXLE), 4);
            recipe()
                    .input('L', lumber)
                    .pattern("L L", "L L", "LLL")
                    .shaped(blocks.get(CompatWood.BlockType.BARREL));
            recipe()
                    .input(blocks.get(CompatWood.BlockType.AXLE))
                    .input(TFCItems.METAL_ITEMS.get(Metal.STEEL).get(Metal.ItemType.INGOT).get().asItem())
                    .shapeless(blocks.get(CompatWood.BlockType.BLADED_AXLE));
            recipe()
                    .input('L', lumber)
                    .input('S', Objects.requireNonNull(material.strippedLog()))
                    .input('M', TFCItems.BRASS_MECHANISMS)
                    .input('A', blocks.get(CompatWood.BlockType.AXLE))
                    .input('R', Tags.Items.DUSTS_REDSTONE)
                    .pattern("LSL", "MAR", "LSL")
                    .shaped(blocks.get(CompatWood.BlockType.CLUTCH), 2);
            recipe()
                    .input('L', lumber)
                    .input('S', Objects.requireNonNull(material.strippedLog()))
                    .input('A', blocks.get(CompatWood.BlockType.AXLE))
                    .pattern(" S ", "LAL", " S ")
                    .shaped(blocks.get(CompatWood.BlockType.ENCASED_AXLE), 4);
            recipe()
                    .input('L', lumber)
                    .input('M', TFCItems.BRASS_MECHANISMS)
                    .pattern(" L ", "LML", " L ")
                    .shaped(blocks.get(CompatWood.BlockType.GEAR_BOX), 2);
            recipe()
                    .input('P', Objects.requireNonNull(material.log()))
                    .input('L', lumber)
                    .pattern("PLP", "PLP")
                    .shaped(blocks.get(CompatWood.BlockType.LOG_FENCE), 8);
            recipe()
                    .input('L', lumber)
                    .input('S', Tags.Items.RODS_WOODEN)
                    .pattern("LLL", "LSL", "L L")
                    .shaped(blocks.get(CompatWood.BlockType.LOOM));
            recipe("from_logs")
                    .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                    .input(woodLogsTagOf(Registries.ITEM, wood))
                    .damageInputs()
                    .shapeless(lumber, 8);
            recipe("from_planks")
                    .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                    .input(Objects.requireNonNull(material.planks()))
                    .damageInputs()
                    .shapeless(lumber, 4);
            recipe("from_stairs")
                    .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                    .input(Objects.requireNonNull(material.plankStairs()))
                    .damageInputs()
                    .shapeless(lumber, 3);
            recipe("from_slabs")
                    .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                    .input(Objects.requireNonNull(material.plankSlab()))
                    .damageInputs()
                    .shapeless(lumber, 2);
            recipe()
                    .input('F', Tags.Items.FEATHERS)
                    .input('D', Tags.Items.DYES_BLACK)
                    .input('S', Objects.requireNonNull(material.plankSlab()))
                    .input('W', Objects.requireNonNull(material.planks()))
                    .pattern("F D", "SSS", "W W")
                    .shaped(blocks.get(CompatWood.BlockType.SCRIBING_TABLE));
            recipe()
                    .input('S', Tags.Items.TOOLS_SHEAR)
                    .input('L', Tags.Items.LEATHERS)
                    .input('P', Objects.requireNonNull(material.planks()))
                    .input('G', Objects.requireNonNull(material.log()))
                    .pattern(" LS", "PPP", "G G")
                    .shaped(blocks.get(CompatWood.BlockType.SEWING_TABLE));
            recipe()
                    .input('L', lumber)
                    .input('P', Objects.requireNonNull(material.planks()))
                    .input('S', Tags.Items.RODS_WOODEN)
                    .pattern("PPP", "L L", "S S")
                    .shaped(blocks.get(CompatWood.BlockType.SHELF), 2);
            recipe()
                    .input('L', lumber)
                    .input('S', Tags.Items.RODS_WOODEN)
                    .pattern("  S", " SL", "SLL")
                    .shaped(blocks.get(CompatWood.BlockType.SLUICE));
            recipe()
                    .input('L', vanillaLogsTag(Registries.ITEM, wood))
                    .input('S', TFCTags.Items.TOOLS_SAW)
                    .pattern("LS", "L ")
                    .damageInputs()
                    .source(0, 1)
                    .shaped(ModItems.SUPPORTS.get(wood), 8);
            recipe()
                    .input('L', lumber)
                    .pattern("LLL", "   ", "LLL")
                    .shaped(blocks.get(CompatWood.BlockType.TOOL_RACK));
            recipe()
                    .input('L', lumber)
                    .input('P', Objects.requireNonNull(material.planks()))
                    .input('A', blocks.get(CompatWood.BlockType.AXLE))
                    .pattern("LPL", "PAP", "LPL")
                    .shaped(blocks.get(CompatWood.BlockType.WATER_WHEEL));
            recipe()
                    .input('L', lumber)
                    .input('S', Objects.requireNonNull(material.strippedLog()))
                    .pattern("SLS", "L L", "SLS")
                    .shaped(blocks.get(CompatWood.BlockType.CRATE));

            //Replace Vanilla
            recipe().to2x2(lumber, Objects.requireNonNull(material.planks()), 1);

            recipe()
                    .input('L', lumber)
                    .pattern("LL", "LL", "LL")
                    .shaped(Objects.requireNonNull(material.door()));
            recipe()
                    .input('P', Objects.requireNonNull(material.planks()))
                    .input('L', lumber)
                    .pattern("PLP", "PLP")
                    .shaped(Objects.requireNonNull(material.fence()), 8);
            recipe()
                    .input('P', Objects.requireNonNull(material.planks()))
                    .input('L', lumber)
                    .pattern("LPL", "LPL")
                    .shaped(Objects.requireNonNull(material.fenceGate()), 2);
            recipe()
                    .input('L', lumber)
                    .pattern("LLL", "LLL")
                    .shaped(Objects.requireNonNull(material.trapdoor()), 2);
            recipe()
                    .input('L', lumber)
                    .input('S', Tags.Items.RODS_WOODEN)
                    .pattern("LLL", "LLL", " S ")
                    .shaped(Objects.requireNonNull(material.sign()), 3);
            recipe()
                    .input('L', lumber)
                    .input('S', Tags.Items.CHAINS)
                    .pattern("S S", "LLL", "LLL")
                    .shaped(Objects.requireNonNull(material.hangingSign()), 4);
            recipe()
                    .input('L', lumber)
                    .pattern("LL")
                    .shaped(Objects.requireNonNull(material.pressurePlate()));
        }

        //Additional Wood
        recipe().to2x2(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_MOSAIC, 4);
        recipe("from_mosaic_stairs")
                .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                .input(Objects.requireNonNull(Blocks.BAMBOO_MOSAIC_STAIRS))
                .damageInputs()
                .shapeless(ModItems.LUMBER.get(CompatWood.BAMBOO), 3);
        recipe("from_mosaic_slabs")
                .inputIsPrimary(TFCTags.Items.TOOLS_SAW)
                .input(Objects.requireNonNull(Blocks.BAMBOO_MOSAIC_SLAB))
                .damageInputs()
                .shapeless(ModItems.LUMBER.get(CompatWood.BAMBOO), 2);


    //Rock
        //TFC hardened cobble
        for(Rock rock : Rock.values()){
            var tfcMap = TFCBlocks.ROCK_BLOCKS.get(rock);
            var cobbleMap = ModBlocks.TFC_ROCK_BLOCKS.get(rock);
            var decorationMap = TFCBlocks.ROCK_DECORATIONS.get(rock);
            List.of(Rock.BlockType.COBBLE, Rock.BlockType.MOSSY_COBBLE).forEach(blockType -> {
                Block cobble = cobbleMap.get(blockType).get();
                Block loose = blockType.equals(Rock.BlockType.COBBLE) ? tfcMap.get(Rock.BlockType.LOOSE).get() : tfcMap.get(Rock.BlockType.MOSSY_LOOSE).get();
                Block slab = decorationMap.get(blockType).slab().get();
                Block stair = decorationMap.get(blockType).stair().get();
                Block wall = decorationMap.get(blockType).wall().get();

                recipe()
                        .input('L', loose)
                        .input('X', TFCItems.MORTAR)
                        .pattern("LXL", "XLX", "LXL")
                        .shaped(cobble, 4);
                recipe()
                        .input('X', cobble.asItem())
                        .pattern("XXX")
                        .shaped(slab, 6);
                recipe()
                        .input('X', cobble.asItem())
                        .pattern("X  ", "XX ", "XXX")
                        .shaped(stair, 4);
                recipe()
                        .input('X', cobble.asItem())
                        .pattern("XXX", "XXX")
                        .shaped(wall, 6);
            });
        }

        //Rock
        for (CompatRock rock : CompatRock.VALUES) {
            CompatRockMaterial material = rock.rockMaterial();

            if (material != null) {

                Item brickItem = rock.brickItem().get();

                recipe()
                        .input('X', ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem())
                        .pattern("XX ", "XX ")
                        .shaped(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.COBBLE).get(),1);
                recipe()
                        .input('X', ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_LOOSE).get().asItem())
                        .pattern("XX ", "XX ")
                        .shaped(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_COBBLE).get(),1);

                if (material.cobble() != null) {
                    recipe()
                            .input('L', ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem())
                            .input('X', TFCItems.MORTAR)
                            .pattern("LXL", "XLX", "LXL")
                            .shaped(material.cobble().base().get(),1);
                } else {
                    recipe()
                            .input('L', ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem())
                            .input('X', TFCItems.MORTAR)
                            .pattern("LXL", "XLX", "LXL")
                            .shaped(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.HARDENED_COBBLE).get(),1);

                    addDecorations(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.HARDENED_COBBLE).get(),
                            ModBlocks.ROCK_DECORATIONS.get(rock).get(CompatRock.BlockType.HARDENED_COBBLE));
                }
                if (material.mossyCobble() != null) {
                    recipe()
                            .input('L', ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_LOOSE).get().asItem())
                            .input('X', TFCItems.MORTAR)
                            .pattern("LXL", "XLX", "LXL")
                            .shaped(material.mossyCobble().base().get(),1);
                } else {
                    recipe()
                            .input('L', ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_LOOSE).get().asItem())
                            .input('X', TFCItems.MORTAR)
                            .pattern("LXL", "XLX", "LXL")
                            .shaped(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_HARDENED_COBBLE).get(),1);

                    addDecorations(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_HARDENED_COBBLE).get(),
                            ModBlocks.ROCK_DECORATIONS.get(rock).get(CompatRock.BlockType.MOSSY_HARDENED_COBBLE));
                }

            //smooth -> polished -> chiseled
                CompatRockSet previousChisel = material.raw();

                if (material.smooth() != null) {
                    recipe().useTool(
                            TFCTags.Items.TOOLS_CHISEL,
                            previousChisel.base().get(),
                            material.smooth().base().get()
                    );
                    previousChisel = material.smooth();
                }

                if (material.polished() != null) {
                    recipe().useTool(
                            TFCTags.Items.TOOLS_CHISEL,
                            previousChisel.base().get(),
                            material.polished().base().get()
                    );
                    previousChisel = material.polished();
                }

                if (material.chiseled() != null) {
                    recipe().useTool(
                            TFCTags.Items.TOOLS_CHISEL,
                            previousChisel.base().get(),
                            material.chiseled().base().get()
                    );
                }

                if (material.brick() != null) {
                    recipe()
                            .input(Ingredient.of(
                                    ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem(),
                                    ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.MOSSY_LOOSE).get().asItem()))
                            .inputIsPrimary(TFCTags.Items.TOOLS_CHISEL)
                            .damageInputs()
                            .shapeless(brickItem);
                    recipe()
                            .input('L', brickItem)
                            .input('X', TFCItems.MORTAR)
                            .pattern("LXL", "XLX", "LXL")
                            .shaped(material.brick().base().get(), 4);
                    //chiseled brick
                    if (material.chiseledBrick() != null) {
                        recipe().useTool(
                                TFCTags.Items.TOOLS_CHISEL,
                                material.brick().base().get(),
                                material.chiseledBrick().base().get()
                        );
                    }
                    //cracked brick
                    if (material.crackedBrick() != null) {
                        recipe().useTool(
                                TFCTags.Items.TOOLS_HAMMER,
                                material.brick().base().get(),
                                material.crackedBrick().base().get()
                        );
                    }
                    recipe()
                            .input('L', brickItem)
                            .input('X', TFCItems.MORTAR)
                            .pattern("L L", "XLX")
                            .shaped(ModBlocks.AQUEDUCTS.get(material).get().asItem());
                }

                if (material.tile() != null) {
                    //cracked tile
                    if (material.crackedTile() != null) {
                        recipe().useTool(
                                TFCTags.Items.TOOLS_HAMMER,
                                material.tile().base().get(),
                                material.crackedTile().base().get()
                        );
                    }
                }
            }
        }
        //Misc Rock
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, ModItems.PRISMARINE_BRICK.get(), Items.PRISMARINE_SHARD);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, ModItems.QUARTZ_BRICK.get(), Items.QUARTZ);
        Block brickAqueduct = ModBlocks.BRICK_AQUEDUCT.get();
        Block prismarineBrickAqueduct = ModBlocks.PRISMARINE_BRICK_AQUEDUCT.get();
        Block quartzBrickAqueduct = ModBlocks.QUARTZ_BRICK_AQUEDUCT.get();

        recipe()
                .input('L', Items.BRICK)
                .input('X', TFCItems.MORTAR)
                .pattern("L L", "XLX")
                .shaped(brickAqueduct.asItem());
        recipe()
                .input('L', ModItems.PRISMARINE_BRICK)
                .input('X', TFCItems.MORTAR)
                .pattern("L L", "XLX")
                .shaped(prismarineBrickAqueduct.asItem());
        recipe()
                .input('L', ModItems.QUARTZ_BRICK)
                .input('X', TFCItems.MORTAR)
                .pattern("L L", "XLX")
                .shaped(quartzBrickAqueduct.asItem());

        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, ModItems.BRICK.get(CompatRock.BLACKSTONE).get(), Items.POLISHED_BLACKSTONE_BUTTON);
        recipe()
                .input('L', ModItems.BRICK.get(CompatRock.BLACKSTONE).get())
                .pattern("LL ")
                .shaped(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, ModItems.BRICK.get(CompatRock.STONE).get(), Items.STONE_BUTTON);
        recipe()
                .input('L', ModItems.BRICK.get(CompatRock.STONE).get())
                .pattern("LL ")
                .shaped(Blocks.STONE_PRESSURE_PLATE);

    //Metal
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.COPPER_BLOCK, Items.CHISELED_COPPER);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.EXPOSED_COPPER, Items.EXPOSED_CHISELED_COPPER);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.WEATHERED_COPPER, Items.WEATHERED_CHISELED_COPPER);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.OXIDIZED_COPPER, Items.OXIDIZED_CHISELED_COPPER);
        recipe()
                .input('S', ingredientOf(Metal.COPPER, Metal.ItemType.SHEET))
                .input('W', ItemTags.PLANKS)
                .input('H', TFCTags.Items.TOOLS_HAMMER)
                .pattern(" SH", "SWS", " S ")
                .damageInputs()
                .source(0, 2)
                .shaped(Blocks.COPPER_BLOCK, 8);
        recipe()
                .input('S', ingredientOf(Metal.GOLD, Metal.ItemType.SHEET))
                .input('W', ItemTags.PLANKS)
                .input('H', TFCTags.Items.TOOLS_HAMMER)
                .pattern(" SH", "SWS", " S ")
                .damageInputs()
                .source(0, 2)
                .shaped(Blocks.GOLD_BLOCK, 8);
        recipe()
                .input('L', TFCBlocks.METALS.get(Metal.COPPER).get(Metal.BlockType.BARS))
                .pattern(" L ", "L L", " L ")
                .shaped(Items.COPPER_GRATE);
        recipe()
                .input('L', Blocks.COPPER_BLOCK)
                .input('X', TFCItems.BRASS_MECHANISMS)
                .input('A', Tags.Items.DUSTS_REDSTONE)
                .pattern(" L ", "LXL", " A ")
                .shaped(Items.COPPER_BULB);
        recipe()
                .input('L', Blocks.EXPOSED_COPPER)
                .input('X', TFCItems.BRASS_MECHANISMS)
                .input('A', Tags.Items.DUSTS_REDSTONE)
                .pattern(" L ", "LXL", " A ")
                .shaped(Items.EXPOSED_COPPER_BULB);
        recipe()
                .input('L', Blocks.WEATHERED_COPPER)
                .input('X', TFCItems.BRASS_MECHANISMS)
                .input('A', Tags.Items.DUSTS_REDSTONE)
                .pattern(" L ", "LXL", " A ")
                .shaped(Items.WEATHERED_COPPER_BULB);
        recipe()
                .input('L', Blocks.OXIDIZED_COPPER)
                .input('X', TFCItems.BRASS_MECHANISMS)
                .input('A', Tags.Items.DUSTS_REDSTONE)
                .pattern(" L ", "LXL", " A ")
                .shaped(Items.OXIDIZED_COPPER_BULB);

        recipe()
                .input('S', ingredientOf(CompatMetal.NETHERITE, CompatMetal.ItemType.SHEET))
                .input('W', ItemTags.PLANKS)
                .input('H', TFCTags.Items.TOOLS_HAMMER)
                .pattern(" SH", "SWS", " S ")
                .damageInputs()
                .source(0, 2)
                .shaped(Blocks.NETHERITE_BLOCK, 8);

        for (CompatMetal metal : CompatMetal.values()) {
            if (metal.isDummy()) continue;
            CompatMetalMaterial material = metal.getMetalMaterial();

            if (material != null) {
                for (CompatMetal.ItemType itemType : CompatMetal.ItemType.values()) {
                    if (CompatMetal.makeItem(material, itemType)) {
                        Item toolHead = ModItems.METAL_ITEMS.get(metal).get(itemType).get();
                        switch (itemType) {
                            case PICKAXE_HEAD -> makeTool(toolHead, Objects.requireNonNull(material.pickaxe()).get());
                            case SHOVEL_HEAD -> makeTool(toolHead, Objects.requireNonNull(material.shovel()).get());
                            case AXE_HEAD -> makeTool(toolHead, Objects.requireNonNull(material.axe()).get());
                            case HOE_HEAD -> makeTool(toolHead, Objects.requireNonNull(material.hoe()).get());

                            case PROPICK_HEAD -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.PROPICK)).get());
                            case SAW_BLADE -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SAW)).get());
                            case KNIFE_BLADE -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.KNIFE)).get());
                            case SCYTHE_BLADE -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SCYTHE)).get());
                            case CHISEL_HEAD -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.CHISEL)).get());
                            case HAMMER_HEAD -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.HAMMER)).get());
                            case JAVELIN_HEAD -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.JAVELIN)).get());

                            case SWORD_BLADE -> makeTool(toolHead, Objects.requireNonNull(material.sword()).get());
                            case MACE_HEAD -> makeTool(toolHead, Objects.requireNonNull(ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.MACE)).get());
                        }
                    }
                }
            }
        }

    //Earthen
        recipe()
                .input('S', Blocks.SAND)
                .input('P', TFCTags.Items.GLASS_POTASH)
                .input('L', TFCItems.POWDERS.get(Powder.LIME).get())
                .pattern("PL ", "S  ")
                .shaped(TFCItems.SILICA_GLASS_BATCH, 4);
        recipe()
                .input('S', Blocks.SAND)
                .input('C', Items.CHARCOAL)
                .input('P', TFCTags.Items.GLASS_POTASH)
                .input('L', TFCItems.POWDERS.get(Powder.LIME).get())
                .pattern("PL ", "SC ")
                .shaped(TFCItems.OLIVINE_GLASS_BATCH, 4);
        recipe()
                .input('S', Blocks.RED_SAND)
                .input('P', TFCTags.Items.GLASS_POTASH)
                .input('L', TFCItems.POWDERS.get(Powder.LIME).get())
                .pattern("PL ", "S  ")
                .shaped(TFCItems.HEMATITIC_GLASS_BATCH, 4);
        recipe()
                .input('S', Blocks.RED_SAND)
                .input('C', Items.CHARCOAL)
                .input('P', TFCTags.Items.GLASS_POTASH)
                .input('L', TFCItems.POWDERS.get(Powder.LIME).get())
                .pattern("PL ", "SC ")
                .shaped(TFCItems.VOLCANIC_GLASS_BATCH, 4);

        recipe()
                .input('L', Blocks.MUD)
                .input('X', TFCItems.STRAW)
                .pattern("LX ")
                .shaped(ModBlocks.DRYING_MUD_BRICK, 4);
        recipe()
                .input('L', ModItems.MUD_BRICK)
                .pattern("LL ", "LL ")
                .shaped(Items.MUD_BRICKS);
        recipe()
                .input('L', Items.MUD)
                .pattern("LL ", "LL ")
                .shaped(Items.PACKED_MUD);

        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.SANDSTONE, Items.SMOOTH_SANDSTONE);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.SMOOTH_SANDSTONE, Items.CUT_SANDSTONE);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE);

        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.SMOOTH_RED_SANDSTONE, Items.CUT_RED_SANDSTONE);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE);

        ModBlocks.TFC_CHISELED_SANDSTONE.forEach((sandBlockType, blockId) -> {
            recipe().useTool(TFCTags.Items.TOOLS_CHISEL, TFCBlocks.SANDSTONE.get(sandBlockType).get(SandstoneBlockType.CUT).get().asItem(), blockId.get().asItem());
        });

        recipe()
                .input('A', Blocks.SANDSTONE)
                .pattern("A  ", "AA ", "AAA")
                .shaped(Items.SANDSTONE_STAIRS, 4);
        recipe()
                .input('A', Blocks.SANDSTONE)
                .pattern("AAA")
                .shaped(Items.SANDSTONE_SLAB, 6);
        recipe()
                .input('A', Blocks.RED_SANDSTONE)
                .pattern("A  ", "AA ", "AAA")
                .shaped(Items.RED_SANDSTONE_STAIRS, 4);
        recipe()
                .input('A', Blocks.RED_SANDSTONE)
                .pattern("AAA")
                .shaped(Items.RED_SANDSTONE_SLAB, 6);

        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.QUARTZ, ModItems.QUARTZ_BRICK);
        recipe()
                .input('L', Items.QUARTZ)
                .input('X', TFCItems.MORTAR)
                .pattern("LXL", "XLX", "LXL")
                .shaped(Items.QUARTZ_BLOCK);
        recipe()
                .input('L', ModItems.QUARTZ_BRICK)
                .input('X', TFCItems.MORTAR)
                .pattern("LXL", "XLX", "LXL")
                .shaped(Items.QUARTZ_BRICKS);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.QUARTZ_BLOCK, Items.SMOOTH_QUARTZ);
        recipe().useTool(TFCTags.Items.TOOLS_CHISEL, Items.SMOOTH_QUARTZ, Items.CHISELED_QUARTZ_BLOCK);

    //Devices
        recipe()
                .input('L', TFCTags.Items.LUMBER)
                .pattern("LLL", "L L", "LLL")
                .shaped(ModBlocks.COMPAT_CHEST.get().asItem());
        recipe()
                .input(ModBlocks.COMPAT_CHEST.get().asItem())
                .input(Items.TRIPWIRE_HOOK)
                .shapeless(ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem());
        recipe()
                .input(ModBlocks.COMPAT_CHEST.get().asItem())
                .input(Items.MINECART)
                .shapeless(ModItems.COMPAT_CHEST_MINECART.get());

        recipe()
                .input('X', TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.SAW_BLADE).get().asItem())
                .input('L', TFCTags.Items.LUMBER)
                .input('B', ItemTags.STONE_BRICKS)
                .pattern(" X ", "LBL")
                .shaped(Items.STONECUTTER);
        recipe()
                .input('A', TFCTags.Items.LUMBER)
                .input('B', Items.SMOOTH_STONE)
                .input('C', ItemTags.PLANKS)
                .pattern("ABA", "C C")
                .shaped(Items.GRINDSTONE);
        recipe()
                .input('B', TFCTags.Items.LUMBER)
                .input('C', Items.CHISELED_BOOKSHELF)
                .pattern("BBB", " C ", " B ")
                .shaped(Items.LECTERN);
        recipe()
                .input('B', Items.BOOK)
                .input('C', Items.CHISELED_BOOKSHELF)
                .pattern("BBB", "BBB", "C  ")
                .shaped(Items.BOOKSHELF);
        recipe()
                .input('B', TFCTags.Items.LUMBER)
                .input('C', Tags.Items.RODS_WOODEN)
                .pattern("BBB", "CCC", "BBB")
                .shaped(Items.CHISELED_BOOKSHELF);
        recipe()
                .input('A', ItemTags.PLANKS)
                .input('B', TFCItems.BRASS_MECHANISMS)
                .pattern("AAA", "ABA", "AAA")
                .shaped(Items.JUKEBOX);
        recipe()
                .input('A', Items.BOOK)
                .input('B', TFCItems.GEMS.get(Ore.DIAMOND))
                .input('C', Items.OBSIDIAN)
                .pattern(" A ", "BCB", "CCC")
                .shaped(Items.ENCHANTING_TABLE);
        recipe()
                .input('A', TFCItems.METAL_ITEMS.get(Metal.CAST_IRON).get(Metal.ItemType.SHEET).get().asItem())
                .pattern("A A", "AAA")
                .shaped(Items.CAULDRON);
        recipe()
                .input('L', Items.CHEST)
                .input('X', Items.TRIPWIRE_HOOK)
                .pattern("LX ")
                .shaped(Items.TRAPPED_CHEST);
        recipe()
                .input('L', TFCItems.ORES.get(Ore.AMETHYST).get())
                .pattern("LL ", "LL ")
                .shaped(Items.AMETHYST_BLOCK);

        recipe()
                .input('L', Blocks.BAMBOO_PLANKS)
                .pattern("L L", "LLL")
                .shaped(Items.BAMBOO_RAFT);

        recipe()
                .input(ModItems.UNFINISHED_LANTERN.get())
                .input(TFCItems.LAMP_GLASS.get())
                .shapeless(ModBlocks.LANTERN.get().asItem());

        for(Metal metal : Metal.values()){
            if(metal.allParts()){
                Item unfinishedItem = TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.UNFINISHED_LAMP).get();
                Item finishedLamp = ModBlocks.COMPAT_LANTERNS.get(metal).get().asItem();

                recipe()
                        .input(unfinishedItem)
                        .input(TFCItems.LAMP_GLASS.get())
                        .shapeless(finishedLamp);
            }
        }
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output.
     */
    private Builder recipe()
    {
        return new Builder((name, r) -> {
            if (name != null) add(name, r);
            else add(r);
        });
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output, plus a suffix. The suffix should not start with an underscore.
     */
    private Builder recipe(String suffix)
    {
        return new Builder((name, r) -> {
            assert !suffix.startsWith("_") : "recipe(String suffix) shouldn't start with an '_', it is added for you!";
            assert name == null : "Cannot use a named recipe and recipe(String suffix) at the same time!";
            add(nameOf(r.getResultItem(lookup()).getItem()) + "_" + suffix, r);
        });
    }

    /**
     * @return A builder for a recipe that will replace a vanilla recipe at {@code name}. Checks for conflicts with removals or other replacements.
     */
    private Builder replace(String name)
    {
        return new Builder((name1, r) -> {
            assert name1 == null : "Cannot used replace() with a named recipe!";
            replace(name, r);
        });
    }

    private <T> TagKey<T> woodLogsTagOf(ResourceKey<Registry<T>> registry, CompatWood wood)
    {
        String suffix = switch (wood) {
            case WARPED, CRIMSON -> "_stems";
            case BAMBOO -> "_blocks";
            default -> "_logs";
        };

        return TagKey.create(registry, ResourceLocation.withDefaultNamespace(wood.getSerializedName() + suffix));
    }

    private void makeTool(Item toolHead, Item tool) {
        recipe()
                .input('H', toolHead)
                .input('R', Tags.Items.RODS_WOODEN)
                .pattern("H  ", "R  ")
                .shaped(tool);
    }

    private void addDecorations(ItemLike input, ModDecorationBlockHolder output)
    {
        recipe()
                .input('#', input)
                .pattern("###")
                .shaped(output.slab(), 6);
        recipe()
                .input('#', input)
                .pattern("#  ", "## ", "###")
                .shaped(output.stair(), 8);
        recipe()
                .input('#', input)
                .pattern("###", "###")
                .shaped(output.wall(), 6);
    }

    private void addGrains(Food crop, Food grain, Food flour, Food dough, Food bread, Food sandwich, Food jamSandwich)
    {
        final var meal = new MealModifier(
                FoodData.ofFood(1f, 0.5f, 4.5f),
                List.of(
                        // For a 3-ingredient sandwich, average nutritional value is 0.75, matching salads
                        new MealModifier.MealPortion(Optional.of(Ingredient.of(TFCItems.FOOD.get(bread))), 0.675f, 0.5f, 0.5f),
                        new MealModifier.MealPortion(Optional.empty(), 0.8f, 0.8f, 0.8f)
                ));

        recipe()
                .input(notRotten(crop))
                .inputIsPrimary(TFCTags.Items.TOOLS_KNIFE)
                .damageInputs()
                .copyFood()
                .extraProduct(TFCItems.STRAW)
                .shapeless(TFCItems.FOOD.get(grain));

        // Non-jam sandwiches
        for (String pattern : List.of("SSS", "SS ", " SS", "S S", "S  ", " S ", "  S"))
        {
            recipe(pattern.replace(" ", "x").toLowerCase())
                    .input('K', TFCTags.Items.TOOLS_KNIFE)
                    .input('B', notRotten(bread))
                    .input('S', notRotten(Ingredient.of(TFCTags.Items.USABLE_IN_SANDWICH)))
                    .pattern("KB ", pattern, " B ")
                    .damageInputs()
                    .addOutputModifier(meal)
                    .shaped(TFCItems.FOOD.get(sandwich), 1);
        }

        // Two and three ingredient jam sandwiches
        for (String pattern : List.of("JSS", "SJS", "SSJ", "JS ", "SJ ", " JS", " SJ", "S J", "J S"))
        {
            String variant = "_jar";
            for (TagKey<Item> tag : List.of(TFCTags.Items.PRESERVES, TFCTags.Items.JAM))
            {
                recipe("" + pattern.replace(" ", "x").toLowerCase() + variant)
                        .input('K', TFCTags.Items.TOOLS_KNIFE)
                        .input('B', notRotten(bread))
                        .input('S', notRotten(Ingredient.of(TFCTags.Items.USABLE_IN_JAM_SANDWICH)))
                        .input('J', notRotten(Ingredient.of(tag)))
                        .pattern("KB ", pattern, " B ")
                        .damageInputs()
                        .addOutputModifier(meal)
                        .shaped(TFCItems.FOOD.get(jamSandwich));
                variant = "_jam";
            }
        }

        // One item jam sandwiches
        for (String pattern : List.of(" J ", "J  ", "  J"))
        {
            String variant = "_jar";
            for (TagKey<Item> tag : List.of(TFCTags.Items.PRESERVES, TFCTags.Items.JAM))
            {
                recipe("" + pattern.replace(" ", "x").toLowerCase() + variant)
                        .input('K', TFCTags.Items.TOOLS_KNIFE)
                        .input('B', notRotten(bread))
                        .input('J', notRotten(Ingredient.of(tag)))
                        .pattern("KB ", pattern, " B ")
                        .damageInputs()
                        .addOutputModifier(meal)
                        .shaped(TFCItems.FOOD.get(jamSandwich));
                variant = "_jam";
            }
        }

        for (int n = 1; n <= 8; n++)
            recipe("" + n)
                    .inputIsPrimary(FluidContentIngredient.of(Fluids.WATER, 100))
                    .input(notRotten(flour), n)
                    .copyOldestFood()
                    .shapeless(TFCItems.FOOD.get(dough), n);
    }

    private void addTools(Metal.ItemType input, Metal.ItemType output)
    {
        for (Metal metal : Metal.values())
            if (metal.allParts())
                recipe()
                        .input('S', Tags.Items.RODS_WOODEN)
                        .input('X', TFCItems.METAL_ITEMS.get(metal).get(input))
                        .pattern("X", "S")
                        .copyForging()
                        .source(0, 0)
                        .shaped(TFCItems.METAL_ITEMS.get(metal).get(output));
    }

    private void addTools(RockCategory.ItemType input, RockCategory.ItemType output)
    {
        for (RockCategory type : RockCategory.values())
            recipe()
                    .input('S', Tags.Items.RODS_WOODEN)
                    .input('X', TFCItems.ROCK_TOOLS.get(type).get(input))
                    .pattern("X", "S")
                    .shaped(TFCItems.ROCK_TOOLS.get(type).get(output));
    }

    private Ingredient notRotten(Food food)
    {
        return notRotten(Ingredient.of(TFCItems.FOOD.get(food)));
    }

    private Ingredient notRotten(Ingredient food)
    {
        return AndIngredient.of(food, NotRottenIngredient.INSTANCE);
    }
}



