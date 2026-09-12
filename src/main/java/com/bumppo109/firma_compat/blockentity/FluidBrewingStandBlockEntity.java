package com.bumppo109.firma_compat.blockentity;

import com.bumppo109.firma_compat.recipe.FluidBrewingRecipe;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipeInput;
import com.bumppo109.firma_compat.recipe.ModRecipes;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class FluidBrewingStandBlockEntity
        extends BlockEntity
        implements Container, MenuProvider {

    public static final int BOTTLE_0 = 0;
    public static final int BOTTLE_1 = 1;
    public static final int BOTTLE_2 = 2;
    public static final int INGREDIENT = 3;
    public static final int FUEL = 4;

    /**
     * Number of ticks required to complete one brewing operation.
     */
    public static final int BREW_TIME = 400;

    /**
     * Maximum amount of internal fuel.
     */
    public static final int MAX_FUEL = 20;

    /**
     * Internal fuel provided by one fuel item.
     */
    public static final int FUEL_PER_ITEM = 20;

    private final NonNullList<ItemStack> items =
            NonNullList.withSize(5, ItemStack.EMPTY);

    private int brewTime;
    private int fuel;

    public final ContainerData data = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> brewTime;
                case 1 -> fuel;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> brewTime = value;
                case 1 -> fuel = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public FluidBrewingStandBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                ModBlockEntities.FLUID_BREWING_STAND.get(),
                pos,
                state
        );
    }

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            FluidBrewingStandBlockEntity entity
    ) {
        /*
         * ------------------------------------------------------------
         * CURRENTLY BREWING
         * ------------------------------------------------------------
         */
        if (entity.brewTime > 0) {

            /*
             * Cancel the brewing operation if there is no longer
             * anything valid to brew.
             */
            if (!entity.canBrew()) {
                entity.brewTime = 0;
                entity.setChanged();
                return;
            }

            entity.brewTime--;

            if (entity.brewTime == 0) {
                entity.doBrew();
                entity.setChanged();
            }

            return;
        }

        /*
         * ------------------------------------------------------------
         * NOT CURRENTLY BREWING
         * ------------------------------------------------------------
         */

        /*
         * Do not consume fuel unless a valid recipe is present.
         */
        if (!entity.canBrew()) {
            return;
        }

        /*
         * Refill internal fuel if necessary.
         */
        if (entity.fuel <= 0) {
            if (!entity.consumeFuelItem()) {
                return;
            }

            entity.setChanged();
        }

        /*
         * Consume one unit of internal fuel for this operation.
         */
        entity.fuel--;
        entity.brewTime = BREW_TIME;

        entity.setChanged();
    }

    /**
     * Returns true if at least one of the three container slots can
     * currently be transformed using the catalyst.
     */
    private boolean canBrew() {
        if (level == null) {
            return false;
        }

        ItemStack catalyst = items.get(INGREDIENT);

        if (catalyst.isEmpty()) {
            System.out.println("[FirmaCompat] No catalyst");
            return false;
        }

        System.out.println(
                "[FirmaCompat] Catalyst: " +
                        BuiltInRegistries.ITEM.getKey(catalyst.getItem())
        );

        for (int i = BOTTLE_0; i <= BOTTLE_2; i++) {
            ItemStack container = items.get(i);

            if (container.isEmpty()) {
                continue;
            }

            System.out.println(
                    "[FirmaCompat] Checking container slot " + i +
                            ": " +
                            BuiltInRegistries.ITEM.getKey(container.getItem())
            );

            Optional<FluidBrewingRecipe> recipe = findRecipe(container);

            System.out.println(
                    "[FirmaCompat] Recipe found: " + recipe.isPresent()
            );

            if (recipe.isPresent()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Finds the recipe matching one specific container and the
     * currently inserted catalyst.
     */
    private Optional<FluidBrewingRecipe> findRecipe(
            ItemStack container
    ) {
        if (level == null) {
            return Optional.empty();
        }

        ItemStack catalyst = items.get(INGREDIENT);

        if (catalyst.isEmpty()) {
            return Optional.empty();
        }

        FluidBrewingRecipeInput input =
                new FluidBrewingRecipeInput(
                        container,
                        catalyst
                );

        return level.getRecipeManager()
                .getRecipeFor(
                        ModRecipes.FLUID_BREWING.get(),
                        input,
                        level
                )
                .map(holder -> holder.value());
    }

    /**
     * Consumes one fuel item and converts it into internal fuel.
     */
    private boolean consumeFuelItem() {
        ItemStack fuelStack = items.get(FUEL);

        if (fuelStack.isEmpty()) {
            return false;
        }

        if (!fuelStack.is(ModTags.Items.BREWING_FUEL)) {
            return false;
        }

        if (fuel >= MAX_FUEL) {
            return false;
        }

        fuelStack.shrink(1);

        if (fuelStack.isEmpty()) {
            items.set(
                    FUEL,
                    ItemStack.EMPTY
            );
        }

        fuel = Math.min(
                fuel + FUEL_PER_ITEM,
                MAX_FUEL
        );

        return true;
    }

    /**
     * Applies the catalyst to every container that currently matches
     * a fluid brewing recipe.
     *
     * The catalyst is consumed exactly once if at least one container
     * was successfully transformed.
     */
    private void doBrew() {
        boolean brewed = false;

        for (int i = BOTTLE_0; i <= BOTTLE_2; i++) {
            ItemStack container = items.get(i);

            if (container.isEmpty()) {
                continue;
            }

            Optional<FluidBrewingRecipe> recipe =
                    findRecipe(container);

            if (recipe.isEmpty()) {
                continue;
            }

            ItemStack result =
                    recipe.get().assembleContainer(container);

            if (result.isEmpty()) {
                continue;
            }

            /*
             * Replace the container with the same container item
             * containing the transformed fluid.
             */
            items.set(i, result);

            brewed = true;
        }

        /*
         * One catalyst powers the entire brewing operation.
         *
         * This matches the vanilla brewing-stand style behavior where
         * one ingredient can affect all three bottles.
         */
        if (!brewed) {
            return;
        }

        ItemStack ingredient = items.get(INGREDIENT);

        if (!ingredient.isEmpty()) {
            ingredient.shrink(1);

            if (ingredient.isEmpty()) {
                items.set(
                        INGREDIENT,
                        ItemStack.EMPTY
                );
            }
        }

        if (level != null) {
            level.playSound(
                    null,
                    worldPosition,
                    net.minecraft.sounds.SoundEvents.BREWING_STAND_BREW,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );
        }
    }

    public int getFuel() {
        return fuel;
    }

    public int getBrewingTicks() {
        return brewTime;
    }

    /*
     * ------------------------------------------------------------------
     * MenuProvider
     * ------------------------------------------------------------------
     */

    @Override
    public Component getDisplayName() {
        return Component.translatable(
                "container.firma_compat.fluid_brewing_stand"
        );
    }

    @Override
    public AbstractContainerMenu createMenu(
            int containerId,
            Inventory inventory,
            Player player
    ) {
        return new FluidBrewingStandMenu(
                containerId,
                inventory,
                this
        );
    }

    /*
     * ------------------------------------------------------------------
     * Container
     * ------------------------------------------------------------------
     */

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(
            int index,
            int count
    ) {
        ItemStack result =
                net.minecraft.world.ContainerHelper.removeItem(
                        items,
                        index,
                        count
                );

        if (!result.isEmpty()) {
            setChanged();
        }

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return net.minecraft.world.ContainerHelper.takeItem(
                items,
                index
        );
    }

    @Override
    public void setItem(
            int index,
            ItemStack stack
    ) {
        items.set(index, stack);

        /*
         * TFC fluid containers must remain single-item stacks while
         * containing fluid.
         */
        if (index >= BOTTLE_0
                && index <= BOTTLE_2) {

            stack.limitSize(1);
        }

        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(
                this,
                player
        );
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    public boolean canPlaceItem(
            int index,
            ItemStack stack
    ) {
        if (index >= BOTTLE_0
                && index <= BOTTLE_2) {

            return stack.is(
                    ModTags.Items.BREWING_CONTAINERS
            );
        }

        if (index == INGREDIENT) {
            return true;
        }

        if (index == FUEL) {
            return stack.is(
                    ModTags.Items.BREWING_FUEL
            );
        }

        return false;
    }

    /*
     * ------------------------------------------------------------------
     * Persistence
     * ------------------------------------------------------------------
     */

    @Override
    protected void saveAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {
        super.saveAdditional(
                tag,
                registries
        );

        net.minecraft.world.ContainerHelper.saveAllItems(
                tag,
                items,
                registries
        );

        tag.putInt(
                "BrewTime",
                brewTime
        );

        tag.putInt(
                "Fuel",
                fuel
        );
    }

    @Override
    protected void loadAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {
        super.loadAdditional(
                tag,
                registries
        );

        net.minecraft.world.ContainerHelper.loadAllItems(
                tag,
                items,
                registries
        );

        brewTime = tag.getInt("BrewTime");

        fuel = Math.min(
                tag.getInt("Fuel"),
                MAX_FUEL
        );
    }
}
