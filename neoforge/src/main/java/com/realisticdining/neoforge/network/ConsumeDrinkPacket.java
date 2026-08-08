package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.DrinkConsumeHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 饮料/零食消耗 C2S 网络包（NeoForge 1.21.1）
 * - 客户端动画播完后 -> 发送 ConsumeDrinkPayload(drinkId)
 * - 服务端接收 -> 调用 DrinkConsumeHandler.handle
 */
public class ConsumeDrinkPacket {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ConsumeDrinkPayload.TYPE,
                ConsumeDrinkPayload.STREAM_CODEC,
                ConsumeDrinkPayload::handleDataOnMain
        );
    }

    public static void sendConsumeToServer(String drinkId) {
        PacketDistributor.sendToServer(new ConsumeDrinkPayload(drinkId));
    }

    public record ConsumeDrinkPayload(String drinkId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ConsumeDrinkPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "consume_drink"));

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

        public static void handleDataOnMain(ConsumeDrinkPayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            DrinkConsumeHandler.handle(player, payload.drinkId());
        }
    }
}
