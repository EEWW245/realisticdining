package com.example.realisticdining.forge.events;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.common.SnackItemRegistry;
import com.example.realisticdining.events.FriedRiceEggPlaceHandler;
import com.example.realisticdining.events.SnackDisplayPlacement;
import com.example.realisticdining.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RealisticDining.MOD_ID)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        // 蛋炒饭放置：副手拿蛋炒饭右键时放置（服务端 shrink）
        if (FriedRiceEggPlaceHandler.onPlayerRightClick(event.getEntity(), event.getLevel(), event.getHand())) {
            event.setCanceled(true);
            return;
        }
        // 对准空气右键：主手持零食/饮料时直接触发饮用动画（仅客户端）
        // 仅当饮用键已改绑为右键时才触发，避免与默认 U 键冲突
        if (event.getLevel().isClientSide) {
            ItemStack held = event.getEntity().getItemInHand(event.getHand());
            if (!held.isEmpty() && SnackItemRegistry.isSnackItem(held.getItem())) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        () -> () -> {
                            if (com.example.realisticdining.forge.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) {
                                com.example.realisticdining.forge.client.arm.FpArmRenderSystem.triggerDrinkForMainHand();
                            }
                        });
            }
        }
    }

    /** 手持零食/饮料右键地面 → 自动创建展示台并放入第一件物品；放置失败（位置放不下）时兜底触发饮用动画 */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // 双端执行：客户端必须取消事件，否则原版 Block.use() 会先执行，
        // 导致森罗物语桌子先在客户端放置 2D 物品贴图（即使服务端取消也来不及）。
        InteractionResult result = SnackDisplayPlacement.tryPlace(
                event.getEntity(), event.getLevel(), event.getHand(),
                event.getPos(), event.getFace());
        if (result != InteractionResult.PASS) {
            event.setCancellationResult(result);
            event.setCanceled(true);
            return;
        }

        // 放置失败（PASS）：若主手持零食/饮料且目标不是展示台本身，则兜底触发饮用动画。
        // 只在客户端执行（动画是纯客户端逻辑），用 DistExecutor 避免服务端加载客户端类。
        if (event.getLevel().isClientSide) {
            ItemStack held = event.getEntity().getItemInHand(event.getHand());
            boolean isSnack = !held.isEmpty() && SnackItemRegistry.isSnackItem(held.getItem());
            boolean notDisplay = event.getLevel().getBlockState(event.getPos()).getBlock() != ModBlocks.SNACK_DISPLAY.get();
            if (isSnack && notDisplay) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        () -> () -> {
                            if (com.example.realisticdining.forge.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) {
                                com.example.realisticdining.forge.client.arm.FpArmRenderSystem.triggerDrinkForMainHand();
                            }
                        });
            }
        }
    }

    /** 玩家退出世界时清除 eating 状态（v2.3.0+ 防状态卡死兜底） */
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerEatingState.reset(event.getEntity().getUUID());
    }

    /** 零食/饮料物品 tooltip：附加「右手右键可放置模型」灰字提示，奶啤/豆浆再加「可消除消极Buff」 */
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
