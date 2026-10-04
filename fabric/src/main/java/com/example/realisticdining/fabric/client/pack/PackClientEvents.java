package com.example.realisticdining.fabric.client.pack;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

/**
 * Fabric 1.20.1 材质包扩展客户端游戏内事件。
 *
 * <p>每帧 tick 时：
 * <ul>
 *   <li>{@link PackHeldItemMotion#tickInertia} / {@link PackHeldItemMotion#tickJump}：
 *       推进惯性 + 跳跃晃动的物理状态（STATIC 模式专用）</li>
 *   <li>{@link PackHandChangeTracker#tick}：检测主手物品切换，PICKUP 模式触发 pickup 动画</li>
 *   <li>{@link PackAnimationLock#tickKeepSlot}：动画播放期间锁定快捷栏槽位（防刷）</li>
 *   <li>动画锁定期间消费 hotbar 数字键 1-9 的 click，防止玩家用数字键切槽位</li>
 * </ul>
 *
 * <p>Fabric 1.20.1 无原版鼠标滚轮事件，滚轮拦截由 {@code MouseHandlerMixin} 完成。
 * 数字键拦截由本类在每帧 tick 开头消费 {@code keyHotbarSlots[*].consumeClick()} 实现。
 */
public final class PackClientEvents {

    private PackClientEvents() {
    }

    public static void register() {
        // START_CLIENT_TICK：在原版 handleKeybindings() 之后，但在渲染之前
        // 强制把 selected 切回锁定槽位，纠正玩家按数字键 1-9 产生的槽位偏移
        // 必须在 tick() HEAD 执行，这样 tickKeepSlot 在 END 阶段检查时主手物品仍匹配，不会误解锁
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (!PackAnimationLock.isLocked()) return;
            if (client.screen != null) return;
            // 强制切回锁定槽位（玩家按数字键切走后立即切回）
            client.player.getInventory().selected = PackAnimationLock.lockedHotbarSlot;
        });

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
                if (client.screen instanceof net.minecraft.client.gui.screens.PauseScreen) {
                    // 玩家按 ESC 打开暂停菜单 → 强制中止动画（作用于当前渲染物品的专属 manager）
                    // 只对 ESC 暂停菜单中断，打开背包/箱子/合成等 GUI 时动画继续播放
                    String itemId = PackEmpty.getCurrentRenderItemId();
                    PackEmpty.stopEatAnimation(itemId);
                    // PICKUP 模式：controller.stop() 会让骨骼 lerp 回 geo.json 初始姿态,
                    // 表现为"静态模型残留"（如 canned_food 的初始罐头模型）。
                    // 补一次 triggerPickupAnimation 让 hold_on_last_frame 重新定格在持物姿态,
                    // 与正常播完路径 finishEatAnimation 末尾的重新定格逻辑保持一致。
                    if (itemId != null
                            && PackDefinitionManager.getMode(itemId) == PackMode.PICKUP) {
                        PackEmpty.triggerPickupAnimation(itemId);
                    }
                    PackAnimationLock.unlock();
                    return;
                }
                PackAnimationLock.tickKeepSlot();
            }
            // eat 动画播完自动消耗（材质包无需手写 finished 指令）
            PackEatFinishWatcher.tick();
        });
    }
}
