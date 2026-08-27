package com.example.realisticdining.fabric.client.pack;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

/**
 * Fabric 1.20.1 材质包扩展客户端初始化。
 *
 * <p>用 {@link ResourceManagerHelper} 注册 {@link PackDefinitionManager} 为客户端资源重载监听器。
 * ResourceManagerHelper 的注册是永久的（一次注册，每次资源重载都生效），
 * 玩家退出世界重进后仍然有效，避免 CLIENT_STARTED 只触发一次导致 listener 丢失。
 */
public final class PackClientSetup {

    private PackClientSetup() {
    }

    public static void register() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new PackDefinitionManager());
    }
}
