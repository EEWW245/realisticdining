package com.realisticdining.neoforge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.neoforge.client.arm.FpArmRenderSystem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
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

        // 饮用动画优先：不依赖手中物品，播放时屏蔽原版双手渲染
        if (FpArmRenderSystem.isArmRenderEnabled() && FpArmRenderSystem.isDrinkPlaying()) {
            if (hand == InteractionHand.MAIN_HAND) {
                FpArmRenderSystem.updateDrinkState(poseStack, bufferSource, packedLight, partialTick);
                if (FpArmRenderSystem.shouldRenderDrink()) {
                    ci.cancel();
                    return;
                }
            } else if (hand == InteractionHand.OFF_HAND) {
                ci.cancel();
                return;
            }
        }

        ItemStack offHandItem = player.getOffhandItem();
        boolean hasRice = KaleidoscopeCookeryCompat.isCookedRice(offHandItem);

        if (!hasRice) {
            return;
        }

        if (!FpArmRenderSystem.isArmRenderEnabled()) {
            return;
        }

        if (hand == InteractionHand.MAIN_HAND) {
            FpArmRenderSystem.updateState(poseStack, bufferSource, packedLight, partialTick);
            
            if (FpArmRenderSystem.shouldRenderEatRice()) {
                ci.cancel();
            }
        } else if (hand == InteractionHand.OFF_HAND) {
            if (FpArmRenderSystem.shouldRenderEatRice() || FpArmRenderSystem.shouldRenderLeftHandRice()) {
                ci.cancel();
            }
        }
    }
}
