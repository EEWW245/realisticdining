package com.realisticdining.fabric.client.renderer;

import com.realisticdining.blockentities.TomatoPoachedEggBlockEntity;
import com.realisticdining.blocks.TomatoPoachedEggBlock;
import com.realisticdining.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TomatoPoachedEggRenderer implements BlockEntityRenderer<TomatoPoachedEggBlockEntity> {
    private final ItemRenderer itemRenderer;

    public TomatoPoachedEggRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(@NotNull TomatoPoachedEggBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState state = blockEntity.getBlockState();
        int picks = state.getValue(TomatoPoachedEggBlock.PICKS);

        ItemStack itemToRender = switch (picks) {
            case 0 -> new ItemStack(ModItems.TOMATO_POACHED_EGG_MODEL_0.get());
            case 1 -> new ItemStack(ModItems.TOMATO_POACHED_EGG_MODEL_1.get());
            case 2 -> new ItemStack(ModItems.TOMATO_POACHED_EGG_MODEL_2.get());
            case 3 -> new ItemStack(ModItems.TOMATO_POACHED_EGG_MODEL_3.get());
            default -> ItemStack.EMPTY;
        };

        if (itemToRender.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        itemRenderer.renderStatic(
                itemToRender,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                blockEntity.getLevel(),
                0
        );

        poseStack.popPose();
    }
}
