package com.example.realisticdining.fabric.client.arm;

import com.example.realisticdining.RealisticDining;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class LeftArmModel extends GeoModel<GeoAnimatable> {

    private static final ResourceLocation ANIMATION_RESOURCE =
            new ResourceLocation(RealisticDining.MOD_ID, "animations/eat_rice.animation.json");

    private static final String[] MODEL_RESOURCES = {
        "geo/eat_rice.geo.json",
        "geo/eat_rice_1.geo.json",
        "geo/eat_rice_2.geo.json",
        "geo/eat_rice_3.geo.json",
        "geo/eat_rice_4.geo.json",
        "geo/eat_rice_5.geo.json",
        "geo/eat_rice_6.geo.json",
        "geo/eat_rice_7.geo.json",
        "geo/eat_rice_8.geo.json"
    };

    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        int biteIndex = EatRiceState.getInstance().getCurrentBiteIndex();
        if (biteIndex < 0) biteIndex = 0;
        if (biteIndex >= MODEL_RESOURCES.length) biteIndex = MODEL_RESOURCES.length - 1;
        return new ResourceLocation(RealisticDining.MOD_ID, MODEL_RESOURCES[biteIndex]);
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
