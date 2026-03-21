package com.example.realisticdining.client.renderer;

import com.example.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.example.realisticdining.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChoppingBoardRenderer implements BlockEntityRenderer<ChoppingBoardBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ChoppingBoardRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(@NotNull ChoppingBoardBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack itemToRender = ItemStack.EMPTY;
        int cutStage = blockEntity.getCutStage();

        // 1. 判断要渲染的是猪肉还是大白菜还是葱还是鸡肉还是红辣椒
        if (blockEntity.hasMeat()) {
            itemToRender = getPorkStageItem(cutStage);
        } else if (blockEntity.hasNapaCabbage()) {
            itemToRender = blockEntity.getCabbageStageItem(cutStage);
        } else if (blockEntity.hasGreenOnion()) {
            itemToRender = blockEntity.getGreenOnionStageItem(cutStage);
        } else if (blockEntity.hasChicken()) {
            itemToRender = blockEntity.getChickenStageItem(cutStage);
        } else if (blockEntity.hasRedPepper()) {
            itemToRender = blockEntity.getRedPepperStageItem(cutStage);
        }

        // 2. 如果菜板上什么都没有，直接结束
        if (itemToRender.isEmpty()) {
            return;
        }

        // 3. 开始渲染模型
        poseStack.pushPose();

        // 将渲染中心点移至方块中央，配合 JSON 里的 fixed 使用
        poseStack.translate(0.5, 0.5, 0.5);

        itemRenderer.renderStatic(
                itemToRender,
                ItemDisplayContext.FIXED, // 使用 fixed 视角锁定坐标
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                blockEntity.getLevel(),
                0
        );

        poseStack.popPose();
    }

    // 猪肉的阶段获取方法保留原样
    private ItemStack getPorkStageItem(int cutStage) {
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.PORK_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.PORK_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.PORK_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.PORK_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.PORK_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.PORK_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.PORK_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.PORK_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.PORK_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.PORK_STAGE_9.get());
            case 10 -> new ItemStack(ModItems.PORK_STAGE_10.get());
            case 11 -> new ItemStack(ModItems.PORK_STAGE_11.get());
            case 12 -> new ItemStack(ModItems.PORK_STAGE_12.get());
            case 13 -> new ItemStack(ModItems.PORK_STAGE_13.get());
            case 14 -> new ItemStack(ModItems.PORK_STAGE_14.get());
            case 15 -> new ItemStack(ModItems.PORK_STAGE_15.get());
            case 16 -> new ItemStack(ModItems.PORK_STAGE_16.get());
            case 17 -> new ItemStack(ModItems.PORK_STAGE_17.get());
            case 18 -> new ItemStack(ModItems.PORK_STAGE_18.get());
            case 19 -> new ItemStack(ModItems.PORK_STAGE_19.get());
            case 20 -> new ItemStack(ModItems.PORK_STAGE_20.get());
            case 21 -> new ItemStack(ModItems.PORK_STAGE_21.get());
            case 22 -> new ItemStack(ModItems.PORK_STAGE_22.get());
            case 23 -> new ItemStack(ModItems.PORK_STAGE_23.get());
            case 24 -> new ItemStack(ModItems.PORK_STAGE_24.get());
            case 25 -> new ItemStack(ModItems.PORK_STAGE_25.get());
            default -> new ItemStack(ModItems.PORK_STAGE_0.get());
        };
    }
}