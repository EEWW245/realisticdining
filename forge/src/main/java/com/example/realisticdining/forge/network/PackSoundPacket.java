package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * 材质包扩展物品音效播放 C2S 网络包（Forge 1.20.1）。
 *
 * <p>动画 {@code sound_effects} 关键帧触发时，客户端根据定义文件的 {@code sounds}
 * 映射查找对应音效 ID，发本包到服务端，由服务端向所有玩家播放对应音效。
 */
public class PackSoundPacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "pack_sound"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, PackSoundMessage.class,
                PackSoundMessage::encode,
                PackSoundMessage::decode,
                PackSoundMessage::handle);
    }

    public static void sendToServer(ResourceLocation soundId) {
        CHANNEL.sendToServer(new PackSoundMessage(soundId));
    }

    public static class PackSoundMessage {
        private final ResourceLocation soundId;

        public PackSoundMessage(ResourceLocation soundId) {
            this.soundId = soundId;
        }

        public PackSoundMessage(FriendlyByteBuf buf) {
            this.soundId = buf.readResourceLocation();
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeResourceLocation(soundId);
        }

        public static PackSoundMessage decode(FriendlyByteBuf buf) {
            return new PackSoundMessage(buf);
        }

        public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                // 不查 ForgeRegistries.SOUND_EVENTS，直接用 createVariableRangeEvent 包装
                // ResourceLocation，让 sounds.json 定义的事件名（包括材质包扩展动态生成的）
                // 都能被播放，无需事先在 ModSounds 注册 SoundEvent。
                SoundEvent event = SoundEvent.createVariableRangeEvent(soundId);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        event, SoundSource.PLAYERS, 1.0F, 1.0F);
            });
            context.setPacketHandled(true);
        }
    }
}
