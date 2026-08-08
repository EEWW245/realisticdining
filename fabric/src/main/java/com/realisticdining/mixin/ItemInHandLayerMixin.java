package com.realisticdining.mixin;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void realisticdining$hideItemWhenRice(LivingEntity livingEntity, ItemStack itemStack, net.minecraft.world.item.ItemDisplayContext itemDisplayContext, net.minecraft.world.entity.HumanoidArm humanoidArm, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (!(livingEntity instanceof Player player)) return;
        if (player != Minecraft.getInstance().player) return;

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCookeryCompat.isCookedRice(offHandItem)) {
            ci.cancel();
        }
    }
}
