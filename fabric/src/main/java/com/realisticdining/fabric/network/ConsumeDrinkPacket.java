package com.realisticdining.fabric.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.DrinkConsumeHandler;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * 饮料/零食消耗 C2S 网络包（Fabric 1.21.1）
 * - 客户端动画播完后 -> 发送 ConsumeDrinkPayload(drinkId)
 * - 服务端接收 -> 调用 DrinkConsumeHandler.handle
 */
public class ConsumeDrinkPacket {

    public static final ResourceLocation CONSUME_DRINK_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "consume_drink");

    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(ConsumeDrinkPayload.TYPE, ConsumeDrinkPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ConsumeDrinkPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    String drinkId = payload.drinkId();
                    context.player().getServer().execute(() ->
                            DrinkConsumeHandler.handle(player, drinkId));
                }
        );
    }

    public static void sendConsumeToServer(String drinkId) {
        ClientPlayNetworking.send(new ConsumeDrinkPayload(drinkId));
    }

    public record ConsumeDrinkPayload(String drinkId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ConsumeDrinkPayload> TYPE =
                new CustomPacketPayload.Type<>(CONSUME_DRINK_PACKET_ID);

        public static final StreamCodec<ByteBuf, ConsumeDrinkPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,
                        ConsumeDrinkPayload::drinkId,
                        ConsumeDrinkPayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
