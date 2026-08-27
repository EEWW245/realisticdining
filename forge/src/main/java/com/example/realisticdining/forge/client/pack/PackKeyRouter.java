package com.example.realisticdining.forge.client.pack;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.forge.network.PackAnimationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * U 键路由器（Forge 1.20.1）。
 *
 * <p>U 键按下时优先检查主手物品是否在材质包扩展列表内：
 * <ul>
 *   <li>命中 → 发 {@link PackAnimationPacket} 触发单动画 + finished 指令消耗，返回 {@code true}</li>
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

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(mainHand.getItem());
        if (id == null || !PackDefinitionManager.containsItem(id.toString())) {
            return false;
        }

        // 命中材质包扩展物品 → 客户端先锁定快捷栏（防刷），再发动画触发包
        RealisticDining.LOGGER.info("[材质包扩展调试] U键命中物品 {}，客户端锁定并发送动画触发包", id);
        PackAnimationLock.lock();
        PackAnimationPacket.sendToServer();
        return true;
    }
}
