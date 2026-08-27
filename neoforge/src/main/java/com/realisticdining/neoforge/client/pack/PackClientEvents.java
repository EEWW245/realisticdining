package com.realisticdining.neoforge.client.pack;

import com.realisticdining.RealisticDining;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * NeoForge 1.21.1 材质包扩展客户端游戏内事件。
 *
 * <p>每帧 tick 时：
 * <ul>
 *   <li>{@link PackHeldItemMotion#tickInertia} / {@link PackHeldItemMotion#tickJump}：
 *       推进惯性 + 跳跃晃动的物理状态（STATIC 模式专用）</li>
 *   <li>{@link PackHandChangeTracker#tick}：检测主手物品切换，PICKUP 模式触发 pickup 动画</li>
 *   <li>{@link PackAnimationLock#tickKeepSlot}：动画播放期间锁定快捷栏槽位（防刷）</li>
 * </ul>
 * 鼠标滚轮事件在动画锁定期间取消，防止切槽位。
 */
@EventBusSubscriber(modid = RealisticDining.MOD_ID, value = Dist.CLIENT)
public final class PackClientEvents {

    private PackClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        PackHeldItemMotion.tickInertia();
        PackHeldItemMotion.tickJump();

        Minecraft mc = Minecraft.getInstance();
        // PICKUP 模式：检测主手切换，触发/停止 pickup 动画（内部处理 player==null）
        PackHandChangeTracker.tick(mc);
        if (mc.player == null) {
            return;
        }
        // 动画锁定期间强制保持槽位
        if (PackAnimationLock.isLocked()) {
            if (mc.screen != null) {
                // 玩家打开背包等界面 → 强制中止动画
                PackEmpty.stopEatAnimation();
                PackAnimationLock.unlock();
                return;
            }
            PackAnimationLock.tickKeepSlot();
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (PackAnimationLock.isLocked()) {
            event.setCanceled(true);
        }
    }
}
