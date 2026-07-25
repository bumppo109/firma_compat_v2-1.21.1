package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;

@EventBusSubscriber
public class SaltWaterSurvivalEvent {

    @SubscribeEvent
    public static void handleSaltWaterBreathing(LivingBreatheEvent event) {
        LivingEntity entity = event.getEntity();

        if(!(entity.getType().is(ModTags.Entities.CAN_SURVIVE_SALT_WATER) || entity instanceof Zombie)) return;

        // Quick check: skip if not in fluid at all
        if (!entity.isInWater() && !entity.isInFluidType(TFCFluids.SALT_WATER.getSource().getFluidType())) {
            return;
        }

        BlockPos eyePos = BlockPos.containing(entity.getEyePosition());
        FluidState eyeFluid = entity.level().getFluidState(eyePos);

        if (!eyeFluid.is(ModTags.Fluids.WATERLOGGING_WATER)) {
            return;
        }

        //Drowned Conversion
        if(entity instanceof Zombie){
            Zombie zombie = (Zombie) entity;

            // Skip if zombie has water breathing or can breathe underwater
            if (zombie.canBreatheUnderwater() || zombie.hasEffect(MobEffects.WATER_BREATHING)) {
                event.setCanBreathe(true);
                event.setRefillAirAmount(4); // fast refill like vanilla out-of-water
                event.setConsumeAirAmount(0);
                return;
            }

            // Normal drowning: consume air, apply damage later via vanilla
            event.setCanBreathe(false);
            event.setConsumeAirAmount(1);      // 1 per tick = 20 per second (vanilla rate)
            event.setRefillAirAmount(0);
        }

        // Default: full immunity in salt water
        event.setCanBreathe(true);
        event.setRefillAirAmount(entity.getMaxAirSupply()); // instant full refill
        event.setConsumeAirAmount(0); // no depletion even if canBreathe=false
    }
}
