package com.bumppo109.firma_compat.entity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
        modid = "firma_compat",
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(
            EntityRenderersEvent.RegisterRenderers event
    ) {
        event.registerEntityRenderer(
                ModEntities.FLUID_SPLASH_POTION.get(),
                ThrownItemRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.FLUID_LINGERING_POTION.get(),
                ThrownItemRenderer::new
        );
    }

    private ModEntityRenderers() {
    }
}
