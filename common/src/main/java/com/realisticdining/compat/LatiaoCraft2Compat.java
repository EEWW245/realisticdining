package com.realisticdining.compat;

import com.realisticdining.init.ModTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 辣条工艺 2 (Latiao Craft 2) 兼容类。
 *
 * <p>该模组的青椒(Green Pepper)与红辣椒(Red Pepper)与本模组的森罗物语青辣椒/红辣椒
 * 在菜板切菜功能上等价：均可放置于菜板并被刀具切成碎片。</p>
 *
 * <p>识别策略与 {@link KaleidoscopeCookeryCompat} 一致：
 * 优先匹配 {@link ModTags.Items#RED_CHILI} /
 * {@link ModTags.Items#GREEN_CHILI}（已包含 ltc2 物品），
 * 若未命中再走模组硬编码 fallback。</p>
 *
 * <p>实际 mod ID 为 {@code ltc2}（非 latiao_craft_2），
 * 物品 ID 为 {@code pepper_red} / {@code pepper_green}（非 red_pepper / green_pepper）。</p>
 */
public class LatiaoCraft2Compat {

    public static final String MOD_ID = "ltc2";

    public static final String RED_PEPPER = "pepper_red";
    public static final String GREEN_PEPPER = "pepper_green";

    /**
     * 检查是否安装了辣条工艺 2
     */
    public static boolean isModLoaded() {
        return BuiltInRegistries.ITEM.keySet().stream()
                .anyMatch(loc -> loc.getNamespace().equals(MOD_ID));
    }

    /**
     * 获取辣条工艺 2 的物品
     */
    public static Item getItem(String itemId) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);
        return BuiltInRegistries.ITEM.get(loc);
    }

    /**
     * 检查物品是否是辣条工艺 2 的红辣椒（功能等价于森罗红辣椒）。
     */
    public static boolean isRedPepper(ItemStack stack) {
        // Tag 优先：red_chili tag 已包含 ltc2:pepper_red
        if (!stack.isEmpty() && stack.is(ModTags.Items.RED_CHILI)) {
            return true;
        }
        return isItem(stack, RED_PEPPER);
    }

    /**
     * 检查物品是否是辣条工艺 2 的青椒（功能等价于森罗青辣椒）。
     */
    public static boolean isGreenPepper(ItemStack stack) {
        // Tag 优先：green_chili tag 已包含 ltc2:pepper_green
        if (!stack.isEmpty() && stack.is(ModTags.Items.GREEN_CHILI)) {
            return true;
        }
        return isItem(stack, GREEN_PEPPER);
    }

    private static boolean isItem(ItemStack stack, String itemId) {
        if (stack.isEmpty()) return false;
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(loc);
    }
}
