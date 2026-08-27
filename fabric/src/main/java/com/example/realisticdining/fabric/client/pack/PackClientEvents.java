package com.example.realisticdining.fabric.client.pack;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

/**
 * Fabric 1.20.1 材质包扩展客户端游戏内事件。
 *
 * <p>每帧 tick 时：
 * <ul>
 *   <li>{@link PackHeldItemMotion#tickInertia} / {@link PackHeldItemMotion#tickJump}：
 *       推进惯性 + 跳跃晃动的物理状态（STATIC 模式专用）</li>
 *   <li>{@link PackHandChangeTracker#tick}：检测主手物品切换，PICKUP 模式触发 pickup 动画</li>
 *   <li>{@link PackAnimationLock#tickKeepSlot}：动画播放期间锁定快捷栏槽位（防刷）</li>
 * </ul>
 *
 * <p>Fabric 1.20.1 无原版鼠标滚轮事件，槽位锁定通过 {@link PackAnimationLock#tickKeepSlot}
 * 每 tick 强制覆盖 selected 实现（玩家滚轮产生的槽位偏移会在下一 tick 立即纠正）。
 */
public final class PackClientEvents {

    private PackClientEvents() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PackHeldItemMotion.tickInertia();
            PackHeldItemMotion.tickJump();

            // PICKUP 模式：检测主手切换，触发/停止 pickup 动画（内部处理 player==null）
            PackHandChangeTracker.tick(client);
            if (client.player == null) {
                return;
            }
            // 动画锁定期间强制保持槽位
            if (PackAnimationLock.isLocked()) {
                if (client.screen != null) {
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
