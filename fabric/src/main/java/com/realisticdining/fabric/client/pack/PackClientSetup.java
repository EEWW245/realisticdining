package com.realisticdining.fabric.client.pack;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

/**
 * Fabric 1.21.1 材质包扩展客户端初始化。
 *
 * <p>注册 {@link PackDefinitionManager} 为客户端资源重载监听器，
 * 让玩家 F3+T 重载资源时重新扫描 definitions/*.json。
 * 同时调用 {@link PackItems#bindInstance} 绑定 EMPTY_ITEM 引用。
 */
public final class PackClientSetup {

    private PackClientSetup() {
    }

    public static void register() {
        // pack_empty GeoItem 已在 onInitialize 阶段（注册表冻结前）由 PackItems.register() 注册，
        // 这里不再重复注册（否则会因注册表已冻结而失败）。

        // 注册 PackDefinitionManager 为客户端资源重载监听器（扫描 definitions/*.json）
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new PackDefinitionManager());

        // 注册客户端游戏内事件（tick + 鼠标滚轮）
        PackClientEvents.register();
    }
}
