package com.bumppo109.firma_compat.blockentity;

import com.bumppo109.firma_compat.block.FluidBrewingStandBlock;
import com.bumppo109.firma_compat.fluid.CompatFluid;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipe;
import com.bumppo109.firma_compat.recipe.FluidBrewingRecipeInput;
import com.bumppo109.firma_compat.recipe.ModRecipes;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.Optional;

public class FluidBrewingStandBlockEntity
        extends BlockEntity
        implements Container, MenuProvider {

    public static final int BOTTLE_0 = 0;
    public static final int BOTTLE_1 = 1;
    public static final int BOTTLE_2 = 2;
    public static final int INGREDIENT = 3;
    public static final int FUEL = 4;
    private static final int DRAGONS_BREATH_COST = 250;

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

    private boolean isSplashCatalyst(ItemStack stack) {
        return !stack.isEmpty() && stack.is(ModTags.Items.SPLASH_CATALYSTS);
    }

    private boolean isLingeringCatalyst(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        // Explicit lingering catalyst items (optional tag)
        if (stack.is(ModTags.Items.LINGERING_CATALYSTS)) {
            return true;
        }

        // Brewing container holding enough dragons breath fluid
        if (!stack.is(ModTags.Items.BREWING_CONTAINERS)) {
            return false;
        }

        return getDragonsBreathAmount(stack) >= DRAGONS_BREATH_COST;
    }

    private int getDragonsBreathAmount(ItemStack stack) {
        FluidStack fluid = FluidHelpers.getContainedFluid(stack);

        if (fluid.isEmpty()) {
            return 0;
        }

        if (!fluid.is(ModFluids.FLUIDS.get(CompatFluid.DRAGONS_BREATH).getSource())) {
            return 0;
        }

        return fluid.getAmount();
    }

    /**
     * Consumes the catalyst after a successful brew.
     * - Tagged item catalysts → shrink 1
     * - Brewing container with dragons breath → drain 250 mb
     */
    private void consumeCatalyst(ItemStack catalyst) {
        if (catalyst.isEmpty()) {
            return;
        }

        // Dragons-breath fluid in a brewing container
        if (catalyst.is(ModTags.Items.BREWING_CONTAINERS)
                && getDragonsBreathAmount(catalyst) >= DRAGONS_BREATH_COST) {

            IFluidHandlerItem handler =
                    catalyst.getCapability(Capabilities.FluidHandler.ITEM);

            if (handler != null) {
                // TFC FluidContainerHandler.drain mutates the component in place
                handler.drain(DRAGONS_BREATH_COST, IFluidHandler.FluidAction.EXECUTE);
                items.set(INGREDIENT, catalyst); // keep (possibly emptier) container
            }
            return;
        }

        // Normal item catalyst (gunpowder, tagged items, etc.)
        catalyst.shrink(1);
        if (catalyst.isEmpty()) {
            items.set(INGREDIENT, ItemStack.EMPTY);
        }
    }

    private ItemStack convertToSplash(ItemStack container) {
        FluidStack fluid = FluidHelpers.getContainedFluid(container);
        if (fluid.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return PotionContainerVariants.getSplashItem(container)
                .map(item -> fillContainer(new ItemStack(item), fluid))
                .orElse(ItemStack.EMPTY);
    }

    private ItemStack convertToLingering(ItemStack container) {
        FluidStack fluid = FluidHelpers.getContainedFluid(container);
        if (fluid.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return PotionContainerVariants.getLingeringItem(container)
                .map(item -> fillContainer(new ItemStack(item), fluid))
                .orElse(ItemStack.EMPTY);
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
            return false;
        }

        boolean splashCatalyst = isSplashCatalyst(catalyst);
        boolean lingeringCatalyst = isLingeringCatalyst(catalyst);

        for (int i = BOTTLE_0; i <= BOTTLE_2; i++) {
            ItemStack container = items.get(i);
            if (container.isEmpty()) {
                continue;
            }

            if (splashCatalyst
                    && PotionContainerVariants.canMakeSplash(container)
                    && !convertToSplash(container).isEmpty()) {
                return true;
            }

            if (lingeringCatalyst
                    && PotionContainerVariants.canMakeLingering(container)
                    && !convertToLingering(container).isEmpty()) {
                return true;
            }

            // Normal fluid recipes only when catalyst is not a conversion item
            if (!splashCatalyst && !lingeringCatalyst && findRecipe(container).isPresent()) {
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

        ItemStack catalyst = items.get(INGREDIENT);
        boolean splashCatalyst = isSplashCatalyst(catalyst);
        boolean lingeringCatalyst = isLingeringCatalyst(catalyst);

        for (int i = BOTTLE_0; i <= BOTTLE_2; i++) {
            ItemStack container = items.get(i);
            if (container.isEmpty()) {
                continue;
            }

            ItemStack result = ItemStack.EMPTY;

            if (splashCatalyst) {
                result = convertToSplash(container);
            } else if (lingeringCatalyst) {
                result = convertToLingering(container);
            } else {
                Optional<FluidBrewingRecipe> recipe = findRecipe(container);
                if (recipe.isPresent()) {
                    result = recipe.get().assembleContainer(container);
                }
            }

            if (result.isEmpty()) {
                continue;
            }

            items.set(i, result);
            updateBottleState(i);
            brewed = true;
        }

        if (!brewed) {
            return;
        }

        consumeCatalyst(items.get(INGREDIENT));

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
            if (index >= BOTTLE_0
                    && index <= BOTTLE_2) {
                updateBottleState(index);
            }

            setChanged();
        }

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack result =
                net.minecraft.world.ContainerHelper.takeItem(
                        items,
                        index
                );

        if (index >= BOTTLE_0
                && index <= BOTTLE_2) {
            updateBottleState(index);
        }

        return result;
    }

    @Override
    public void setItem(
            int index,
            ItemStack stack
    ) {
        items.set(index, stack);

        if (index >= BOTTLE_0
                && index <= BOTTLE_2) {

            stack.limitSize(1);
            updateBottleState(index);
        }

        setChanged();
    }


    private void updateBottleState(int slot) {
        if (level == null || level.isClientSide) {
            return;
        }

        if (slot < BOTTLE_0 || slot > BOTTLE_2) {
            return;
        }

        boolean hasBottle = !items.get(slot).isEmpty();

        switch (slot) {
            case BOTTLE_0 ->
                    level.setBlock(
                            worldPosition,
                            getBlockState().setValue(
                                    FluidBrewingStandBlock.HAS_BOTTLE_0,
                                    hasBottle
                            ),
                            3
                    );

            case BOTTLE_1 ->
                    level.setBlock(
                            worldPosition,
                            getBlockState().setValue(
                                    FluidBrewingStandBlock.HAS_BOTTLE_1,
                                    hasBottle
                            ),
                            3
                    );

            case BOTTLE_2 ->
                    level.setBlock(
                            worldPosition,
                            getBlockState().setValue(
                                    FluidBrewingStandBlock.HAS_BOTTLE_2,
                                    hasBottle
                            ),
                            3
                    );
        }
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
        if (index >= BOTTLE_0 && index <= BOTTLE_2) {
            return stack.is(ModTags.Items.BREWING_CONTAINERS);
        }

        if (index == INGREDIENT) {
            if (stack.is(ModTags.Items.BREWING_FUEL)) {
                return false;
            }
            if (stack.is(ModTags.Items.BREWING_CONTAINERS)) {
                // Only allowed here as a dragons-breath catalyst
                return getDragonsBreathAmount(stack) >= DRAGONS_BREATH_COST;
            }
            return true; // normal recipe catalysts (magma cream, gunpowder, etc.)
        }

        if (index == FUEL) {
            return stack.is(ModTags.Items.BREWING_FUEL);
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

        if (level != null && !level.isClientSide) {
            level.setBlock(
                    worldPosition,
                    getBlockState()
                            .setValue(
                                    FluidBrewingStandBlock.HAS_BOTTLE_0,
                                    !items.get(BOTTLE_0).isEmpty()
                            )
                            .setValue(
                                    FluidBrewingStandBlock.HAS_BOTTLE_1,
                                    !items.get(BOTTLE_1).isEmpty()
                            )
                            .setValue(
                                    FluidBrewingStandBlock.HAS_BOTTLE_2,
                                    !items.get(BOTTLE_2).isEmpty()
                            ),
                    3
            );
        }

    }

    /**
     * Creates a new container item filled with the same fluid as the source.
     * Returns ItemStack.EMPTY if the target cannot accept the fluid.
     */
    private ItemStack fillContainer(ItemStack emptyContainer, FluidStack fluid) {
        if (emptyContainer.isEmpty() || fluid.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = emptyContainer.copyWithCount(1);

        IFluidHandlerItem handler =
                result.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return ItemStack.EMPTY;
        }

        FluidStack toFill = fluid.copy();
        toFill.setAmount(handler.getTankCapacity(0));

        if (!handler.isFluidValid(0, toFill)) {
            return ItemStack.EMPTY;
        }

        int filled = handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
        if (filled <= 0) {
            return ItemStack.EMPTY;
        }

        return result;
    }
}
