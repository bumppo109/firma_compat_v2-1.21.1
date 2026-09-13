package com.bumppo109.firma_compat.world.chunkData;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;

public record ClimateSyncPacket(ChunkPos pos, ClimateData data) implements CustomPacketPayload {

    public static final Type<ClimateSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "climate_sync"));

    public static final StreamCodec<FriendlyByteBuf, ClimateSyncPacket> STREAM_CODEC =
            StreamCodec.of(
                    ClimateSyncPacket::encode,
                    ClimateSyncPacket::decode
            );

    private static void encode(FriendlyByteBuf buf, ClimateSyncPacket packet) {
        buf.writeChunkPos(packet.pos);

        buf.writeFloat(packet.data.temperature);
        buf.writeFloat(packet.data.vegetation);
        buf.writeFloat(packet.data.continentalness);
        buf.writeFloat(packet.data.erosion);
        buf.writeFloat(packet.data.weirdness);
    }

    private static ClimateSyncPacket decode(FriendlyByteBuf buf) {
        ChunkPos pos = buf.readChunkPos();

        ClimateData data = new ClimateData(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );

        return new ClimateSyncPacket(pos, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ClimateSyncPacket packet) {
        Minecraft.getInstance().execute(() -> {
            ClientClimateCache.put(packet.pos(), packet.data());
        });
    }
}