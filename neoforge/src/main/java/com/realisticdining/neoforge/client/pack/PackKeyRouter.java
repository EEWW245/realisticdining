package com.realisticdining.neoforge.client.pack;

import com.realisticdining.neoforge.network.PackAnimationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * U 键路由器（NeoForge 1.21.1）。
 *
 * <p>U 键按下时优先检查主手物品是否在材质包扩展列表内：
 * <ul>
 *   <li>命中 → 锁定快捷栏 + 本地触发 {@code eat} 动画 + 发 {@link PackAnimationPacket}
 *       （服务端仅设置 ServerEatingState 防刷），返回 {@code true}</li>
 *   <li>未命中 → 返回 {@code false}，调用方继续走原 {@code triggerDrinkForMainHand} 路径</li>
 * </ul>
 * 保证两条路径互斥，不会同时触发。
 */
public final class PackKeyRouter {

    private PackKeyRouter() {
    }

    public static boolean tryRoutePackAnimation() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        // 动画锁定期间忽略按键，避免重复触发
        if (PackAnimationLock.isLocked()) return true;

        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.isEmpty()) return false;

        ResourceLocation id = BuiltInRegistries.ITEM.getKey(mainHand.getItem());
        if (id == null || !PackDefinitionManager.containsItem(id.toString())) {
            return false;
        }

        // 命中材质包扩展物品 → 锁定 + 本地触发动画 + 发服务端防刷包
        PackAnimationLock.lock();
        PackEmpty.triggerEatAnimation();
        PackAnimationPacket.sendToServer();
        return true;
    }
}
