package com.example.realisticdining.fabric.client.pack;

import com.example.realisticdining.fabric.network.PackAnimationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * U 键路由器（Fabric 1.20.1）。
 *
 * <p>U 键按下时优先检查主手物品是否在材质包扩展列表内：
 * <ul>
 *   <li>命中 → 锁定客户端快捷栏（防刷） + 发 {@link PackAnimationPacket} 触发单动画 + finished 指令消耗，返回 {@code true}</li>
 *   <li>未命中 → 返回 {@code false}，调用方继续走原 {@code triggerDrinkForMainHand} 路径</li>
 * </ul>
 * 保证两条路径互斥，不会同时触发。
 *
 * <p>Fabric 1.20.1 与 Forge 的差异：{@code PackAnimationLock.lock()} 在客户端调用，
 * 不在服务端 packet handler 内调用——避免服务端 handler 引用 client-only 类
 * （Minecraft.getInstance()）导致专用服崩溃。
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
        PackAnimationLock.lock();
        PackAnimationPacket.sendToServer();
        return true;
    }
}
