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
 * 饮料/零食消耗 C2S 网络包（Forge 1.20.1）
 * - 客户端动画播完后 -> sendToServer(drinkId)
 * - 服务端接收 -> DrinkConsumeHandler.handle
 */
public class ConsumeDrinkPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "consume_drink"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, ConsumeDrinkMessage.class,
                ConsumeDrinkMessage::encode,
                ConsumeDrinkMessage::decode,
                ConsumeDrinkMessage::handle);
    }

    public static void sendToServer(String drinkId) {
        CHANNEL.sendToServer(new ConsumeDrinkMessage(drinkId));
    }

    public static class ConsumeDrinkMessage {
        private final String drinkId;

        public ConsumeDrinkMessage(String drinkId) {
            this.drinkId = drinkId;
        }

        public ConsumeDrinkMessage(FriendlyByteBuf buf) {
            this.drinkId = buf.readUtf(64);
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeUtf(drinkId, 64);
        }

        public static ConsumeDrinkMessage decode(FriendlyByteBuf buf) {
            return new ConsumeDrinkMessage(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                DrinkConsumeHandler.handle(player, drinkId);
            });
            context.setPacketHandled(true);
        }
    }
}
