package com.realisticdining.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PlatformHelper {

    @ExpectPlatform
    public static Platform getPlatform() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openCookbookMenu(ServerPlayer player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openEatRiceGuideMenu(ServerPlayer player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openVendingMachineMenu(ServerPlayer player, BlockPos pos) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void sendVendingPurchase(ResourceLocation itemId) {
        throw new AssertionError();
    }

    /**
     * 客户端 → 服务端：饮料/零食动画播完后请求消耗物品。
     * @param drinkId 与 {@link com.realisticdining.common.DrinkItemMapping} 中登记的 drinkId 一致
     */
    public static void sendDrinkConsume(String drinkId) {
        sendDrinkConsume(drinkId, false);
    }

    /**
     * 客户端 → 服务端：饮料/零食动画状态同步。
     * @param drinkId   与 {@link com.realisticdining.common.DrinkItemMapping} 中登记的 drinkId 一致
     * @param startEating true=动画开始（pickup 触发），服务端仅标记 ServerEatingState；
     *                    false=动画自然结束，服务端清状态并消耗物品
     */
    @ExpectPlatform
    public static void sendDrinkConsume(String drinkId, boolean startEating) {
        throw new AssertionError();
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.tryBuild("realisticdining", path);
    }

    public enum Platform {
        FABRIC,
        FORGE,
        NEOFORGE
    }
}
