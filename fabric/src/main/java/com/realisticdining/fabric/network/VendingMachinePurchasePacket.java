package com.realisticdining.fabric.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.menu.VendingMachinePurchaseHandler;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * 自动售货机购买 C2S 网络包（Fabric 1.21.1）
 * - 客户端点击 GUI 物品图标 -> 发送 VendingPurchasePayload(itemId)
 * - 服务端接收 -> 调用 VendingMachinePurchaseHandler.handle
 */
public class VendingMachinePurchasePacket {

    public static final ResourceLocation PURCHASE_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "vending_purchase");

    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(VendingPurchasePayload.TYPE, VendingPurchasePayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(VendingPurchasePayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    ResourceLocation itemId = payload.itemId();
                    context.player().getServer().execute(() ->
                            VendingMachinePurchaseHandler.handle(player, itemId));
                }
        );
    }

    public static void sendPurchaseToServer(ResourceLocation itemId) {
        ClientPlayNetworking.send(new VendingPurchasePayload(itemId));
    }

    public record VendingPurchasePayload(ResourceLocation itemId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<VendingPurchasePayload> TYPE =
                new CustomPacketPayload.Type<>(PURCHASE_PACKET_ID);

        public static final StreamCodec<ByteBuf, VendingPurchasePayload> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC,
                        VendingPurchasePayload::itemId,
                        VendingPurchasePayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
