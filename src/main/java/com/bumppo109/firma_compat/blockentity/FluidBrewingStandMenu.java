package com.bumppo109.firma_compat.blockentity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;

public class FluidBrewingStandMenu
        extends AbstractContainerMenu {

    public static final int BREWING_SLOT_COUNT = 5;

    public static final int PLAYER_INV_START = 5;
    public static final int PLAYER_INV_END = 32;
    public static final int HOTBAR_START = 32;
    public static final int HOTBAR_END = 41;

    private final Container blockEntity;
    private final ContainerData data;

    /**
     * Client-side constructor.
     *
     * This is the constructor referenced by ModMenus.
     *
     * The client does not have to use the real block entity here.
     * The server synchronizes the menu slots and ContainerData.
     */
    public FluidBrewingStandMenu(
            int containerId,
            Inventory playerInventory
    ) {
        this(
                containerId,
                playerInventory,
                new SimpleContainer(BREWING_SLOT_COUNT),
                new SimpleContainerData(2)
        );
    }

    /**
     * Server-side constructor.
     */
    public FluidBrewingStandMenu(
            int containerId,
            Inventory playerInventory,
            FluidBrewingStandBlockEntity blockEntity
    ) {
        this(
                containerId,
                playerInventory,
                blockEntity,
                blockEntity.data
        );
    }

    /**
     * Common constructor.
     */
    public FluidBrewingStandMenu(
            int containerId,
            Inventory playerInventory,
            Container blockEntity,
            ContainerData data
    ) {
        super(
                ModMenus.FLUID_BREWING_STAND.get(),
                containerId
        );

        this.blockEntity = blockEntity;
        this.data = data;

        /*
         * --------------------------------------------------------
         * BREWING STAND SLOTS
         * --------------------------------------------------------
         */

        // Bottle 0
        addSlot(
                new Slot(
                        blockEntity,
                        FluidBrewingStandBlockEntity.BOTTLE_0,
                        56,
                        51
                )
        );

        // Bottle 1
        addSlot(
                new Slot(
                        blockEntity,
                        FluidBrewingStandBlockEntity.BOTTLE_1,
                        79,
                        58
                )
        );

        // Bottle 2
        addSlot(
                new Slot(
                        blockEntity,
                        FluidBrewingStandBlockEntity.BOTTLE_2,
                        102,
                        51
                )
        );

        // Ingredient
        addSlot(
                new Slot(
                        blockEntity,
                        FluidBrewingStandBlockEntity.INGREDIENT,
                        79,
                        17
                )
        );

        // Fuel
        addSlot(
                new Slot(
                        blockEntity,
                        FluidBrewingStandBlockEntity.FUEL,
                        17,
                        17
                )
        );

        /*
         * --------------------------------------------------------
         * PLAYER INVENTORY
         * --------------------------------------------------------
         */

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(
                        new Slot(
                                playerInventory,
                                column + row * 9 + 9,
                                8 + column * 18,
                                84 + row * 18
                        )
                );
            }
        }

        /*
         * Hotbar.
         */
        for (int column = 0; column < 9; column++) {
            addSlot(
                    new Slot(
                            playerInventory,
                            column,
                            8 + column * 18,
                            142
                    )
            );
        }

        /*
         * Synchronize:
         *
         * 0 = brew time
         * 1 = fuel
         */
        addDataSlots(data);
    }

    public int getFuel() {
        return data.get(1);
    }

    public int getBrewingTicks() {
        return data.get(0);
    }

    public Container getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {
        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        /*
         * Brewing stand -> player inventory.
         */
        if (index < BREWING_SLOT_COUNT) {
            if (!moveItemStackTo(
                    stack,
                    PLAYER_INV_START,
                    HOTBAR_END,
                    true
            )) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, original);
        }

        /*
         * Player inventory -> brewing stand.
         */
        else {
            /*
             * Try fuel first.
             */
            if (stack.is(com.bumppo109.firma_compat.util.ModTags.Items.BREWING_FUEL)) {
                if (!moveItemStackTo(
                        stack,
                        FluidBrewingStandBlockEntity.FUEL,
                        FluidBrewingStandBlockEntity.FUEL + 1,
                        false
                )) {
                    return ItemStack.EMPTY;
                }
            }

            /*
             * Otherwise try the ingredient slot.
             */
            else if (!moveItemStackTo(
                    stack,
                    FluidBrewingStandBlockEntity.INGREDIENT,
                    FluidBrewingStandBlockEntity.INGREDIENT + 1,
                    false
            )) {
                /*
                 * Finally try bottle slots.
                 */
                if (!moveItemStackTo(
                        stack,
                        FluidBrewingStandBlockEntity.BOTTLE_0,
                        FluidBrewingStandBlockEntity.BOTTLE_2 + 1,
                        false
                )) {
                    /*
                     * Main inventory -> hotbar.
                     */
                    if (index >= PLAYER_INV_START
                            && index < HOTBAR_START) {

                        if (!moveItemStackTo(
                                stack,
                                HOTBAR_START,
                                HOTBAR_END,
                                false
                        )) {
                            return ItemStack.EMPTY;
                        }

                        /*
                         * Hotbar -> main inventory.
                         */
                    } else if (index >= HOTBAR_START
                            && index < HOTBAR_END) {

                        if (!moveItemStackTo(
                                stack,
                                PLAYER_INV_START,
                                PLAYER_INV_END,
                                false
                        )) {
                            return ItemStack.EMPTY;
                        }

                    } else {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        /*
         * The client-side dummy container cannot perform the real
         * block-entity validity check.
         *
         * The server-side block entity can.
         */
        if (blockEntity instanceof FluidBrewingStandBlockEntity brewingStand) {
            return brewingStand.stillValid(player);
        }

        return true;
    }

    @Override
    public boolean clickMenuButton(
            Player player,
            int id
    ) {
        return false;
    }
}
