package com.realisticdining.neoforge.client.arm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.realisticdining.RealisticDining;
import com.realisticdining.neoforge.client.arm.VanillaArmRenderer;
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
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 吃米饭右臂渲染器（v2.2.0+ 原版手臂版，MC 1.21.1）。
 *
 * <p>"Right Arm" 骨骼不再渲染自定义 cube，改为捕获矩阵后置渲染原版玩家手臂
 * （参照 TaCZ 方案，见 {@link VanillaArmRenderer}）。
 * 筷子骨骼照常贴图路由渲染。
 */
public class RightArmRenderer extends GeoObjectRenderer<GeoAnimatable> {

    private static final Vector3f XP = new Vector3f(1, 0, 0);
    private static final Vector3f YP = new Vector3f(0, 1, 0);

    private static final Map<String, ResourceLocation> TEXTURE_GROUPS = new HashMap<>();

    static {
        TEXTURE_GROUPS.put("chopstick", ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "textures/item/chopsticks.png"));
        TEXTURE_GROUPS.put("chopstick2", ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "textures/item/chopsticks.png"));
        TEXTURE_GROUPS.put("chopstick3", ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "textures/item/chopsticks.png"));
    }

    /** fallback 贴图（非手臂、非筷子的未识别骨骼走玩家皮肤，保持旧行为）。 */
    private ResourceLocation playerSkinTexture = null;

    /** 本次渲染 pass 捕获的手臂矩阵（模型 pass 后渲染原版手臂）。 */
    private final List<VanillaArmRenderer.CapturedArm> capturedArms = new ArrayList<>();

    public RightArmRenderer(GeoModel<GeoAnimatable> model) {
        super(model);
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

    public void renderRightArm(PoseStack poseStack, GeoAnimatable animatable, MultiBufferSource bufferSource,
                                int packedLight, float partialTick) {
        updatePlayerSkin();
        int currentPackedLight = getPackedLightAtPlayer();
        this.animatable = animatable;

        capturedArms.clear();
        defaultRender(poseStack, animatable, bufferSource, null, null, 0, partialTick, currentPackedLight);

        // 模型 pass 完成：渲染原版右臂（自动玩家皮肤）
        VanillaArmRenderer.renderCapturedArms(capturedArms, bufferSource, currentPackedLight);
        capturedArms.clear();
    }

    private void updatePlayerSkin() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            playerSkinTexture = mc.player.getSkin().texture();
        } else {
            playerSkinTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/player/wide/steve.png");
        }
    }


    /**
     * v2.2.0+ 重写递归渲染（参照 TaCZ 结构）：
     * "Right Arm" 骨骼捕获矩阵不渲染 cube；筷子等其他骨骼贴图路由正常渲染。
     */
    @Override
    public void renderRecursively(PoseStack poseStack, GeoAnimatable animatable, GeoBone bone, RenderType renderType,
                                   MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                   float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        RenderUtil.prepMatrixForBone(poseStack, bone);

        boolean armBone = "Right Arm".equals(bone.getName()) && VanillaArmRenderer.isArmBone(bone);

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
            ResourceLocation texture = getTextureForBone(bone);
            VertexConsumer boneBuffer = buffer;
            if (texture != null && bufferSource != null) {
                boneBuffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            }
            renderCubesOfBone(poseStack, bone, boneBuffer, packedLight, packedOverlay, colour);
        }

        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }

    private ResourceLocation getTextureForBone(GeoBone bone) {
        String boneName = bone.getName();

        if (boneName.equals("Right Arm")) {
            // 手臂骨骼已改为原版手臂渲染，不在此渲染自定义 cube
            return null;
        }

        if (boneName.startsWith("chopstick")) {
            return TEXTURE_GROUPS.get("chopstick");
        }

        return playerSkinTexture;
    }

    @Override
    public void preRender(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
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

        poseStack.scale(-1.0F, 1.0F, 1.0F);

        poseStack.scale(0.9F, 0.9F, 0.9F);
    }

    @Override
    public RenderType getRenderType(GeoAnimatable animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
