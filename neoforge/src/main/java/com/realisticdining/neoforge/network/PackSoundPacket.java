package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 材质包扩展物品音效播放 C2S 网络包（NeoForge 1.21.1）。
 *
 * <p>动画 {@code sound_effects} 关键帧触发时，客户端根据定义文件的 {@code sounds}
 * 映射查找对应音效 ID，发本包到服务端，由服务端向所有玩家播放对应音效。
 */
public class PackSoundPacket {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                PackSoundPayload.TYPE,
                PackSoundPayload.STREAM_CODEC,
                PackSoundPayload::handleDataOnMain
        );
    }

    public static void sendToServer(ResourceLocation soundId) {
        PacketDistributor.sendToServer(new PackSoundPayload(soundId));
    }

    public record PackSoundPayload(ResourceLocation soundId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<PackSoundPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "pack_sound"));

        public static final StreamCodec<ByteBuf, PackSoundPayload> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC,
                PackSoundPayload::soundId,
                PackSoundPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(PackSoundPayload payload, IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;
            // 不查 BuiltInRegistries.SOUND_EVENT，直接用 createVariableRangeEvent 包装
            // ResourceLocation，让 sounds.json 定义的事件名（包括材质包扩展动态生成的）
            // 都能被播放，无需事先在 ModSounds 注册 SoundEvent。
            SoundEvent event = SoundEvent.createVariableRangeEvent(payload.soundId());
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    event, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
