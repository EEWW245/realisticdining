package com.realisticdining.neoforge.mixin;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.neoforge.client.arm.FpArmRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"dev.tr7zw.firstperson.LogicHandler"}, remap = false)
public class FirstPersonModelLogicHandlerMixin {

    @Inject(method = "showVanillaHands()Z", at = @At("HEAD"), cancellable = true)
    private void realisticdining$showVanillaHandsWhenRice(CallbackInfoReturnable<Boolean> cir) {
        if (!FpArmRenderSystem.isArmRenderEnabled()) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // 饮料/零食动画播放时，强制显示原版双手（由 ItemInHandRendererMixin 拦截渲染动画手臂）
        if (FpArmRenderSystem.isDrinkPlaying()) {
            cir.setReturnValue(true);
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCookeryCompat.isCookedRice(offHandItem)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hideArmsAndItems(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void realisticdining$hideArmsWhenRice(net.minecraft.world.entity.LivingEntity livingEntity, ItemStack mainhand, ItemStack offhand, CallbackInfoReturnable<Boolean> cir) {
        if (!FpArmRenderSystem.isArmRenderEnabled()) return;
        // 饮料/零食动画播放时，让 FirstPersonModel 隐藏自身手臂，避免与动画手臂重叠
        if (FpArmRenderSystem.isDrinkPlaying()) {
            cir.setReturnValue(true);
            return;
        }
        if (offhand != null && KaleidoscopeCookeryCompat.isCookedRice(offhand)) {
            cir.setReturnValue(true);
        }
    }
}
