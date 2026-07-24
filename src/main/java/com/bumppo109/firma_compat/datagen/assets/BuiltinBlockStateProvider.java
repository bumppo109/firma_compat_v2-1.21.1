package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.*;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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

        for (CompatRock rock : CompatRock.VALUES) {
            var rockMap = ModBlocks.ROCK_BLOCKS.get(rock);

            ResourceLocation rawTexture = rock.blockAsset().textures().get(BlockTextureSlot.SIDE);
            ResourceLocation cobbleTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + rock.getSerializedName() + "_cobble");
            ResourceLocation mossyCobbleTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + "mossy_" + rock.getSerializedName() + "_cobble");

            for (CompatRock.BlockType blockType : CompatRock.BlockType.VALUES) {
                Block block = rockMap.get(blockType).get();
                ResourceLocation looseRockTexture = blockType.name().startsWith("MOSSY") && blockType.name().endsWith("COBBLE") ? mossyCobbleTexture : rawTexture;

                switch (blockType) {
                    case HARDENED -> cubeAllWithItem(block, rawTexture);
                    case LOOSE, MOSSY_LOOSE -> looseRockWithItem(block, looseRockTexture, rock.category());
                    case COBBLE, HARDENED_COBBLE -> cubeAllWithItem(block, cobbleTexture);
                    case MOSSY_COBBLE, MOSSY_HARDENED_COBBLE -> cubeAllWithItem(block, mossyCobbleTexture);
                    case SPIKE -> spikeWithItem(block, rawTexture);
                }

                /**
                 * assumes only COMPAT_HARDENED_COBBLE and MOSSY_HARDENED_COBBLE have variants
                 */
                if(blockType.hasVariants()) {
                    slabWithItem(ModBlocks.ROCK_DECORATIONS.get(rock).get(blockType).slab().get(), block, looseRockTexture);
                    stairsWithItem(ModBlocks.ROCK_DECORATIONS.get(rock).get(blockType).stair().get(), looseRockTexture);
                    wallWithItem(ModBlocks.ROCK_DECORATIONS.get(rock).get(blockType).wall().get(), looseRockTexture);
                }
            }
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
    }

    private void cubeAllWithItem(Block block, ResourceLocation texture) {
        ResourceLocation blockRes = BuiltInRegistries.BLOCK.getKey(block);

        simpleBlockWithItem(block, models().cubeAll(blockRes.getPath(), texture));
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
