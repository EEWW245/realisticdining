package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.forge.client.pack.PackDefinitionManager;
import com.example.realisticdining.forge.client.pack.PackEmpty;
import com.example.realisticdining.forge.client.pack.PackItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.function.Supplier;

/**
 * 材质包扩展动画触发 C2S 网络包（Forge 1.20.1）。
 *
 * <p>客户端按 U 键且主手物品在材质包扩展列表内时发送；服务端接收后调用
 * {@code triggerAnim} 在客户端侧播放 {@code eat} 动画（GeckoLib 同步触发）。
 */
public class PackAnimationPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "pack_animation"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, PackAnimationPacket.class,
                (packet, buf) -> {},
                buf -> new PackAnimationPacket(),
                PackAnimationPacket::handle);
    }

    public static void sendToServer() {
        CHANNEL.sendToServer(new PackAnimationPacket());
    }

    public static void handle(PackAnimationPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            // 校验主手物品确实是材质包扩展物品（防伪造）
            net.minecraft.world.item.ItemStack stack = player.getMainHandItem();
            if (stack.isEmpty()) return;
            ResourceLocation id = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (id == null || !PackDefinitionManager.containsItem(id.toString())) return;

            // 触发 GeckoLib 同步动画
            PackEmpty empty = PackItems.EMPTY_ITEM;
            if (empty == null) {
                com.example.realisticdining.RealisticDining.LOGGER.warn("[材质包扩展调试] 服务端 EMPTY_ITEM 为 null，触发包被丢弃");
                return;
            }
            long geoId = GeoItem.getOrAssignId(empty.getRenderStack(), player.serverLevel());
            com.example.realisticdining.RealisticDining.LOGGER.info("[材质包扩展调试] 服务端收到触发包，物品 {}，geoId={}，发送 triggerAnim", id, geoId);
            empty.triggerAnim(player, geoId, "eat", "eat");
        });
        context.setPacketHandled(true);
    }
}
