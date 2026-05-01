package com.realisticdining.neoforge.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.realisticdining.blockentities.WokBlockEntity;
import com.realisticdining.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WokRenderer implements BlockEntityRenderer<WokBlockEntity> {
    private final ItemRenderer itemRenderer;

    public WokRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(@NotNull WokBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (!blockEntity.hasOil()) {
            return;
        }

        if (!blockEntity.hasPork() && !blockEntity.hasChicken() && !blockEntity.hasRedPepper()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.4, 0.5);

        boolean isSpicyChicken = blockEntity.hasChicken() && blockEntity.hasRedPepper() && blockEntity.hasGreenOnion();

        if (blockEntity.isReadyToStir()) {
            long gameTime = blockEntity.getLevel().getGameTime();
            long timeSinceStir = gameTime - blockEntity.getLastStirTime();
            float animDuration = 10.0f;

            if (timeSinceStir < animDuration && blockEntity.getStirCount() > 0) {
                float progress = (timeSinceStir + partialTick) / animDuration;
                int variant = blockEntity.getCurrentStirVariant();

                ItemStack baseItem = isSpicyChicken ? 
                    getSpicyChickenBaseItem(variant, blockEntity.getStirCount()) : 
                    getWokBaseItem(variant, blockEntity.getStirCount());
                itemRenderer.renderStatic(baseItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);

                poseStack.pushPose();
                poseStack.translate(0, Math.sin(progress * Math.PI) * 0.3, 0);

                float spinAngle = progress * 360.0f;
                poseStack.translate(0, -0.34f, 0);

                int axisDecider = variant % 4;
                if (axisDecider == 0) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(spinAngle));
                } else if (axisDecider == 1) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-spinAngle));
                } else if (axisDecider == 2) {
                    poseStack.mulPose(Axis.ZP.rotationDegrees(spinAngle));
                } else {
                    poseStack.mulPose(Axis.ZP.rotationDegrees(-spinAngle));
                }

                poseStack.translate(0, 0.34f, 0);

                ItemStack flyItem = isSpicyChicken ? 
                    getSpicyChickenFlyItem(variant, blockEntity.getStirCount()) : 
                    getWokFlyItem(variant, blockEntity.getStirCount());
                itemRenderer.renderStatic(flyItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
                poseStack.popPose();

            } else {
                int stirCount = blockEntity.getStirCount();
                ItemStack idleItem;
                if (isSpicyChicken) {
                    if (stirCount >= 25) {
                        idleItem = new ItemStack(ModItems.WOK_CHICKEN_3_IDLE_STAGE3.get());
                    } else if (stirCount >= 19) {
                        idleItem = new ItemStack(ModItems.WOK_CHICKEN_3_IDLE_STAGE2.get());
                    } else {
                        idleItem = new ItemStack(ModItems.WOK_CHICKEN_3_IDLE.get());
                    }
                } else {
                    if (stirCount >= 25) {
                        idleItem = new ItemStack(ModItems.WOK_IDLE_STAGE3.get());
                    } else if (stirCount >= 19) {
                        idleItem = new ItemStack(ModItems.WOK_IDLE_STAGE2.get());
                    } else {
                        idleItem = new ItemStack(ModItems.WOK_IDLE.get());
                    }
                }
                itemRenderer.renderStatic(idleItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
            }

        } else {
            ItemStack meatOnlyItem;
            if (blockEntity.hasChicken() && blockEntity.hasRedPepper() && blockEntity.hasGreenOnion()) {
                meatOnlyItem = new ItemStack(ModItems.WOK_CHICKEN_ONLY_3.get());
            } else if (blockEntity.hasChicken() && blockEntity.hasRedPepper()) {
                meatOnlyItem = new ItemStack(ModItems.WOK_RED_PEPPER_ONLY.get());
            } else if (blockEntity.hasChicken()) {
                meatOnlyItem = new ItemStack(ModItems.WOK_CHICKEN_ONLY.get());
            } else {
                meatOnlyItem = new ItemStack(ModItems.WOK_PORK_ONLY.get());
            }
            itemRenderer.renderStatic(meatOnlyItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
        }

        poseStack.popPose();
    }

    private ItemStack getWokBaseItem(int variant, int stirCount) {
        if (stirCount >= 25) {
            return switch (variant) {
                case 1 -> new ItemStack(ModItems.WOK_BASE_STAGE3_1.get());
                case 2 -> new ItemStack(ModItems.WOK_BASE_STAGE3_2.get());
                case 3 -> new ItemStack(ModItems.WOK_BASE_STAGE3_3.get());
                case 4 -> new ItemStack(ModItems.WOK_BASE_STAGE3_4.get());
                case 5 -> new ItemStack(ModItems.WOK_BASE_STAGE3_5.get());
                case 6 -> new ItemStack(ModItems.WOK_BASE_STAGE3_6.get());
                case 7 -> new ItemStack(ModItems.WOK_BASE_STAGE3_7.get());
                case 8 -> new ItemStack(ModItems.WOK_BASE_STAGE3_8.get());
                default -> new ItemStack(ModItems.WOK_BASE_STAGE3_1.get());
            };
        } else if (stirCount >= 19) {
            return switch (variant) {
                case 1 -> new ItemStack(ModItems.WOK_BASE_STAGE2_1.get());
                case 2 -> new ItemStack(ModItems.WOK_BASE_STAGE2_2.get());
                case 3 -> new ItemStack(ModItems.WOK_BASE_STAGE2_3.get());
                case 4 -> new ItemStack(ModItems.WOK_BASE_STAGE2_4.get());
                case 5 -> new ItemStack(ModItems.WOK_BASE_STAGE2_5.get());
                case 6 -> new ItemStack(ModItems.WOK_BASE_STAGE2_6.get());
                case 7 -> new ItemStack(ModItems.WOK_BASE_STAGE2_7.get());
                case 8 -> new ItemStack(ModItems.WOK_BASE_STAGE2_8.get());
                default -> new ItemStack(ModItems.WOK_BASE_STAGE2_1.get());
            };
        }

        return switch (variant) {
            case 1 -> new ItemStack(ModItems.WOK_BASE_1.get());
            case 2 -> new ItemStack(ModItems.WOK_BASE_2.get());
            case 3 -> new ItemStack(ModItems.WOK_BASE_3.get());
            case 4 -> new ItemStack(ModItems.WOK_BASE_4.get());
            case 5 -> new ItemStack(ModItems.WOK_BASE_5.get());
            case 6 -> new ItemStack(ModItems.WOK_BASE_6.get());
            case 7 -> new ItemStack(ModItems.WOK_BASE_7.get());
            case 8 -> new ItemStack(ModItems.WOK_BASE_8.get());
            default -> new ItemStack(ModItems.WOK_BASE_1.get());
        };
    }

    private ItemStack getWokFlyItem(int variant, int stirCount) {
        if (stirCount >= 25) {
            return switch (variant) {
                case 1 -> new ItemStack(ModItems.WOK_FLY_STAGE3_1.get());
                case 2 -> new ItemStack(ModItems.WOK_FLY_STAGE3_2.get());
                case 3 -> new ItemStack(ModItems.WOK_FLY_STAGE3_3.get());
                case 4 -> new ItemStack(ModItems.WOK_FLY_STAGE3_4.get());
                case 5 -> new ItemStack(ModItems.WOK_FLY_STAGE3_5.get());
                case 6 -> new ItemStack(ModItems.WOK_FLY_STAGE3_6.get());
                case 7 -> new ItemStack(ModItems.WOK_FLY_STAGE3_7.get());
                case 8 -> new ItemStack(ModItems.WOK_FLY_STAGE3_8.get());
                default -> new ItemStack(ModItems.WOK_FLY_STAGE3_1.get());
            };
        } else if (stirCount >= 19) {
            return switch (variant) {
                case 1 -> new ItemStack(ModItems.WOK_FLY_STAGE2_1.get());
                case 2 -> new ItemStack(ModItems.WOK_FLY_STAGE2_2.get());
                case 3 -> new ItemStack(ModItems.WOK_FLY_STAGE2_3.get());
                case 4 -> new ItemStack(ModItems.WOK_FLY_STAGE2_4.get());
                case 5 -> new ItemStack(ModItems.WOK_FLY_STAGE2_5.get());
                case 6 -> new ItemStack(ModItems.WOK_FLY_STAGE2_6.get());
                case 7 -> new ItemStack(ModItems.WOK_FLY_STAGE2_7.get());
                case 8 -> new ItemStack(ModItems.WOK_FLY_STAGE2_8.get());
                default -> new ItemStack(ModItems.WOK_FLY_STAGE2_1.get());
            };
        }

        return switch (variant) {
            case 1 -> new ItemStack(ModItems.WOK_FLY_1.get());
            case 2 -> new ItemStack(ModItems.WOK_FLY_2.get());
            case 3 -> new ItemStack(ModItems.WOK_FLY_3.get());
            case 4 -> new ItemStack(ModItems.WOK_FLY_4.get());
            case 5 -> new ItemStack(ModItems.WOK_FLY_5.get());
            case 6 -> new ItemStack(ModItems.WOK_FLY_6.get());
            case 7 -> new ItemStack(ModItems.WOK_FLY_7.get());
            case 8 -> new ItemStack(ModItems.WOK_FLY_8.get());
            default -> new ItemStack(ModItems.WOK_FLY_1.get());
        };
    }

    private ItemStack getSpicyChickenBaseItem(int variant, int stirCount) {
        int v = ((variant - 1) % 30) + 1;
        
        if (stirCount >= 25) {
            return getSpicyChickenBaseStage3(v);
        } else if (stirCount >= 19) {
            return getSpicyChickenBaseStage2(v);
        }
        return getSpicyChickenBaseStage1(v);
    }

    private ItemStack getSpicyChickenFlyItem(int variant, int stirCount) {
        int v = ((variant - 1) % 30) + 1;
        
        if (stirCount >= 25) {
            return getSpicyChickenFlyStage3(v);
        }
        return getSpicyChickenFlyStage1(v);
    }

    private ItemStack getSpicyChickenBaseStage1(int v) {
        return switch (v) {
            case 1 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_1.get());
            case 2 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_2.get());
            case 3 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_3.get());
            case 4 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_4.get());
            case 5 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_5.get());
            case 6 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_6.get());
            case 7 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_7.get());
            case 8 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_8.get());
            case 9 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_9.get());
            case 10 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_10.get());
            case 11 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_11.get());
            case 12 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_12.get());
            case 13 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_13.get());
            case 14 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_14.get());
            case 15 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_15.get());
            case 16 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_16.get());
            case 17 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_17.get());
            case 18 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_18.get());
            case 19 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_19.get());
            case 20 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_20.get());
            case 21 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_21.get());
            case 22 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_22.get());
            case 23 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_23.get());
            case 24 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_24.get());
            case 25 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_25.get());
            case 26 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_26.get());
            case 27 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_27.get());
            case 28 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_28.get());
            case 29 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_29.get());
            case 30 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_30.get());
            default -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_1.get());
        };
    }

    private ItemStack getSpicyChickenBaseStage2(int v) {
        return switch (v) {
            case 1 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_1.get());
            case 2 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_2.get());
            case 3 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_3.get());
            case 4 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_4.get());
            case 5 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_5.get());
            case 6 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_6.get());
            case 7 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_7.get());
            case 8 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_8.get());
            case 9 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_9.get());
            case 10 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_10.get());
            case 11 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_11.get());
            case 12 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_12.get());
            case 13 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_13.get());
            case 14 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_14.get());
            case 15 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_15.get());
            case 16 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_16.get());
            case 17 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_17.get());
            case 18 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_18.get());
            case 19 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_19.get());
            case 20 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_20.get());
            case 21 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_21.get());
            case 22 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_22.get());
            case 23 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_23.get());
            case 24 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_24.get());
            case 25 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_25.get());
            case 26 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_26.get());
            case 27 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_27.get());
            case 28 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_28.get());
            case 29 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_29.get());
            case 30 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_30.get());
            default -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE2_1.get());
        };
    }

    private ItemStack getSpicyChickenBaseStage3(int v) {
        return switch (v) {
            case 1 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_1.get());
            case 2 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_2.get());
            case 3 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_3.get());
            case 4 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_4.get());
            case 5 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_5.get());
            case 6 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_6.get());
            case 7 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_7.get());
            case 8 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_8.get());
            case 9 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_9.get());
            case 10 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_10.get());
            case 11 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_11.get());
            case 12 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_12.get());
            case 13 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_13.get());
            case 14 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_14.get());
            case 15 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_15.get());
            case 16 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_16.get());
            case 17 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_17.get());
            case 18 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_18.get());
            case 19 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_19.get());
            case 20 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_20.get());
            case 21 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_21.get());
            case 22 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_22.get());
            case 23 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_23.get());
            case 24 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_24.get());
            case 25 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_25.get());
            case 26 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_26.get());
            case 27 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_27.get());
            case 28 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_28.get());
            case 29 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_29.get());
            case 30 -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_30.get());
            default -> new ItemStack(ModItems.WOK_CHICKEN_3_BASE_STAGE3_1.get());
        };
    }

    private ItemStack getSpicyChickenFlyStage1(int v) {
        return switch (v) {
            case 1 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_1.get());
            case 2 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_2.get());
            case 3 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_3.get());
            case 4 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_4.get());
            case 5 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_5.get());
            case 6 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_6.get());
            case 7 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_7.get());
            case 8 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_8.get());
            case 9 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_9.get());
            case 10 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_10.get());
            case 11 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_11.get());
            case 12 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_12.get());
            case 13 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_13.get());
            case 14 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_14.get());
            case 15 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_15.get());
            case 16 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_16.get());
            case 17 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_17.get());
            case 18 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_18.get());
            case 19 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_19.get());
            case 20 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_20.get());
            case 21 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_21.get());
            case 22 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_22.get());
            case 23 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_23.get());
            case 24 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_24.get());
            case 25 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_25.get());
            case 26 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_26.get());
            case 27 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_27.get());
            case 28 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_28.get());
            case 29 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_29.get());
            case 30 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_30.get());
            default -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_1.get());
        };
    }

    private ItemStack getSpicyChickenFlyStage3(int v) {
        return switch (v) {
            case 1 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_1.get());
            case 2 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_2.get());
            case 3 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_3.get());
            case 4 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_4.get());
            case 5 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_5.get());
            case 6 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_6.get());
            case 7 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_7.get());
            case 8 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_8.get());
            case 9 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_9.get());
            case 10 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_10.get());
            case 11 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_11.get());
            case 12 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_12.get());
            case 13 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_13.get());
            case 14 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_14.get());
            case 15 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_15.get());
            case 16 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_16.get());
            case 17 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_17.get());
            case 18 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_18.get());
            case 19 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_19.get());
            case 20 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_20.get());
            case 21 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_21.get());
            case 22 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_22.get());
            case 23 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_23.get());
            case 24 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_24.get());
            case 25 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_25.get());
            case 26 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_26.get());
            case 27 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_27.get());
            case 28 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_28.get());
            case 29 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_29.get());
            case 30 -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_30.get());
            default -> new ItemStack(ModItems.WOK_CHICKEN_3_FLY_STAGE3_1.get());
        };
    }
}
