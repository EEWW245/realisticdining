package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.common.DrinkConsumeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * 动画播放中分时段触发饱食度的 C2S 包（Forge 1.20.1）
 *
 * <p>用于薯片等"一口一口吃"的零食：客户端动画到 hungerCue 时间点 -> sendToServer(drinkId)
 * -> 服务端 DrinkConsumeHandler.applyHunger 增加饱食度（不消耗物品）。
 */
public class ApplyHungerPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "apply_hunger"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, ApplyHungerMessage.class,
                ApplyHungerMessage::encode,
                ApplyHungerMessage::decode,
                ApplyHungerMessage::handle);
    }

    public static void sendToServer(String drinkId) {
        CHANNEL.sendToServer(new ApplyHungerMessage(drinkId));
    }

    public static class ApplyHungerMessage {
        private final String drinkId;

        public ApplyHungerMessage(String drinkId) {
            this.drinkId = drinkId;
        }

        public ApplyHungerMessage(FriendlyByteBuf buf) {
            this.drinkId = buf.readUtf(64);
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeUtf(drinkId, 64);
        }

        public static ApplyHungerMessage decode(FriendlyByteBuf buf) {
            return new ApplyHungerMessage(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                DrinkConsumeHandler.applyHunger(player, drinkId);
            });
            context.setPacketHandled(true);
        }
    }
}
