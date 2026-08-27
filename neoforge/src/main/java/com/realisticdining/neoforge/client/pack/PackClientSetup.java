package com.realisticdining.neoforge.client.pack;

import com.realisticdining.RealisticDining;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

/**
 * NeoForge 1.21.1 材质包扩展客户端初始化。
 *
 * <p>注册 {@link PackDefinitionManager} 为客户端资源重载监听器，
 * 让玩家 F3+T 重载资源时重新扫描 definitions/*.json。
 * 同时在 client setup 阶段绑定 {@link PackItems#EMPTY_ITEM}。
 */
@EventBusSubscriber(modid = RealisticDining.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PackClientSetup {

    private PackClientSetup() {
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new PackDefinitionManager());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PackItems::bindInstance);
    }
}
