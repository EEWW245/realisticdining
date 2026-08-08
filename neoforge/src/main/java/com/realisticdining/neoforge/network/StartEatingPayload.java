package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StartEatingPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<StartEatingPayload> TYPE = 
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "start_eating"));

    public static final StreamCodec<ByteBuf, StartEatingPayload> STREAM_CODEC = 
        StreamCodec.unit(new StartEatingPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleDataOnMain(final StartEatingPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ConsumeRicePacket.handleStartEating(context);
        });
    }
}
