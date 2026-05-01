package com.realisticdining.neoforge.client.renderer;

import com.realisticdining.blockentities.ChoppingBoardBlockEntity;
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

        if (blockEntity.hasMeat()) {
            itemToRender = getPorkStageItem(cutStage);
        } else if (blockEntity.hasNapaCabbage()) {
            itemToRender = getNapaCabbageStageItem(cutStage);
        } else if (blockEntity.hasGreenOnion()) {
            itemToRender = getGreenOnionStageItem(cutStage);
        } else if (blockEntity.hasChicken()) {
            itemToRender = getChickenStageItem(cutStage);
        } else if (blockEntity.hasRedPepper()) {
            itemToRender = getRedPepperStageItem(cutStage);
        } else if (blockEntity.hasGreenChili()) {
            itemToRender = getGreenChiliStageItem(cutStage);
        } else if (blockEntity.hasCoriander()) {
            itemToRender = getCorianderStageItem(cutStage);
        } else if (blockEntity.hasYellowSteak()) {
            itemToRender = getYellowSteakStageItem(cutStage);
        } else if (blockEntity.hasTomato()) {
            itemToRender = blockEntity.getTomatoStageItem(cutStage);
        }

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

    private ItemStack getPorkStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 25) {
            return new ItemStack(ModItems.PORK_STAGE_0.get());
        }
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

    private ItemStack getNapaCabbageStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 70) {
            return new ItemStack(ModItems.NAPA_CABBAGE_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_9.get());
            case 10 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_10.get());
            case 11 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_11.get());
            case 12 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_12.get());
            case 13 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_13.get());
            case 14 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_14.get());
            case 15 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_15.get());
            case 16 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_16.get());
            case 17 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_17.get());
            case 18 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_18.get());
            case 19 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_19.get());
            case 20 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_20.get());
            case 21 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_21.get());
            case 22 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_22.get());
            case 23 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_23.get());
            case 24 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_24.get());
            case 25 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_25.get());
            case 26 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_26.get());
            case 27 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_27.get());
            case 28 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_28.get());
            case 29 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_29.get());
            case 30 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_30.get());
            case 31 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_31.get());
            case 32 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_32.get());
            case 33 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_33.get());
            case 34 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_34.get());
            case 35 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_35.get());
            case 36 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_36.get());
            case 37 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_37.get());
            case 38 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_38.get());
            case 39 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_39.get());
            case 40 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_40.get());
            case 41 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_41.get());
            case 42 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_42.get());
            case 43 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_43.get());
            case 44 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_44.get());
            case 45 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_45.get());
            case 46 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_46.get());
            case 47 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_47.get());
            case 48 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_48.get());
            case 49 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_49.get());
            case 50 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_50.get());
            case 51 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_51.get());
            case 52 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_52.get());
            case 53 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_53.get());
            case 54 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_54.get());
            case 55 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_55.get());
            case 56 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_56.get());
            case 57 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_57.get());
            case 58 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_58.get());
            case 59 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_59.get());
            case 60 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_60.get());
            case 61 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_61.get());
            case 62 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_62.get());
            case 63 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_63.get());
            case 64 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_64.get());
            case 65 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_65.get());
            case 66 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_66.get());
            case 67 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_67.get());
            case 68 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_68.get());
            case 69 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_69.get());
            case 70 -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_70.get());
            default -> new ItemStack(ModItems.NAPA_CABBAGE_STAGE_0.get());
        };
    }

    private ItemStack getGreenOnionStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 12) {
            return new ItemStack(ModItems.GREEN_ONION_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.GREEN_ONION_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.GREEN_ONION_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.GREEN_ONION_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.GREEN_ONION_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.GREEN_ONION_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.GREEN_ONION_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.GREEN_ONION_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.GREEN_ONION_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.GREEN_ONION_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.GREEN_ONION_STAGE_9.get());
            case 10 -> new ItemStack(ModItems.GREEN_ONION_STAGE_10.get());
            case 11 -> new ItemStack(ModItems.GREEN_ONION_STAGE_11.get());
            case 12 -> new ItemStack(ModItems.GREEN_ONION_STAGE_12.get());
            default -> new ItemStack(ModItems.GREEN_ONION_STAGE_0.get());
        };
    }

    private ItemStack getChickenStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 20) {
            return new ItemStack(ModItems.CHICKEN_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.CHICKEN_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.CHICKEN_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.CHICKEN_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.CHICKEN_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.CHICKEN_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.CHICKEN_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.CHICKEN_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.CHICKEN_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.CHICKEN_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.CHICKEN_STAGE_9.get());
            case 10 -> new ItemStack(ModItems.CHICKEN_STAGE_10.get());
            case 11 -> new ItemStack(ModItems.CHICKEN_STAGE_11.get());
            case 12 -> new ItemStack(ModItems.CHICKEN_STAGE_12.get());
            case 13 -> new ItemStack(ModItems.CHICKEN_STAGE_13.get());
            case 14 -> new ItemStack(ModItems.CHICKEN_STAGE_14.get());
            case 15 -> new ItemStack(ModItems.CHICKEN_STAGE_15.get());
            case 16 -> new ItemStack(ModItems.CHICKEN_STAGE_16.get());
            case 17 -> new ItemStack(ModItems.CHICKEN_STAGE_17.get());
            case 18 -> new ItemStack(ModItems.CHICKEN_STAGE_18.get());
            case 19 -> new ItemStack(ModItems.CHICKEN_STAGE_19.get());
            case 20 -> new ItemStack(ModItems.CHICKEN_STAGE_20.get());
            default -> new ItemStack(ModItems.CHICKEN_STAGE_0.get());
        };
    }

    private ItemStack getRedPepperStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 8) {
            return new ItemStack(ModItems.RED_PEPPER_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.RED_PEPPER_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.RED_PEPPER_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.RED_PEPPER_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.RED_PEPPER_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.RED_PEPPER_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.RED_PEPPER_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.RED_PEPPER_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.RED_PEPPER_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.RED_PEPPER_STAGE_8.get());
            default -> new ItemStack(ModItems.RED_PEPPER_STAGE_0.get());
        };
    }

    private ItemStack getGreenChiliStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 8) {
            return new ItemStack(ModItems.GREEN_CHILI_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.GREEN_CHILI_STAGE_8.get());
            default -> new ItemStack(ModItems.GREEN_CHILI_STAGE_0.get());
        };
    }

    private ItemStack getCorianderStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 7) {
            return new ItemStack(ModItems.CORIANDER_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.CORIANDER_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.CORIANDER_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.CORIANDER_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.CORIANDER_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.CORIANDER_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.CORIANDER_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.CORIANDER_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.CORIANDER_STAGE_7.get());
            default -> new ItemStack(ModItems.CORIANDER_STAGE_0.get());
        };
    }

    private ItemStack getYellowSteakStageItem(int cutStage) {
        if (cutStage < 0 || cutStage > 9) {
            return new ItemStack(ModItems.YELLOW_STEAK_STAGE_0.get());
        }
        return switch (cutStage) {
            case 0 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_0.get());
            case 1 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_1.get());
            case 2 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_2.get());
            case 3 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_3.get());
            case 4 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_4.get());
            case 5 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_5.get());
            case 6 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_6.get());
            case 7 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_7.get());
            case 8 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_8.get());
            case 9 -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_9.get());
            default -> new ItemStack(ModItems.YELLOW_STEAK_STAGE_0.get());
        };
    }
}
