package com.realisticdining.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 农夫乐事(Farmer's Delight)兼容层。
 *
 * 仅暴露与森罗物语功能等价的三个食材：
 *  - cabbage      等价于森罗的 lettuce(可当白菜上菜板、当白菜丝入锅)
 *  - tomato       等价于森罗的 tomato(可上菜板切番茄片)
 *  - cooked_rice  等价于森罗的 cooked_rice(副手右键放置米饭碗)
 *
 * 判断入口统一走 {@link KaleidoscopeCookeryCompat} 的 isLettuce / isTomato / isCookedRice,
 * 这样调用处无需感知具体来源模组。
 */
public class FarmersDelightCompat {

    public static final String MOD_ID = "farmersdelight";

    // 农夫乐事物品ID
    public static final String CABBAGE = "cabbage";
    public static final String TOMATO = "tomato";
    public static final String COOKED_RICE = "cooked_rice";

    /**
     * 检查是否安装了农夫乐事
     */
    public static boolean isModLoaded() {
        return BuiltInRegistries.ITEM.keySet().stream()
                .anyMatch(loc -> loc.getNamespace().equals(MOD_ID));
    }

    /**
     * 获取农夫乐事的物品
     */
    public static Item getItem(String itemId) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);
        return BuiltInRegistries.ITEM.get(loc);
    }

    /**
     * 检查物品是否是农夫乐事的卷心菜(等价于森罗生菜)
     */
    public static boolean isCabbage(ItemStack stack) {
        return isItem(stack, CABBAGE);
    }

    /**
     * 检查物品是否是农夫乐事的番茄(等价于森罗番茄)
     */
    public static boolean isTomato(ItemStack stack) {
        return isItem(stack, TOMATO);
    }

    /**
     * 检查物品是否是农夫乐事的熟米饭(等价于森罗米饭)
     */
    public static boolean isCookedRice(ItemStack stack) {
        return isItem(stack, COOKED_RICE);
    }

    /**
     * 获取农夫乐事熟米饭物品(用于米饭碗破坏回收)。
     * 未安装时返回 {@link Items#AIR}。
     */
    public static Item getCookedRiceItem() {
        return getItem(COOKED_RICE);
    }

    private static boolean isItem(ItemStack stack, String itemId) {
        if (stack.isEmpty()) return false;
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(loc);
    }
}
