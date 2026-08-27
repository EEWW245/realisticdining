package com.example.realisticdining.fabric.mixin;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.fabric.client.arm.FpArmRenderSystem;
import com.example.realisticdining.fabric.client.pack.PackDefinitionManager;
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

    @Inject(method = "showVanillaHands()Z", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
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
        if (KaleidoscopeCompat.isCookedRice(offHandItem)) {
            cir.setReturnValue(true);
        }

        // 主手或副手是材质包扩展物品（Pack）时，强制显示原版双手，
        // 避免 FirstPersonModel 取代 renderArmWithItem 导致 Pack 3D 模型不渲染
        ItemStack mainHandItem = player.getMainHandItem();
        if (isPackItem(mainHandItem) || isPackItem(offHandItem)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hideArmsAndItems(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void realisticdining$hideArmsWhenRice(net.minecraft.world.entity.LivingEntity livingEntity, ItemStack mainhand, ItemStack offhand, CallbackInfoReturnable<Boolean> cir) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        // 动画在播 OR 手里有饮料物品时，让 FirstPersonModel 隐藏自身手臂，避免与动画/原版手臂重叠
        if (FpArmRenderSystem.isArmRenderEnabled()
                && (FpArmRenderSystem.isDrinkPlaying() || FpArmRenderSystem.hasDrinkItemInHand(player))) {
            cir.setReturnValue(true);
            return;
        }
        if (offhand != null && KaleidoscopeCompat.isCookedRice(offhand)) {
            cir.setReturnValue(true);
        }
        // 手持材质包扩展物品时，隐藏 FirstPersonModel 自身手臂，避免与 Pack 3D 模型重叠
        if (mainhand != null && isPackItem(mainhand)) {
            cir.setReturnValue(true);
            return;
        }
        if (offhand != null && isPackItem(offhand)) {
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
