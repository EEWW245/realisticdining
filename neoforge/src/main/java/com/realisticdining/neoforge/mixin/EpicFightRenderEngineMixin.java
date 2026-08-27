package com.realisticdining.neoforge.mixin;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.neoforge.client.arm.FpArmRenderSystem;
import com.realisticdining.neoforge.client.pack.PackDefinitionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

        // 动画在播 OR 手里有饮料物品时，取消 Epic Fight 的手部渲染，避免抢占动画手臂
        // IDLE 态手里有饮料物品也必须取消，否则 Epic Fight 会取代 renderArmWithItem,
        // 导致 updateDrinkState 不被调用，tickMainHandChange 无法触发 PICKUP 起始动画
        if (FpArmRenderSystem.isArmRenderEnabled()
                && (FpArmRenderSystem.isDrinkPlaying() || FpArmRenderSystem.hasDrinkItemInHand(player))) {
            ci.cancel();
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (KaleidoscopeCookeryCompat.isCookedRice(offHandItem)) {
            ci.cancel();
            return;
        }

        // 手持材质包扩展物品（Pack）时，取消 Epic Fight 手部渲染，避免抢占 Pack 3D 模型
        ItemStack mainHandItem = player.getMainHandItem();
        if (isPackItem(mainHandItem) || isPackItem(offHandItem)) {
            ci.cancel();
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
