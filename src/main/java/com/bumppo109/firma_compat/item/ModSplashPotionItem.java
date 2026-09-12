package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.entity.FluidPotionProjectile;
import com.bumppo109.firma_compat.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModSplashPotionItem extends FluidPotionItem {

    public ModSplashPotionItem(Properties properties) {
        super(properties);
    }

    @Override
    protected EntityType<FluidPotionProjectile> projectileType() {
        return ModEntities.FLUID_SPLASH_POTION.get();
    }

    @Override
    protected void playThrowSound(
            ServerLevel level,
            Player player
    ) {
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SPLASH_POTION_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (
                        level.getRandom().nextFloat() * 0.4F
                                + 0.8F
                )
        );
    }
}
