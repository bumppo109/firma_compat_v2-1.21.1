package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.entity.FluidPotionThrowing;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

public class SplashPotionContainerItem extends FluidContainerItem {

    public SplashPotionContainerItem(
            Item.Properties properties,
            Supplier<Integer> capacity,
            TagKey<Fluid> whitelist
    ) {
        super(
                properties,
                capacity,
                whitelist,
                false,
                () -> false
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (!net.dries007.tfc.common.fluids.FluidHelpers
                .getContainedFluid(stack)
                .isEmpty()) {

            if (!level.isClientSide) {
                FluidPotionThrowing.throwSplash(level, player, stack);

                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, new ItemStack(this));
                }
            }

            return InteractionResultHolder.sidedSuccess(
                    stack,
                    level.isClientSide
            );
        }

        return super.use(level, player, hand);
    }

}
