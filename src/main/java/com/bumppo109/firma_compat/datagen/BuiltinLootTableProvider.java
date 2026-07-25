package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.loot.ApplyStackSizeFunction;
import net.dries007.tfc.util.loot.IsIsolatedCondition;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.predicates.ExplosionCondition.survivesExplosion;

public class BuiltinLootTableProvider extends LootTableProvider {
    public BuiltinLootTableProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)), registries);
    }

    private static class ModBlockLoot extends BlockLootSubProvider {
        protected ModBlockLoot(net.minecraft.core.HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
        //Vanilla Changes
            add(Blocks.BARREL, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootTableItem(ModItems.LUMBER.get(CompatWood.OAK))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                                    .apply(ApplyExplosionDecay.explosionDecay())     // optional: less items if exploded
                            )
                            .when(survivesExplosion())
                    )
            );

            add(Blocks.BOOKSHELF, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootTableItem(ModItems.LUMBER.get(CompatWood.OAK))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f)))
                                    .apply(ApplyExplosionDecay.explosionDecay())     // optional: less items if exploded
                            )
                            .when(survivesExplosion())
                    )
            );

            add(Blocks.ANCIENT_DEBRIS, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))
                    )
                )
            );

        //Wood
            for (CompatWood wood : CompatWood.VALUES) {
                var woodMap = ModBlocks.WOODS.get(wood);

                for (CompatWood.BlockType type : CompatWood.BlockType.values()) {
                    if (woodMap.containsKey(type)) {
                        Block block = woodMap.get(type).get();

                        if (type == CompatWood.BlockType.WINDMILL) {
                            Block axleBlock = woodMap.get(CompatWood.BlockType.AXLE).get();
                            add(block, LootTable.lootTable()
                                    .withPool(lootPool()
                                            .name("loot_pool")
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(lootTableItem(axleBlock))
                                            .when(survivesExplosion())
                                    )
                            );
                        } else if (type.equals(CompatWood.BlockType.SLUICE)) {
                            sluiceBlockLoot(block);
                        } else if (type.equals(CompatWood.BlockType.BARREL)) {
                            createBarrelLoot(block);
                        } else if (type.equals(CompatWood.BlockType.VERTICAL_SUPPORT)  || type.equals(CompatWood.BlockType.HORIZONTAL_SUPPORT)) {
                            dropOther(block, ModItems.SUPPORTS.get(wood).get());
                        } else if (type.needsItem()) {
                            dropSelf(block);
                        }
                    }
                }
            }
            dropSelf(ModBlocks.COMPAT_CHEST.get());
            dropSelf(ModBlocks.COMPAT_TRAPPED_CHEST.get());

            //Rock
            for (CompatRock rock : CompatRock.VALUES) {
                var rockMap = ModBlocks.ROCK_BLOCKS.get(rock);

                for (CompatRock.BlockType type : CompatRock.BlockType.values()) {
                    if (rockMap.containsKey(type)) {
                        Block block = rockMap.get(type).get();

                        if(type.equals(CompatRock.BlockType.LOOSE) || type.equals(CompatRock.BlockType.MOSSY_LOOSE)){
                            addLooseRockLoot(block);
                        } else if(type.equals(CompatRock.BlockType.HARDENED)){
                            addHardenedRockLoot(block, rock);
                        } else if(type.equals(CompatRock.BlockType.SPIKE) || type.equals(CompatRock.BlockType.ROPE_ANCHOR)){
                            addSpikeLoot(block, rock);
                        } else {
                            dropSelf(block);
                        }
                    }
                }
            }

            //deposit
            dropSelf(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get());
            dropSelf(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get());
            dropSelf(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get());
            dropSelf(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get());

            //aqueduct
            ModBlocks.AQUEDUCTS.forEach((compatRockSets, blockId) -> dropSelf(blockId.get()));
            dropSelf(ModBlocks.BRICK_AQUEDUCT.get());
            dropSelf(ModBlocks.PRISMARINE_BRICK_AQUEDUCT.get());
            dropSelf(ModBlocks.RED_NETHER_BRICK_AQUEDUCT.get());
            dropSelf(ModBlocks.QUARTZ_BRICK_AQUEDUCT.get());


            // Non-graded ores (normal only)
            ModBlocks.ORES.forEach((rock, oreMap) -> {
                oreMap.forEach((ore, id) -> {
                    if (id != null) {
                        Block oreBlock = id.get();
                        String oreName = ore.name().toLowerCase(Locale.ROOT);
                        ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath("tfc", "ore/" + oreName);
                        addSimpleOreDrop(oreBlock, itemId);
                    }
                });
            });

            // Graded ores (poor/normal/rich) – use the correct map (gradeMap)
            ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> {
                oreMap.forEach((ore, gradeMap) -> {
                    gradeMap.forEach((grade, id) -> {
                        if (id != null) {
                            Block oreBlock = id.get();  // ← this is the graded block
                            String oreName = ore.name().toLowerCase(Locale.ROOT);
                            String gradePrefix = grade.name().toLowerCase(Locale.ROOT);
                            ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath("tfc", "ore/" + gradePrefix + "_" + oreName);
                            addSimpleOreDrop(oreBlock, itemId);
                        }
                    });
                });
            });

        //Earthen
            add(ModBlocks.CLAY_GRASS_BLOCK.get(), LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.CLAY_BALL)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );
            add(ModBlocks.CLAY_DIRT.get(), LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.CLAY_BALL)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );
            add(ModBlocks.CLAY_PODZOL.get(), LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.CLAY_BALL)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );
            add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TFCItems.KAOLIN_CLAY)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );
            add(ModBlocks.KAOLIN_CLAY_DIRT.get(), LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TFCItems.KAOLIN_CLAY)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );
            add(ModBlocks.KAOLIN_CLAY_PODZOL.get(), LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TFCItems.KAOLIN_CLAY)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );

            dropOther(ModBlocks.COMPAT_FARMLAND.get(), Blocks.DIRT);

            addDryingBricksLoot(ModBlocks.DRYING_MUD_BRICK.get(), ModItems.MUD_BRICK.get());
        }

        private void sluiceBlockLoot(Block block) {
            add(block, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(block)
                                    .when(LootItemBlockStatePropertyCondition
                                            .hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(SluiceBlock.UPPER, true)
                                            )
                                    )
                            )
                    )
            );
        }

        private void addSimpleOreDrop(Block oreBlock, ResourceLocation oreItemId) {
            Item oreItem = BuiltInRegistries.ITEM.get(oreItemId);
            if (oreItem == null || oreItem == Items.AIR) {
                FirmaCompat.LOGGER.error("Missing ore item: {}", oreItemId);
                return;
            }

            add(oreBlock, LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(oreItem))
                            .when(survivesExplosion())
                    )
            );
        }

        private void addSimpleOreDrop(Block oreBlock, Item oreItem) {
            if (oreItem == null || oreItem == Items.AIR) {
                FirmaCompat.LOGGER.error("Missing ore item: {}", oreItem);
                return;
            }

            add(oreBlock, LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(oreItem))
                            .when(survivesExplosion())
                    )
            );
        }

        private void addLooseRockLoot(Block looseBlock) {
            // looseBlock is already the Block instance → it implements ItemLike and drops its own item

            LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(looseBlock)  // ← pass the Block directly
                    // Base count = 1 is default (no function needed)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(looseBlock)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(LooseRockBlock.COUNT, 2))))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(looseBlock)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(LooseRockBlock.COUNT, 3))))
                    .apply(ApplyExplosionDecay.explosionDecay());

            add(looseBlock, LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(entry)
                            .when(survivesExplosion())
                    )
            );
        }

        private void addHardenedRockLoot(Block hardenedBlock, CompatRock rock) {
            Item raw = rock.rockMaterial().raw().base().get().asItem();

            Item loose = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem();

            var alternatives = AlternativesEntry.alternatives(
                    LootItem.lootTableItem(raw)
                            .when(() -> IsIsolatedCondition.INSTANCE),
                    LootItem.lootTableItem(loose)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
            );

            add(hardenedBlock, LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(alternatives)
                            .when(survivesExplosion())
                    )
            );
        }

        private void addSpikeLoot(Block spikeBlock, CompatRock rock) {
            Item loose = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem();

            add(spikeBlock, LootTable.lootTable()
                    .withPool(lootPool()
                            .name("loot_pool")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(loose)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                            .when(survivesExplosion())
                    )
            );
        }

        /**
         * Adds a loot table for a drying bricks block that drops:
         * - drying_bricks/<soil> when not dried (based on count)
         * - mud_brick/<soil> when dried (based on count)
         * Matches your exact multi-pool JSON structure.
         */
        private void addDryingBricksLoot(Block dryingBlock, Item driedBrick) {
            LootTable.Builder builder = LootTable.lootTable();

            // Add one pool per possible count value (1 to 4)
            for (int count = 1; count <= 4; count++) {
                // Pool for NOT dried → drying bricks
                LootPoolEntryContainer.Builder<?> notDriedEntry = LootItem.lootTableItem(dryingBlock)
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(dryingBlock)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DryingBricksBlock.COUNT, count)
                                        .hasProperty(DryingBricksBlock.DRIED, false)))
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));

                builder.withPool(lootPool()
                        .name("loot_pool")  // all pools can share the name, or make unique if preferred
                        .setRolls(ConstantValue.exactly(1))
                        .add(notDriedEntry)
                        .when(survivesExplosion()));

                // Pool for DRIED → mud brick
                LootPoolEntryContainer.Builder<?> driedEntry = LootItem.lootTableItem(driedBrick)
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(dryingBlock)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DryingBricksBlock.COUNT, count)
                                        .hasProperty(DryingBricksBlock.DRIED, true)))
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));

                builder.withPool(lootPool()
                        .name("loot_pool")
                        .setRolls(ConstantValue.exactly(1))
                        .add(driedEntry)
                        .when(survivesExplosion()));
            }

            add(dryingBlock, builder);
        }

        protected void createBarrelLoot(Block barrelBlock) {
            add(barrelBlock, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(AlternativesEntry.alternatives(
                                    lootTableItem(barrelBlock)
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(barrelBlock)
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(TFCBlockStateProperties.SEALED, true)))
                                            .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                    .include(DataComponents.CUSTOM_NAME)
                                                    .include(TFCComponents.BARREL.get()))
                                            .apply(ApplyStackSizeFunction.simpleBuilder(ApplyStackSizeFunction::new)),
                                    lootTableItem(barrelBlock.asItem())
                            ))
                            .when(survivesExplosion())
                    )
            );
        }


        private boolean needsCustomLoot(CompatRock.BlockType type) {
            return type == CompatRock.BlockType.LOOSE;  // add other types here later, e.g. || type == SLAB || type == STAIRS
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> knownBlocks = new ArrayList<>();

        //Vanilla Changes
            knownBlocks.add(Blocks.BARREL);
            knownBlocks.add(Blocks.ANCIENT_DEBRIS);
            knownBlocks.add(Blocks.BOOKSHELF);

        //Wood
            ModBlocks.WOODS.forEach((compatWood, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    knownBlocks.add(blockId.get());
                });
            });
            knownBlocks.add(ModBlocks.COMPAT_CHEST.get());
            knownBlocks.add(ModBlocks.COMPAT_TRAPPED_CHEST.get());

        //Rock
            ModBlocks.ROCK_BLOCKS.forEach((compatRock, blockTypeIdMap) -> {
                blockTypeIdMap.forEach((blockType, blockId) -> {
                    knownBlocks.add(blockId.get());
                });
            });
            ModBlocks.ORES.forEach((rock, oreMap) -> {
                oreMap.forEach((ore, id) -> {
                    if (id != null) {
                        knownBlocks.add(id.get());
                    }
                });
            });
            ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> {
                oreMap.forEach((ore, gradeMap) -> {
                    gradeMap.forEach((grade, id) -> {
                        if (id != null) {
                            knownBlocks.add(id.get());
                        }
                    });
                });
            });
            ModBlocks.AQUEDUCTS.forEach((brick, id) -> knownBlocks.add(id.get()));
            knownBlocks.add(ModBlocks.BRICK_AQUEDUCT.get());
            knownBlocks.add(ModBlocks.PRISMARINE_BRICK_AQUEDUCT.get());
            knownBlocks.add(ModBlocks.RED_NETHER_BRICK_AQUEDUCT.get());
            knownBlocks.add(ModBlocks.QUARTZ_BRICK_AQUEDUCT.get());

        //Earthen
            knownBlocks.add(ModBlocks.DRYING_MUD_BRICK.get());
            knownBlocks.add(ModBlocks.COMPAT_FARMLAND.get());
            knownBlocks.add(ModBlocks.CLAY_GRASS_BLOCK.get());
            knownBlocks.add(ModBlocks.CLAY_DIRT.get());
            knownBlocks.add(ModBlocks.CLAY_PODZOL.get());
            knownBlocks.add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get());
            knownBlocks.add(ModBlocks.KAOLIN_CLAY_DIRT.get());
            knownBlocks.add(ModBlocks.KAOLIN_CLAY_PODZOL.get());

            knownBlocks.add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get());
            knownBlocks.add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get());
            knownBlocks.add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get());
            knownBlocks.add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get());

            return knownBlocks;
        }
    }
}
