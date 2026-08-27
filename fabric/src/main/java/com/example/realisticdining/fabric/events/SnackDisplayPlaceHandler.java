package com.example.realisticdining.fabric.events;

import com.example.realisticdining.common.SnackItemRegistry;
import com.example.realisticdining.events.SnackDisplayPlacement;
import com.example.realisticdining.fabric.client.pack.PackDefinitionManager;
import com.example.realisticdining.init.ModBlocks;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric 端：手持零食/饮料右键地面自动创建展示台。
 *
 * <p>UseBlockCallback 在原版方块交互之前触发，返回 SUCCESS 跳过原版处理。
 * 右键已有展示台时返回 PASS，走 {@link com.example.realisticdining.blocks.SnackDisplayBlock#use}。
 * 放置失败（PASS）时，客户端侧兜底触发饮用动画。
 */
public class SnackDisplayPlaceHandler {

    public static void register() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            InteractionResult result = SnackDisplayPlacement.tryPlace(player, level, hand,
                    hitResult.getBlockPos(), hitResult.getDirection());

            // 放置失败（PASS）：若主手持零食/饮料且目标不是展示台本身，则兜底触发饮用动画（仅客户端）
            if (result == InteractionResult.PASS && level.isClientSide) {
                ItemStack held = player.getItemInHand(hand);
                boolean isSnack = !held.isEmpty() && SnackItemRegistry.isSnackItem(held.getItem());
                boolean notDisplay = level.getBlockState(hitResult.getBlockPos()).getBlock() != ModBlocks.SNACK_DISPLAY.get();
                if (notDisplay
                        && com.example.realisticdining.fabric.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) {
                    // 主模组零食/饮料：兜底触发饮用动画
                    if (isSnack) {
                        com.example.realisticdining.fabric.client.arm.FpArmRenderSystem.triggerDrinkForMainHand();
                    }
                    // 材质包扩展物品：饮用键=右键时兜底触发材质包动画
                    if (isPackItem(held)) {
                        com.example.realisticdining.fabric.client.ModKeybinds.triggerDrinkPressed();
                    }
                }
            }
            return result;
        });
    }

    /** 判断物品是否是材质包扩展物品（Pack）。 */
    private static boolean isPackItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && PackDefinitionManager.containsItem(id.toString());
    }
}
