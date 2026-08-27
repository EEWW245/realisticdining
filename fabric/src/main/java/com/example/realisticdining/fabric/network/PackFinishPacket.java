package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;

/**
 * 材质包扩展物品消耗 C2S 网络包（Fabric 1.20.1）。
 *
 * <p>动画结尾的 {@code timeline: "finished;"} 自定义指令触发：
 * <ul>
 *   <li>客户端 {@link com.example.realisticdining.fabric.client.pack.PackEmpty#registerControllers}
 *       的 {@code setCustomInstructionKeyframeHandler} 收到 {@code finished}</li>
 *   <li>发本包到服务端 → 服务端调用 {@code finishUsingItem} 真正消耗主手物品</li>
 * </ul>
 *
 * <p>同时清服务端 ServerEatingState（如果未来要接入防刷校验，状态需要复位）。
 */
public class PackFinishPacket {

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "pack_finish");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            InteractionHand hand = buf.readEnum(InteractionHand.class);
            server.execute(() -> handleOnServer(player, hand));
        });
    }

    public static void sendToServer(InteractionHand hand) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeEnum(hand);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    private static void handleOnServer(ServerPlayer player, InteractionHand hand) {
        if (player == null) return;
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            ServerEatingState.setEating(player.getUUID(), false);
            return;
        }
        ItemStack result;
        if (stack.getItem() instanceof MilkBucketItem) {
            // 牛奶桶：绕过原版 use 状态机，手动消耗并返还空铁桶（避免"喝两次才消耗"）
            stack.shrink(1);
            result = new ItemStack(Items.BUCKET);
        } else if (stack.isEdible() && stack.getUseDuration() > 0) {
            // 食物等可使用物品：走原版 finishUsingItem
            result = stack.finishUsingItem(player.level(), player);
        } else {
            // 非食物或不可使用：仍清状态，避免卡死，不消耗
            ServerEatingState.setEating(player.getUUID(), false);
            return;
        }
        player.setItemInHand(hand, result);
        ServerEatingState.setEating(player.getUUID(), false);
    }
}
