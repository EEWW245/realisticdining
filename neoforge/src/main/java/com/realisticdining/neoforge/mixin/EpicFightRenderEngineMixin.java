package com.realisticdining.neoforge.mixin;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.neoforge.client.arm.FpArmRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {"yesman.epicfight.client.events.engine.RenderEngine"}, remap = false)
public class EpicFightRenderEngineMixin {

    @Inject(method = "epicfight$renderHand", at = @At("HEAD"), cancellable = true)
    private void realisticdining$skipEpicFightRender(RenderHandEvent event, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // 饮料/零食动画播放时，取消 Epic Fight 的手部渲染，避免抢占动画手臂
        if (FpArmRenderSystem.isArmRenderEnabled() && FpArmRenderSystem.isDrinkPlaying()) {
            ci.cancel();
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCookeryCompat.isCookedRice(offHandItem)) {
            ci.cancel();
        }
    }
}
