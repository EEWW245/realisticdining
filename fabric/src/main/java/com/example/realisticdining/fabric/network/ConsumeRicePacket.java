package com.example.realisticdining.fabric.network;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ConsumeRicePacket {

    public static final ResourceLocation CONSUME_PACKET_ID = new ResourceLocation(RealisticDining.MOD_ID, "consume_rice");
    public static final ResourceLocation START_EATING_PACKET_ID = new ResourceLocation(RealisticDining.MOD_ID, "start_eating");
    public static final ResourceLocation APPLY_SATURATION_PACKET_ID = new ResourceLocation(RealisticDining.MOD_ID, "apply_saturation");

    private static final int FOOD_PER_BITE = 1;
    private static final float SATURATION_PER_BITE = 0.2f;

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(CONSUME_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
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
        });

        ServerPlayNetworking.registerGlobalReceiver(START_EATING_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerEatingState.setEating(player.getUUID(), true);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(APPLY_SATURATION_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // 每口仅恢复 1 点饱食度 + 0.2 饱和度。
                // 不施加 MobEffects.SATURATION（该效果每 tick 自动填饱食度，导致一口就满）。
                player.getFoodData().eat(FOOD_PER_BITE, SATURATION_PER_BITE);
            });
        });
    }

    public static void sendConsumeToServer() {
        ClientPlayNetworking.send(CONSUME_PACKET_ID, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public static void sendStartEatingToServer() {
        ClientPlayNetworking.send(START_EATING_PACKET_ID, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public static void sendApplySaturationToServer() {
        ClientPlayNetworking.send(APPLY_SATURATION_PACKET_ID, new FriendlyByteBuf(Unpooled.buffer()));
    }
}
