package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.menu.VendingMachinePurchaseHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * 客户端 → 服务端：自动售货机购买请求（Fabric 1.20.1）
 */
public class VendingMachinePurchasePacket {

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "vending_machine_purchase");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            ResourceLocation itemId = buf.readResourceLocation();
            server.execute(() -> VendingMachinePurchaseHandler.handle(player, itemId));
        });
    }

    public static void sendToServer(ResourceLocation itemId) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(itemId);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }
}
