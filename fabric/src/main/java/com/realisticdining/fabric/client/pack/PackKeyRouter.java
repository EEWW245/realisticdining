package com.realisticdining.fabric.client.pack;

import com.realisticdining.fabric.network.PackAnimationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * U 键路由器（Fabric 1.21.1）。
 *
 * <p>U 键按下时优先检查主手物品是否在材质包扩展列表内：
 * <ul>
 *   <li>命中 → 客户端先锁定快捷栏（防刷），再发 {@link PackAnimationPacket}
 *       触发 eat 动画（服务端通过 GeckoLib 同步触发 triggerAnim），返回 {@code true}</li>
 *   <li>未命中 → 返回 {@code false}，调用方继续走原 {@code triggerDrinkForMainHand} 路径</li>
 * </ul>
 * 保证两条路径互斥，不会同时触发。
 *
 * <p>与 1.20.1 一致：不在客户端本地触发动画，而是由服务端 triggerAnim 同步触发，
 * 避免 currentRenderItemId 残留导致的动画状态问题。
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

        // 命中材质包扩展物品 → 客户端先锁定快捷栏（防刷），再发动画触发包
        // 动画由服务端 triggerAnim 同步触发（与 1.20.1 一致）
        PackAnimationLock.lock();
        PackAnimationPacket.sendToServer();
        return true;
    }
}
