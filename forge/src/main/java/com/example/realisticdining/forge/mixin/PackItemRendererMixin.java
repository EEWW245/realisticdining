package com.example.realisticdining.forge.mixin;

import com.example.realisticdining.forge.client.pack.PackCustomRenderer;
import com.example.realisticdining.forge.client.pack.PackDefinitionManager;
import com.example.realisticdining.forge.client.pack.PackItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 拦截原版 ItemRenderer.renderStatic，把材质包扩展物品的第一人称主手渲染
 * 替换为 {@link PackCustomRenderer}（GeckoLib 3D 模型 + 程序化晃动）。
 *
 * <p>仅当以下条件全部满足时拦截：
 * <ul>
 *   <li>context == FIRST_PERSON_RIGHT_HAND（仅第一人称主手）</li>
 *   <li>玩家未在使用物品（避免与原版 use 动作冲突，让原版处理）</li>
 *   <li>物品 ID 在 {@link PackDefinitionManager#itemIdList}（仅材质包扩展物品）</li>
 * </ul>
 * 其余场景一律放行，让原版或其他模组的 mixin 继续处理。
 *
 * <p>与 {@link ItemInHandRendererMixin} 互不冲突：本 mixin 注入 ItemRenderer.renderStatic
 * （渲染 3D 物品模型），而 ItemInHandRendererMixin 注入 ItemInHandRenderer.renderArmWithItem
 * （渲染手持层），目标类不同。
 */
@Mixin(value = ItemRenderer.class, priority = 500)
public class PackItemRendererMixin {

    private static final Map<String, PackCustomRenderer> rendererCache = new HashMap<>();

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
            at = @At("HEAD"), cancellable = true)
    private void realisticdining$onRenderStatic(
            LivingEntity entity, ItemStack stack, ItemDisplayContext context, boolean leftHand,
            PoseStack poseStack, MultiBufferSource buffer, Level level,
            int light, int overlay, int seed, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();

        // 副手物品隐藏：主手持 Pack 扩展物品时，隐藏副手手上的物品（独立 Pack 逻辑，不复用主模组饮料代码）
        if (context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            if (mc.player != null && !mc.player.getMainHandItem().isEmpty()
                    && PackDefinitionManager.containsItem(ForgeRegistries.ITEMS.getKey(mc.player.getMainHandItem().getItem()).toString())) {
                ci.cancel();
                return;
            }
            return;
        }

        if (context != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            return;
        }
        if (mc.player != null && mc.player.isUsingItem()
                && ItemStack.isSameItem(mc.player.getUseItem(), stack)) {
            // 正在吃/喝（原版使用动画中）→ 放行让原版处理
            return;
        }
        if (stack.isEmpty()) {
            return;
        }
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) {
            return;
        }
        if (!PackDefinitionManager.containsItem(id.toString())) {
            return;
        }
        if (PackItems.EMPTY_ITEM == null) {
            return;
        }

        PackCustomRenderer renderer = rendererCache.computeIfAbsent(id.toString(), PackCustomRenderer::new);
        poseStack.pushPose();
        // 第一人称手持位置修正（参考 ImmersiveEating）
        poseStack.translate(-1.05F, -0.35F, -0.8F);
        renderer.renderByItem(PackItems.EMPTY_ITEM.getRenderStack(), context, poseStack, buffer, light, overlay);
        poseStack.popPose();
        ci.cancel();
    }
}
