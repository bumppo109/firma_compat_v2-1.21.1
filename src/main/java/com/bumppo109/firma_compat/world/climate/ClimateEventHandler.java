package com.bumppo109.firma_compat.world.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.ModCompat;
import com.bumppo109.firma_compat.addon.eclipticseasons.EclipticSeasonsClimateModel;
import com.bumppo109.firma_compat.world.chunkData.ClimateData;
import com.bumppo109.firma_compat.world.chunkData.ClimateSyncPacket;
import com.bumppo109.firma_compat.world.chunkData.ServerClimateCache;
import com.bumppo109.firma_compat.world.chunkData.VanillaNoiseSampler;
import net.dries007.tfc.util.climate.BiomeBasedClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = FirmaCompat.MODID)
public class ClimateEventHandler {

    private static final int RADIUS = 8;

    /*
     * Tracks which chunks have already been synced to which players.
     *
     * key format:
     * playerUUID:chunkX:chunkZ
     */
    private static final Set<String> SENT = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onSelectClimateModel(SelectClimateModelEvent event) {

        if (event.getModel() instanceof BiomeBasedClimateModel) {

            if(ModCompat.loaded("eclipticseasons")){
                event.setModel(EclipticSeasonsClimateModel.INSTANCE);
                FirmaCompat.LOGGER.debug("Applied Ecliptic Seasons model for {}", event.level() != null ? event.level().dimension().location() : "unknown");
            } else {
                event.setModel(VanillaClimateModel.INSTANCE);
                FirmaCompat.LOGGER.debug("Applied Vanilla Firma Compat model for {}", event.level() != null ? event.level().dimension().location() : "unknown");
            }
        }
    }

    /**
     * Sample and cache climate data when chunks load server-side.
     */
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {

        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (!(event.getChunk() instanceof LevelChunk chunk)) {
            return;
        }

        ChunkPos pos = chunk.getPos();

        int x = pos.getMiddleBlockX();
        int z = pos.getMiddleBlockZ();

        ClimateData data = VanillaNoiseSampler.sample(level, x, z);

        if (data == null) {
            return;
        }

        ServerClimateCache.put(level, pos, data);

        PacketDistributor.sendToPlayersTrackingChunk(
                level,
                pos,
                new ClimateSyncPacket(pos, data)
        );
    }

    /**
     * Send nearby cached climate data to players when they join.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        ChunkPos center = new ChunkPos(player.blockPosition());

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {

                ChunkPos pos = new ChunkPos(
                        center.x + dx,
                        center.z + dz
                );

                ClimateData data = ServerClimateCache.get(level, pos);

                if (data == null) {
                    continue;
                }

                PacketDistributor.sendToPlayer(
                        player,
                        new ClimateSyncPacket(pos, data)
                );

                SENT.add(makeKey(player, pos));
            }
        }
    }

    /**
     * Incrementally sync newly encountered chunks.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        ChunkPos center = new ChunkPos(player.blockPosition());

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {

                ChunkPos pos = new ChunkPos(
                        center.x + dx,
                        center.z + dz
                );

                String key = makeKey(player, pos);

                if (SENT.contains(key)) {
                    continue;
                }

                ClimateData data = ServerClimateCache.get(level, pos);

                if (data == null) {
                    continue;
                }

                PacketDistributor.sendToPlayer(
                        player,
                        new ClimateSyncPacket(pos, data)
                );

                SENT.add(key);
            }
        }
    }

    /**
     * Prevent memory leaks when players leave.
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        String prefix = player.getUUID().toString() + ":";

        SENT.removeIf(key -> key.startsWith(prefix));
    }

    private static String makeKey(ServerPlayer player, ChunkPos pos) {
        return player.getUUID() + ":" + pos.x + ":" + pos.z;
    }
}