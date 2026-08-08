package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.menu.VendingMachinePurchaseHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 自动售货机购买 C2S 网络包（NeoForge 1.21.1）
 * - 客户端点击 GUI 物品图标 -> 发送 VendingPurchasePayload(itemId)
 * - 服务端接收 -> 调用 VendingMachinePurchaseHandler.handle
 */
public class VendingMachinePurchasePacket {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                VendingPurchasePayload.TYPE,
                VendingPurchasePayload.STREAM_CODEC,
                VendingPurchasePayload::handleDataOnMain
        );
    }

    public static void sendPurchaseToServer(ResourceLocation itemId) {
        PacketDistributor.sendToServer(new VendingPurchasePayload(itemId));
    }

    public record VendingPurchasePayload(ResourceLocation itemId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<VendingPurchasePayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "vending_purchase"));

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

        public static void handleDataOnMain(VendingPurchasePayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            VendingMachinePurchaseHandler.handle(player, payload.itemId());
        }
    }
}
