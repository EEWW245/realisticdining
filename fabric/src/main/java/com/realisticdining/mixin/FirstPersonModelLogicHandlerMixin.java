package com.realisticdining.mixin;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.fabric.client.arm.FpArmRenderSystem;
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

    @Inject(method = "showVanillaHands", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void realisticdining$showVanillaHandsWhenRice(CallbackInfoReturnable<Boolean> cir) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // 饮料/零食动画播放时，强制显示原版双手（由 ItemInHandRendererMixin 拦截渲染动画手臂）
        if (FpArmRenderSystem.isArmRenderEnabled() && FpArmRenderSystem.isDrinkPlaying()) {
            cir.setReturnValue(true);
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCookeryCompat.isCookedRice(offHandItem)) {
            cir.setReturnValue(true);
        }
    }
}
