package com.realisticdining.fabric.client.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.realisticdining.RealisticDining;
import com.realisticdining.fabric.client.arm.VanillaArmRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 材质包扩展物品的渲染器（Fabric 1.21.1）。
 *
 * <p>每个扩展物品共用同一个 {@link PackEmpty} GeoItem，但渲染时根据
 * {@link PackDefinitionManager#getDerivedName} 取对应 name，用 name 派生
 * model/texture/animation 路径。
 *
 * <p>渲染流程：
 * <ol>
 *   <li>{@link #renderByItem} 入口：先叠加 {@link PackHeldItemMotion} 4 种程序化晃动</li>
 *   <li>{@link #renderRecursively}：当不在播放动画时，隐藏 {@code invisible} 骨骼；
 *       播放动画时显示全部</li>
 * </ol>
 */
public class PackCustomRenderer extends GeoItemRenderer<PackEmpty> {

    private final String itemId;
    private final String derivedName;
    /** 捕获的手臂姿态列表（renderRecursively 填充，renderByItem 结尾用原版贴图渲染）。 */
    private final List<VanillaArmRenderer.CapturedArm> capturedArms = new ArrayList<>();

    public PackCustomRenderer(String itemId) {
        super(new Model(PackDefinitionManager.getDerivedName(itemId)));
        this.itemId = itemId;
        this.derivedName = PackDefinitionManager.getDerivedName(itemId);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack,
                              MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // 设置当前渲染的物品 ID，供 PackEmpty 控制器谓词判断持物模式
        PackEmpty.setCurrentRenderItemId(itemId);
        capturedArms.clear();

        if (PackDefinitionManager.getMode(itemId) == PackMode.PICKUP) {
            // PICKUP 模式：让 GeckoLib 动画（pickup + hold_on_last_frame）接管渲染
            super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
            // 模型渲染完成后，用原版玩家皮肤贴图渲染捕获的手臂
            VanillaArmRenderer.renderCapturedArms(capturedArms, bufferSource, packedLight);
            capturedArms.clear();
            return;
        }
        // STATIC 模式：叠加 4 种程序化晃动
        poseStack.pushPose();
        // partialTick：1.21.1 getFrameTime 已移除，暂用 0
        float partialTick = 0.0F;
        PackHeldItemMotion.applyIdleMotion(poseStack, partialTick);
        PackHeldItemMotion.applyWalkMotion(poseStack, partialTick);
        PackHeldItemMotion.applyInertiaMotion(poseStack, partialTick);
        PackHeldItemMotion.applyJumpMotion(poseStack, partialTick);
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
        // 模型渲染完成后，用原版玩家皮肤贴图渲染捕获的手臂
        VanillaArmRenderer.renderCapturedArms(capturedArms, bufferSource, packedLight);
        capturedArms.clear();
    }

    @Override
    public void renderRecursively(PoseStack poseStack, PackEmpty animatable, GeoBone bone, RenderType renderType,
                                  MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                  float partialTick, int packedLight, int packedOverlay,
                                  int colour) {
        poseStack.pushPose();
        RenderUtil.prepMatrixForBone(poseStack, bone);

        boolean armBone = VanillaArmRenderer.isArmBone(bone);
        boolean hiddenInModelStage = isHiddenInModelStage(bone);

        if (armBone && !hiddenInModelStage) {
            // 手臂骨骼：不渲染自定义 cube，捕获姿态供后续用原版贴图渲染
            if (!isReRender) {
                VanillaArmRenderer.CapturedArm capturedArm = VanillaArmRenderer.captureArmPose(poseStack, bone);
                if (capturedArm != null) {
                    capturedArms.add(capturedArm);
                }
            }
        } else if (!hiddenInModelStage) {
            // 普通骨骼：正常渲染 cube
            renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
        }
        if (!isReRender && !armBone && !hiddenInModelStage) {
            applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }
        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }

    @Override
    public RenderType getRenderType(PackEmpty animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentCull(texture);
    }

    /** 持物阶段（非动画播放）隐藏的骨骼：定义文件 invisible 字段或父骨骼命中即隐藏。 */
    private boolean isHiddenInModelStage(GeoBone bone) {
        if (isPlayingAnimation()) return false;
        for (GeoBone current = bone; current != null; current = current.getParent()) {
            if (PackDefinitionManager.isInvisible(itemId, current.getName())) {
                return true;
            }
        }
        return false;
    }

    /** 当前是否在播放 eat 触发动画。 */
    private boolean isPlayingAnimation() {
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null) return false;
        long instanceId = software.bernie.geckolib.animatable.GeoItem.getId(empty.getRenderStack());
        AnimatableInstanceCache cache = empty.getAnimatableInstanceCache();
        if (cache == null) return false;
        AnimatableManager<?> manager = cache.getManagerForId(instanceId);
        if (manager == null) return false;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        return controller != null && controller.isPlayingTriggeredAnimation();
    }

    /**
     * 模型：按 derivedName 派生 model/texture/animation 路径。
     * 资源必须放在 {@code assets/realisticdining/} 下，命名空间固定 {@code realisticdining}。
     */
    private static class Model extends GeoModel<PackEmpty> {
        private final String name;

        public Model(String name) {
            this.name = name;
        }

        @Override
        public ResourceLocation getModelResource(PackEmpty animatable) {
            return ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "geo/" + name + ".geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(PackEmpty animatable) {
            return ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "textures/item/" + name + ".png");
        }

        @Override
        public ResourceLocation getAnimationResource(PackEmpty animatable) {
            return ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "animations/" + name + ".animation.json");
        }
    }
}
