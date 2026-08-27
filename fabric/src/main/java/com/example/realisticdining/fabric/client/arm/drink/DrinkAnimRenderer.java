package com.example.realisticdining.fabric.client.arm.drink;

import com.example.realisticdining.client.arm.VanillaArmRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 饮用动画通用第一人称渲染器（v2.2.0+ 原版手臂版）。
 *
 * <p>贴图路由（方案 C）：
 * <ul>
 *   <li>Left Arm / Right Arm / Right_Arm2 → 原版手臂（不渲染自定义 cube，捕获矩阵后置渲染）</li>
 *   <li>其他所有骨骼 → 当前饮料的瓶子/罐子贴图</li>
 * </ul>
 *
 * <p>原版手臂方案（参照 TaCZ）：renderRecursively 遍历到手臂骨骼时，
 * 不渲染该骨骼的自定义 cube，改为 {@link VanillaArmRenderer#captureArmPose} 捕获
 * 骨骼世界矩阵；模型 pass 完成后在 {@link #renderDrink} 末尾用
 * {@link VanillaArmRenderer#renderCapturedArms} 渲染原版玩家手臂（自动玩家皮肤，
 * 含袖子/皮肤外层），手臂随骨骼动画旋转/位移。
 */
public class DrinkAnimRenderer extends GeoObjectRenderer<GeoAnimatable> {

    private static final Vector3f XP = new Vector3f(1, 0, 0);
    private static final Vector3f YP = new Vector3f(0, 1, 0);

    /** 玩家手臂骨骼（原版皮肤布局约定）→ 改为原版手臂锚点，不再渲染自定义模型。 */
    private static final Set<String> PLAYER_SKIN_BONES = Set.of("Left Arm", "Right Arm", "Right_Arm2");

    private final ResourceLocation bottleTexture;
    private final Map<String, ResourceLocation> boneTextures;
    private final boolean hideLeftArm;
    /** v2.1.4+ 当前状态机阶段，用于持物阶段隐藏左臂。每帧由 handler 更新。 */
    private DrinkAnimState.Phase currentPhase = DrinkAnimState.Phase.IDLE;

    /** 本次渲染 pass 捕获的手臂矩阵（模型 pass 后渲染原版手臂）。 */
    private final List<VanillaArmRenderer.CapturedArm> capturedArms = new ArrayList<>();

    /** 第一组贴图（默认）。 */
    public DrinkAnimRenderer(GeoModel<GeoAnimatable> model, DrinkAnimConfig config) {
        this(model, config, 1);
    }

    /**
     * 指定组的贴图。
     * @param group 1 = 第一组（texturePath/boneTextures），2 = 第二组（texturePath2/boneTextures2）
     */
    public DrinkAnimRenderer(GeoModel<GeoAnimatable> model, DrinkAnimConfig config, int group) {
        super(model);
        if (group == 2) {
            this.bottleTexture = config.textureResource2();
            this.boneTextures = config.boneTextureResources2();
        } else {
            this.bottleTexture = config.textureResource();
            this.boneTextures = config.boneTextureResources();
        }
        this.hideLeftArm = config.hideLeftArm();
    }

    /** v2.1.4+ 设置当前状态机阶段（每帧由 handler 调用）。 */
    public void setCurrentPhase(DrinkAnimState.Phase phase) {
        this.currentPhase = phase;
    }

    private int getPackedLightAtPlayer() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) {
            return 15728880;
        }
        BlockPos blockPos = new BlockPos((int) Math.floor(player.getX()), (int) Math.floor(player.getEyeY()), (int) Math.floor(player.getZ()));
        return LevelRenderer.getLightColor(mc.level, blockPos);
    }

    @Override
    public long getInstanceId(GeoAnimatable animatable) {
        return 0L;
    }

    public void renderDrink(PoseStack poseStack, GeoAnimatable animatable, MultiBufferSource bufferSource,
                            int packedLight, float partialTick) {
        int currentPackedLight = getPackedLightAtPlayer();
        this.animatable = animatable;

        capturedArms.clear();
        defaultRender(poseStack, animatable, bufferSource, null, null, 0, partialTick, currentPackedLight);

        // 模型 pass 完成：用捕获的骨骼矩阵渲染原版手臂（自动玩家皮肤）
        VanillaArmRenderer.renderCapturedArms(capturedArms, bufferSource, currentPackedLight);
        capturedArms.clear();
    }

    /**
     * v2.2.0+ 重写递归渲染（参照 TaCZ 结构）：
     * <ul>
     *   <li>pushPose + prepMatrixForBone 应用骨骼变换（此刻 poseStack 即骨骼世界矩阵）</li>
     *   <li>手臂骨骼（PLAYER_SKIN_BONES）：不渲染自定义 cube，{@code !isReRender} 时捕获矩阵</li>
     *   <li>其他骨骼：贴图路由后正常渲染 cube</li>
     *   <li>递归子骨骼后 popPose</li>
     * </ul>
     */
    @Override
    public void renderRecursively(PoseStack poseStack, GeoAnimatable animatable, GeoBone bone, RenderType renderType,
                                   MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                   float partialTick, int packedLight, int packedOverlay,
                                   float red, float green, float blue, float alpha) {
        // v2.1.4+ 持物阶段（PICKUP/HOLD）隐藏 Left Arm 骨骼（不捕获 → 原版手臂也不渲染）
        if (hideLeftArm && "Left Arm".equals(bone.getName())
                && (currentPhase == DrinkAnimState.Phase.PICKUP || currentPhase == DrinkAnimState.Phase.HOLD)) {
            return;
        }

        poseStack.pushPose();
        RenderUtils.prepMatrixForBone(poseStack, bone);

        boolean armBone = PLAYER_SKIN_BONES.contains(bone.getName()) && VanillaArmRenderer.isArmBone(bone);

        if (armBone) {
            // 手臂锚点骨骼：跳过自定义 cube 渲染，捕获矩阵供模型 pass 后渲染原版手臂
            if (!isReRender) {
                VanillaArmRenderer.CapturedArm capturedArm =
                        VanillaArmRenderer.captureArmPose(poseStack, bone);
                if (capturedArm != null) {
                    capturedArms.add(capturedArm);
                }
            }
        } else {
            // 其他骨骼：贴图路由（瓶子/罐子贴图）后正常渲染 cube
            ResourceLocation texture = getTextureForBone(bone);
            VertexConsumer boneBuffer = buffer;
            if (texture != null && bufferSource != null) {
                boneBuffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            }
            renderCubesOfBone(poseStack, bone, boneBuffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    private ResourceLocation getTextureForBone(GeoBone bone) {
        String name = bone.getName();
        if (PLAYER_SKIN_BONES.contains(name)) {
            // 手臂骨骼已改为原版手臂渲染，此分支仅供 armBone=false 的兜底（正常不会走到）
            return null;
        }
        ResourceLocation custom = boneTextures.get(name);
        if (custom != null) {
            return custom;
        }
        return bottleTexture;
    }

    @Override
    public void preRender(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender) {
            applyFirstPersonTransforms(poseStack, partialTick);
        }

        this.objectRenderTranslations = new Matrix4f(poseStack.last().pose());

        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable,
                model, isReRender, partialTick, packedLight, packedOverlay);
    }

    private void applyFirstPersonTransforms(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        float swingProgress = player.getAttackAnim(partialTick);

        poseStack.translate(0.0F, -0.52F, -0.72F);

        if (swingProgress > 0.0F) {
            float swingAngle = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
            poseStack.mulPose(new Quaternionf().rotationAxis(swingAngle * -20.0F * Mth.DEG_TO_RAD, XP));
        }

        poseStack.mulPose(new Quaternionf().rotationAxis(-85.0F * Mth.DEG_TO_RAD, XP));
        poseStack.mulPose(new Quaternionf().rotationAxis(0.0F * Mth.DEG_TO_RAD, YP));
        poseStack.mulPose(new Quaternionf().rotationAxis(90.0F * Mth.DEG_TO_RAD, XP));

        poseStack.scale(0.9F, 0.9F, 0.9F);
    }

    @Override
    public RenderType getRenderType(GeoAnimatable animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
