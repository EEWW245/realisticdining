package com.realisticdining.fabric.client.arm.drink;

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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

import java.util.Map;
import java.util.Set;

/**
 * 饮用动画通用第一人称渲染器。
 *
 * <p>贴图路由（方案 C）：
 * <ul>
 *   <li>Left Arm / Right Arm / Right_Arm2 → 玩家皮肤</li>
 *   <li>其他所有骨骼 → 当前饮料的瓶子/罐子贴图</li>
 * </ul>
 *
 * <p>这样无论骨骼名是 Bottle / BottleCap（矿泉水）还是 milk beer / can lid / pull tab（奶啤），
 * 都自动走饮料贴图，无需为每种饮料的骨骼名特化 Renderer。
 */
public class DrinkAnimRenderer extends GeoObjectRenderer<GeoAnimatable> {

    private static final Vector3f XP = new Vector3f(1, 0, 0);
    private static final Vector3f YP = new Vector3f(0, 1, 0);

    /** 玩家手臂骨骼（原版皮肤布局约定），走玩家皮肤贴图。 */
    private static final Set<String> PLAYER_SKIN_BONES = Set.of("Left Arm", "Right Arm", "Right_Arm2");

    private final ResourceLocation bottleTexture;
    private final Map<String, ResourceLocation> boneTextures;
    private ResourceLocation playerSkinTexture = null;

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
    }

    private int getPackedLightAtPlayer() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) {
            return 15728880;
        }
        BlockPos blockPos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
        return LevelRenderer.getLightColor(mc.level, blockPos);
    }

    @Override
    public long getInstanceId(GeoAnimatable animatable) {
        return 0L;
    }

    public void renderDrink(PoseStack poseStack, GeoAnimatable animatable, MultiBufferSource bufferSource,
                            int packedLight, float partialTick) {
        updatePlayerSkin();
        int currentPackedLight = getPackedLightAtPlayer();
        render(poseStack, animatable, bufferSource, null, null, currentPackedLight, partialTick);
    }

    private void updatePlayerSkin() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            playerSkinTexture = mc.player.getSkin().texture();
        } else {
            playerSkinTexture = ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
        }
    }

    @Override
    public void render(PoseStack poseStack, GeoAnimatable animatable, @Nullable MultiBufferSource bufferSource,
                       @Nullable RenderType renderType, @Nullable VertexConsumer buffer,
                       int packedLight, float partialTick) {
        this.animatable = animatable;

        if (bufferSource == null) {
            bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        defaultRender(poseStack, animatable, bufferSource, renderType, buffer, 0, partialTick, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, GeoAnimatable animatable, GeoBone bone, RenderType renderType,
                                   MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                   float partialTick, int packedLight, int packedOverlay, int color) {
        ResourceLocation texture = getTextureForBone(bone);
        if (texture != null && bufferSource != null) {
            RenderType newRenderType = RenderType.entityCutoutNoCull(texture);
            VertexConsumer newBuffer = bufferSource.getBuffer(newRenderType);
            super.renderRecursively(poseStack, animatable, bone, newRenderType, bufferSource, newBuffer,
                    isReRender, partialTick, packedLight, packedOverlay, color);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                    isReRender, partialTick, packedLight, packedOverlay, color);
        }
    }

    private ResourceLocation getTextureForBone(GeoBone bone) {
        String name = bone.getName();
        if (PLAYER_SKIN_BONES.contains(name)) {
            return playerSkinTexture;
        }
        ResourceLocation custom = boneTextures.get(name);
        if (custom != null) {
            return custom;
        }
        return bottleTexture;
    }

    @Override
    public void preRender(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel model,
                          @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
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

        poseStack.scale(0.9F, 0.9F, 0.9F);
    }

    @Override
    public RenderType getRenderType(GeoAnimatable animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
