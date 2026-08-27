package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.neoforge.client.pack.PackDefinitionManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 材质包扩展动画触发 C2S 网络包（NeoForge 1.21.1）。
 *
 * <p>NeoForge 1.21.1 改用「客户端本地触发 + 服务端防刷」策略：
 * <ul>
 *   <li>客户端按 U 键 → {@link com.realisticdining.neoforge.client.pack.PackKeyRouter}
 *       本地调用 {@code triggerAnim} 触发 GeckoLib {@code eat} 动画</li>
 *   <li>同时发本包到服务端 → 服务端校验主手物品确实是材质包扩展物品后，
 *       设置 ServerEatingState.setEating(true) 防止玩家在动画期间放置零食展示等</li>
 * </ul>
 * 与 Forge 版的区别：Forge 版由服务端反向同步 triggerAnim 给所有客户端，1.21.1 改为
 * 本地触发（节省服务端→客户端再同步的延迟；多人游戏时其他玩家看不到，但第一人称视角自洽）。
 */
public class PackAnimationPacket {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                PackAnimationPayload.TYPE,
                PackAnimationPayload.STREAM_CODEC,
                PackAnimationPayload::handleDataOnMain
        );
    }

    public static void sendToServer() {
        PacketDistributor.sendToServer(new PackAnimationPayload());
    }

    public record PackAnimationPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<PackAnimationPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "pack_animation"));

        public static final StreamCodec<ByteBuf, PackAnimationPayload> STREAM_CODEC =
                StreamCodec.unit(new PackAnimationPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(PackAnimationPayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;
            // 校验主手物品确实是材质包扩展物品（防伪造）
            ItemStack stack = player.getMainHandItem();
            if (stack.isEmpty()) return;
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id == null || !PackDefinitionManager.containsItem(id.toString())) return;

            // 标记正在吃/喝，让 SnackDisplayPlaceHandler 等防无限刷逻辑生效
            ServerEatingState.setEating(player.getUUID(), true);
        }
    }
}
