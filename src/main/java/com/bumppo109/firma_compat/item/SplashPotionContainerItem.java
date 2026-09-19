package com.bumppo109.firma_compat.item;
import com.bumppo109.firma_compat.entity.FluidPotionThrowing;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

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

        if (FluidHelpers.getContainedFluid(stack).isEmpty()) {
            return super.use(level, player, hand);
        }

        if (!level.isClientSide) {
            FluidPotionThrowing.throwSplash(level, player, stack);
        }

        // Survival → consume the item
        // Creative → leave the stack completely untouched (keeps the fluid)
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}