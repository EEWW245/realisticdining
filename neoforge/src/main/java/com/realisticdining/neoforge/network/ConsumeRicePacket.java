package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ConsumeRicePacket {

    private static final int FOOD_PER_BITE = 1;
    private static final float SATURATION_PER_BITE = 0.2f;

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playToServer(
            ConsumeRicePayload.TYPE,
            ConsumeRicePayload.STREAM_CODEC,
            ConsumeRicePayload::handleDataOnMain
        );
        
        registrar.playToServer(
            StartEatingPayload.TYPE,
            StartEatingPayload.STREAM_CODEC,
            StartEatingPayload::handleDataOnMain
        );

        registrar.playToServer(
            ApplySaturationPayload.TYPE,
            ApplySaturationPayload.STREAM_CODEC,
            ApplySaturationPayload::handleDataOnMain
        );
    }

    public static void sendConsumeToServer() {
        PacketDistributor.sendToServer(new ConsumeRicePayload());
    }

    public static void sendStartEatingToServer() {
        PacketDistributor.sendToServer(new StartEatingPayload());
    }

    public static void sendApplySaturationToServer() {
        PacketDistributor.sendToServer(new ApplySaturationPayload());
    }

    public static void handleConsumeRice(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
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

    public static void handleStartEating(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        ServerEatingState.setEating(player.getUUID(), true);
    }

    public static void handleApplySaturation(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        // 每口仅恢复 1 点饱食度 + 0.2 饱和度。
        // 不施加 MobEffects.SATURATION（该效果每 tick 自动填饱食度，导致一口就满）。
        player.getFoodData().eat(FOOD_PER_BITE, SATURATION_PER_BITE);
    }

    public record ConsumeRicePayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ConsumeRicePayload> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "consume_rice"));

        public static final StreamCodec<ByteBuf, ConsumeRicePayload> STREAM_CODEC = 
            StreamCodec.unit(new ConsumeRicePayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(ConsumeRicePayload payload, IPayloadContext context) {
            handleConsumeRice(context);
        }
    }

    public record StartEatingPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<StartEatingPayload> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "start_eating"));

        public static final StreamCodec<ByteBuf, StartEatingPayload> STREAM_CODEC = 
            StreamCodec.unit(new StartEatingPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(StartEatingPayload payload, IPayloadContext context) {
            handleStartEating(context);
        }
    }

    public record ApplySaturationPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ApplySaturationPayload> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "apply_saturation"));

        public static final StreamCodec<ByteBuf, ApplySaturationPayload> STREAM_CODEC = 
            StreamCodec.unit(new ApplySaturationPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handleDataOnMain(ApplySaturationPayload payload, IPayloadContext context) {
            handleApplySaturation(context);
        }
    }
}
