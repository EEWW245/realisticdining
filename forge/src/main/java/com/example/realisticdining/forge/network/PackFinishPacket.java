package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * 材质包扩展物品消耗 C2S 网络包（Forge 1.20.1）。
 *
 * <p>动画结尾的 {@code timeline: "finished;"} 自定义指令触发：
 * <ul>
 *   <li>客户端 {@link com.example.realisticdining.forge.client.pack.PackEmpty#registerControllers}
 *       的 {@code setCustomInstructionKeyframeHandler} 收到 {@code finished}</li>
 *   <li>发本包到服务端 → 服务端调用 {@code finishUsingItem} 真正消耗主手物品</li>
 * </ul>
 *
 * <p>同时清服务端 ServerEatingState（如果未来要接入防刷校验，状态需要复位）。
 */
public class PackFinishPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "pack_finish"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, PackFinishMessage.class,
                PackFinishMessage::encode,
                PackFinishMessage::decode,
                PackFinishMessage::handle);
    }

    public static void sendToServer(InteractionHand hand) {
        CHANNEL.sendToServer(new PackFinishMessage(hand));
    }

    public static class PackFinishMessage {
        private final InteractionHand hand;

        public PackFinishMessage(InteractionHand hand) {
            this.hand = hand;
        }

        public PackFinishMessage(FriendlyByteBuf buf) {
            this.hand = buf.readEnum(InteractionHand.class);
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeEnum(hand);
        }

        public static PackFinishMessage decode(FriendlyByteBuf buf) {
            return new PackFinishMessage(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                ItemStack stack = player.getItemInHand(hand);
                com.example.realisticdining.RealisticDining.LOGGER.info("[材质包扩展调试] 服务端收到消耗包，主手物品 {}，useDuration={}",
                        stack.getItem(), stack.getUseDuration());
                if (stack.isEmpty()) {
                    ServerEatingState.setEating(player.getUUID(), false);
                    return;
                }
                ItemStack result;
                if (stack.getItem() instanceof MilkBucketItem) {
                    // 牛奶桶：绕过原版 use 状态机，手动消耗并返还空铁桶。
                    // 原因：按 U 键触发动画从未调用 startUsingItem()，直接 finishUsingItem
                    // 会导致牛奶桶返还空桶逻辑状态不完整，出现"喝两次才消耗"的 bug。
                    stack.shrink(1);
                    result = new ItemStack(Items.BUCKET);
                } else if (stack.getUseDuration() > 0) {
                    // 可使用物品（食物等）：走原版 finishUsingItem
                    result = stack.finishUsingItem(player.level(), player);
                } else {
                    // 非可使用物品（定义文件绑定的任意物品）：手动减 1
                    stack.shrink(1);
                    result = stack;
                }
                player.setItemInHand(hand, result);
                ServerEatingState.setEating(player.getUUID(), false);
            });
            context.setPacketHandled(true);
        }
    }
}
