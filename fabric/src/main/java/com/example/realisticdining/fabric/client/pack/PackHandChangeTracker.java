package com.example.realisticdining.fabric.client.pack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 主手物品切换检测器（Fabric 1.20.1）。
 *
 * <p>PICKUP 模式专用：每帧 tick 时检测玩家主手物品是否切换。
 * <ul>
 *   <li>新主手物品是扩展物品 + mode = PICKUP → 调 {@link PackEmpty#triggerPickupAnimation}
 *       本地触发 pickup 动画，播完后由 GeckoLib {@code hold_on_last_frame} 定格在持物姿态</li>
 *   <li>主手切走物品（旧物品是扩展物品 + mode = PICKUP）→ 调
 *       {@link PackEmpty#stopEatAnimation} 停止定格状态</li>
 * </ul>
 *
 * <p>STATIC 模式物品不在此处理——static 模式由 {@link PackHeldItemMotion} 程序化晃动接管，
 * 不依赖 pickup 动画。
 *
 * <p>动画锁定期间（eat 动画播放中）忽略切换检测，避免重复触发 pickup 打断 eat。
 *
 * <p>首次检查特判：玩家进游戏时若主手已持有 PICKUP 模式物品，直接触发 pickup
 * （解决"进游戏时无起始动画"问题）。
 *
 * <p>Fabric 1.20.1 不使用 ForgeRegistries，改用 {@link BuiltInRegistries#ITEM} 查 Item 的 ID。
 */
public final class PackHandChangeTracker {

    /** 上一 tick 的主手物品，用于检测切换。 */
    private static Item lastMainHandItem = null;
    /** 是否首次检查（进游戏第一帧）。 */
    private static boolean firstCheck = true;

    private PackHandChangeTracker() {
    }

    /**
     * 每帧由 {@link PackClientEvents#register} 注册的 ClientTickEvents.END_CLIENT_TICK 回调调用。
     *
     * @param mc Minecraft 实例
     */
    public static void tick(Minecraft mc) {
        if (mc.player == null) {
            lastMainHandItem = null;
            firstCheck = true;
            return;
        }
        ItemStack mainHandStack = mc.player.getMainHandItem();
        Item current = mainHandStack.isEmpty() ? null : mainHandStack.getItem();

        // 首次检查特判：进游戏时手里已持有扩展物品 → 直接触发 pickup
        if (firstCheck) {
            lastMainHandItem = current;
            firstCheck = false;
            tryTriggerPickup(current);
            return;
        }

        // 主手物品未变化 → 跳过
        if (current == lastMainHandItem) return;

        // 旧物品处理：如果是 PICKUP 模式扩展物品 → 停止定格动画
        stopAnimationIfPickup(lastMainHandItem);

        // 新物品处理：如果是 PICKUP 模式扩展物品 → 触发 pickup
        tryTriggerPickup(current);

        lastMainHandItem = current;
    }

    /**
     * 尝试为指定物品触发 pickup 动画。
     * 仅当物品是扩展物品 + mode = PICKUP + 当前未在 eat 动画锁定中时才触发。
     */
    private static void tryTriggerPickup(Item item) {
        if (item == null) return;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) return;
        if (!PackDefinitionManager.containsItem(id.toString())) return;
        if (PackDefinitionManager.getMode(id.toString()) != PackMode.PICKUP) return;
        // eat 动画播放中（locked）→ 不打断，等 eat 播完自然回到 pickup 定格
        if (PackAnimationLock.isLocked()) return;
        PackEmpty.triggerPickupAnimation();
    }

    /**
     * 尝试停止指定物品的 pickup 定格动画。
     * 仅当物品是扩展物品 + mode = PICKUP 时才停止。
     */
    private static void stopAnimationIfPickup(Item item) {
        if (item == null) return;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) return;
        if (!PackDefinitionManager.containsItem(id.toString())) return;
        if (PackDefinitionManager.getMode(id.toString()) != PackMode.PICKUP) return;
        PackEmpty.stopEatAnimation();
    }

    /** 重置状态（玩家退出世界时调用，避免跨世界残留）。 */
    public static void reset() {
        lastMainHandItem = null;
        firstCheck = true;
    }
}
