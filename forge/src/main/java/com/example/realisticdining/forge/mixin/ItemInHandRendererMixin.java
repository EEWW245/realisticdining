package com.example.realisticdining.forge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.forge.client.arm.FpArmRenderSystem;
import com.example.realisticdining.forge.client.pack.PackDefinitionManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// priority = 500：让本 Mixin 优先于 Hold My Items - Reforged（默认 priority=1000）注入到
// ItemInHandRenderer#renderArmWithItem 的 HEAD，当我们的第一人称动画激活时 ci.cancel()
// 会跳过 HeldItemsMixin 的 handler，避免其重复渲染手臂/物品。
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

        // 材质包扩展物品（Pack）：不 cancel renderArmWithItem，让原版正常执行
        // renderArmWithItem → ItemRenderer.renderStatic → PackItemRendererMixin 拦截渲染 Pack 3D 模型。
        // Hold My Items - Reforged 的 handler（priority=1000）会在本 mixin 之后执行，
        // 若其重复渲染手臂/物品，由 PackItemRendererMixin 的 3D 模型覆盖（渲染顺序在后）。
        // 这里仅做标记，不 cancel，避免 renderStatic 不被执行导致 Pack 模型消失。

        ItemStack offHandItem = player.getOffhandItem();
        boolean hasRice = KaleidoscopeCompat.isCookedRice(offHandItem);

        if (!hasRice) {
            return;
        }

        // 主手是材质包扩展物品（Pack）时，不渲染吃米饭手臂，避免与 Pack 3D 模型重叠
        ItemStack mainHandStack = player.getMainHandItem();
        if (!mainHandStack.isEmpty()) {
            ResourceLocation mainId = ForgeRegistries.ITEMS.getKey(mainHandStack.getItem());
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
        } else if (hand == InteractionHand.OFF_HAND) {
            if (FpArmRenderSystem.shouldRenderEatRice() || FpArmRenderSystem.shouldRenderLeftHandRice()) {
                ci.cancel();
            }
        }
    }
}
