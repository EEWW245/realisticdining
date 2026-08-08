package com.realisticdining.fabric.client.arm;

import com.realisticdining.RealisticDining;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class LeftHandRiceModel extends GeoModel<GeoAnimatable> {

    private static final ResourceLocation MODEL_RESOURCE =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "geo/left_hand_rice.geo.json");
    private static final ResourceLocation ANIMATION_RESOURCE =
            ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "animations/right_arm_fp.animation.json");

    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            return mc.player.getSkin().texture();
        }
        return ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return ANIMATION_RESOURCE;
    }
}
