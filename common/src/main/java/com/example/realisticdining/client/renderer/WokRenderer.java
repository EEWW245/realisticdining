package com.example.realisticdining.client.renderer;

import com.example.realisticdining.blockentities.WokBlockEntity;
import com.example.realisticdining.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class WokRenderer implements BlockEntityRenderer<WokBlockEntity> {
    private final ItemRenderer itemRenderer;

    public WokRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(@NotNull WokBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        // ================= ✨ 1. 高级物理抛物线炒菜逻辑 ✨ =================
        // 只要锅里放了猪肉或鸡肉或红辣椒，就触发我们的高级定制渲染逻辑
        if (blockEntity.hasPork() || blockEntity.hasChicken() || blockEntity.hasRedPepper()) {
            poseStack.pushPose();

            // 黄金绝对原点！因为 Python 生成的模型已经限定了 1.01 的高度，
            // 绝不能在这里再往下降，否则会插进平底锅的铁皮里。
            poseStack.translate(0.5, 0.4, 0.5);

            // 判断菜品类型
            // 辣子鸡组合：鸡肉+红辣椒+葱
            boolean isSpicyChicken = blockEntity.hasChicken() && blockEntity.hasRedPepper() && blockEntity.hasGreenOnion();

            if (blockEntity.isReadyToStir()) {
                // 肉和菜都有了，准备执行抛物线动画！
                long gameTime = blockEntity.getLevel().getGameTime();
                long timeSinceStir = gameTime - blockEntity.getLastStirTime();
                float animDuration = 10.0f; // 动画总时长：10个游戏刻（0.5秒）

                if (timeSinceStir < animDuration && blockEntity.getStirCount() > 0) {
                    // == 正在翻炒中：播放抛物线飞天动画 ==
                    float progress = (timeSinceStir + partialTick) / animDuration;

                    // 核心数学魔法：正弦曲线抛物线。最高点在 0.8 个方块高度 (可调)
                    double jumpHeight = Math.sin(progress * Math.PI) * 0.8;
                    int variant = blockEntity.getCurrentStirVariant();

                    // a. 渲染乖乖待在平底锅底的食材 (Base)
                    ItemStack baseItem = isSpicyChicken ? 
                        getSpicyChickenBaseItem(variant, blockEntity.getStirCount()) : 
                        getWokBaseItem(variant, blockEntity.getStirCount());
                    itemRenderer.renderStatic(baseItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);

                    // b. 渲染被铲子扬起飞天的食材 (Fly)
                    poseStack.pushPose();

                    // ✨ 修复1：大幅降低高度，0.3 代表只飞起不到 5 个像素的高度，完美限制在锅内！
                    poseStack.translate(0, Math.sin(progress * Math.PI) * 0.3, 0);

                    // 算出 0~360 度的完整自转角度
                    float spinAngle = progress * 360.0f;

                    // ✨ 修复2：修正自转中心！
                    // 因为方块中心在 Y=8，食材在 Y=2.5。我们先往下平移 5.5 个像素 (5.5/16 = 0.34f)
                    // 这样食材就会"原地翻滚"，而不是像摩天轮一样被甩出去！
                    poseStack.translate(0, -0.34f, 0);

                    // ✨ 修复3：解决翻转方向不对齐的问题！
                    // 利用 variant (1~8) 让食材每次翻炒时，前后左右随机乱翻，完美模拟真实炒菜的混沌感！
                    int axisDecider = variant % 4;
                    if (axisDecider == 0) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(spinAngle));  // 沿着X轴前空翻
                    } else if (axisDecider == 1) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-spinAngle)); // 沿着X轴后空翻
                    } else if (axisDecider == 2) {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(spinAngle));  // 沿着Z轴左侧翻
                    } else {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(-spinAngle)); // 沿着Z轴右侧翻
                    }

                    // 翻转完，把坐标轴重新移回方块中心
                    poseStack.translate(0, 0.34f, 0);

                    ItemStack flyItem = isSpicyChicken ? 
                        getSpicyChickenFlyItem(variant, blockEntity.getStirCount()) : 
                        getWokFlyItem(variant, blockEntity.getStirCount());
                    itemRenderer.renderStatic(flyItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
                    poseStack.popPose();

                } else {
                    // == 安静状态：所有食材平躺在锅底 ==
                    int stirCount = blockEntity.getStirCount();
                    ItemStack idleItem;
                    if (isSpicyChicken) {
                        // 辣子鸡的安静状态
                        if (stirCount >= 25) {
                            idleItem = new ItemStack(ModItems.WOK_CHICKEN_3_IDLE_STAGE3.get());
                        } else if (stirCount >= 19 && stirCount <= 24) {
                            idleItem = new ItemStack(ModItems.WOK_CHICKEN_3_IDLE_STAGE2.get());
                        } else {
                            idleItem = new ItemStack(ModItems.WOK_CHICKEN_3_IDLE.get());
                        }
                    } else {
                        // 猪肉炒白菜的安静状态
                        if (stirCount >= 25) {
                            idleItem = new ItemStack(ModItems.WOK_IDLE_STAGE3.get());
                        } else if (stirCount >= 19 && stirCount <= 24) {
                            idleItem = new ItemStack(ModItems.WOK_IDLE_STAGE2.get());
                        } else {
                            idleItem = new ItemStack(ModItems.WOK_IDLE.get());
                        }
                    }
                    itemRenderer.renderStatic(idleItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
                }

            } else {
                // == 只有肉，还没放青菜的中间状态 ==
                // 根据放入的是猪肉、鸡肉、鸡肉+红辣椒、还是鸡肉+红辣椒+葱，显示不同的模型
                ItemStack meatOnlyItem;
                if (blockEntity.hasChicken() && blockEntity.hasRedPepper() && blockEntity.hasGreenOnion()) {
                    // 鸡肉 + 红辣椒 + 葱
                    meatOnlyItem = new ItemStack(ModItems.WOK_CHICKEN_ONLY_3.get());
                } else if (blockEntity.hasChicken() && blockEntity.hasRedPepper()) {
                    // 鸡肉 + 红辣椒
                    meatOnlyItem = new ItemStack(ModItems.WOK_RED_PEPPER_ONLY.get());
                } else if (blockEntity.hasChicken()) {
                    // 只有鸡肉
                    meatOnlyItem = new ItemStack(ModItems.WOK_CHICKEN_ONLY.get());
                } else {
                    // 猪肉
                    meatOnlyItem = new ItemStack(ModItems.WOK_PORK_ONLY.get());
                }
                itemRenderer.renderStatic(meatOnlyItem, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
            }

            poseStack.popPose();
            return; // 结束渲染，拦截掉下面的老逻辑
        }

        // ================= 🍲 2. 传统 GUI 配方渲染逻辑 (老代码完美保留) =================
        renderIngredient(blockEntity, poseStack, bufferSource, packedLight);
    }

    // --- 获取 1~8 种锅底状态的方法 ---
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
        } else if (stirCount >= 19 && stirCount <= 24) {
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

    // --- 获取 1~8 种飞天状态的方法 ---
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
        } else if (stirCount >= 19 && stirCount <= 24) {
            return switch (variant) {
                case 1 -> new ItemStack(ModItems.WOK_FLY_STAGE2_1.get());
                case 2 -> new ItemStack(ModItems.WOK_FLY_STAGE2_2.get());
                case 3 -> new ItemStack(ModItems.WOK_FLY_STAGE3_3.get());
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

    // --- 辣子鸡：获取 1~30 种锅底状态的方法 ---
    private ItemStack getSpicyChickenBaseItem(int variant, int stirCount) {
        // 辣子鸡使用30个变体，每个变体随机2-4个元素被炒起
        int v = ((variant - 1) % 30) + 1;
        
        // 根据翻炒次数选择阶段
        if (stirCount >= 25) {
            // STAGE3 (25-30次)
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
        } else if (stirCount >= 19 && stirCount <= 24) {
            // STAGE2 (19-24次)
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
        
        // STAGE1 (0-18次)
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

    // --- 辣子鸡：获取 1~30 种飞天状态的方法 ---
    private ItemStack getSpicyChickenFlyItem(int variant, int stirCount) {
        // 辣子鸡使用30个变体，每个变体随机2-4个元素被炒起
        int v = ((variant - 1) % 30) + 1;
        
        // 根据翻炒次数选择阶段
        if (stirCount >= 25) {
            // STAGE3 (25-30次)
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
        
        // STAGE1 (0-18次)
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

    // ================= 以下为原版兼容代码，一字未改 =================

    private void renderIngredient(WokBlockEntity blockEntity, PoseStack poseStack,
                                  MultiBufferSource bufferSource, int packedLight) {
        ItemStack ingredient = blockEntity.getItem(0);
        if (!ingredient.isEmpty()) {
            int cookStage = blockEntity.getCookStage();
            poseStack.pushPose();
            poseStack.translate(0.5, 0.065, 0.5); // 老逻辑依然保留 0.065 高度修正
            renderCookingStage(cookStage, ingredient, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }

        ItemStack result = blockEntity.getItem(1);
        if (!result.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.065, 0.5);
            renderItem(result, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
    }

    private void renderCookingStage(int stage, ItemStack item, PoseStack poseStack,
                                    MultiBufferSource bufferSource, int packedLight) {
        switch (stage) {
            case 1: poseStack.mulPose(Axis.XP.rotationDegrees(45)); break;
            case 2: poseStack.mulPose(Axis.XP.rotationDegrees(-45)); break;
            case 3: poseStack.mulPose(Axis.XP.rotationDegrees(90)); break;
            case 4: poseStack.mulPose(Axis.YP.rotationDegrees(45)); break;
            case 5: poseStack.mulPose(Axis.YP.rotationDegrees(90)); break;
            case 6: poseStack.mulPose(Axis.YP.rotationDegrees(135)); break;
            case 7: poseStack.mulPose(Axis.YP.rotationDegrees(180)); break;
            default: break;
        }
        renderItem(item, poseStack, bufferSource, packedLight);
    }

    private void renderItem(ItemStack item, PoseStack poseStack,
                            MultiBufferSource bufferSource, int packedLight) {
        BakedModel model = itemRenderer.getModel(item, null, null, 0);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        itemRenderer.render(item, ItemDisplayContext.GROUND,
                false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, model);
    }
}
