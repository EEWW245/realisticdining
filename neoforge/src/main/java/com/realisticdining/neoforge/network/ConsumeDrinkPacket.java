package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.DrinkConsumeHandler;
import com.realisticdining.common.ServerEatingState;
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
 *
 * <p>双模式（v2.3.0+ 加 startEating 字段，用于 SnackDisplayPlaceHandler 防无限刷）：
 * <ul>
 *   <li>startEating=true：动画开始（pickup 触发）→ 服务端 ServerEatingState.setEating(true)</li>
 *   <li>startEating=false：动画自然结束 → 服务端 setEating(false) + DrinkConsumeHandler.handle 消耗物品</li>
 * </ul>
 *
 * <p>动画中断（切物品/丢物品）场景由客户端在 reset() 后发送 startEating=false；
 * 服务端 handle 内部若找不到主手饮料会静默 return，不会误消耗。
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

    /** 动画自然结束 → 消耗物品 + 清状态（兼容旧调用） */
    public static void sendConsumeToServer(String drinkId) {
        sendConsumeToServer(drinkId, false);
    }

    /**
     * @param startEating true=动画开始，仅设置 ServerEatingState；false=动画结束，清状态并消耗物品
     */
    public static void sendConsumeToServer(String drinkId, boolean startEating) {
        PacketDistributor.sendToServer(new ConsumeDrinkPayload(drinkId, startEating));
    }

    public record ConsumeDrinkPayload(String drinkId, boolean startEating) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ConsumeDrinkPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "consume_drink"));

        public static final StreamCodec<ByteBuf, ConsumeDrinkPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,
                        ConsumeDrinkPayload::drinkId,
                        ByteBufCodecs.BOOL,
                        ConsumeDrinkPayload::startEating,
                        ConsumeDrinkPayload::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(ConsumeDrinkPayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            if (payload.startEating()) {
                // 动画开始：标记正在吃/喝，SnackDisplayPlaceHandler 会拒绝放置
                ServerEatingState.setEating(player.getUUID(), true);
            } else {
                // 动画结束：清状态 + 消耗物品（找不到则静默 return）
                ServerEatingState.setEating(player.getUUID(), false);
                DrinkConsumeHandler.handle(player, payload.drinkId());
            }
        }
    }
}
