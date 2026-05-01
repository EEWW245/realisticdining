package com.example.realisticdining.forge.events;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.events.FriedRiceEggPlaceHandler;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RealisticDining.MOD_ID)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (FriedRiceEggPlaceHandler.onPlayerRightClick(event.getEntity(), event.getLevel(), event.getHand())) {
            event.setCanceled(true);
        }
    }
}
