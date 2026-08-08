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
 * 动画播放中分时段触发饱食度的 C2S 包（Fabric 1.21.1）
 *
 * <p>用于薯片等"一口一口吃"的零食：客户端动画到 hungerCue 时间点 -> sendToServer(drinkId)
 * -> 服务端 DrinkConsumeHandler.applyHunger 增加饱食度（不消耗物品）。
 */
public class ApplyHungerPacket {

    public static final ResourceLocation APPLY_HUNGER_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "apply_hunger");

    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(ApplyHungerPayload.TYPE, ApplyHungerPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ApplyHungerPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    String drinkId = payload.drinkId();
                    context.player().getServer().execute(() ->
                            DrinkConsumeHandler.applyHunger(player, drinkId));
                }
        );
    }

    public static void sendToServer(String drinkId) {
        ClientPlayNetworking.send(new ApplyHungerPacket.ApplyHungerPayload(drinkId));
    }

    public record ApplyHungerPayload(String drinkId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ApplyHungerPayload> TYPE =
                new CustomPacketPayload.Type<>(APPLY_HUNGER_PACKET_ID);

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
    }
}
