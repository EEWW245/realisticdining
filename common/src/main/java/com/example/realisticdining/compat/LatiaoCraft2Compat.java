package com.example.realisticdining.compat;

import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 辣条工艺 2 (Latiao Craft 2) 兼容类。
 *
 * <p>该模组的青椒(Green Pepper)与红辣椒(Red Pepper)与本模组的森罗物语青辣椒/红辣椒
 * 在菜板切菜功能上等价：均可放置于菜板并被刀具切成碎片。</p>
 *
 * <p>识别策略与 {@link KaleidoscopeCompat} 一致：
 * 优先匹配 {@link com.example.realisticdining.init.ModTags.Items#RED_CHILI} /
 * {@link com.example.realisticdining.init.ModTags.Items#GREEN_CHILI}（已包含 ltc2 物品），
 * 若未命中再走模组硬编码 fallback。</p>
 *
 * <p>实际 mod ID 为 {@code ltc2}（非 latiao_craft_2），
 * 物品 ID 为 {@code pepper_red} / {@code pepper_green}（非 red_pepper / green_pepper）。</p>
 */
public class LatiaoCraft2Compat {

    public static final String MOD_ID = "ltc2";

    private static Boolean loaded = null;
    private static Item redPepperItem = null;
    private static Item greenPepperItem = null;

    public static boolean isLoaded() {
        if (loaded == null) {
            loaded = ServiceHelper.getPlatformServices().isModLoaded(MOD_ID);
        }
        return loaded;
    }

    /**
     * 检查物品是否是辣条工艺 2 的红辣椒（功能等价于森罗红辣椒）。
     */
    public static boolean isRedPepper(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：red_chili tag 已包含 ltc2:pepper_red
        if (stack.is(com.example.realisticdining.init.ModTags.Items.RED_CHILI)) {
            return true;
        }
        // 模组硬编码 fallback
        if (isLoaded()) {
            Item redPepper = getRedPepper();
            if (redPepper != null && stack.getItem() == redPepper) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查物品是否是辣条工艺 2 的青椒（功能等价于森罗青辣椒）。
     */
    public static boolean isGreenPepper(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：green_chili tag 已包含 ltc2:pepper_green
        if (stack.is(com.example.realisticdining.init.ModTags.Items.GREEN_CHILI)) {
            return true;
        }
        // 模组硬编码 fallback
        if (isLoaded()) {
            Item greenPepper = getGreenPepper();
            if (greenPepper != null && stack.getItem() == greenPepper) {
                return true;
            }
        }
        return false;
    }

    private static Item getRedPepper() {
        if (redPepperItem == null && isLoaded()) {
            redPepperItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "pepper_red"));
        }
        return redPepperItem;
    }

    private static Item getGreenPepper() {
        if (greenPepperItem == null && isLoaded()) {
            greenPepperItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "pepper_green"));
        }
        return greenPepperItem;
    }
}
