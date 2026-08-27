package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.common.DrinkConsumeHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * 饮料/零食消耗 C2S 网络包（Fabric 1.20.1）
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

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "consume_drink");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            String drinkId = buf.readUtf(64);
            boolean startEating = buf.readBoolean();
            server.execute(() -> {
                if (startEating) {
                    // 动画开始：标记正在吃/喝，SnackDisplayPlacement 会拒绝放置
                    ServerEatingState.setEating(player.getUUID(), true);
                } else {
                    // 动画结束：清状态 + 消耗物品（找不到则静默 return）
                    ServerEatingState.setEating(player.getUUID(), false);
                    DrinkConsumeHandler.handle(player, drinkId);
                }
            });
        });
    }

    /** 动画自然结束 → 消耗物品 + 清状态（兼容旧调用） */
    public static void sendToServer(String drinkId) {
        sendToServer(drinkId, false);
    }

    /**
     * @param startEating true=动画开始，仅设置 ServerEatingState；false=动画结束，清状态并消耗物品
     */
    public static void sendToServer(String drinkId, boolean startEating) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(drinkId, 64);
        buf.writeBoolean(startEating);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }
}
