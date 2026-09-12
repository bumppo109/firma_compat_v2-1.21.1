package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.entity.FluidPotionProjectile;
import com.bumppo109.firma_compat.fluid.FluidPotionHelper;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public abstract class FluidPotionItem
        extends FluidContainerItem
        implements ProjectileItem {

    protected FluidPotionItem(Properties properties) {
        super(
                properties,
                () -> 250,
                ModTags.Fluids.POTIONS,
                false,
                () -> false
        );
    }

    protected abstract EntityType<FluidPotionProjectile>
    projectileType();

    protected abstract void playThrowSound(
            ServerLevel level,
            Player player
    );

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (FluidPotionHelper.getPotion(stack).isEmpty()) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;

            FluidPotionProjectile projectile =
                    new FluidPotionProjectile(
                            projectileType(),
                            serverLevel
                    );

            projectile.setOwner(player);

            projectile.setItem(
                    stack.copyWithCount(1)
            );

            /*
             * Explicitly put the projectile in front of
             * the player's eyes.
             */
            projectile.setPos(
                    player.getX(),
                    player.getEyeY() - 0.1D,
                    player.getZ()
            );

            /*
             * Explicitly give it velocity.
             *
             * Do NOT rely on the constructor's rotation here.
             */
            projectile.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    -20.0F,
                    0.5F,
                    1.0F
            );

            /*
             * Debug the actual velocity.
             */
            System.out.println(
                    "[FirmaCompat] THROW: " +
                            "pos=" + projectile.position() +
                            " delta=" + projectile.getDeltaMovement()
            );

            boolean added = serverLevel.addFreshEntity(projectile);

            System.out.println(
                    "[FirmaCompat] PROJECTILE ADDED: " +
                            added +
                            " removed=" +
                            projectile.isRemoved()
            );

            if (added) {
                playThrowSound(serverLevel, player);
                stack.consume(1, player);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResultHolder.sidedSuccess(
                stack,
                level.isClientSide()
        );
    }


    @Override
    public Projectile asProjectile(
            Level level,
            Position position,
            ItemStack stack,
            Direction direction
    ) {
        FluidPotionProjectile projectile =
                new FluidPotionProjectile(
                        projectileType(),
                        level,
                        position.x(),
                        position.y(),
                        position.z()
                );

        projectile.setItem(
                stack.copyWithCount(1)
        );

        return projectile;
    }

    @Override
    public ProjectileItem.DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder()
                .uncertainty(
                        ProjectileItem.DispenseConfig.DEFAULT
                                .uncertainty() * 0.5F
                )
                .power(
                        ProjectileItem.DispenseConfig.DEFAULT
                                .power() * 1.25F
                )
                .build();
    }
}
