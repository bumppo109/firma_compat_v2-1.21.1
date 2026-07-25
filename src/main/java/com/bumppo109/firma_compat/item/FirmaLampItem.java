package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.data.ModDataComponents;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.common.component.fluid.FluidContainerInfo;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.LampBlockItem;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.data.LampFuel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FirmaLampItem extends LampBlockItem {

    public FirmaLampItem(final Block block, Properties properties) {
        super(block, properties);
        FluidContainerInfo containerInfo = new FluidContainerInfo() {
            public boolean canContainFluid(Fluid input) {
                return LampFuel.get(input, block.defaultBlockState()) != null;
            }

            public int fluidCapacity() {
                return (Integer) TFCConfig.SERVER.lampCapacity.get();
            }
        };
    }

    public boolean isLit(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.LIT.get(), false);
    }

    public void setLit(ItemStack stack, boolean lit) {
        stack.set(ModDataComponents.LIT.get(), lit);
    }

    public boolean hasFuel(ItemStack stack) {
        FluidStack fluid = FluidHelpers.getContainedFluid(stack);
        if (fluid.isEmpty()) {
            return false;
        }

        LampFuel fuel = LampFuel.get(
                fluid.getFluid(),
                ((LampBlock) getBlock()).defaultBlockState()
        );

        return fuel != null && fluid.getAmount() > 0;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(
            BlockPos pos,
            Level level,
            @Nullable Player player,
            ItemStack stack,
            BlockState state
    ) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        if (!level.isClientSide) {
            boolean lit = isLit(stack);

            level.getServer().tell(
                    new net.minecraft.server.TickTask(
                            level.getServer().getTickCount() + 1,
                            () -> {
                                BlockState current = level.getBlockState(pos);
                                if (current.hasProperty(LampBlock.LIT)) {
                                    level.setBlock(
                                            pos,
                                            current.setValue(LampBlock.LIT, lit),
                                            11
                                    );

                                    FirmaCompat.LOGGER.debug("updateCustomBlockEntityTag set LIT={}", lit);

                                    FirmaCompat.LOGGER.debug(
                                            "Placed lamp: side={}, state={}, entity={}",
                                            level.isClientSide ? "CLIENT" : "SERVER",
                                            level.getBlockState(pos).getValue(LampBlock.LIT),
                                            level.getBlockEntity(pos)
                                    );
                                }
                            }
                    )
            );
        }

        return result;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide) return;

        if (isLit(stack) && !hasFuel(stack)) {
            setLit(stack, false);           // Auto extinguish
            return;
        }

        if(isLit(stack)) {
            IFluidHandler handler = (IFluidHandler)stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler == null) return;

            FluidStack fluid = FluidHelpers.getContainedFluid(stack);
            if (fluid.isEmpty()) return;

            //Determine fuel rules from TFC
            LampFuel fuel = LampFuel.get(
                    fluid.getFluid(),
                    ((LampBlock) getBlock()).defaultBlockState()
            );

            if (fuel == null) return;

            int burnRate = fuel.burnRate();
            if (burnRate <= 0) return;

            long time = level.getGameTime();

            if (time % burnRate == 0) {
                FirmaCompat.LOGGER.debug("FirmaLampItem - burning fuel");
                handler.drain(1, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(
            ItemStack oldStack,
            ItemStack newStack,
            boolean slotChanged
    ) {
        return false;
    }
}
