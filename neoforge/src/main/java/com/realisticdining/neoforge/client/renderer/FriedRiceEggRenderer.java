package com.realisticdining.neoforge.client.renderer;

import com.realisticdining.blockentities.FriedRiceEggBlockEntity;
import com.realisticdining.blocks.FriedRiceEggBlock;
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

public class FriedRiceEggRenderer implements BlockEntityRenderer<FriedRiceEggBlockEntity> {
    private final ItemRenderer itemRenderer;

    public FriedRiceEggRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(@NotNull FriedRiceEggBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState state = blockEntity.getBlockState();
        int bites = state.getValue(FriedRiceEggBlock.BITES);

        ItemStack itemToRender = switch (bites) {
            case 0 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_0.get());
            case 1 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_1.get());
            case 2 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_2.get());
            case 3 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_3.get());
            case 4 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_4.get());
            case 5 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_5.get());
            case 6 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_6.get());
            case 7 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_7.get());
            case 8 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_8.get());
            case 9 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_9.get());
            case 10 -> new ItemStack(ModItems.FRIED_RICE_EGG_MODEL_10.get());
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
