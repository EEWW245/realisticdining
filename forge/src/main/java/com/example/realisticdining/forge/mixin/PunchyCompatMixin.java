package com.example.realisticdining.forge.mixin;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.forge.client.arm.drink.DrinkAnimRegistry;
import com.example.realisticdining.forge.client.pack.PackDefinitionManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Punchy（动手党）物品黑名单运行时增强（v2.2.4 punchy 兼容）。
 *
 * <p>问题：punchy 与本模组都会对同一物品触发第一人称手臂动画，
 * 双方各渲染一条手臂 → "双右臂"冲突。punchy 官方提供 itemBlacklist
 * 配置（config/punchy/punchy_config.json），但需要玩家逐个搜索添加。
 *
 * <p>本 Mixin 在运行时直接把本模组接管动画的物品注入 punchy 的黑名单判断：
 * <ul>
 *   <li>{@code isItemBlacklisted}：手持该物品的手，punchy 不接管（渲染原版手臂）</li>
 *   <li>{@code isItemBlacklistDualHanded}：另一只手持该物品时，本手也不接管
 *       （副手米饭 + 主手筷子场景：punchy 释放主手，吃米饭动画由本模组独占）</li>
 * </ul>
 *
 * <p>覆盖物品（与动画激活条件严格一致）：
 * <ul>
 *   <li>森罗物语米饭 / 农夫乐事米饭（{@link KaleidoscopeCompat#isCookedRice}，含 tag 兜底）</li>
 *   <li>本模组全部零食/饮料（{@link DrinkAnimRegistry#drinkIdForItem} 命中）</li>
 * </ul>
 * 筷子不拉黑：单独持筷无动画冲突，punchy 照常工作。
 *
 * <p>{@code @Pseudo} + {@code require = 0}：punchy 未安装时本 Mixin 静默失效，
 * 模式与 {@link FirstPersonModelLogicHandlerMixin} 相同。
 * method 只写方法名（punchy 自身方法，无重载），规避跨平台类型描述符 remap 问题。
 */
@Pseudo
@Mixin(targets = "punchy.config.PunchyConfig", remap = false)
public class PunchyCompatMixin {

    @Inject(method = "isItemBlacklisted", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void realisticdining$blacklistOurAnimatedItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (realisticdining$isOurAnimatedItem(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isItemBlacklistDualHanded", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void realisticdining$dualHandedOurAnimatedItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (realisticdining$isOurAnimatedItem(stack)) {
            cir.setReturnValue(true);
        }
    }

    /** 是否为本模组动画接管的物品（吃米饭的米饭 + 饮用/零食的 19 个物品）。 */
    private static boolean realisticdining$isOurAnimatedItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        // 森罗米饭 / 农夫乐事米饭 / tag 统一米饭（吃米饭动画激活条件）
        if (KaleidoscopeCompat.isCookedRice(stack)) {
            return true;
        }
        // 本模组零食/饮料（饮用动画激活条件）
        if (DrinkAnimRegistry.drinkIdForItem(stack.getItem()) != null) {
            return true;
        }
        // 材质包扩展物品（进食动画接管，punchy 需释放该手避免双右臂）
        return PackDefinitionManager.containsItem(stack.getItem());
    }
}
