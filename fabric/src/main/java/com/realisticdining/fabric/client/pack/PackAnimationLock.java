package com.realisticdining.fabric.client.pack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

/**
 * 材质包扩展动画播放期间的全局锁（Fabric 1.21.1）。
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
        mc.player.getInventory().selected = lockedHotbarSlot;
    }

    /** 解除锁定。 */
    public static void unlock() {
        lockedStack = null;
        lockedHotbarSlot = -1;
    }
}
