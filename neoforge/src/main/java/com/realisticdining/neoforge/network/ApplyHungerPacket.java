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
 * 动画播放中分时段触发饱食度的 C2S 包（NeoForge 1.21.1）
 *
 * <p>用于薯片等"一口一口吃"的零食：客户端动画到 hungerCue 时间点 -> sendToServer(drinkId)
 * -> 服务端 DrinkConsumeHandler.applyHunger 增加饱食度（不消耗物品）。
 */
public class ApplyHungerPacket {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ApplyHungerPayload.TYPE,
                ApplyHungerPayload.STREAM_CODEC,
                ApplyHungerPayload::handleDataOnMain
        );
    }

    public static void sendToServer(String drinkId) {
        PacketDistributor.sendToServer(new ApplyHungerPayload(drinkId));
    }

    public record ApplyHungerPayload(String drinkId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ApplyHungerPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "apply_hunger"));

        public static final StreamCodec<ByteBuf, ApplyHungerPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,
                        ApplyHungerPayload::drinkId,
                        ApplyHungerPayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(ApplyHungerPayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            DrinkConsumeHandler.applyHunger(player, payload.drinkId());
        }
    }
}
