package com.bumppo109.firma_compat.entity;

import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.fluid.Potion;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.data.Drinkable;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public abstract class AbstractThrownFluidPotion
        extends ThrowableItemProjectile {

    protected AbstractThrownFluidPotion(
            EntityType<? extends AbstractThrownFluidPotion> type,
            Level level
    ) {
        super(type, level);
    }

    protected AbstractThrownFluidPotion(
            EntityType<? extends AbstractThrownFluidPotion> type,
            Level level,
            LivingEntity owner,
            ItemStack stack
    ) {
        super(type, level);

        setOwner(owner);
        setItem(stack);
    }

    /**
     * The projectile has no dedicated item of its own.
     *
     * This is only the fallback/default required by
     * ThrowableItemProjectile for entity construction/loading.
     *
     * The actual projectile appearance is supplied by setItem()
     * with the filled TFC container.
     */
    @Override
    protected abstract Item getDefaultItem();

    protected FluidStack getFluidStack() {
        return FluidHelpers.getContainedFluid(getItem());
    }

    protected List<MobEffectInstance> getEffects() {
        FluidStack fluidStack = getFluidStack();

        if (fluidStack.isEmpty()) {
            return List.of();
        }

        Drinkable drinkable = Drinkable.get(fluidStack.getFluid());

        if (drinkable == null || drinkable.effects().isEmpty()) {
            return List.of();
        }

        return drinkable.effects().stream()
                .map(effect -> new MobEffectInstance(
                        effect.type(),
                        effect.duration(),
                        effect.amplifier(),
                        false,
                        true,
                        true
                ))
                .toList();
    }


    protected int getPotionColor() {
        return PotionContents.getColor(getEffects());
    }

    protected boolean hasInstantEffect() {
        return getEffects().stream()
                .anyMatch(effect ->
                        effect.getEffect()
                                .value()
                                .isInstantenous()
                );
    }

    /**
     * Applies one effect using vanilla splash-potion behavior.
     */
    protected void applyEffect(
            LivingEntity target,
            MobEffectInstance original,
            double intensity
    ) {
        Holder<MobEffect> effect = original.getEffect();

        if (effect.value().isInstantenous()) {
            effect.value().applyInstantenousEffect(
                    this,
                    getOwner(),
                    target,
                    original.getAmplifier(),
                    intensity
            );
            return;
        }

        int duration = (int) (
                original.getDuration() * intensity + 0.5D
        );

        if (duration < 20) {
            return;
        }

        MobEffectInstance instance = new MobEffectInstance(
                effect,
                duration,
                original.getAmplifier(),
                original.isAmbient(),
                original.isVisible(),
                original.showIcon()
        );

        target.addEffect(instance, getEffectSource());
    }
}
