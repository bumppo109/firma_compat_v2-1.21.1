package com.bumppo109.firma_compat.event;


import com.bumppo109.firma_compat.item.FirmaLampItem;
import net.dries007.tfc.common.blockentities.LampBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


@EventBusSubscriber
public class FirmaLampItemLightEvent {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {

        ItemStack stack = event.getItemStack();

        var player = event.getEntity();
        var level = event.getLevel();

        //exit event if targeted block is a LampBlockEntity
        if (level.getBlockEntity(event.getPos()) instanceof LampBlockEntity) return;



        if(!(stack.getItem() instanceof FirmaLampItem lamp)) return;

        if(player.isShiftKeyDown()) {

            if(lamp.isLit(stack)) {

                lamp.setLit(stack,false);

                level.playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.FIRE_EXTINGUISH,
                        player.getSoundSource()
                );

                event.setCancellationResult(
                        InteractionResult.SUCCESS
                );

                event.setCanceled(true);
            }

            return;
        }


        if(lamp.isLit(stack))
            return;


        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();


        boolean igniter =
                main.is(Tags.Items.TOOLS_IGNITER)
                        ||
                        off.is(Tags.Items.TOOLS_IGNITER);


        if(!igniter || !lamp.hasFuel(stack))
            return;



        lamp.setLit(stack,true);


        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.FLINTANDSTEEL_USE,
                player.getSoundSource()
        );


        if(player instanceof ServerPlayer serverPlayer) {

            ItemStack used =
                    main.is(Tags.Items.TOOLS_IGNITER)
                            ? main
                            : off;


            if(used.isDamageableItem()) {

                used.hurtAndBreak(
                        1,
                        serverPlayer,
                        player.getEquipmentSlotForItem(used)
                );
            }
        }


        event.setCancellationResult(
                InteractionResult.SUCCESS
        );

        event.setCanceled(true);
    }
}