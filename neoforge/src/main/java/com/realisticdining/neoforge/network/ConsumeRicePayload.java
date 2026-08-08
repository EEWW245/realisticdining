package com.realisticdining.neoforge.network;

import com.realisticdining.RealisticDining;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ConsumeRicePayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ConsumeRicePayload> TYPE = 
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "consume_rice"));

    public static final StreamCodec<ByteBuf, ConsumeRicePayload> STREAM_CODEC = 
        StreamCodec.unit(new ConsumeRicePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleDataOnMain(final ConsumeRicePayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ConsumeRicePacket.handleConsumeRice(context);
        });
    }
}
