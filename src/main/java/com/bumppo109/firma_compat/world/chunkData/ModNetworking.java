package com.bumppo109.firma_compat.world.chunkData;

import com.bumppo109.firma_compat.FirmaCompat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = FirmaCompat.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar =
                event.registrar(PROTOCOL_VERSION);

        registrar.playToClient(
                ClimateSyncPacket.TYPE,
                ClimateSyncPacket.STREAM_CODEC,
                (packet, context) -> ClimateSyncPacket.handle(packet)
        );
    }
}