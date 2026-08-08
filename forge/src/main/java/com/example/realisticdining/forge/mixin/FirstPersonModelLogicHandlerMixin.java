package com.example.realisticdining.forge.mixin;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.forge.client.arm.FpArmRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {"dev.tr7zw.firstperson.LogicHandler"}, remap = false)
public class FirstPersonModelLogicHandlerMixin {

    @Inject(method = "showVanillaHands()Z", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void realisticdining$showVanillaHandsWhenRice(CallbackInfoReturnable<Boolean> cir) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // 饮料/零食动画播放时，强制显示原版双手（由 ItemInHandRendererMixin 拦截渲染动画手臂）
        if (FpArmRenderSystem.isArmRenderEnabled() && FpArmRenderSystem.isDrinkPlaying()) {
            cir.setReturnValue(true);
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCompat.isCookedRice(offHandItem)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hideArmsAndItems(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void realisticdining$hideArmsWhenRice(net.minecraft.world.entity.LivingEntity livingEntity, ItemStack mainhand, ItemStack offhand, CallbackInfoReturnable<Boolean> cir) {
        // 饮料/零食动画播放时，让 FirstPersonModel 隐藏自身手臂，避免与动画手臂重叠
        if (FpArmRenderSystem.isArmRenderEnabled() && FpArmRenderSystem.isDrinkPlaying()) {
            cir.setReturnValue(true);
            return;
        }
        if (offhand != null && KaleidoscopeCompat.isCookedRice(offhand)) {
            cir.setReturnValue(true);
        }
    }
}
