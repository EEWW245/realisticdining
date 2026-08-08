package com.example.realisticdining.fabric.client.arm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.example.realisticdining.RealisticDining;
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
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

import java.util.HashMap;
import java.util.Map;

public class LeftArmRenderer extends GeoObjectRenderer<GeoAnimatable> {

    private static final Vector3f XP = new Vector3f(1, 0, 0);
    private static final Vector3f YP = new Vector3f(0, 1, 0);

    private static final Map<String, ResourceLocation> TEXTURE_GROUPS = new HashMap<>();
    private static final ResourceLocation OAK_PLANKS_TEXTURE = new ResourceLocation("minecraft", "textures/block/oak_planks.png");

    static {
        TEXTURE_GROUPS.put("white_rice", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/white_rice.png"));
        TEXTURE_GROUPS.put("rice", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/white_rice.png"));
    }

    private ResourceLocation playerSkinTexture = null;
    private int currentPackedLight = 15728880;

    public LeftArmRenderer(GeoModel<GeoAnimatable> model) {
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

    public void renderLeftArm(PoseStack poseStack, GeoAnimatable animatable, MultiBufferSource bufferSource,
                               int packedLight, float partialTick) {
        updatePlayerSkin();
        currentPackedLight = getPackedLightAtPlayer();
        this.animatable = animatable;
        defaultRender(poseStack, animatable, bufferSource, null, null, 0, partialTick, currentPackedLight);
    }

    private void updatePlayerSkin() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            playerSkinTexture = mc.player.getSkinTextureLocation();
        } else {
            playerSkinTexture = new ResourceLocation("minecraft", "textures/entity/player/wide/steve.png");
        }
    }

    @Override
    public void render(PoseStack poseStack, GeoAnimatable animatable, MultiBufferSource bufferSource,
                       RenderType renderType, VertexConsumer buffer, int packedLight) {
        this.animatable = animatable;

        if (bufferSource == null) {
            bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        float partialTick = Minecraft.getInstance().getFrameTime();
        defaultRender(poseStack, animatable, bufferSource, renderType, buffer, 0, partialTick, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, GeoAnimatable animatable, GeoBone bone, RenderType renderType,
                                   MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                   float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ResourceLocation texture = getTextureForBone(bone);
        if (texture != null && bufferSource != null) {
            RenderType newRenderType = RenderType.entityCutoutNoCull(texture);
            VertexConsumer newBuffer = bufferSource.getBuffer(newRenderType);
            super.renderRecursively(poseStack, animatable, bone, newRenderType, bufferSource, newBuffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    private ResourceLocation getTextureForBone(GeoBone bone) {
        String boneName = bone.getName();
        
        if (boneName.equals("Left Arm")) {
            return playerSkinTexture;
        }
        
        if (boneName.equals("oak_planks")) {
            return OAK_PLANKS_TEXTURE;
        }
        
        ResourceLocation direct = TEXTURE_GROUPS.get(boneName);
        if (direct != null) {
            return direct;
        }
        
        if (boneName.startsWith("white_rice")) {
            return TEXTURE_GROUPS.get("white_rice");
        }
        
        return playerSkinTexture;
    }

    @Override
    public void preRender(PoseStack poseStack, GeoAnimatable animatable, software.bernie.geckolib.cache.object.BakedGeoModel model,
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

        poseStack.scale(-1.0F, 1.0F, 1.0F);

        poseStack.scale(0.9F, 0.9F, 0.9F);
    }

    @Override
    public RenderType getRenderType(GeoAnimatable animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
