package com.example.realisticdining.fabric.mixin;

import com.example.realisticdining.fabric.client.pack.PackAnimationLock;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link MouseHandler}.
 * 材质包动画锁定期间拦截鼠标滚轮事件，防止玩家用滚轮切换快捷栏槽位。
 * 对应 Forge 端 {@code InputEvent.MouseScrollingEvent} 的 {@code setCanceled(true)}。
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    /**
     * onScroll 方法的注入点：动画锁定期间取消滚轮事件。
     * 1.20.1 MouseHandler.onScroll 签名：
     *   private void onScroll(long handle, double xoffset, double yoffset)
     */
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void realisticdining$cancelScrollWhenLocked(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        if (PackAnimationLock.isLocked()) {
            ci.cancel();
        }
    }
}
