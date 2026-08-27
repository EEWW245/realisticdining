package com.example.realisticdining.forge.client.pack;

import com.example.realisticdining.RealisticDining;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge 1.20.1 材质包扩展客户端初始化。
 *
 * <p>注册 {@link PackDefinitionManager} 为客户端资源重载监听器，
 * 让玩家 F3+T 重载资源时重新扫描 definitions/*.json。
 */
@Mod.EventBusSubscriber(modid = RealisticDining.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PackClientSetup {

    private PackClientSetup() {
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new PackDefinitionManager());
    }
}
