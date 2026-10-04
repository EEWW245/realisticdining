package com.example.realisticdining.fabric.compat;

import com.example.realisticdining.fabric.client.arm.FpArmRenderSystem;
import com.example.realisticdining.fabric.client.pack.PackDefinitionManager;
import com.example.realisticdining.fabric.mixin.Accessor.ItemInHandRendererAccessor;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

/**
 * First-person Model 兼容性处理器（Fabric 1.20.1）。
 * 当 RealisticDining 的动画正在播放或手持相关物品时，强制禁用 First-person Model，
 * 使用原版第一人称渲染，避免动画显示异常。
 */
public final class FirstPersonModHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float VANILLA_PITCH_MAX = 60.0F;

    // 强制使用原版第一人称
    private static boolean forcedVanilla = false;
    private static boolean previousEnabled = false;

    private FirstPersonModHandler() {
    }

    /**
     * 注册事件监听器。
     */
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(FirstPersonModHandler::onClientTick);
        ClientPlayConnectionEvents.DISCONNECT.register(FirstPersonModHandler::onClientLogout);
    }

    /**
     * 客户端 tick 结束时检查是否需要禁用 First-person Model。
     */
    private static void onClientTick(Minecraft mc) {
        if (mc.player == null || !mc.options.getCameraType().isFirstPerson()) {
            restorePreviousState();
            return;
        }

        if (!FirstPersonModApi.findApi()) {
            return;
        }

        // 检查是否需要强制使用原版第一人称
        if (shouldForceVanillaFirstPerson(mc.player)) {
            forceVanillaFirstPerson(mc);
        } else {
            restorePreviousState();
        }
    }

    /**
     * 客户端登出时恢复 First-person Model 状态。
     */
    private static void onClientLogout(ClientPacketListener packetListener, Minecraft mc) {
        restorePreviousState();
    }

    /**
     * 检查是否需要强制使用原版第一人称渲染。
     * 条件：
     * 1. 手臂渲染已启用，且（饮料动画正在播放 或 手持饮料物品）
     * 2. 副手有米饭
     * 3. 主手或副手有材质包扩展物品
     */
    private static boolean shouldForceVanillaFirstPerson(Player player) {
        // 条件 1：饮料动画相关
        if (FpArmRenderSystem.isArmRenderEnabled()
                && (FpArmRenderSystem.isDrinkPlaying() || FpArmRenderSystem.hasDrinkItemInHand(player))) {
            return true;
        }

        // 条件 2：副手有米饭
        ItemStack offHandItem = player.getOffhandItem();
        if (isCookedRice(offHandItem)) {
            return true;
        }

        // 条件 3：材质包扩展物品
        ItemStack mainHandItem = player.getMainHandItem();
        if (isPackItem(mainHandItem) || isPackItem(offHandItem)) {
            return true;
        }

        return false;
    }

    /**
     * 检查物品是否为煮熟的米饭。
     */
    private static boolean isCookedRice(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null) {
            return false;
        }
        String idStr = id.toString();
        return idStr.contains("rice") && !idStr.contains("uncooked");
    }

    /**
     * 检查物品是否为材质包扩展物品。
     */
    private static boolean isPackItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && PackDefinitionManager.itemIdList.contains(id.toString());
    }

    /**
     * 强制使用原版第一人称渲染。
     * 如果 First-person Model 之前是启用的，会禁用它并触发假的 reequip 动画。
     */
    private static void forceVanillaFirstPerson(Minecraft minecraft) {
        if (!forcedVanilla) {
            previousEnabled = FirstPersonModApi.isFirstPersonModEnabled();
            forcedVanilla = true;
            if (previousEnabled) {
                playVanillaReequipAnimation(minecraft);
            }
        }
        FirstPersonModApi.setFirstPersonModEnabled(false);
    }

    /**
     * 触发假的 reequip 动画，让手臂摆动看起来自然。
     */
    private static void playVanillaReequipAnimation(Minecraft minecraft) {
        ItemStack stack = minecraft.player.getMainHandItem();
        if (stack.isEmpty()) {
            return;
        }
        ItemInHandRendererAccessor itemInHandRenderer = (ItemInHandRendererAccessor) minecraft.gameRenderer.itemInHandRenderer;
        ItemStack fakeStack = stack.copy();
        fakeStack.setCount(stack.getCount() + 1);
        itemInHandRenderer.realisticdining$setMainHandItem(fakeStack);
    }

    /**
     * 恢复 First-person Model 到之前的状态。
     */
    private static void restorePreviousState() {
        if (!forcedVanilla || !FirstPersonModApi.findApi()) {
            forcedVanilla = false;
            return;
        }
        FirstPersonModApi.setFirstPersonModEnabled(previousEnabled);
        forcedVanilla = false;
    }
}
