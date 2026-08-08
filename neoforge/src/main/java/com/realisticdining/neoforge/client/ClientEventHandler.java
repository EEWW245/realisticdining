package com.realisticdining.neoforge.client;

import com.realisticdining.RealisticDining;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * 客户端 tick 事件监听。
 * 修复：原先 ModKeybinds.checkKeybindings() 是 public static 但没有任何 tick 事件调用它，
 *      导致 T/Y/P/O/U 等按键实际不会触发。这里在客户端 tick END 阶段调用，按键才能响应。
 */
@EventBusSubscriber(modid = RealisticDining.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ModKeybinds.checkKeybindings();
    }
}
