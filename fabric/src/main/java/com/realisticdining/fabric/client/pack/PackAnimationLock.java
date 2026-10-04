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
        com.realisticdining.RealisticDining.LOGGER.info("[RD诊断] Lock: lock() 物品={} 数量={} 槽位={}",
                net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(lockedStack.getItem()),
                lockedStack.getCount(), lockedHotbarSlot);
    }

    /** 强制保持锁定的槽位（每帧调用）。 */
    public static void tickKeepSlot() {
        if (!isLocked()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            unlock();
            return;
        }
        // 先强制切回锁定槽位：Fabric 端 MouseHandlerMixin 可能没生效，玩家滚轮/数字键
        // 切走后必须先切回，否则后续 mainHand 检查的是新槽位物品 → ItemStack.matches
        // 返回 false → 误解锁。Forge/NeoForge 端有 onMouseScroll 拦截滚轮不会切走，
        // 但 Fabric 端没有，所以必须把 selected 切回放在物品检查之前。
        mc.player.getInventory().selected = lockedHotbarSlot;

        // 原版使用状态冲突（极早期，动画未开始）→ 中断+解锁防死锁
        if (mc.player.isUsingItem() && !PackEmpty.isEatAnimationPlaying(PackEmpty.getCurrentRenderItemId())) {
            mc.player.stopUsingItem();
            PackEmpty.stopEatAnimation(PackEmpty.getCurrentRenderItemId());
            unlock();
            return;
        }
        // 锁定期间主手物品被丢弃/移除（Q 丢单个、Ctrl+Q 丢整组等）→ 中断动画并解锁；
        // 此时 selected 已切回，mainHand 是锁定槽位的物品，若不匹配说明真的被丢弃了
        // 只比较物品类型，不比较数量：动画期间服务端可能已消耗 1 个（数量减 1），
        // 若用 ItemStack.matches 会误解锁 → PackEatFinishWatcher 不再调用 finishEatAnimation → 不消耗
        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.isEmpty() || !ItemStack.isSameItem(mainHand, lockedStack)) {
            com.realisticdining.RealisticDining.LOGGER.info("[RD诊断] Lock: 主手物品类型不匹配(丢弃/放置/移除) → 中断+解锁");
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
