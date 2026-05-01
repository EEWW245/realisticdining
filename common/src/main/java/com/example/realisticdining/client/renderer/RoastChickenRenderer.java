package com.example.realisticdining.client.renderer;

import com.example.realisticdining.blockentities.RoastChickenBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class RoastChickenRenderer implements BlockEntityRenderer<RoastChickenBlockEntity> {
    
    public RoastChickenRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull RoastChickenBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }
}
