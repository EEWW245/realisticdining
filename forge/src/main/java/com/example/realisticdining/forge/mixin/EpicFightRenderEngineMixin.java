package com.example.realisticdining.forge.mixin;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.forge.client.arm.FpArmRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = {"yesman.epicfight.client.events.engine.RenderEngine$Events"}, remap = false)
public class EpicFightRenderEngineMixin {

    @Inject(method = "renderHand(Lnet/minecraftforge/client/event/RenderHandEvent;)V", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void realisticdining$skipEpicFightRender(RenderHandEvent event, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // 饮料/零食动画播放时，取消 Epic Fight 的手部渲染，避免抢占动画手臂
        if (FpArmRenderSystem.isArmRenderEnabled() && FpArmRenderSystem.isDrinkPlaying()) {
            ci.cancel();
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCompat.isCookedRice(offHandItem)) {
            ci.cancel();
        }
    }
}
