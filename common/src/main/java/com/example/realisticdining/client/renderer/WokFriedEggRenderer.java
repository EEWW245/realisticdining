package com.example.realisticdining.client.renderer;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.blockentities.WokFriedEggBlockEntity;
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

public class WokFriedEggRenderer extends GeoBlockRenderer<WokFriedEggBlockEntity> {

    private static final Map<String, ResourceLocation> TEXTURE_GROUPS = new HashMap<>();
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png");

    static {
        TEXTURE_GROUPS.put("fried_egg", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/fried_egg.png"));
        TEXTURE_GROUPS.put("wok_metal", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png"));
        TEXTURE_GROUPS.put("wok_metal1", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png"));
        TEXTURE_GROUPS.put("wok_handle", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_handle.png"));
        TEXTURE_GROUPS.put("vegetable_oil", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/vegetable_oil.png"));
        TEXTURE_GROUPS.put("tomato_slice", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_slice.png"));
        TEXTURE_GROUPS.put("tomato_1", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_1.png"));
        TEXTURE_GROUPS.put("tomato_2", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_2.png"));
        TEXTURE_GROUPS.put("tomato_3", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_1.png"));
        TEXTURE_GROUPS.put("tomato_4", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_2.png"));
        TEXTURE_GROUPS.put("tomato_5", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_1.png"));
        TEXTURE_GROUPS.put("tomato_6", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_2.png"));
        TEXTURE_GROUPS.put("tomato_broth", new ResourceLocation(RealisticDining.MOD_ID, "textures/item/tomato_broth.png"));
        TEXTURE_GROUPS.put("bone", new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png"));
    }

    public WokFriedEggRenderer(BlockEntityRendererProvider.Context context) {
        super(new WokFriedEggModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, WokFriedEggBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
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

    public static class WokFriedEggModel extends GeoModel<WokFriedEggBlockEntity> {

        private static final ResourceLocation MODEL_0 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_fried_egg.geo.json");
        private static final ResourceLocation MODEL_1 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_fried_egg_1.geo.json");
        private static final ResourceLocation MODEL_2 = new ResourceLocation(RealisticDining.MOD_ID, "geo/wok_fried_egg_2.geo.json");

        private static final ResourceLocation ANIM_0 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_fried_egg.animation.json");
        private static final ResourceLocation ANIM_1 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_fried_egg_1.animation.json");
        private static final ResourceLocation ANIM_2 = new ResourceLocation(RealisticDining.MOD_ID, "animations/wok_fried_egg_2.animation.json");

        @Override
        public ResourceLocation getModelResource(WokFriedEggBlockEntity animatable) {
            if (!animatable.hasTomatoSlice()) {
                return MODEL_0;
            }
            int clickCount = animatable.getSpatulaClickCount();
            if (clickCount >= 4) {
                return MODEL_2;
            }
            if (clickCount == 3 && !animatable.isCurrentlyAnimating()) {
                return MODEL_2;
            }
            return MODEL_1;
        }

        @Override
        public ResourceLocation getTextureResource(WokFriedEggBlockEntity animatable) {
            return new ResourceLocation(RealisticDining.MOD_ID, "textures/block/wok_metal.png");
        }

        @Override
        public ResourceLocation getAnimationResource(WokFriedEggBlockEntity animatable) {
            if (!animatable.hasTomatoSlice()) {
                return ANIM_0;
            }

            int clickCount = animatable.getSpatulaClickCount();
            if (clickCount == 0) {
                return ANIM_0;
            }

            int animIndex = (clickCount - 1) % 3;
            return switch (animIndex) {
                case 0 -> ANIM_0;
                case 1 -> ANIM_1;
                default -> ANIM_2;
            };
        }
    }
}
