package com.realisticdining.neoforge.client;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.SnackItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * NeoForge 端：零食/饮料物品 tooltip 附加「右手右键可放置模型」灰字提示。
 *
 * <p>ItemTooltipEvent 是客户端事件，挂 GAME 总线。
 */
@EventBusSubscriber(modid = RealisticDining.MOD_ID, value = Dist.CLIENT)
public class SnackTooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (!SnackItemRegistry.isSnackItem(stack.getItem())) return;
        event.getToolTip().add(Component.translatable("tooltip.realisticdining.snack_place_hint")
                .withStyle(ChatFormatting.GRAY));
        if (SnackItemRegistry.isCureBeverage(stack.getItem())) {
            event.getToolTip().add(Component.translatable("tooltip.realisticdining.cure_negative_buff")
                    .withStyle(ChatFormatting.GRAY));
        }
        if (SnackItemRegistry.isBeer(stack.getItem())) {
            event.getToolTip().add(Component.translatable("tooltip.realisticdining.beer_drink_hint")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
