package com.realisticdining.neoforge.client.arm.drink;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

/**
 * 饮用动画的 GeoModel。
 *
 * <p>geo / animation 资源路径来自 {@link DrinkAnimConfig}，可被多个饮料复用。
 * 贴图默认返回玩家皮肤，渲染时由 {@link DrinkAnimRenderer} 按骨骼名切换。
 *
 * <p>双模型模式下，Handler 会创建两个 DrinkAnimModel 实例，分别传入 group=1 / group=2
 * 来加载第一组或第二组 geo/animation 资源。
 */
public class DrinkAnimModel extends GeoModel<GeoAnimatable> {

    private final ResourceLocation modelResource;
    private final ResourceLocation animationResource;

    /** 加载第一组 geo/animation（默认）。 */
    public DrinkAnimModel(DrinkAnimConfig config) {
        this(config, 1);
    }

    /**
     * 加载指定组的 geo/animation。
     * @param group 1 = 第一组（geoPath/animationPath），2 = 第二组（geoPath2/animationPath2）
     */
    public DrinkAnimModel(DrinkAnimConfig config, int group) {
        if (group == 2) {
            this.modelResource = ResourceLocation.fromNamespaceAndPath(config.modId(), config.geoPath2());
            this.animationResource = ResourceLocation.fromNamespaceAndPath(config.modId(), config.animationPath2());
        } else {
            this.modelResource = ResourceLocation.fromNamespaceAndPath(config.modId(), config.geoPath());
            this.animationResource = ResourceLocation.fromNamespaceAndPath(config.modId(), config.animationPath());
        }
    }

    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable) {
        // 默认玩家皮肤；Renderer 按骨骼名切换
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            return mc.player.getSkin().texture();
        }
        return ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return animationResource;
    }
}
