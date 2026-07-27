package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnR;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRBlocks;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.datagen.assets.addon.FirmalifeCustomLoaderBuilder;
import com.eerussianguy.firmalife.common.blocks.*;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Locale;

public class BuiltinBlockStateProvider extends BlockStateProvider {
    public BuiltinBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FirmaCompat.MODID, existingFileHelper);

        BlockAssets.bootstrap();
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile emptyModel = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("tfc", "block/empty"));

        ModBlocks.WOODS.forEach((compatWood, blockTypeIdMap) -> {
            CompatWoodMaterial material = compatWood.compatWoodMaterial();

            ResourceLocation planksTexture = BlockAssets.get(material.planks()).textures().get(BlockTextureSlot.SIDE);
            ResourceLocation logSideTexture = BlockAssets.getColumn(material.log()).textures().get(BlockTextureSlot.SIDE);
            ResourceLocation logTopTexture = BlockAssets.getColumn(material.log()).textures().get(BlockTextureSlot.END);
            ResourceLocation strippedLogTexture = BlockAssets.getColumn(material.strippedLog()).textures().get(BlockTextureSlot.SIDE);
            ResourceLocation crateTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/" + compatWood.getSerializedName() + "_crate");

            ModelFile connection = supportConnectionModel(compatWood, strippedLogTexture);
            ModelFile connectionInventory = supportInventoryModel(compatWood, strippedLogTexture);

            blockTypeIdMap.forEach((blockType, blockId) -> {
                if(blockType.equals(CompatWood.BlockType.VERTICAL_SUPPORT)) {
                    verticalSupport(blockId.get(), connection, strippedLogTexture);
                } else if(blockType.equals(CompatWood.BlockType.HORIZONTAL_SUPPORT)){
                    horizontalSupport(blockId.get(), connection, strippedLogTexture);
                }
                switch (blockType) {
                    case TWIG -> twigWithItem(blockId.get(), logSideTexture, logTopTexture);
                    case SLUICE -> sluice(blockId.get(), strippedLogTexture);
                    case TOOL_RACK -> toolRack(blockId.get(), planksTexture);
                    case SCRIBING_TABLE -> scribingTable(blockId.get(), planksTexture, logSideTexture, strippedLogTexture);
                    case SEWING_TABLE -> sewingTable(blockId.get(), planksTexture, logSideTexture);
                    case SHELF -> shelf(blockId.get(), planksTexture);
                    case BARREL -> barrelBlock(blockId.get(), planksTexture, strippedLogTexture);
                    case LOG_FENCE -> logFence(blockId.get(), planksTexture, logSideTexture);
                    case LOOM -> loom(blockId.get(), planksTexture);
                    case AXLE -> axle(blockId.get(), emptyModel, strippedLogTexture);
                    case BLADED_AXLE -> bladedAxle(blockId.get(), emptyModel, strippedLogTexture);
                    case ENCASED_AXLE -> encasedAxle(blockId.get(), planksTexture, strippedLogTexture);
                    case CLUTCH -> clutch(blockId.get(), strippedLogTexture, planksTexture);
                    case GEAR_BOX -> gearBox(blockId.get(), planksTexture);
                    case WATER_WHEEL -> waterWheel(blockId.get(), planksTexture);
                    case WINDMILL -> simpleBlock(blockId.get(), emptyModel);
                    case CRATE -> cubeAllWithItem(blockId.get(), crateTexture);
                }
            });
        });

        simpleBlock(ModBlocks.COMPAT_CHEST.get(), models().getBuilder("compat_chest")
                .texture("particle", ResourceLocation.withDefaultNamespace("entity/chest/normal")));
        simpleBlock(ModBlocks.COMPAT_TRAPPED_CHEST.get(), models().getBuilder("compat_trapped_chest")
                .texture("particle", ResourceLocation.withDefaultNamespace("entity/chest/normal")));

        for (CompatRock rock : CompatRock.VALUES) {
            var rockMap = ModBlocks.ROCK_BLOCKS.get(rock);
            var oreMap = ModBlocks.ORES.get(rock);
            var gradedOreMap = ModBlocks.GRADED_ORES.get(rock);

            ResourceLocation rawTexture = rock.blockAsset().textures().get(BlockTextureSlot.SIDE);
            ResourceLocation cobbleTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + rock.getSerializedName() + "_cobble");
            ResourceLocation mossyCobbleTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + "mossy_" + rock.getSerializedName() + "_cobble");

            for (CompatRock.BlockType blockType : CompatRock.BlockType.VALUES) {
                Block block = rockMap.get(blockType).get();
                ResourceLocation cobbleMossyTexture = blockType.name().startsWith("MOSSY") && blockType.name().endsWith("COBBLE") ? mossyCobbleTexture : cobbleTexture;

                switch (blockType) {
                    case HARDENED -> cubeAllWithItem(block, rawTexture);
                    case LOOSE -> looseRockWithItem(block, rawTexture, rock.category());
                    case MOSSY_LOOSE -> looseRockWithItem(block, mossyCobbleTexture, rock.category());
                    case COBBLE, HARDENED_COBBLE -> cubeAllWithItem(block, cobbleTexture);
                    case MOSSY_COBBLE, MOSSY_HARDENED_COBBLE -> cubeAllWithItem(block, mossyCobbleTexture);
                    case SPIKE -> spikeWithItem(block, rawTexture);
                }

                /**
                 * assumes only COMPAT_HARDENED_COBBLE and MOSSY_HARDENED_COBBLE have variants
                 */
                if(blockType.hasVariants()) {
                    slabWithItem(ModBlocks.ROCK_DECORATIONS.get(rock).get(blockType).slab().get(), block, cobbleMossyTexture);
                    stairsWithItem(ModBlocks.ROCK_DECORATIONS.get(rock).get(blockType).stair().get(), cobbleMossyTexture);
                    wallWithItem(ModBlocks.ROCK_DECORATIONS.get(rock).get(blockType).wall().get(), cobbleMossyTexture);
                }
            }

            oreMap.forEach((ore, blockId) -> {
                ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(blockId.get());
                ResourceLocation overlayTexture = ResourceLocation.fromNamespaceAndPath("tfc", "block/ore/" + ore.name().toLowerCase(Locale.ROOT));

                simpleBlockWithItem(blockId.get(),
                        models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                                .texture("all", rawTexture)
                                .texture("overlay", overlayTexture)
                );
            });

            gradedOreMap.forEach((ore, gradeIdMap) -> {
                gradeIdMap.forEach((grade, blockId) -> {
                    ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(blockId.get());
                    ResourceLocation overlayTexture = ResourceLocation.fromNamespaceAndPath("tfc", "block/ore/" + grade.name().toLowerCase(Locale.ROOT) + "_" + ore.name().toLowerCase(Locale.ROOT));

                    simpleBlockWithItem(blockId.get(),
                            models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                                    .texture("all", rawTexture)
                                    .texture("overlay", overlayTexture)
                    );
                });
            });

            rockAnvilWithItem(ModBlocks.ROCK_ANVILS.get(rock).get(), rawTexture);
        }

        ModBlocks.TFC_ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/" + blockType.getSerializedName() + "/" + rock.getSerializedName());

                cubeAllWithItem(blockId.get(), texture);
            });
        });

        ModBlocks.AQUEDUCTS.forEach((compatRockSets, blockId) -> {
            assert compatRockSets.brick() != null;
            ResourceLocation texture = BlockAssets.get(compatRockSets.brick().base().get()).textures().get(BlockTextureSlot.SIDE);
            aqueductWithItem(blockId.get(), texture);
        });

        Block brickAqueduct = ModBlocks.BRICK_AQUEDUCT.get();
        aqueductWithItem(brickAqueduct, BlockAssets.get(Blocks.BRICKS).textures().get(BlockTextureSlot.SIDE));
        Block prismarineBrickAqueduct = ModBlocks.PRISMARINE_BRICK_AQUEDUCT.get();
        aqueductWithItem(prismarineBrickAqueduct, BlockAssets.get(Blocks.PRISMARINE_BRICKS).textures().get(BlockTextureSlot.SIDE));
        Block quartzBrickAqueduct = ModBlocks.QUARTZ_BRICK_AQUEDUCT.get();
        aqueductWithItem(quartzBrickAqueduct, BlockAssets.get(Blocks.QUARTZ_BRICKS).textures().get(BlockTextureSlot.SIDE));
        Block redNetherBrickAqueduct = ModBlocks.RED_NETHER_BRICK_AQUEDUCT.get();
        aqueductWithItem(redNetherBrickAqueduct, BlockAssets.get(Blocks.RED_NETHER_BRICKS).textures().get(BlockTextureSlot.SIDE));



    //Natural
        ResourceLocation dirtTexture = ResourceLocation.withDefaultNamespace("block/dirt");
        ResourceLocation farmlandTexture = ResourceLocation.withDefaultNamespace("block/farmland");
        ResourceLocation mudTexture = ResourceLocation.withDefaultNamespace("block/mud");
        ResourceLocation mudBricksTexture = ResourceLocation.withDefaultNamespace("block/mud_bricks");
        ResourceLocation grassTopTexture = ResourceLocation.withDefaultNamespace("block/grass_block_top");
        ResourceLocation grassBlockOverlayTexture = ResourceLocation.withDefaultNamespace("block/grass_block_side_overlay");
        ResourceLocation podzolTopTexture = ResourceLocation.withDefaultNamespace("block/podzol_top");
        ResourceLocation podzolBlockOverlayTexture = ResourceLocation.fromNamespaceAndPath("firma_compat","block/podzol_overlay");
        ResourceLocation clayDirtTexture = ResourceLocation.fromNamespaceAndPath("firma_compat","block/clay_dirt");
        ResourceLocation kaolinDirtTexture = ResourceLocation.fromNamespaceAndPath("firma_compat","block/kaolin_clay");

        grassBlockWithItem(ModBlocks.CLAY_GRASS_BLOCK.get(), clayDirtTexture, clayDirtTexture, grassTopTexture, grassBlockOverlayTexture);
        grassBlockWithItem(ModBlocks.CLAY_PODZOL.get(), clayDirtTexture, clayDirtTexture, podzolTopTexture, podzolBlockOverlayTexture);
        grassBlockWithItem(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), kaolinDirtTexture, kaolinDirtTexture, grassTopTexture, grassBlockOverlayTexture);
        grassBlockWithItem(ModBlocks.KAOLIN_CLAY_PODZOL.get(), kaolinDirtTexture, kaolinDirtTexture, podzolTopTexture, podzolBlockOverlayTexture);

        cubeAllWithItem(ModBlocks.CLAY_DIRT.get(), clayDirtTexture);
        cubeAllWithItem(ModBlocks.KAOLIN_CLAY_DIRT.get(), kaolinDirtTexture);

        simpleBlockWithItem(ModBlocks.COMPAT_FARMLAND.get(),
                models().withExistingParent("compat_farmland", mcLoc("template_farmland"))
                        .texture("top", farmlandTexture)
                        .texture("dirt", dirtTexture)
        );

        dryingMudBricksWithItem(ModBlocks.DRYING_MUD_BRICK.get(), mcLoc("block/mud"), mcLoc("block/packed_mud"));

        simpleBlockWithItem(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get(),
                models().withExistingParent("cassiterite_gravel_deposit", ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                        .texture("all", mcLoc("block/gravel"))
                        .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc","block/deposit/cassiterite"))
        );
        simpleBlockWithItem(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get(),
                models().withExistingParent("native_silver_gravel_deposit", ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                        .texture("all", mcLoc("block/gravel"))
                        .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc","block/deposit/native_silver"))
        );
        simpleBlockWithItem(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get(),
                models().withExistingParent("native_gold_gravel_deposit", ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                        .texture("all", mcLoc("block/gravel"))
                        .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc","block/deposit/native_gold"))
        );
        simpleBlockWithItem(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get(),
                models().withExistingParent("native_copper_gravel_deposit", ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                        .texture("all", mcLoc("block/gravel"))
                        .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc","block/deposit/native_copper"))
        );

        ModBlocks.TFC_SUSPICIOUS_GRAVEL.forEach((rock, brushableBlockId) -> {
            ResourceLocation zero = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_gravel/" + rock.getSerializedName() + "_0");
            ResourceLocation one = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_gravel/" + rock.getSerializedName() + "_1");
            ResourceLocation two = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_gravel/" + rock.getSerializedName() + "_2");
            ResourceLocation three = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_gravel/" + rock.getSerializedName() + "_3");

            suspiciousBlockWithItem(brushableBlockId.get(), zero, one, two, three);
        });

        ModBlocks.TFC_SUSPICIOUS_SAND.forEach((sand, brushableBlockId) -> {
            String sandName = sand.name().toLowerCase(Locale.ROOT);
            ResourceLocation zero = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_sand/" + sandName + "_0");
            ResourceLocation one = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_sand/" + sandName + "_1");
            ResourceLocation two = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_sand/" + sandName + "_2");
            ResourceLocation three = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_sand/" + sandName + "_3");

            suspiciousBlockWithItem(brushableBlockId.get(), zero, one, two, three);
        });

        suspiciousBlockWithItem(ModBlocks.SUSPICIOUS_RED_SAND.get(),
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_red_sand_0"),
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_red_sand_1"),
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_red_sand_2"),
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/suspicious_red_sand_3"));


    //=============== Firmalife =================

        for (CompatWood wood : CompatWood.VALUES) {

            CompatWoodMaterial material = wood.compatWoodMaterial();
            ResourceLocation planksTexture = BlockAssets.get(material.planks()).textures().get(BlockTextureSlot.SIDE);
            ResourceLocation strippedLogTexture = BlockAssets.get(material.strippedLog()).textures().get(BlockTextureSlot.SIDE);
            ResourceLocation logSideTexture = BlockAssets.get(material.log()).textures().get(BlockTextureSlot.SIDE);
            String woodstr = wood.getSerializedName();

            Block foodShelfBlock = CompatFLBlocks.FOOD_SHELVES.get(wood).get();
            Block hangerBlock = CompatFLBlocks.HANGERS.get(wood).get();
            Block jarbnetBlock = CompatFLBlocks.JARBNETS.get(wood).get();
            Block kegBlock = CompatFLBlocks.KEGS.get(wood).get();
            Block kegSubBlock = CompatFLBlocks.KEG_SUBS.get(wood).get();
            Block stompBarrelBlock = CompatFLBlocks.STOMPING_BARRELS.get(wood).get();
            Block barrelPressBlock = CompatFLBlocks.BARREL_PRESSES.get(wood).get();
            Block wineShelfBlock = CompatFLBlocks.WINE_SHELVES.get(wood).get();

            foodShelfWithItem(foodShelfBlock, planksTexture);
            hangerWithItem(hangerBlock, planksTexture);
            jarbnetWithItem(jarbnetBlock, planksTexture, strippedLogTexture, logSideTexture);
            kegWithItem(woodstr, kegBlock, kegSubBlock, logSideTexture);
            stompingBarrelWithItem(stompBarrelBlock, planksTexture);
            barrelPressWithItem(barrelPressBlock, strippedLogTexture);
            wineShelfWithItem(wineShelfBlock, planksTexture, strippedLogTexture, logSideTexture);
        }

        CompatFLBlocks.CHROMITE_ORES.forEach((rock, gradeIdMap) -> {
            gradeIdMap.forEach((grade, blockId) -> {
                ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(blockId.get());
                ResourceLocation overlayTexture = ResourceLocation.fromNamespaceAndPath("firmalife", "block/ore/" + grade.name().toLowerCase(Locale.ROOT) + "_chromite");
                CompatRockMaterial material = rock.rockMaterial();

                simpleBlockWithItem(blockId.get(),
                        models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                                .texture("all", BlockAssets.get(material.raw().base().get()).textures().get(BlockTextureSlot.SIDE))
                                .texture("overlay", overlayTexture)
                );
            });
        });

    //=============== Roofs and Roads =================
        ResourceLocation gravelTexture = ResourceLocation.withDefaultNamespace("block/gravel");

        for (CompatWood wood : CompatWood.VALUES) {
            Block shingleBlock = CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get();
            StairBlock shingleStair = (StairBlock) CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get();
            SlabBlock shingleSlab = (SlabBlock) CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get();

            ResourceLocation woodShingleTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + wood.getSerializedName() + "_shingles");

            cubeAllWithItem(shingleBlock, woodShingleTexture);
            stairsWithItem(shingleStair, woodShingleTexture);
            slabWithItem(shingleSlab, shingleBlock, woodShingleTexture);
        }

        for (CompatRock rock : CompatRock.VALUES) {
            for (CompatRnR compatRnR : CompatRnR.VALUES) {
                ResourceLocation topTexture = switch (compatRnR) {
                    case COBBLED_ROAD -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/" + rock.getSerializedName() + "_cobble");
                    case SETT_ROAD -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/" + rock.getSerializedName() + "_sett");
                    case FLAGSTONES -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/" + rock.getSerializedName() + "_flagstones");
                };
                rnrPathBlockWithItem(CompatRnRBlocks.ROCK_BLOCKS.get(rock).get(compatRnR).get(), topTexture, gravelTexture);
                rnrPathStairWithItem(CompatRnRBlocks.ROCK_STAIRS.get(rock).get(compatRnR).get(), topTexture);
                rnrPathSlabWithItem(CompatRnRBlocks.ROCK_SLABS.get(rock).get(compatRnR).get(), topTexture, gravelTexture);
            }
        }

        tampedBlockWithItem(CompatRnRBlocks.TAMPED_DIRT.get(), ResourceLocation.withDefaultNamespace("block/dirt"));
        tampedBlockWithItem(CompatRnRBlocks.TAMPED_MUD.get(), ResourceLocation.withDefaultNamespace("block/mud"));
        rnrOverfillWithItem(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get(), gravelTexture, gravelTexture);
        rnrPathBlockWithItem(CompatRnRBlocks.GRAVEL_ROAD.get(), gravelTexture, gravelTexture);
        rnrPathStairWithItem(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get(), gravelTexture);
        rnrPathSlabWithItem(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get(), gravelTexture, gravelTexture);
        rnrPathBlockWithItem(CompatRnRBlocks.MACADAM_ROAD.get(), gravelTexture, gravelTexture);
        rnrPathStairWithItem(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get(), gravelTexture);
        rnrPathSlabWithItem(CompatRnRBlocks.MACADAM_ROAD_SLAB.get(), gravelTexture, gravelTexture);
    }

    private void rnrOverfillWithItem(Block block, ResourceLocation topTexture, ResourceLocation gravelTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile overfillModel = models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("rnr","block/overfilled_block"))
                .texture("top", topTexture)
                .texture("gravel", gravelTexture);

        simpleBlockWithItem(block, overfillModel);
    }

    private void tampedBlockWithItem(Block block, ResourceLocation dirt) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile tampedModel = models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("rnr","block/tamped_block"))
                .texture("dirt", dirt);

        simpleBlockWithItem(block, tampedModel);
    }

    private void rnrPathBlockWithItem(Block block, ResourceLocation topTexture, ResourceLocation gravelTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile pathBlockModel = models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("rnr","block/path_block"))
                .texture("top", topTexture)
                .texture("gravel", gravelTexture);

        simpleBlockWithItem(block, pathBlockModel);
    }

    private void rnrPathSlabWithItem(Block block, ResourceLocation topTexture, ResourceLocation gravelTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile pathSlabModel = models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("rnr","block/path_slab"))
                .texture("top", topTexture)
                .texture("gravel", gravelTexture);

        simpleBlockWithItem(block, pathSlabModel);
    }

    private void rnrPathStairWithItem(Block block, ResourceLocation texture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile pathStairsModel = models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("rnr","block/path_stairs"))
                .texture("top", texture)
                .texture("side", texture)
                .texture("bottom", texture);
        ModelFile pathStairsOuterModel = models().withExistingParent(blockRes.getPath() + "_outer", ResourceLocation.fromNamespaceAndPath("rnr","block/path_outer_stairs"))
                .texture("top", texture)
                .texture("side", texture)
                .texture("bottom", texture);
        ModelFile pathStairsInnerModel = models().withExistingParent(blockRes.getPath() + "_inner", ResourceLocation.fromNamespaceAndPath("rnr","block/path_inner_stairs"))
                .texture("top", texture)
                .texture("side", texture)
                .texture("bottom", texture);

        VariantBlockStateBuilder flagstoneBuilder = getVariantBuilder(block);
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.EAST).with(StairBlock.SHAPE, StairsShape.STRAIGHT)
                .modelForState().modelFile(pathStairsModel).rotationY(0).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.WEST).with(StairBlock.SHAPE, StairsShape.STRAIGHT)
                .modelForState().modelFile(pathStairsModel).rotationY(180).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.SOUTH).with(StairBlock.SHAPE, StairsShape.STRAIGHT)
                .modelForState().modelFile(pathStairsModel).rotationY(90).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.NORTH).with(StairBlock.SHAPE, StairsShape.STRAIGHT)
                .modelForState().modelFile(pathStairsModel).rotationY(270).uvLock(true).addModel();

        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.EAST).with(StairBlock.SHAPE, StairsShape.OUTER_RIGHT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(0).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.WEST).with(StairBlock.SHAPE, StairsShape.OUTER_RIGHT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(180).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.SOUTH).with(StairBlock.SHAPE, StairsShape.OUTER_RIGHT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(90).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.NORTH).with(StairBlock.SHAPE, StairsShape.OUTER_RIGHT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(270).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.EAST).with(StairBlock.SHAPE, StairsShape.OUTER_LEFT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(270).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.WEST).with(StairBlock.SHAPE, StairsShape.OUTER_LEFT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(90).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.SOUTH).with(StairBlock.SHAPE, StairsShape.OUTER_LEFT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(0).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.NORTH).with(StairBlock.SHAPE, StairsShape.OUTER_LEFT)
                .modelForState().modelFile(pathStairsOuterModel).rotationY(180).uvLock(true).addModel();

        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.EAST).with(StairBlock.SHAPE, StairsShape.INNER_RIGHT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(0).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.WEST).with(StairBlock.SHAPE, StairsShape.INNER_RIGHT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(180).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.SOUTH).with(StairBlock.SHAPE, StairsShape.INNER_RIGHT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(90).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.NORTH).with(StairBlock.SHAPE, StairsShape.INNER_RIGHT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(270).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.EAST).with(StairBlock.SHAPE, StairsShape.INNER_LEFT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(270).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.WEST).with(StairBlock.SHAPE, StairsShape.INNER_LEFT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(90).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.SOUTH).with(StairBlock.SHAPE, StairsShape.INNER_LEFT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(0).uvLock(true).addModel();
        flagstoneBuilder.partialState().with(StairBlock.FACING, Direction.NORTH).with(StairBlock.SHAPE, StairsShape.INNER_LEFT)
                .modelForState().modelFile(pathStairsInnerModel).rotationY(180).uvLock(true).addModel();

        simpleBlockItem(block, pathStairsModel);
    }

    private void foodShelfWithItem(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile foodShelfModel = models()
                .withExistingParent(("block/food_shelf/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("firmalife","block/food_shelf_base"))
                .texture("wood", planksTexture);
        ModelFile foodShelfDynamicModel = models().getBuilder("block/food_shelf/" + blockRes.getPath() + "_dynamic")
                .customLoader((parent, helper) -> new FirmalifeCustomLoaderBuilder<>("food_shelf", parent, helper)
                        .base(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/food_shelf/" + blockRes.getPath()))).end();

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(foodShelfDynamicModel).rotationY(270).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(foodShelfDynamicModel).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(foodShelfDynamicModel).rotationY(0).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(foodShelfDynamicModel).rotationY(90).addModel();

        simpleBlockItem(block, foodShelfModel);
    }

    private void hangerWithItem(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile hangerModel = models()
                .withExistingParent(("block/hanger/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("firmalife","block/hanger_base"))
                .texture("wood", planksTexture)
                .texture("string", "minecraft:block/white_wool");
        ModelFile hangerDynamicModel = models().getBuilder("block/hanger/" + blockRes.getPath() + "_dynamic")
                .customLoader((parent, helper) -> new FirmalifeCustomLoaderBuilder<>("hanger", parent, helper)
                        .base(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/hanger/" + blockRes.getPath()))).end();

        simpleBlock(block, hangerDynamicModel);
        simpleBlockItem(block, hangerModel);
    }

    private void jarbnetWithItem(Block block, ResourceLocation planksTexture, ResourceLocation strippedLogSideTexture, ResourceLocation logSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile jarbnetModel = models()
                .withExistingParent(("block/jarbnet/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("firmalife","block/jarbnet"))
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogSideTexture)
                .texture("log", logSideTexture);
        ModelFile jarbnetDynamicModel = models().getBuilder("block/jarbnet/" + blockRes.getPath() + "_dynamic")
                .customLoader((parent, helper) -> new FirmalifeCustomLoaderBuilder<>("jarbnet", parent, helper)
                        .base(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/jarbnet/" + blockRes.getPath()))).end();

        getVariantBuilder(block)
                //Open true
                .partialState().with(FourWayDeviceBlock.FACING, Direction.EAST).with(JarbnetBlock.OPEN, true)
                .modelForState().modelFile(jarbnetDynamicModel).rotationY(90).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.NORTH).with(JarbnetBlock.OPEN, true)
                .modelForState().modelFile(jarbnetDynamicModel).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.SOUTH).with(JarbnetBlock.OPEN, true)
                .modelForState().modelFile(jarbnetDynamicModel).rotationY(180).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.WEST).with(JarbnetBlock.OPEN, true)
                .modelForState().modelFile(jarbnetDynamicModel).rotationY(270).addModel()
                //Open false
                .partialState().with(FourWayDeviceBlock.FACING, Direction.EAST).with(JarbnetBlock.OPEN, false)
                .modelForState().modelFile(jarbnetDynamicModel).rotationY(90).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.NORTH).with(JarbnetBlock.OPEN, false)
                .modelForState().modelFile(jarbnetDynamicModel).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.SOUTH).with(JarbnetBlock.OPEN, false)
                .modelForState().modelFile(jarbnetDynamicModel).rotationY(180).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.WEST).with(JarbnetBlock.OPEN, false)
                .modelForState().modelFile(jarbnetDynamicModel).rotationY(270).addModel();

        simpleBlockItem(block, jarbnetModel);
    }

    private void wineShelfWithItem(Block block, ResourceLocation planksTexture, ResourceLocation strippedLogSideTexture, ResourceLocation logSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile wineShelfModel = models()
                .withExistingParent(("block/wine_shelf/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("firmalife","block/wine_shelf"))
                .texture("0", planksTexture)
                .texture("2", strippedLogSideTexture)
                .texture("3", strippedLogSideTexture);
        ModelFile wineShelfDynamicModel = models().getBuilder("block/wine_shelf/" + blockRes.getPath() + "_dynamic")
                .customLoader((parent, helper) -> new FirmalifeCustomLoaderBuilder<>("wine_shelf", parent, helper)
                        .base(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/wine_shelf/" + blockRes.getPath()))).end();

        getVariantBuilder(block)
                .partialState().with(FourWayDeviceBlock.FACING, Direction.EAST)
                .modelForState().modelFile(wineShelfDynamicModel).rotationY(90).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(wineShelfDynamicModel).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(wineShelfDynamicModel).rotationY(180).addModel()
                .partialState().with(FourWayDeviceBlock.FACING, Direction.WEST)
                .modelForState().modelFile(wineShelfDynamicModel).rotationY(270).addModel();

        simpleBlockItem(block, wineShelfModel);
    }

    private void stompingBarrelWithItem(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile stompBarrelModel = models()
                .withExistingParent(("block/stomping_barrel/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("firmalife","block/stomping_barrel"))
                .texture("0", planksTexture);

        simpleBlockWithItem(block, stompBarrelModel);
    }

    private void barrelPressWithItem(Block block, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile barrelPressModel = models()
                .withExistingParent(("block/barrel_press/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("firmalife","block/barrel_press"))
                .texture("0", strippedLogSideTexture);

        simpleBlockWithItem(block, barrelPressModel);

    }

    private void kegWithItem(String woodStr, Block block, Block kegSubBlock, ResourceLocation logSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        String bbPrefix = FirmaCompat.MODID + ":block/big_barrel/" + woodStr;

        // 1. big_barrel_0_sealed
        ModelFile bigBarrel0Sealed = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_0_sealed", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_0_sealed"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);  // dynamic TFC log texture

        // 2. big_barrel_0_unsealed
        ModelFile bigBarrel0Unsealed = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_0_unsealed", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_0_unsealed"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 3. big_barrel_1
        ModelFile bigBarrel1 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_1", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_1"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 4. big_barrel_2
        ModelFile bigBarrel2 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_2", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_2"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 5. big_barrel_3
        ModelFile bigBarrel3 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_3", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_3"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 6. big_barrel_4
        ModelFile bigBarrel4 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_4", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_4"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 7. big_barrel_5
        ModelFile bigBarrel5 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_5", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_5"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 8. big_barrel_6
        ModelFile bigBarrel6 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_6", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_6"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 9. big_barrel_7
        ModelFile bigBarrel7 = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_7", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_7"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        // 10. big_barrel_item (inventory model)
        ModelFile bigBarrelItem = models()
                .withExistingParent("block/big_barrel/" + blockRes.getPath() + "_item", ResourceLocation.fromNamespaceAndPath("firmalife","block/big_barrel_item"))
                .texture("0", bbPrefix + "_3_side")
                .texture("1", bbPrefix + "_0")
                .texture("2", bbPrefix + "_0_side")
                .texture("3", bbPrefix + "_1")
                .texture("4", bbPrefix + "_1_side")
                .texture("5", bbPrefix + "_2")
                .texture("6", bbPrefix + "_2_side")
                .texture("7", bbPrefix + "_3")
                .texture("8", bbPrefix + "_3_top")
                .texture("9", bbPrefix + "_0_top")
                .texture("10", bbPrefix + "_1_top")
                .texture("11", bbPrefix + "_2_top")
                .texture("12", logSideTexture);

        getVariantBuilder(block)
                // Unsealed (sealed=false)
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.EAST).with(KegCoreBlock.SEALED, false)
                .modelForState().modelFile(bigBarrel0Unsealed).rotationY(90).addModel()
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.NORTH).with(KegCoreBlock.SEALED, false)
                .modelForState().modelFile(bigBarrel0Unsealed).rotationY(0).addModel()
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.SOUTH).with(KegCoreBlock.SEALED, false)
                .modelForState().modelFile(bigBarrel0Unsealed).rotationY(180).addModel()
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.WEST).with(KegCoreBlock.SEALED, false)
                .modelForState().modelFile(bigBarrel0Unsealed).rotationY(270).addModel()

                // Sealed (sealed=true)
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.EAST).with(KegCoreBlock.SEALED, true)
                .modelForState().modelFile(bigBarrel0Sealed).rotationY(90).addModel()
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.NORTH).with(KegCoreBlock.SEALED, true)
                .modelForState().modelFile(bigBarrel0Sealed).rotationY(0).addModel()
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.SOUTH).with(KegCoreBlock.SEALED, true)
                .modelForState().modelFile(bigBarrel0Sealed).rotationY(180).addModel()
                .partialState().with(TwoByTwoCoreBlock.FACING, Direction.WEST).with(KegCoreBlock.SEALED, true)
                .modelForState().modelFile(bigBarrel0Sealed).rotationY(270).addModel();


        var subBuilder = getVariantBuilder(kegSubBlock);

        ModelFile[] partModels = {
                null,                    // index 0 unused
                bigBarrel1,
                bigBarrel2,
                bigBarrel3,
                bigBarrel4,
                bigBarrel5,
                bigBarrel6,
                bigBarrel7
        };

        for (int part = 1; part <= 7; part++) {
            ModelFile model = partModels[part];

            // East
            subBuilder.partialState()
                    .with(KegSubBlock.BARREL_PART, part)
                    .with(TwoByTwoSubBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(model).rotationY(90).addModel();

            // North
            subBuilder.partialState()
                    .with(KegSubBlock.BARREL_PART, part)
                    .with(TwoByTwoSubBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(model).rotationY(0).addModel();

            // South
            subBuilder.partialState()
                    .with(KegSubBlock.BARREL_PART, part)
                    .with(TwoByTwoSubBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(model).rotationY(180).addModel();

            // West
            subBuilder.partialState()
                    .with(KegSubBlock.BARREL_PART, part)
                    .with(TwoByTwoSubBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(model).rotationY(270).addModel();
        }
        simpleBlockItem(block, bigBarrelItem);
    }

    private void cubeAllWithItem(Block block, ResourceLocation texture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        simpleBlockWithItem(block, models().cubeAll(blockRes.getPath(), texture));
    }

    private void rockAnvilWithItem(Block block, ResourceLocation raw) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile anvil = models().withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("tfc","block/rock/anvil"))
                .texture("texture", raw);

        simpleBlockWithItem(block, anvil);
    }

    private void suspiciousBlockWithItem(Block block, ResourceLocation zero, ResourceLocation one, ResourceLocation two, ResourceLocation three) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile susGravelModel0 = models().cubeAll(blockRes.getPath() + "_0", zero);
        ModelFile susGravelModel1 = models().cubeAll(blockRes.getPath() + "_1", one);
        ModelFile susGravelModel2 = models().cubeAll(blockRes.getPath() + "_2", two);
        ModelFile susGravelModel3 = models().cubeAll(blockRes.getPath() + "_3", three);

        getVariantBuilder(block)
                .partialState().with(BlockStateProperties.DUSTED, 0).modelForState().modelFile(susGravelModel0).addModel()
                .partialState().with(BlockStateProperties.DUSTED, 1).modelForState().modelFile(susGravelModel1).addModel()
                .partialState().with(BlockStateProperties.DUSTED, 2).modelForState().modelFile(susGravelModel2).addModel()
                .partialState().with(BlockStateProperties.DUSTED, 3).modelForState().modelFile(susGravelModel3).addModel();
        simpleBlockItem(block, models().cubeAll(blockRes.getPath(), three));
    }

    private void grassBlockWithItem(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation overlay) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        simpleBlockWithItem(block,
                models().withExistingParent(blockRes.getPath(), mcLoc("grass_block"))
                        .texture("bottom", bottom)
                        .texture("top", top)
                        .texture("side", side)
                        .texture("overlay", overlay)
                        .texture("particle", side)
        );
    }

    private void cubeBottomTopWithItem(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        simpleBlockWithItem(block, models().cubeBottomTop(blockRes.getPath(), side, bottom, top));
    }

    private void slabWithItem(SlabBlock block, Block full, ResourceLocation texture) {
        slabBlock(block, BuiltInRegistries.BLOCK.getKey(full), texture);
        blockItem(block);
    }

    private void stairsWithItem(StairBlock block, ResourceLocation textureAll) {
        stairsBlock(block, textureAll);
        blockItem(block);
    }

    private void wallWithItem(WallBlock block, ResourceLocation textureAll) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile inventory = models().wallInventory(blockRes.getPath() + "_inventory", textureAll);

        wallBlock(block, textureAll);
        simpleBlockItem(block, inventory);
    }

    private void dryingMudBricksWithItem(Block block, ResourceLocation wet, ResourceLocation dry) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile brickWet1Model = models().withExistingParent(blockRes.getPath() + "_wet_1", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/1"))
                .texture("mud", wet);
        ModelFile brickDry1Model = models().withExistingParent(blockRes.getPath() +"_dry_1", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/1"))
                .texture("mud", dry);
        ModelFile brickWet2Model = models().withExistingParent(blockRes.getPath() +"_wet_2", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/2"))
                .texture("mud", wet);
        ModelFile brickDry2Model = models().withExistingParent(blockRes.getPath() +"_dry_2", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/2"))
                .texture("mud", dry);
        ModelFile brickWet3Model = models().withExistingParent(blockRes.getPath() +"_wet_3", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/3"))
                .texture("mud", wet);
        ModelFile brickDry3Model = models().withExistingParent(blockRes.getPath() +"_dry_3", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/3"))
                .texture("mud", dry);
        ModelFile brickWet4Model = models().withExistingParent(blockRes.getPath() +"_wet_4", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/4"))
                .texture("mud", wet);
        ModelFile brickDry4Model = models().withExistingParent(blockRes.getPath() +"_dry_4", ResourceLocation.fromNamespaceAndPath("tfc", "block/mud_bricks/4"))
                .texture("mud", dry);

        getVariantBuilder(block)
                .partialState().with(DryingBricksBlock.COUNT, 1).with(DryingBricksBlock.DRIED, false)
                .modelForState().modelFile(brickWet1Model).addModel()
                .partialState().with(DryingBricksBlock.COUNT, 1).with(DryingBricksBlock.DRIED, true)
                .modelForState().modelFile(brickDry1Model).addModel()

                .partialState().with(DryingBricksBlock.COUNT, 2).with(DryingBricksBlock.DRIED, false)
                .modelForState().modelFile(brickWet2Model).addModel()
                .partialState().with(DryingBricksBlock.COUNT, 2).with(DryingBricksBlock.DRIED, true)
                .modelForState().modelFile(brickDry2Model).addModel()

                .partialState().with(DryingBricksBlock.COUNT, 3).with(DryingBricksBlock.DRIED, false)
                .modelForState().modelFile(brickWet3Model).addModel()
                .partialState().with(DryingBricksBlock.COUNT, 3).with(DryingBricksBlock.DRIED, true)
                .modelForState().modelFile(brickDry3Model).addModel()

                .partialState().with(DryingBricksBlock.COUNT, 4).with(DryingBricksBlock.DRIED, false)
                .modelForState().modelFile(brickWet4Model).addModel()
                .partialState().with(DryingBricksBlock.COUNT, 4).with(DryingBricksBlock.DRIED, true)
                .modelForState().modelFile(brickDry4Model).addModel();
    }

    private void aqueductWithItem(Block block, ResourceLocation texture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile aqueductBaseModel = models().withExistingParent("block/aqueduct/" + blockRes.getPath() + "/base", ResourceLocation.fromNamespaceAndPath("tfc", "block/aqueduct/base"))
                .texture("texture", texture)
                .texture("particle", texture);
        ModelFile aqueductNorthModel = models().withExistingParent("block/aqueduct/" + blockRes.getPath() + "/north", ResourceLocation.fromNamespaceAndPath("tfc", "block/aqueduct/north"))
                .texture("texture", texture)
                .texture("particle", texture);
        ModelFile aqueductSouthModel = models().withExistingParent("block/aqueduct/" + blockRes.getPath() + "/south", ResourceLocation.fromNamespaceAndPath("tfc", "block/aqueduct/south"))
                .texture("texture", texture)
                .texture("particle", texture);
        ModelFile aqueductEastModel = models().withExistingParent("block/aqueduct/" + blockRes.getPath() + "/east", ResourceLocation.fromNamespaceAndPath("tfc", "block/aqueduct/east"))
                .texture("texture", texture)
                .texture("particle", texture);
        ModelFile aqueductWestModel = models().withExistingParent("block/aqueduct/" + blockRes.getPath() + "/west", ResourceLocation.fromNamespaceAndPath("tfc", "block/aqueduct/west"))
                .texture("texture", texture)
                .texture("particle", texture);

        getMultipartBuilder(block)
                .part().modelFile(aqueductBaseModel).addModel().end()
                .part().modelFile(aqueductNorthModel).addModel().condition(BlockStateProperties.NORTH, false).end()
                .part().modelFile(aqueductSouthModel).addModel().condition(BlockStateProperties.SOUTH, false).end()
                .part().modelFile(aqueductEastModel).addModel().condition(BlockStateProperties.EAST, false).end()
                .part().modelFile(aqueductWestModel).addModel().condition(BlockStateProperties.WEST, false).end();

        simpleBlockItem(block, aqueductBaseModel);
    }

    private void spikeWithItem(Block block, ResourceLocation texture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile base = models()
                .withExistingParent(blockRes.getPath() + "_base", ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/spike_base"))
                .texture("texture", texture).texture("particle", texture);
        ModelFile middle = models()
                .withExistingParent(blockRes.getPath() + "_middle", ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/spike_middle"))
                .texture("texture", texture).texture("particle", texture);
        ModelFile tip = models()
                .withExistingParent(blockRes.getPath() + "_tip", ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/spike_tip"))
                .texture("texture", texture).texture("particle", texture);

        getVariantBuilder(block)
                .partialState().with(RockSpikeBlock.PART, RockSpikeBlock.Part.BASE)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(base)
                                .build()
                )
                .partialState().with(RockSpikeBlock.PART, RockSpikeBlock.Part.MIDDLE)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(middle)
                                .build()
                )
                .partialState().with(RockSpikeBlock.PART, RockSpikeBlock.Part.TIP)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(tip)
                                .build()
                );

        simpleBlockItem(block, base);
    }

    private void looseRockWithItem(Block block, ResourceLocation texture, RockDisplayCategory category) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        String categoryParent = switch (category) {
            case MAFIC_IGNEOUS_EXTRUSIVE, INTERMEDIATE_IGNEOUS_EXTRUSIVE, FELSIC_IGNEOUS_EXTRUSIVE -> "igneous_extrusive";
            case MAFIC_IGNEOUS_INTRUSIVE, INTERMEDIATE_IGNEOUS_INTRUSIVE, FELSIC_IGNEOUS_INTRUSIVE -> "igneous_intrusive";
            case METAMORPHIC -> "metamorphic";
            case SEDIMENTARY -> "sedimentary";
        };

        ModelFile one = models()
                .withExistingParent(blockRes.getPath() + "_1", ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/loose_" + categoryParent + "_1"))
                .texture("texture", texture);
        ModelFile two = models()
                .withExistingParent(blockRes.getPath() + "_2", ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/loose_" + categoryParent + "_2"))
                .texture("texture", texture);
        ModelFile three = models()
                .withExistingParent(blockRes.getPath() + "_3", ResourceLocation.fromNamespaceAndPath("tfc", "block/rock/loose_" + categoryParent + "_3"))
                .texture("texture", texture);

        getVariantBuilder(block)
                .partialState().with(LooseRockBlock.COUNT, 1)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(one)
                                .rotationY(90)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 1)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(one)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 1)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(one)
                                .rotationY(180)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 1)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(one)
                                .rotationY(270)
                                .build()
                )

                .partialState().with(LooseRockBlock.COUNT, 2)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(two)
                                .rotationY(90)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 2)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(two)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 2)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(two)
                                .rotationY(180)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 2)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(two)
                                .rotationY(270)
                                .build()
                )

                .partialState().with(LooseRockBlock.COUNT, 3)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(three)
                                .rotationY(90)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 3)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(three)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 3)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(three)
                                .rotationY(180)
                                .build()
                )
                .partialState().with(LooseRockBlock.COUNT, 3)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(three)
                                .rotationY(270)
                                .build()
                );
    }

    private void twigWithItem(Block block, ResourceLocation logSideTexture, ResourceLocation logTopTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile twigModel = models()
                .withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("tfc", "block/groundcover/twig"))
                .texture("side", logSideTexture)
                .texture("top", logTopTexture);

        simpleBlock(block,
                ConfiguredModel.builder().modelFile(twigModel).rotationY(90).buildLast(),
                ConfiguredModel.builder().modelFile(twigModel).buildLast(),
                ConfiguredModel.builder().modelFile(twigModel).rotationY(180).buildLast(),
                ConfiguredModel.builder().modelFile(twigModel).rotationY(270).buildLast());
    }

    private void sluice(Block block, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile sluiceLowerModel = models()
                .withExistingParent(("block/sluice/" + blockRes.getPath() + "_lower"), ResourceLocation.fromNamespaceAndPath("tfc", "block/sluice_lower"))
                .texture("texture", strippedLogSideTexture);
        ModelFile sluiceUpperModel = models()
                .withExistingParent(("block/sluice/" + blockRes.getPath() + "_upper"), ResourceLocation.fromNamespaceAndPath("tfc", "block/sluice_upper"))
                .texture("texture", strippedLogSideTexture);

        getVariantBuilder(block)
                // Upper part (upper=true)
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(sluiceUpperModel).rotationY(90).addModel()
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(sluiceUpperModel).rotationY(0).addModel()
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(sluiceUpperModel).rotationY(180).addModel()
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(sluiceUpperModel).rotationY(270).addModel()

                // Lower part (upper=false)
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(sluiceLowerModel).rotationY(90).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(sluiceLowerModel).rotationY(0).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(sluiceLowerModel).rotationY(180).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(sluiceLowerModel).rotationY(270).addModel();

        simpleBlockItem(block, sluiceLowerModel);
    }

    private void toolRack(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile toolRackModel = models()
                .withExistingParent(("block/tool_rack/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/tool_rack"))
                .texture("texture", planksTexture)
                .texture("particle", planksTexture);

        horizontalBlock(block, toolRackModel, 0);
        simpleBlockItem(block, toolRackModel);
    }

    private void scribingTable(Block block, ResourceLocation planksTexture, ResourceLocation logSideTexture, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile scribingTableModel = models()
                .withExistingParent(("block/scribing_table/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/scribing_table"))
                .texture("top", strippedLogSideTexture)
                .texture("leg", logSideTexture)
                .texture("side", planksTexture)
                .texture("misc", ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/scribing_table/scribing_paraphernalia"))
                .texture("particle", planksTexture);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(scribingTableModel).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(scribingTableModel).rotationY(0).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(scribingTableModel).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(scribingTableModel).rotationY(270).addModel();

        simpleBlockItem(block, scribingTableModel);
    }

    private void vexxedScribingTable(Block block, ResourceLocation planksTexture, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile scribingTableModel = models()
                .withExistingParent(("block/scribing_table/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/scribing_table"))
                .texture("sheet", strippedLogSideTexture)
                .texture("planks", planksTexture);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(scribingTableModel).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(scribingTableModel).rotationY(0).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(scribingTableModel).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(scribingTableModel).rotationY(270).addModel();

        simpleBlockItem(block, scribingTableModel);
    }

    private void sewingTable(Block block, ResourceLocation planksTexture, ResourceLocation logSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile sewingTableModel = models()
                .withExistingParent(("block/sewing_table/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/sewing_table"))
                .texture("0", logSideTexture)
                .texture("1", planksTexture);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(sewingTableModel).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(sewingTableModel).rotationY(0).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(sewingTableModel).rotationY(180).addModel()  // default, no rotation
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(sewingTableModel).rotationY(270).addModel();

        simpleBlockItem(block, sewingTableModel);
    }

    private void shelf(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile shelfModel = models()
                .withExistingParent(("block/shelf/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/shelf"))
                .texture("0", planksTexture);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(shelfModel).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(shelfModel).rotationY(0).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(shelfModel).rotationY(180).addModel()  // default, no rotation
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(shelfModel).rotationY(270).addModel();

        simpleBlockItem(block, shelfModel);
    }

    private void barrelBlock(Block barrelBlock, ResourceLocation planksTexture, ResourceLocation strippedLogTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(barrelBlock);
        ModelFile barrelModel = models()
                .withExistingParent(("block/barrel/" + blockRes.getPath() + "_barrel"), ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel"))
                .texture("particle", planksTexture)
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogTexture);
        ModelFile barrelSealedModel = models()
                .withExistingParent(("block/barrel/" + blockRes.getPath() + "_barrel_sealed"), ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_sealed"))
                .texture("particle", planksTexture)
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogTexture);
        ModelFile barrelSideModel = models()
                .withExistingParent(("block/barrel/" + blockRes.getPath() + "_barrel_side"), ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side"))
                .texture("particle", planksTexture)
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogTexture);
        ModelFile barrelSideRackModel = models()
                .withExistingParent(("block/barrel/" + blockRes.getPath() + "_barrel_side_rack"), ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side_rack"))
                .texture("particle", planksTexture)
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogTexture);
        ModelFile barrelSideSealedModel = models()
                .withExistingParent(("block/barrel/" + blockRes.getPath() + "_barrel_side_sealed"), ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side_sealed"))
                .texture("particle", planksTexture)
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogTexture);
        ModelFile barrelSideSealedRackModel = models()
                .withExistingParent(("block/barrel/" + blockRes.getPath() + "_barrel_side_sealed_rack"), ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side_sealed_rack"))
                .texture("particle", planksTexture)
                .texture("planks", planksTexture)
                .texture("sheet", strippedLogTexture);

        var builder = getVariantBuilder(barrelBlock);

        // Facing UP — no rotation needed
        builder
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, true)
                .modelForState().modelFile(barrelSealedModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, false)
                .modelForState().modelFile(barrelModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, true)
                .modelForState().modelFile(barrelSealedModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, false)
                .modelForState().modelFile(barrelModel).addModel();

        // Horizontal facings — adjust rotation so barrel lies along the facing axis
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            // Base rotation from direction
            int baseYRot = (int) dir.toYRot(); // N=0, E=90, S=180, W=270

            // Add 90° so that when facing east/west, the model aligns east-west
            // This compensates for side models being authored facing north
            int fixedYRot = (baseYRot + 90) % 360;

            ModelFile rackModel = BarrelBlock.SEALED.getPossibleValues().contains(true) ? barrelSideSealedRackModel : barrelSideRackModel;
            ModelFile noRackModel = BarrelBlock.SEALED.getPossibleValues().contains(true) ? barrelSideSealedModel : barrelSideModel;

            // Rack + Sealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, true)
                    .modelForState().modelFile(barrelSideSealedRackModel).rotationY(fixedYRot).addModel();

            // Rack + Unsealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, false)
                    .modelForState().modelFile(barrelSideRackModel).rotationY(fixedYRot).addModel();

            // No Rack + Sealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, true)
                    .modelForState().modelFile(barrelSideSealedModel).rotationY(fixedYRot).addModel();

            // No Rack + Unsealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, false)
                    .modelForState().modelFile(barrelSideModel).rotationY(fixedYRot).addModel();
        }

        simpleBlockItem(barrelBlock, barrelModel);
    }

    private void logFence(Block block, ResourceLocation planksTexture, ResourceLocation logSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile logFenceInventoryModel = models()
                .withExistingParent(("block/log_fence/" + blockRes.getPath() + "_inventory"), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/log_fence/inventory"))
                .texture("log", logSideTexture)
                .texture("planks", planksTexture);
        ModelFile logFencePostModel = models()
                .withExistingParent(("block/log_fence/" + blockRes.getPath() + "_post"), mcLoc("block/fence_post"))
                .texture("texture", logSideTexture);
        ModelFile logFenceSideModel = models()
                .withExistingParent(("block/log_fence/" + blockRes.getPath() + "_side"), mcLoc("block/fence_side"))
                .texture("texture", planksTexture);

        getMultipartBuilder(block)
                // Always show the post
                .part()
                .modelFile(logFencePostModel)
                .addModel()
                .end()

                // North connection
                .part()
                .modelFile(logFenceSideModel)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.NORTH, true)
                .end()

                // East connection
                .part()
                .modelFile(logFenceSideModel)
                .rotationY(90)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.EAST, true)
                .end()

                // South connection
                .part()
                .modelFile(logFenceSideModel)
                .rotationY(180)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.SOUTH, true)
                .end()

                // West connection
                .part()
                .modelFile(logFenceSideModel)
                .rotationY(270)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.WEST, true)
                .end();

        simpleBlockItem(block, logFenceInventoryModel);
    }

    private void verticalSupport(Block block, ModelFile connection, ResourceLocation strippedLogTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile supportVerticalModel = models()
                .withExistingParent(("block/support/" + blockRes.getPath() + "_vertical"), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/support/vertical"))
                .texture("texture", strippedLogTexture)
                .texture("particle", strippedLogTexture);

        getMultipartBuilder(block)
                // Always show the vertical beam
                .part()
                .modelFile(supportVerticalModel)
                .addModel()
                .end()

                // Connections
                .part()
                .modelFile(connection)
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.NORTH, true)
                .end()
                .part()
                .modelFile(connection)
                .addModel()  // east: no rotation
                .condition(BlockStateProperties.EAST, true)
                .end()
                .part()
                .modelFile(connection)
                .rotationY(90)
                .addModel()
                .condition(BlockStateProperties.SOUTH, true)
                .end()
                .part()
                .modelFile(connection)
                .rotationY(180)
                .addModel()
                .condition(BlockStateProperties.WEST, true)
                .end();
    }

    private void horizontalSupport(Block block, ModelFile connection, ResourceLocation strippedLogTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile supportHorizontalModel = models()
                .withExistingParent(("block/support/" + blockRes.getPath() + "_horizontal"), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/support/horizontal"))
                .texture("texture", strippedLogTexture)
                .texture("particle", strippedLogTexture);

        getMultipartBuilder(block)
                // Always show the vertical beam
                .part()
                .modelFile(supportHorizontalModel)
                .addModel()
                .end()

                // Connections
                .part()
                .modelFile(connection)
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.NORTH, true)
                .end()
                .part()
                .modelFile(connection)
                .addModel()  // east: no rotation
                .condition(BlockStateProperties.EAST, true)
                .end()
                .part()
                .modelFile(connection)
                .rotationY(90)
                .addModel()
                .condition(BlockStateProperties.SOUTH, true)
                .end()
                .part()
                .modelFile(connection)
                .rotationY(180)
                .addModel()
                .condition(BlockStateProperties.WEST, true)
                .end();
    }

    private ModelFile supportConnectionModel(CompatWood wood, ResourceLocation strippedLogTexture) {
        return models()
                .withExistingParent(("block/support/" + wood.getSerializedName() + "_connection"), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/support/connection"))
                .texture("texture", strippedLogTexture)
                .texture("particle", strippedLogTexture);
    }

    private ModelFile supportInventoryModel(CompatWood wood, ResourceLocation strippedLogTexture) {
        return models()
                .withExistingParent(("block/support/" + wood.getSerializedName() + "_inventory"), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/support/inventory"))
                .texture("texture", strippedLogTexture);
    }

    private void loom(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        ModelFile loomModel = models()
                .withExistingParent(blockRes.getPath(), ResourceLocation.fromNamespaceAndPath("tfc", "block/loom"))
                .texture("texture", planksTexture)
                .texture("particle", planksTexture);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                .modelForState().modelFile(loomModel).rotationY(270).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .modelForState().modelFile(loomModel).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                .modelForState().modelFile(loomModel).rotationY(0).addModel()  // default, no rotation
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                .modelForState().modelFile(loomModel).rotationY(90).addModel();

        simpleBlockItem(block, loomModel);
    }

    private void bladedAxle(Block block, ModelFile emptyModel, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile bladedAxleModel = models()
                .withExistingParent(("block/bladed_axle/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/bladed_axle"))
                .texture("wood", strippedLogSideTexture);

        simpleBlock(block, emptyModel);
    }

    private void axle(Block block, ModelFile emptyModel, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile axleModel = models()
                .withExistingParent(("block/axle/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/axle"))
                .texture("wood", strippedLogSideTexture);

        simpleBlock(block, emptyModel);
    }

    private void waterWheel(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile waterWheelModel = models()
                .getBuilder("block/water_wheel/" + blockRes.getPath())
                .texture("particle", planksTexture);

        simpleBlock(block, waterWheelModel);
    }

    private void encasedAxle(Block block, ResourceLocation planksTexture, ResourceLocation strippedLogSideTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile encasedAxleModel = models()
                .withExistingParent(("block/encased_axle/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/ore_column"))
                .texture("side", strippedLogSideTexture)
                .texture("end", planksTexture)
                .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing"))
                .texture("overlay_end", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_front"))
                .texture("particle", strippedLogSideTexture);

        getVariantBuilder(block)
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X)
                .modelForState().modelFile(encasedAxleModel).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(encasedAxleModel).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(encasedAxleModel).rotationX(90).addModel();

        simpleBlockItem(block, encasedAxleModel);
    }

    private void gearBox(Block block, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile gearBoxPortModel = models()
                .withExistingParent(("block/gear_box/" + blockRes.getPath() + "_port"), ResourceLocation.fromNamespaceAndPath("tfc", "block/gear_box_port"))
                .texture("all", planksTexture)
                .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_front"));
        ModelFile gearBoxFaceModel = models()
                .withExistingParent(("block/gear_box/" + blockRes.getPath() + "_face"), ResourceLocation.fromNamespaceAndPath("tfc", "block/gear_box_face"))
                .texture("all", planksTexture)
                .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_round"));

        getMultipartBuilder(block)
                // North
                .part().modelFile(gearBoxPortModel).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(gearBoxFaceModel).addModel().condition(BlockStateProperties.NORTH, false).end()

                // South
                .part().modelFile(gearBoxPortModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(gearBoxFaceModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, false).end()

                // East
                .part().modelFile(gearBoxPortModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(gearBoxFaceModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, false).end()

                // West
                .part().modelFile(gearBoxPortModel).rotationY(270).addModel().condition(BlockStateProperties.WEST, true).end()
                .part().modelFile(gearBoxFaceModel).rotationY(270).addModel().condition(BlockStateProperties.WEST, false).end()

                // Down
                .part().modelFile(gearBoxPortModel).rotationX(90).addModel().condition(BlockStateProperties.DOWN, true).end()
                .part().modelFile(gearBoxFaceModel).rotationX(90).addModel().condition(BlockStateProperties.DOWN, false).end()

                // Up
                .part().modelFile(gearBoxPortModel).rotationX(270).addModel().condition(BlockStateProperties.UP, true).end()
                .part().modelFile(gearBoxFaceModel).rotationX(270).addModel().condition(BlockStateProperties.UP, false).end();
    }

    private void clutch(Block block, ResourceLocation strippedLogSideTexture, ResourceLocation planksTexture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile clutchModel = models()
                .withExistingParent(("block/clutch/" + blockRes.getPath()), ResourceLocation.fromNamespaceAndPath("tfc", "block/ore_column"))
                .texture("side", strippedLogSideTexture)
                .texture("end", planksTexture)
                .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_unpowered"))
                .texture("overlay_end", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_front"))
                .texture("particle", strippedLogSideTexture);
        ModelFile clutchPoweredModel = models()
                .withExistingParent(("block/clutch/" + blockRes.getPath() + "_powered"), ResourceLocation.fromNamespaceAndPath("tfc", "block/ore_column"))
                .texture("side", strippedLogSideTexture)
                .texture("end", planksTexture)
                .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_powered"))
                .texture("overlay_end", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_front"))
                .texture("particle", strippedLogSideTexture);

        getVariantBuilder(block)
                // Axis X
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).with(BlockStateProperties.POWERED, false)
                .modelForState().modelFile(clutchModel).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).with(BlockStateProperties.POWERED, true)
                .modelForState().modelFile(clutchPoweredModel).rotationX(90).rotationY(90).addModel()

                // Axis Y
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).with(BlockStateProperties.POWERED, false)
                .modelForState().modelFile(clutchModel).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).with(BlockStateProperties.POWERED, true)
                .modelForState().modelFile(clutchPoweredModel).addModel()

                // Axis Z
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).with(BlockStateProperties.POWERED, false)
                .modelForState().modelFile(clutchModel).rotationX(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).with(BlockStateProperties.POWERED, true)
                .modelForState().modelFile(clutchPoweredModel).rotationX(90).addModel();

        simpleBlockItem(block, clutchModel);
    }

    private void blockItem(Block block) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        simpleBlockItem(
                block,
                new ModelFile.UncheckedModelFile(
                        ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "block/" + blockRes.getPath()
                        )
                )
        );
    }
}
