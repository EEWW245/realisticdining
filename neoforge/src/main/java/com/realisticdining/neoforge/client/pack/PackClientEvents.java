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
                if (mc.screen instanceof net.minecraft.client.gui.screens.PauseScreen) {
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
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (PackAnimationLock.isLocked()) {
            event.setCanceled(true);
        }
    }
}
