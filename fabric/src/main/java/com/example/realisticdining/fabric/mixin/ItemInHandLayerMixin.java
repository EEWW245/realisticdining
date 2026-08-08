package com.example.realisticdining.fabric.mixin;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void realisticdining$hideItemWhenRice(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, net.minecraft.world.entity.HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (!(livingEntity instanceof Player player)) return;
        if (player != Minecraft.getInstance().player) return;

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCompat.isCookedRice(offHandItem)) {
            ci.cancel();
        }
    }
}
