package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 材质包扩展物品消耗 C2S 网络包（NeoForge 1.21.1）。
 *
 * <p>动画结尾的 {@code timeline: "finished;"} 自定义指令触发：
 * <ul>
 *   <li>客户端 {@link com.realisticdining.neoforge.client.pack.PackEmpty#registerControllers}
 *       的 {@code setCustomInstructionKeyframeHandler} 收到 {@code finished}</li>
 *   <li>发本包到服务端 → 服务端调用 {@code finishUsingItem} 真正消耗主手物品</li>
 * </ul>
 *
 * <p>同时清服务端 ServerEatingState（如果未来要接入防刷校验，状态需要复位）。
 */
public class PackFinishPacket {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                PackFinishPayload.TYPE,
                PackFinishPayload.STREAM_CODEC,
                PackFinishPayload::handleDataOnMain
        );
    }

    public static void sendToServer(InteractionHand hand) {
        PacketDistributor.sendToServer(new PackFinishPayload(hand));
    }

    public record PackFinishPayload(InteractionHand hand) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<PackFinishPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "pack_finish"));

        // 用 boolean 编码 InteractionHand：true=MAIN_HAND, false=OFF_HAND
        // 直接实现 StreamCodec 接口，保守写法，不依赖 enumById/map 等 API
        public static final StreamCodec<ByteBuf, PackFinishPayload> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public PackFinishPayload decode(ByteBuf buf) {
                boolean mainHand = buf.readBoolean();
                return new PackFinishPayload(mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            }

            @Override
            public void encode(ByteBuf buf, PackFinishPayload payload) {
                buf.writeBoolean(payload.hand() == InteractionHand.MAIN_HAND);
            }
        };

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(PackFinishPayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;
            InteractionHand hand = payload.hand();
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
            } else {
                FoodProperties food = stack.get(DataComponents.FOOD);
                if (food == null) {
                    // 非食物：仍清状态，避免卡死
                    ServerEatingState.setEating(player.getUUID(), false);
                    return;
                }
                result = stack.finishUsingItem(player.level(), player);
            }
            player.setItemInHand(hand, result);
            ServerEatingState.setEating(player.getUUID(), false);
        }
    }
}
