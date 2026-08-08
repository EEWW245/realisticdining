package com.example.realisticdining.forge.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ConsumeRicePacket {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RealisticDining.MOD_ID, "eat_rice"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static final int FOOD_PER_BITE = 1;
    private static final float SATURATION_PER_BITE = 0.2f;

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, ConsumeRiceMessage.class,
                ConsumeRiceMessage::encode,
                ConsumeRiceMessage::decode,
                ConsumeRiceMessage::handle);

        CHANNEL.registerMessage(nextId++, StartEatingMessage.class,
                StartEatingMessage::encode,
                StartEatingMessage::decode,
                StartEatingMessage::handle);

        CHANNEL.registerMessage(nextId++, ApplySaturationMessage.class,
                ApplySaturationMessage::encode,
                ApplySaturationMessage::decode,
                ApplySaturationMessage::handle);
    }

    public static void sendConsumeToServer() {
        CHANNEL.sendToServer(new ConsumeRiceMessage());
    }

    public static void sendStartEatingToServer() {
        CHANNEL.sendToServer(new StartEatingMessage());
    }

    public static void sendApplySaturationToServer() {
        CHANNEL.sendToServer(new ApplySaturationMessage());
    }

    public static class ConsumeRiceMessage {
        public ConsumeRiceMessage() {}

        public ConsumeRiceMessage(FriendlyByteBuf buf) {}

        public void encode(FriendlyByteBuf buf) {}

        public static ConsumeRiceMessage decode(FriendlyByteBuf buf) {
            return new ConsumeRiceMessage(buf);
        }

        public void handle(Supplier<net.minecraftforge.network.NetworkEvent.Context> contextSupplier) {
            net.minecraftforge.network.NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                ItemStack offhandItem = player.getOffhandItem();
                if (KaleidoscopeCompat.isCookedRice(offhandItem)) {
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
            });
            context.setPacketHandled(true);
        }
    }

    public static class StartEatingMessage {
        public StartEatingMessage() {}

        public StartEatingMessage(FriendlyByteBuf buf) {}

        public void encode(FriendlyByteBuf buf) {}

        public static StartEatingMessage decode(FriendlyByteBuf buf) {
            return new StartEatingMessage(buf);
        }

        public void handle(Supplier<net.minecraftforge.network.NetworkEvent.Context> contextSupplier) {
            net.minecraftforge.network.NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                ServerEatingState.setEating(player.getUUID(), true);
            });
            context.setPacketHandled(true);
        }
    }

    public static class ApplySaturationMessage {
        public ApplySaturationMessage() {}

        public ApplySaturationMessage(FriendlyByteBuf buf) {}

        public void encode(FriendlyByteBuf buf) {}

        public static ApplySaturationMessage decode(FriendlyByteBuf buf) {
            return new ApplySaturationMessage(buf);
        }

        public void handle(Supplier<net.minecraftforge.network.NetworkEvent.Context> contextSupplier) {
            net.minecraftforge.network.NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null) return;
                // 每口仅恢复 1 点饱食度 + 0.2 饱和度。
                // 不施加 MobEffects.SATURATION（该效果每 tick 自动填饱食度，导致一口就满）。
                player.getFoodData().eat(FOOD_PER_BITE, SATURATION_PER_BITE);
            });
            context.setPacketHandled(true);
        }
    }
}
