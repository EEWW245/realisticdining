package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.common.DrinkConsumeHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * 动画播放中分时段触发饱食度的 C2S 包（Fabric 1.20.1）
 *
 * <p>用于薯片等"一口一口吃"的零食：客户端动画到 hungerCue 时间点 -> sendToServer(drinkId)
 * -> 服务端 DrinkConsumeHandler.applyHunger 增加饱食度（不消耗物品）。
 */
public class ApplyHungerPacket {

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "apply_hunger");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            String drinkId = buf.readUtf(64);
            server.execute(() -> DrinkConsumeHandler.applyHunger(player, drinkId));
        });
    }

    public static void sendToServer(String drinkId) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(drinkId, 64);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }
}
