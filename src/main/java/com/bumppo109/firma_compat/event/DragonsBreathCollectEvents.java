package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.fluid.CompatFluid;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

@EventBusSubscriber
public final class DragonsBreathCollectEvents {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.is(ModTags.Items.BREWING_CONTAINERS)) {
            return;
        }

        AreaEffectCloud cloud = findDragonBreathCloud(level, player);
        if (cloud == null) {
            return;
        }

        ItemStack filled = fillWithDragonsBreath(stack);
        if (filled.isEmpty()) {
            return;
        }

        if (!level.isClientSide) {
            // Consume / replace held item
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (stack.isEmpty()) {
                player.setItemInHand(hand, filled);
            } else if (!player.getAbilities().instabuild) {
                if (!player.getInventory().add(filled)) {
                    player.drop(filled, false);
                }
            } else {
                // Creative: give a filled copy if they don't already have this stack behaviour
                if (!player.getInventory().contains(filled)) {
                    player.getInventory().add(filled);
                }
            }

            // Shrink the cloud like vanilla
            float newRadius = cloud.getRadius() - 0.5F;
            if (newRadius <= 0.5F) {
                cloud.discard();
            } else {
                cloud.setRadius(newRadius);
            }

            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    net.minecraft.sounds.SoundEvents.BOTTLE_FILL_DRAGONBREATH,
                    net.minecraft.sounds.SoundSource.NEUTRAL,
                    1.0F,
                    1.0F
            );
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        event.setCanceled(true);
    }

    /**
     * Finds a living dragon-breath area effect cloud near the player.
     */
    private static AreaEffectCloud findDragonBreathCloud(Level level, Player player) {
        AABB searchBox = player.getBoundingBox().inflate(2.0D);

        List<AreaEffectCloud> clouds = level.getEntitiesOfClass(
                AreaEffectCloud.class,
                searchBox,
                cloud -> cloud.isAlive()
                        && cloud.getRadius() > 0.5F
                        && isDragonBreathCloud(cloud)
        );

        if (clouds.isEmpty()) {
            return null;
        }

        // Prefer the closest cloud
        AreaEffectCloud closest = null;
        double closestDist = Double.MAX_VALUE;

        for (AreaEffectCloud cloud : clouds) {
            double dist = cloud.distanceToSqr(player);
            if (dist < closestDist) {
                closestDist = dist;
                closest = cloud;
            }
        }

        return closest;
    }

    /**
     * Vanilla dragon breath clouds are owned by the Ender Dragon.
     * Adjust if your game version stores potion data differently.
     */
    private static boolean isDragonBreathCloud(AreaEffectCloud cloud) {
        //return cloud.getOwner() instanceof EnderDragon;
        return true;
    }

    private static ItemStack fillWithDragonsBreath(ItemStack container) {
        ItemStack result = container.copyWithCount(1);

        IFluidHandlerItem handler =
                result.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return ItemStack.EMPTY;
        }

        FluidStack dragonsBreath = new FluidStack(
                ModFluids.FLUIDS.get(CompatFluid.DRAGONS_BREATH).getSource(),
                250
        );

        // Don't put Dragon's Breath into a container containing another fluid.
        FluidStack existing = handler.getFluidInTank(0);
        if (!existing.isEmpty()
                && !existing.is(dragonsBreath.getFluid())) {
            return ItemStack.EMPTY;
        }

        if (!handler.isFluidValid(0, dragonsBreath)) {
            return ItemStack.EMPTY;
        }

        int filled = handler.fill(
                dragonsBreath,
                IFluidHandler.FluidAction.EXECUTE
        );

        if (filled <= 0) {
            return ItemStack.EMPTY;
        }

        return result;
    }

}