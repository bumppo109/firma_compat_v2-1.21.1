package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FirmaCompat.MODID);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FIRMA_COMPAT_TAB =
            CREATIVE_TABS.register(FirmaCompat.MODID, () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.LUMBER.get(CompatWood.OAK)))
                            .title(Component.translatable(
                                    "firma_compat.creative_tab.firma_compat"
                            ))

                            .displayItems((parameters, output) -> {


                                FirmaCompat.LOGGER.info(
                                        "[CreativeTab DEBUG] Starting FirmaCompat tab build"
                                );


                                /*
                                 * WOODS
                                 */
                                for (CompatWood wood : CompatWood.VALUES) {

                                    add(
                                            output,
                                            "Lumber " + wood.name(),
                                            ModItems.LUMBER.get(wood)
                                    );


                                    var blocks = ModBlocks.WOODS.get(wood);

                                    if (blocks == null) {

                                        FirmaCompat.LOGGER.error(
                                                "[CreativeTab DEBUG] Missing wood map -> {}",
                                                wood.name()
                                        );

                                        continue;
                                    }


                                    for (CompatWood.BlockType type : CompatWood.BlockType.values()) {

                                        if (type == CompatWood.BlockType.VERTICAL_SUPPORT ||
                                                type == CompatWood.BlockType.HORIZONTAL_SUPPORT) {
                                            continue;
                                        }


                                        add(
                                                output,
                                                "Wood Block " +
                                                        wood.name() +
                                                        " " +
                                                        type.name(),
                                                blocks.get(type)
                                        );
                                    }


                                    add(
                                            output,
                                            "Support " + wood.name(),
                                            ModItems.SUPPORTS.get(wood)
                                    );
                                }



                                /*
                                 * ROCKS
                                 */
                                for (CompatRock rock : CompatRock.VALUES) {

                                    var rockMap = ModBlocks.ROCK_BLOCKS.get(rock);
                                    var rockDecoMap = ModBlocks.ROCK_DECORATIONS.get(rock);


                                    if (rockMap == null) {

                                        FirmaCompat.LOGGER.error(
                                                "[CreativeTab DEBUG] Missing rock map -> {}",
                                                rock.name()
                                        );

                                        continue;
                                    }


                                    add(
                                            output,
                                            "Rock Brick " + rock.name(),
                                            rock.brickItem()
                                    );


                                    for (CompatRock.BlockType blockType :
                                            CompatRock.BlockType.values()) {


                                        add(
                                                output,
                                                "Rock Block " +
                                                        rock.name() +
                                                        " " +
                                                        blockType.name(),
                                                rockMap.get(blockType)
                                        );


                                        if (blockType.hasVariants()) {

                                            if (rockDecoMap == null) {

                                                FirmaCompat.LOGGER.error(
                                                        "[CreativeTab DEBUG] Missing rock decoration map -> {} {}",
                                                        rock.name(),
                                                        blockType.name()
                                                );

                                                continue;
                                            }


                                            var decoration =
                                                    rockDecoMap.get(blockType);


                                            if (decoration == null) {

                                                FirmaCompat.LOGGER.error(
                                                        "[CreativeTab DEBUG] Missing decoration -> {} {}",
                                                        rock.name(),
                                                        blockType.name()
                                                );

                                                continue;
                                            }


                                            add(
                                                    output,
                                                    "Rock Stair " +
                                                            rock.name() +
                                                            " " +
                                                            blockType.name(),
                                                    decoration.stair()
                                            );


                                            add(
                                                    output,
                                                    "Rock Slab " +
                                                            rock.name() +
                                                            " " +
                                                            blockType.name(),
                                                    decoration.slab()
                                            );


                                            add(
                                                    output,
                                                    "Rock Wall " +
                                                            rock.name() +
                                                            " " +
                                                            blockType.name(),
                                                    decoration.wall()
                                            );
                                        }
                                    }
                                }


                                FirmaCompat.LOGGER.info(
                                        "[CreativeTab DEBUG] Finished FirmaCompat tab build"
                                );

                            })
                            .build()
            );



    private static void add(
            CreativeModeTab.Output output,
            String name,
            Supplier<? extends ItemLike> supplier
    ) {

        if (supplier == null) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] NULL SUPPLIER -> {}",
                    name
            );

            return;
        }


        ItemLike itemLike;

        try {

            itemLike = supplier.get();

        } catch (Throwable t) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] Supplier exception -> {}",
                    name,
                    t
            );

            return;
        }



        if (itemLike == null) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] supplier returned NULL -> {}",
                    name
            );

            return;
        }



        Item item;

        try {

            item = itemLike.asItem();

        } catch (Throwable t) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] asItem exception -> {} ({})",
                    name,
                    describe(itemLike),
                    t
            );

            return;
        }



        if (item == null) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] asItem returned NULL -> {} ({})",
                    name,
                    describe(itemLike)
            );

            return;
        }



        if (item == Items.AIR) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] AIR ITEM -> {} ({})",
                    name,
                    describe(itemLike)
            );

            return;
        }



        var registryName = BuiltInRegistries.ITEM.getKey(item);


        if (registryName == null) {

            FirmaCompat.LOGGER.error(
                    "[CreativeTab DEBUG] No registry name -> {} ({})",
                    name,
                    describe(itemLike)
            );

            return;
        }



        FirmaCompat.LOGGER.info(
                "[CreativeTab DEBUG] Added {} -> {}",
                name,
                registryName
        );


        output.accept(item);
    }



    private static String describe(ItemLike itemLike) {

        if (itemLike == null)
            return "null";


        if (itemLike instanceof DeferredHolder<?, ?> holder) {

            return "DeferredHolder[" +
                    holder.getId() +
                    "]";
        }


        try {

            Item item = itemLike.asItem();

            return "Item[" +
                    BuiltInRegistries.ITEM.getKey(item) +
                    "] " +
                    item.getClass().getName();

        } catch (Throwable t) {

            return "Broken ItemLike " +
                    itemLike.getClass().getName();
        }
    }
}