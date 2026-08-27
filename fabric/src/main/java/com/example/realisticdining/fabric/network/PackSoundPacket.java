package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

/**
 * 材质包扩展物品音效播放 C2S 网络包（Fabric 1.20.1）。
 *
 * <p>动画 {@code sound_effects} 关键帧触发时，客户端根据定义文件的 {@code sounds}
 * 映射查找对应音效 ID，发本包到服务端，由服务端向所有玩家播放对应音效。
 *
 * <p>Fabric 1.20.1 与 Forge 差异：用 {@link BuiltInRegistries#SOUND_EVENT} 查 SoundEvent
 * 实例（替代 ForgeRegistries.SOUND_EVENTS）。
 */
public class PackSoundPacket {

    public static final ResourceLocation PACKET_ID =
            new ResourceLocation(RealisticDining.MOD_ID, "pack_sound");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (server, player, handler, buf, responseSender) -> {
            ResourceLocation soundId = buf.readResourceLocation();
            server.execute(() -> handleOnServer(player, soundId));
        });
    }

    public static void sendToServer(ResourceLocation soundId) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(soundId);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    private static void handleOnServer(ServerPlayer player, ResourceLocation soundId) {
        if (player == null) return;
        // 不查 BuiltInRegistries.SOUND_EVENT，直接用 createVariableRangeEvent 包装
        // ResourceLocation，让 sounds.json 定义的事件名（包括材质包扩展动态生成的）
        // 都能被播放，无需事先在 ModSounds 注册 SoundEvent。
        SoundEvent event = SoundEvent.createVariableRangeEvent(soundId);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                event, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
