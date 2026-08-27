package com.example.realisticdining.fabric.events;

import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.common.SnackItemRegistry;
import com.example.realisticdining.events.FriedRiceEggPlaceHandler;
import com.example.realisticdining.fabric.client.pack.PackDefinitionManager;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public class FabricEventHandler {

    public static void register() {
        UseItemCallback.EVENT.register((player, level, hand) -> {
            // 蛋炒饭放置：副手拿蛋炒饭右键时放置（服务端 shrink）
            if (FriedRiceEggPlaceHandler.onPlayerRightClick(player, level, hand)) {
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            // 对准空气右键（仅客户端）
            if (level.isClientSide) {
                ItemStack held = player.getItemInHand(hand);
                if (!held.isEmpty()
                        && com.example.realisticdining.fabric.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) {
                    // 主模组零食/饮料：兜底触发饮用动画
                    if (SnackItemRegistry.isSnackItem(held.getItem())) {
                        com.example.realisticdining.fabric.client.arm.FpArmRenderSystem.triggerDrinkForMainHand();
                    }
                    // 材质包扩展物品：饮用键=右键时 consumeClick 可能因原版 keyUse 冲突检测不到，
                    // 这里通过 UseItemCallback 兜底触发材质包动画
                    if (isPackItem(held)) {
                        com.example.realisticdining.fabric.client.ModKeybinds.triggerDrinkPressed();
                    }
                }
            }
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        });

        // v2.3.0+ 玩家退出世界时清除 eating 状态（防状态卡死兜底）
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerEatingState.reset(handler.getPlayer().getUUID());
        });
    }

    /** 判断物品是否是材质包扩展物品（Pack）。 */
    private static boolean isPackItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && PackDefinitionManager.containsItem(id.toString());
    }
}
