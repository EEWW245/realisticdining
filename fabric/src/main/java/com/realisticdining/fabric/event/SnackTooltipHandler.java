package com.realisticdining.fabric.event;

import com.realisticdining.common.SnackItemRegistry;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric 端：零食/饮料物品 tooltip 附加「右手右键可放置模型」灰字提示。
 *
 * <p>ItemTooltipCallback 是客户端事件，必须在 ClientModInitializer 中注册。
 */
public class SnackTooltipHandler {

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.isEmpty()) return;
            if (!SnackItemRegistry.isSnackItem(stack.getItem())) return;
            lines.add(Component.translatable("tooltip.realisticdining.snack_place_hint")
                    .withStyle(ChatFormatting.GRAY));
            if (SnackItemRegistry.isCureBeverage(stack.getItem())) {
                lines.add(Component.translatable("tooltip.realisticdining.cure_negative_buff")
                        .withStyle(ChatFormatting.GRAY));
            }
            if (SnackItemRegistry.isBeer(stack.getItem())) {
                lines.add(Component.translatable("tooltip.realisticdining.beer_drink_hint")
                        .withStyle(ChatFormatting.GRAY));
            }
        });
    }
}
