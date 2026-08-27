package com.realisticdining.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.fabric.client.arm.FpArmRenderSystem;
import com.realisticdining.fabric.client.pack.PackDefinitionManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemInHandRenderer.class, priority = 500)
public class ItemInHandRendererMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void realisticdining$onRenderArmWithItem(
            AbstractClientPlayer player,
            float partialTick,
            float pitch,
            InteractionHand hand,
            float swingProgress,
            ItemStack stack,
            float equippedProgress,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            CallbackInfo ci) {

        // 饮用动画优先：动画在播 OR 手里有饮料物品时，都需要调用 updateDrinkState
        // （IDLE 态手里有饮料物品时也必须每帧调用，否则 tickMainHandChange 不执行，无法触发 PICKUP）
        boolean drinkActive = FpArmRenderSystem.isArmRenderEnabled()
                && (FpArmRenderSystem.isDrinkPlaying() || FpArmRenderSystem.hasDrinkItemInHand(player));
        if (drinkActive) {
            if (hand == InteractionHand.MAIN_HAND) {
                FpArmRenderSystem.updateDrinkState(poseStack, bufferSource, packedLight, partialTick);
                if (FpArmRenderSystem.shouldRenderDrink()) {
                    ci.cancel();
                    return;
                }
            } else if (hand == InteractionHand.OFF_HAND) {
                if (FpArmRenderSystem.isDrinkPlaying()) {
                    ci.cancel();
                    return;
                }
            }
        }

        ItemStack offHandItem = player.getOffhandItem();
        boolean hasRice = KaleidoscopeCookeryCompat.isCookedRice(offHandItem);

        if (!hasRice) {
            return;
        }

        // 主手是材质包扩展物品（Pack）时，不渲染吃米饭手臂，避免与 Pack 3D 模型重叠
        ItemStack mainHandStack = player.getMainHandItem();
        if (!mainHandStack.isEmpty()) {
            ResourceLocation mainId = BuiltInRegistries.ITEM.getKey(mainHandStack.getItem());
            if (mainId != null && PackDefinitionManager.containsItem(mainId.toString())) {
                return;
            }
        }

        if (!FpArmRenderSystem.isArmRenderEnabled()) {
            return;
        }

        if (hand == InteractionHand.MAIN_HAND) {
            FpArmRenderSystem.updateState(poseStack, bufferSource, packedLight, partialTick);

            if (FpArmRenderSystem.shouldRenderEatRice()) {
                ci.cancel();
            }
            // v2.1.13+ shouldRenderEatRice=false 时，不 cancel，原版 renderArmWithItem 执行渲染主手
        } else if (hand == InteractionHand.OFF_HAND) {
            // v2.1.13+ 在 OFF_HAND 分支渲染 GeckoLib 左臂（米饭模型）
            FpArmRenderSystem.renderLeftHandRice(poseStack, bufferSource, packedLight, partialTick);
            ci.cancel();
        }
    }
}
