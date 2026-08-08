package com.realisticdining.fabric.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ConsumeRicePacket {

    public static final ResourceLocation CONSUME_PACKET_ID = ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "consume_rice");
    public static final ResourceLocation START_EATING_PACKET_ID = ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "start_eating");
    public static final ResourceLocation APPLY_SATURATION_PACKET_ID = ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "apply_saturation");

    private static final int FOOD_PER_BITE = 1;
    private static final float SATURATION_PER_BITE = 0.2f;

    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(ConsumeRicePayload.TYPE, ConsumeRicePayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(StartEatingPayload.TYPE, StartEatingPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ApplySaturationPayload.TYPE, ApplySaturationPayload.STREAM_CODEC);
        
        ServerPlayNetworking.registerGlobalReceiver(ConsumeRicePayload.TYPE, 
            (payload, context) -> {
                ServerPlayer player = context.player();
                ItemStack offhandItem = player.getOffhandItem();
                if (KaleidoscopeCookeryCompat.isCookedRice(offhandItem)) {
                    offhandItem.shrink(1);
                    if (offhandItem.isEmpty()) {
                        player.getInventory().offhand.set(0, ItemStack.EMPTY);
                    }
                    
                    ItemStack bowl = new ItemStack(Items.BOWL);
                    if (!player.getInventory().add(bowl)) {
                        player.drop(bowl, false);
                    }
                }
                ServerEatingState.reset(player.getUUID());
            }
        );
        
        ServerPlayNetworking.registerGlobalReceiver(StartEatingPayload.TYPE,
            (payload, context) -> {
                ServerPlayer player = context.player();
                ServerEatingState.setEating(player.getUUID(), true);
            }
        );

        ServerPlayNetworking.registerGlobalReceiver(ApplySaturationPayload.TYPE,
            (payload, context) -> {
                ServerPlayer player = context.player();
                // 每口仅恢复 1 点饱食度 + 0.2 饱和度。
                // 不施加 MobEffects.SATURATION（该效果每 tick 自动填饱食度，导致一口就满）。
                player.getFoodData().eat(FOOD_PER_BITE, SATURATION_PER_BITE);
            }
        );
    }

    public static void sendConsumeToServer() {
        ClientPlayNetworking.send(new ConsumeRicePayload());
    }

    public static void sendStartEatingToServer() {
        ClientPlayNetworking.send(new StartEatingPayload());
    }

    public static void sendApplySaturationToServer() {
        ClientPlayNetworking.send(new ApplySaturationPayload());
    }

    public record ConsumeRicePayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ConsumeRicePayload> TYPE = 
            new CustomPacketPayload.Type<>(CONSUME_PACKET_ID);

        public static final StreamCodec<ByteBuf, ConsumeRicePayload> STREAM_CODEC = 
            StreamCodec.unit(new ConsumeRicePayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record StartEatingPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<StartEatingPayload> TYPE = 
            new CustomPacketPayload.Type<>(START_EATING_PACKET_ID);

        public static final StreamCodec<ByteBuf, StartEatingPayload> STREAM_CODEC = 
            StreamCodec.unit(new StartEatingPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record ApplySaturationPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ApplySaturationPayload> TYPE = 
            new CustomPacketPayload.Type<>(APPLY_SATURATION_PACKET_ID);

        public static final StreamCodec<ByteBuf, ApplySaturationPayload> STREAM_CODEC = 
            StreamCodec.unit(new ApplySaturationPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
