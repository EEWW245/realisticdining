package com.example.realisticdining.client.renderer;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.blockentities.WokYellowSteakBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.HashMap;
import java.util.Map;

public class WokYellowSteakRenderer extends GeoBlockRenderer<WokYellowSteakBlockEntity> {

    private static final Map<String, ResourceLocation> TEXTURE_GROUPS = new HashMap<>();
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(RealisticDining.MOD_ID, "textures/item/yellow_steak.png");

    static {
        TEXTURE_GROUPS.put("coriander_8", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/coriander_8.png"));
        TEXTURE_GROUPS.put("yellow_steak", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/yellow_steak.png"));
        TEXTURE_GROUPS.put("red_chili", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/red_pepper.png"));
        TEXTURE_GROUPS.put("red_pepper", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/red_pepper.png"));
        TEXTURE_GROUPS.put("red_chili2", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/red_pepper.png"));
        TEXTURE_GROUPS.put("green_chili", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/green_chili.png"));
        TEXTURE_GROUPS.put("wok_metal", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png"));
        TEXTURE_GROUPS.put("wok_block", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png"));
        TEXTURE_GROUPS.put("wok_handle", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_handle.png"));
        TEXTURE_GROUPS.put("vegetable_oil", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/vegetable_oil.png"));
    }

    public WokYellowSteakRenderer(BlockEntityRendererProvider.Context context) {
        super(new WokYellowSteakModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, WokYellowSteakBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ResourceLocation texture = getTextureForBone(bone);
        if (texture != null) {
            RenderType newRenderType = RenderType.entityCutoutNoCull(texture);
            VertexConsumer newBuffer = bufferSource.getBuffer(newRenderType);
            super.renderRecursively(poseStack, animatable, bone, newRenderType, bufferSource, newBuffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    private ResourceLocation getTextureForBone(GeoBone bone) {
        String boneName = bone.getName();

        ResourceLocation direct = TEXTURE_GROUPS.get(boneName);
        if (direct != null) {
            return direct;
        }

        GeoBone current = bone;
        while (current != null) {
            String parentName = current.getParent() != null ? current.getParent().getName() : null;
            if (parentName != null && TEXTURE_GROUPS.containsKey(parentName)) {
                return TEXTURE_GROUPS.get(parentName);
            }
            current = current.getParent();
        }

        return DEFAULT_TEXTURE;
    }

    public static class WokYellowSteakModel extends GeoModel<WokYellowSteakBlockEntity> {

        private static final ResourceLocation MODEL_0 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_yellow_steak.geo.json");
        private static final ResourceLocation MODEL_1 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_yellow_steak_1.geo.json");
        private static final ResourceLocation MODEL_2 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_yellow_steak_2.geo.json");
        private static final ResourceLocation MODEL_STIR = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_yellow_steak_3.geo.json");
        private static final ResourceLocation MODEL_STIR_1 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_yellow_steak_3_1.geo.json");
        private static final ResourceLocation MODEL_STIR_2 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_yellow_steak_3_2.geo.json");

        private static final ResourceLocation ANIM_0 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_yellow_steak.animation.json");
        private static final ResourceLocation ANIM_1 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_yellow_steak_1.animation.json");
        private static final ResourceLocation ANIM_2 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_yellow_steak_2.animation.json");
        private static final ResourceLocation ANIM_STIR_0 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_yellow_steak_3.animation.json");
        private static final ResourceLocation ANIM_STIR_1 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_yellow_steak_3_1.animation.json");
        private static final ResourceLocation ANIM_STIR_2 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_yellow_steak_3_2.animation.json");

        private ResourceLocation lastStirAnimation = ANIM_STIR_0;

        @Override
        public ResourceLocation getModelResource(WokYellowSteakBlockEntity animatable) {
            int stage = animatable.getYellowSteakStage();
            if (stage < 0) stage = 0;

            if (stage == 3) {
                int stirPhase = animatable.getStirPhase();
                if (stirPhase == 0) {
                    return MODEL_STIR;
                } else if (stirPhase == 1) {
                    return MODEL_STIR;
                } else if (stirPhase == 2) {
                    return MODEL_STIR_1;
                } else {
                    return MODEL_STIR_2;
                }
            }

            return switch (stage) {
                case 1 -> MODEL_1;
                case 2 -> MODEL_2;
                default -> MODEL_0;
            };
        }

        @Override
        public ResourceLocation getTextureResource(WokYellowSteakBlockEntity animatable) {
            return new ResourceLocation(RealisticDining.MOD_ID, "textures/item/yellow_steak.png");
        }

        @Override
        public ResourceLocation getAnimationResource(WokYellowSteakBlockEntity animatable) {
            int stage = animatable.getYellowSteakStage();
            if (stage < 0) stage = 0;

            if (stage == 3) {
                if (animatable.isCurrentlyStirring()) {
                    int stirPhase = animatable.getStirPhase();
                    if (stirPhase == 1) {
                        lastStirAnimation = ANIM_STIR_0;
                    } else if (stirPhase == 2) {
                        lastStirAnimation = ANIM_STIR_1;
                    } else {
                        lastStirAnimation = ANIM_STIR_2;
                    }
                    return lastStirAnimation;
                }
                if (animatable.getStirPhase() == 0) {
                    return ANIM_0;
                }
                return lastStirAnimation;
            }

            return switch (stage) {
                case 1 -> ANIM_1;
                case 2 -> ANIM_2;
                default -> ANIM_0;
            };
        }
    }
}
