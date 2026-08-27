package com.realisticdining.mixin;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.fabric.client.arm.FpArmRenderSystem;
import com.realisticdining.fabric.client.pack.PackDefinitionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

        // 动画在播 OR 手里有饮料物品时，强制显示原版双手（由 ItemInHandRendererMixin 拦截渲染动画手臂）
        // IDLE 态手里有饮料物品也必须走原版路径，否则 FirstPersonModel 会取代 renderArmWithItem，
        // 导致 updateDrinkState 不被调用，tickMainHandChange 无法触发 PICKUP 起始动画
        if (FpArmRenderSystem.isArmRenderEnabled()
                && (FpArmRenderSystem.isDrinkPlaying() || FpArmRenderSystem.hasDrinkItemInHand(player))) {
            cir.setReturnValue(true);
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCookeryCompat.isCookedRice(offHandItem)) {
            cir.setReturnValue(true);
        }

        // 主手或副手是材质包扩展物品（Pack）时，强制显示原版双手，
        // 避免 FirstPersonModel 取代 renderArmWithItem 导致 Pack 3D 模型不渲染
        ItemStack mainHandItem = player.getMainHandItem();
        if (isPackItem(mainHandItem) || isPackItem(offHandItem)) {
            cir.setReturnValue(true);
        }
    }

    private static boolean isPackItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && PackDefinitionManager.containsItem(id.toString());
    }
}
