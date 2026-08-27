package com.realisticdining.fabric.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.fabric.client.pack.PackDefinitionManager;
import com.realisticdining.fabric.client.pack.PackEmpty;
import com.realisticdining.fabric.client.pack.PackItems;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;

/**
 * 材质包扩展动画触发 C2S 网络包（Fabric 1.21.1）。
 *
 * <p>Fabric 1.21.1 采用「客户端本地触发 + 服务端防刷」策略：
 * <ul>
 *   <li>客户端按 U 键 → {@link com.realisticdining.fabric.client.pack.PackKeyRouter}
 *       本地调用 {@code triggerAnim} 触发 GeckoLib {@code eat} 动画</li>
 *   <li>同时发本包到服务端 → 服务端校验主手物品确实是材质包扩展物品后，
 *       设置 ServerEatingState.setEating(true) 防止玩家在动画期间放置零食展示等</li>
 * </ul>
 * 动画本地触发节省服务端→客户端再同步的延迟；多人游戏时其他玩家看不到，但第一人称视角自洽。
 */
public class PackAnimationPacket {

    public static final ResourceLocation PACK_ANIMATION_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "pack_animation");

    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(PackAnimationPayload.TYPE, PackAnimationPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(PackAnimationPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    if (player == null) return;
                    context.player().getServer().execute(() -> {
                        // 校验主手物品确实是材质包扩展物品（防伪造）
                        ItemStack stack = player.getMainHandItem();
                        if (stack.isEmpty()) return;
                        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
                        if (id == null || !PackDefinitionManager.containsItem(id.toString())) return;

                        // 标记正在吃/喝，让 SnackDisplayPlaceHandler 等防无限刷逻辑生效
                        ServerEatingState.setEating(player.getUUID(), true);

                        // 触发 GeckoLib 同步动画（与 1.20.1 一致：服务端 triggerAnim → 客户端播放）
                        PackEmpty empty = PackItems.EMPTY_ITEM;
                        if (empty == null) return;
                        long geoId = GeoItem.getOrAssignId(empty.getRenderStack(), player.serverLevel());
                        empty.triggerAnim(player, geoId, "eat", "eat");
                    });
                }
        );
    }

    public static void sendToServer() {
        ClientPlayNetworking.send(new PackAnimationPayload());
    }

    public record PackAnimationPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<PackAnimationPayload> TYPE =
                new CustomPacketPayload.Type<>(PACK_ANIMATION_PACKET_ID);

        public static final StreamCodec<ByteBuf, PackAnimationPayload> STREAM_CODEC =
                StreamCodec.unit(new PackAnimationPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
