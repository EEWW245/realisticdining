package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.common.DrinkConsumeHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * 饮料/零食消耗 C2S 网络包（Fabric 1.20.1）
 * - 客户端动画播完后 -> sendToServer(drinkId)
 * - 服务端接收 -> DrinkConsumeHandler.handle
 */
public class ConsumeDrinkPacket {

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "consume_drink");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            String drinkId = buf.readUtf(64);
            server.execute(() -> DrinkConsumeHandler.handle(player, drinkId));
        });
    }

    public static void sendToServer(String drinkId) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(drinkId, 64);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }
}
