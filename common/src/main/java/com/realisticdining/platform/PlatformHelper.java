package com.realisticdining.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.shapes.VoxelShape;

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
     * 返回 SnackDisplayBlock 的视线检测箱（SHAPE）。
     * Fabric 平台使用完整 1 格（16,16,16），NeoForge/Forge 平台使用 0.25 格薄板（16,4,16）。
     * 按用户偏好：Fabric 端追求右键命中精度，NeoForge/Forge 端追求可越过展示台上方放置方块。
     */
    @ExpectPlatform
    public static VoxelShape getSnackDisplayShape() {
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
