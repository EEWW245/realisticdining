package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
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
 *
 * <p>双模式（v2.3.0+ 加 startEating 字段，用于 SnackDisplayPlacement 防无限刷）：
 * <ul>
 *   <li>startEating=true：动画开始（pickup 触发）→ 服务端 ServerEatingState.setEating(true)</li>
 *   <li>startEating=false：动画自然结束 → 服务端 setEating(false) + DrinkConsumeHandler.handle 消耗物品</li>
 * </ul>
 *
 * <p>动画中断（切物品/丢物品）场景由客户端在 reset() 后发送 startEating=false；
 * 服务端 handle 内部若找不到主手饮料会静默 return，不会误消耗。
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

    /** 动画自然结束 → 消耗物品 + 清状态（兼容旧调用） */
    public static void sendToServer(String drinkId) {
        sendToServer(drinkId, false);
    }

    /**
     * @param startEating true=动画开始，仅设置 ServerEatingState；false=动画结束，清状态并消耗物品
     */
    public static void sendToServer(String drinkId, boolean startEating) {
        CHANNEL.sendToServer(new ConsumeDrinkMessage(drinkId, startEating));
    }

    public static class ConsumeDrinkMessage {
        private final String drinkId;
        private final boolean startEating;

        public ConsumeDrinkMessage(String drinkId, boolean startEating) {
            this.drinkId = drinkId;
            this.startEating = startEating;
        }

        public ConsumeDrinkMessage(FriendlyByteBuf buf) {
            this.drinkId = buf.readUtf(64);
            this.startEating = buf.readBoolean();
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeUtf(drinkId, 64);
            buf.writeBoolean(startEating);
        }

        public static ConsumeDrinkMessage decode(FriendlyByteBuf buf) {
            return new ConsumeDrinkMessage(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                if (startEating) {
                    // 动画开始：标记正在吃/喝，SnackDisplayPlacement 会拒绝放置
                    ServerEatingState.setEating(player.getUUID(), true);
                } else {
                    // 动画结束：清状态 + 消耗物品（找不到则静默 return）
                    ServerEatingState.setEating(player.getUUID(), false);
                    DrinkConsumeHandler.handle(player, drinkId);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
