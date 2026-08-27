package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.fabric.client.pack.PackDefinitionManager;
import com.example.realisticdining.fabric.client.pack.PackEmpty;
import com.example.realisticdining.fabric.client.pack.PackItems;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import software.bernie.geckolib.animatable.GeoItem;

/**
 * 材质包扩展动画触发 C2S 网络包（Fabric 1.20.1）。
 *
 * <p>客户端按 U 键且主手物品在材质包扩展列表内时发送；服务端接收后调用
 * {@code triggerAnim} 在客户端侧播放 {@code eat} 动画（GeckoLib 同步触发）。
 *
 * <p>Fabric 1.20.1 与 Forge 差异：
 * <ul>
 *   <li>无 SimpleChannel，用 {@link ServerPlayNetworking#registerGlobalReceiver} 注册服务端接收器</li>
 *   <li>无 pack payload，发送空 buf 即可</li>
 *   <li>客户端快捷栏锁定（防刷）已移至 {@link com.example.realisticdining.fabric.client.pack.PackKeyRouter}
 *       客户端侧调用，避免服务端 handler 引用 client-only Minecraft 类</li>
 * </ul>
 */
public class PackAnimationPacket {

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "pack_animation");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            // 无 payload，直接在主线程处理
            server.execute(() -> handleOnServer(player));
        });
    }

    public static void sendToServer() {
        ClientPlayNetworking.send(PACKET_ID, new FriendlyByteBuf(Unpooled.buffer()));
    }

    private static void handleOnServer(ServerPlayer player) {
        if (player == null) return;
        // 校验主手物品确实是材质包扩展物品（防伪造）
        net.minecraft.world.item.ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null || !PackDefinitionManager.containsItem(id.toString())) return;

        // 触发 GeckoLib 同步动画
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null) return;
        long geoId = GeoItem.getOrAssignId(empty.getRenderStack(), player.serverLevel());
        empty.triggerAnim(player, geoId, "eat", "eat");
    }
}
