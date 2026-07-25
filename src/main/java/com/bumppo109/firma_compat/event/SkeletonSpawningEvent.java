package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompatConfig;
import net.dries007.tfc.util.EnvironmentHelpers;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;


@EventBusSubscriber
public class SkeletonSpawningEvent
{

    @SubscribeEvent
    public static void replaceSkeletonWithVariant(
            EntityJoinLevelEvent event)
    {
        if (!(event.getLevel() instanceof ServerLevel level))
        {
            return;
        }


        if (event.loadedFromDisk())
        {
            return;
        }


        if (!(event.getEntity() instanceof Skeleton skeleton))
        {
            return;
        }


        double chance =
                FirmaCompatConfig.COMMON
                        .skeletonVariantChance
                        .get();


        if (level.random.nextDouble() >= chance)
        {
            return;
        }


        BlockPos pos =
                skeleton.blockPosition();


        ChunkData data =
                ChunkData.get(level, pos);


        if (data == null)
        {
            return;
        }


        AbstractSkeleton replacement = null;


        if (isBoggedClimateValid(level, pos))
        {
            replacement =
                    EntityType.BOGGED.create(level);
        }
        else if (isStrayClimateValid(data, pos))
        {
            replacement =
                    EntityType.STRAY.create(level);
        }


        if (replacement == null)
        {
            return;
        }


        event.setCanceled(true);


        replacement.moveTo(
                skeleton.getX(),
                skeleton.getY(),
                skeleton.getZ(),
                skeleton.getYRot(),
                skeleton.getXRot()
        );


        replacement.finalizeSpawn(
                level,
                level.getCurrentDifficultyAt(pos),
                MobSpawnType.NATURAL,
                null
        );


        level.addFreshEntity(replacement);
    }


    private static boolean isBoggedClimateValid(
            ServerLevel level,
            BlockPos pos)
    {
        var biome =
                level.getBiome(pos);


        return biome.is(
                ResourceLocation.fromNamespaceAndPath(
                        "tfc",
                        "lowlands"
                )
        )
                ||
                biome.is(
                        ResourceLocation.fromNamespaceAndPath(
                                "tfc",
                                "salt_marsh"
                        )
                );
    }


    private static boolean isStrayClimateValid(
            ChunkData data,
            BlockPos pos)
    {
        float temp =
                EnvironmentHelpers.adjustAvgTempForElev(
                        pos.getY(),
                        data.getAverageSeaLevelTemp(pos)
                );


        return temp < 0.0f;
    }
}