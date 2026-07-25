package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber
public class DenyTaggedEntitySpawnEvent {

    //Most spawns should be covered by biome modifiers but this catches cows as part of the village structure

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Mob mob)) {
            return;
        }

        // This is the correct check
        if (event.loadedFromDisk()) {
            return;  // ← entity was loaded from disk (chunk load, dimension travel, etc.)
        }

        // Only new/natural/spawner/etc. spawns reach this point
        if (mob.getType().is(ModTags.Entities.REMOVE_VANILLA_MOBS)) {
            event.setCanceled(true);

            // Optional visual feedback
            /*
            if (event.getLevel() instanceof ServerLevel server) {
                server.sendParticles(
                        ParticleTypes.SMOKE,
                        mob.getX(), mob.getY() + mob.getBbHeight() / 2.0, mob.getZ(),
                        8, 0.3, 0.3, 0.3, 0.02
                );
            }

             */
        }
    }
}
