package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.menu.VendingMachinePurchaseHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * 客户端 → 服务端：自动售货机购买请求
 * 客户端发送物品 ID，服务端扣金粒+给物品。
 */
public class VendingMachinePurchasePacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "vending_machine"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, PurchaseMessage.class,
                PurchaseMessage::encode,
                PurchaseMessage::decode,
                PurchaseMessage::handle);
    }

    public static void sendToServer(ResourceLocation itemId) {
        CHANNEL.sendToServer(new PurchaseMessage(itemId));
    }

    public static class PurchaseMessage {
        private final ResourceLocation itemId;

        public PurchaseMessage(ResourceLocation itemId) {
            this.itemId = itemId;
        }

        public PurchaseMessage(FriendlyByteBuf buf) {
            this.itemId = buf.readResourceLocation();
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeResourceLocation(itemId);
        }

        public static PurchaseMessage decode(FriendlyByteBuf buf) {
            return new PurchaseMessage(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                VendingMachinePurchaseHandler.handle(player, itemId);
            });
            context.setPacketHandled(true);
        }
    }
}
