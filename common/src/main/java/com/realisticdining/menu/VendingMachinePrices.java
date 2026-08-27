package com.realisticdining.menu;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 自动售货机商品价格表（单位：金粒）
 * - sports_drink（加得乐）: 5 金粒
 * - 其余饮料（含罐装）: 3 金粒
 * - 薯片类: 2 金粒
 * - 饼干类: 2 金粒
 * - energy_bar（能量棒）: 1 金粒
 */
public final class VendingMachinePrices {

    private static final int DEFAULT_PRICE = 1;
    private static final LinkedHashMap<ResourceLocation, Integer> PRICES = new LinkedHashMap<>();

    static {
        // === 饮料（10 个） ===
        add("mineral_water", 3);
        add("milk_beer", 3);
        add("peach_grapefruit_tea", 3);
        add("sports_drink", 5);          // 加得乐：唯一 5 金粒
        add("iced_tea", 3);
        add("coconut_juice", 3);
        add("orange_juice", 3);
        add("soymilk", 3);
        add("cola", 3);
        add("beer", 3);
        add("pearl_milk_tea", 8);        // 珍珠奶茶：8 金粒

        // === 零食（8 个） ===
        add("potato_chips", 2);
        add("potato_chips_bbq", 2);
        add("potato_chips_cucumber", 2);
        add("potato_chips_tomato", 2);
        add("crispy_fish_chips", 2);
        add("energy_bar", 1);            // 能量棒
        add("cookie_bag", 2);
        add("cookie_bag_coconut_latte", 2);
    }

    private VendingMachinePrices() {}

    private static void add(String itemId, int price) {
        PRICES.put(ResourceLocation.fromNamespaceAndPath("realisticdining", itemId), price);
    }

    public static int getPrice(ResourceLocation itemId) {
        return PRICES.getOrDefault(itemId, DEFAULT_PRICE);
    }

    public static List<ResourceLocation> getAllItems() {
        return List.copyOf(PRICES.keySet());
    }
}
