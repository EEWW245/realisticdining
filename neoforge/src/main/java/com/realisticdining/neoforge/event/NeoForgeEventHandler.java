package com.realisticdining.neoforge.event;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * NeoForge 通用事件处理器（1.21.1）。
 *
 * <p>v2.3.0+ 玩家退出世界时清除 ServerEatingState，防止饮料/零食动画状态卡死
 * 导致后续右键地面放展示台被永久拦截。
 */
@EventBusSubscriber(modid = RealisticDining.MOD_ID)
public class NeoForgeEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerEatingState.reset(event.getEntity().getUUID());
    }
}
