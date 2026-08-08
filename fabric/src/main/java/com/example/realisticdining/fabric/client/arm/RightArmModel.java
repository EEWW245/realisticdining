package com.example.realisticdining.fabric.client.arm;

import com.example.realisticdining.RealisticDining;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class RightArmModel extends GeoModel<GeoAnimatable> {

    private static final ResourceLocation MODEL_RESOURCE =
            new ResourceLocation(RealisticDining.MOD_ID, "geo/eat_rice.geo_1.json");
    private static final ResourceLocation ANIMATION_RESOURCE =
            new ResourceLocation(RealisticDining.MOD_ID, "animations/eat_rice_1.animation.json");

    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            return mc.player.getSkinTextureLocation();
        }
        return new ResourceLocation("minecraft", "textures/entity/player/wide/steve.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return ANIMATION_RESOURCE;
    }
}
