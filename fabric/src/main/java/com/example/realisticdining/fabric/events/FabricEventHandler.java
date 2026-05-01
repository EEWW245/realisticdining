package com.example.realisticdining.fabric.events;

import com.example.realisticdining.events.FriedRiceEggPlaceHandler;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public class FabricEventHandler {

    public static void register() {
        UseItemCallback.EVENT.register((player, level, hand) -> {
            if (FriedRiceEggPlaceHandler.onPlayerRightClick(player, level, hand)) {
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        });
    }
}
