package com.realisticdining.neoforge.client.pack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

/**
 * 材质包扩展动画播放期间的全局锁（NeoForge 1.21.1）。
 *
 * <p>动画播放期间锁定快捷栏槽位 + 拦截鼠标滚轮，防止玩家切物品刷动画。
 * 对应 ImmersiveEating 的 Food.temp + Food.lockedHotbarSlot。
 */
public final class PackAnimationLock {

    /** 当前正被动画占用的物品 ItemStack（动画期间不可为 null）。 */
    public static ItemStack lockedStack = null;
    /** 锁定的快捷栏槽位（-1 表示未锁）。 */
    public static int lockedHotbarSlot = -1;

    private PackAnimationLock() {
    }

    public static boolean isLocked() {
        return lockedStack != null && lockedHotbarSlot >= 0;
    }

    /** 进入锁定状态：记录当前主手物品 + 当前槽位。 */
    public static void lock() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        lockedStack = mc.player.getMainHandItem().copy();
        lockedHotbarSlot = mc.player.getInventory().selected;
    }

    /** 强制保持锁定的槽位（每帧调用）。 */
    public static void tickKeepSlot() {
        if (!isLocked()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            unlock();
            return;
        }
        // 先强制切回锁定槽位：玩家滚轮/数字键切走后必须先切回，
        // 否则后续 mainHand 检查的是新槽位物品 → 误解锁
        mc.player.getInventory().selected = lockedHotbarSlot;

        // 原版使用状态冲突（极早期，动画未开始）→ 中断+解锁防死锁
        if (mc.player.isUsingItem() && !PackEmpty.isEatAnimationPlaying(PackEmpty.getCurrentRenderItemId())) {
            mc.player.stopUsingItem();
            PackEmpty.stopEatAnimation(PackEmpty.getCurrentRenderItemId());
            unlock();
            return;
        }
        // 锁定期间主手物品被丢弃/移除（Q 丢单个、Ctrl+Q 丢整组等）→ 中断动画并解锁
        // 只比较物品类型，不比较数量：动画期间服务端可能已消耗 1 个（数量减 1），
        // 若用 ItemStack.matches 会误解锁 → PackEatFinishWatcher 不再调用 finishEatAnimation → 不消耗
        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.isEmpty() || !ItemStack.isSameItem(mainHand, lockedStack)) {
            PackEmpty.stopEatAnimation(PackEmpty.getCurrentRenderItemId());
            unlock();
            return;
        }
    }

    /** 解除锁定。 */
    public static void unlock() {
        lockedStack = null;
        lockedHotbarSlot = -1;
    }
}
