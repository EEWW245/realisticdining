package com.realisticdining.fabric.client.pack;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

/**
 * Fabric 1.21.1 材质包扩展客户端游戏内事件。
 *
 * <p>每帧 tick 时：
 * <ul>
 *   <li>{@link PackHeldItemMotion#tickInertia} / {@link PackHeldItemMotion#tickJump}：
 *       推进惯性 + 跳跃晃动的物理状态（STATIC 模式专用）</li>
 *   <li>{@link PackHandChangeTracker#tick}：检测主手物品切换，PICKUP 模式触发 pickup 动画</li>
 *   <li>{@link PackAnimationLock#tickKeepSlot}：动画播放期间锁定快捷栏槽位（防刷）</li>
 * </ul>
 *
 * <p>注意：鼠标滚轮事件需通过 Mixin 拦截 MouseHandler 才能在原版级别取消，
 * Fabric API 暂未暴露给普通用户。当前依赖 {@link PackAnimationLock#tickKeepSlot}
 * 每帧强制保持槽位，鼠标滚轮切槽会被立即复位，已足够防刷。
 */
public final class PackClientEvents {

    private PackClientEvents() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
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
        });
    }
}
