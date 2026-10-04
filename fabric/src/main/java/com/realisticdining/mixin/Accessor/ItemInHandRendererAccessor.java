package com.realisticdining.mixin.Accessor;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


/**
 * Accessor mixin for {@link ItemInHandRenderer}.
 * 用于触发假的 reequip 动画（切换物品时的手臂摆动动画）。
 */
@Mixin(ItemInHandRenderer.class)
public interface ItemInHandRendererAccessor {

    @Accessor("mainHandItem")
    void realisticdining$setMainHandItem(ItemStack stack);
}
