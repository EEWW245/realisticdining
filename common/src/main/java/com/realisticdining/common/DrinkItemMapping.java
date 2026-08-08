package com.realisticdining.common;

import com.realisticdining.registry.ModItems;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * 饮料/零食物品 ↔ drinkId 双向映射（服务端可访问）。
 *
 * <p>客户端 {@code DrinkAnimRegistry} 也维护一份 item→drinkId 映射用于触发动画，
 * 但那个类在 client 包，服务端访问不到。这里提供一份服务端可访问的独立映射，
 * 供 {@code ConsumeDrinkPacket} 服务端处理时校验主手物品。
 */
public final class DrinkItemMapping {

    private static final Map<String, RegistrySupplier<Item>> DRINK_TO_SUPPLIER = new HashMap<>();

    static {
        // 瓶装饮料（2 次）
        DRINK_TO_SUPPLIER.put("mineral_water", ModItems.MINERAL_WATER);
        DRINK_TO_SUPPLIER.put("peach_grapefruit_tea", ModItems.PEACH_GRAPEFRUIT_TEA);
        DRINK_TO_SUPPLIER.put("sports_drink", ModItems.SPORTS_DRINK);
        DRINK_TO_SUPPLIER.put("iced_tea", ModItems.ICED_TEA);
        DRINK_TO_SUPPLIER.put("coconut_juice", ModItems.COCONUT_JUICE);
        DRINK_TO_SUPPLIER.put("orange_juice", ModItems.ORANGE_JUICE);
        // 罐装饮料（1 次）
        DRINK_TO_SUPPLIER.put("milk_beer", ModItems.MILK_BEER);
        DRINK_TO_SUPPLIER.put("soymilk", ModItems.SOYMILK);
        DRINK_TO_SUPPLIER.put("cola", ModItems.COLA);
        DRINK_TO_SUPPLIER.put("beer", ModItems.BEER);
        // 零食（1 次）
        DRINK_TO_SUPPLIER.put("crisp", ModItems.CRISP);
        DRINK_TO_SUPPLIER.put("potato_chips", ModItems.POTATO_CHIPS);
        DRINK_TO_SUPPLIER.put("potato_chips_bbq", ModItems.POTATO_CHIPS_BBQ);
        DRINK_TO_SUPPLIER.put("potato_chips_cucumber", ModItems.POTATO_CHIPS_CUCUMBER);
        DRINK_TO_SUPPLIER.put("potato_chips_tomato", ModItems.POTATO_CHIPS_TOMATO);
        DRINK_TO_SUPPLIER.put("crispy_fish_chips", ModItems.CRISPY_FISH_CHIPS);
        DRINK_TO_SUPPLIER.put("energy_bar", ModItems.ENERGY_BAR);
        DRINK_TO_SUPPLIER.put("cookie_bag", ModItems.COOKIE_BAG);
        DRINK_TO_SUPPLIER.put("cookie_bag_coconut_latte", ModItems.COOKIE_BAG_COCONUT_LATTE);
    }

    private DrinkItemMapping() {}

    /** 查询物品对应的 drinkId，无绑定返回 null。 */
    public static String getDrinkIdForItem(Item item) {
        for (Map.Entry<String, RegistrySupplier<Item>> e : DRINK_TO_SUPPLIER.entrySet()) {
            Item bound = e.getValue().get();
            if (bound != null && bound == item) return e.getKey();
        }
        return null;
    }

    /** 查询 drinkId 对应的物品，无绑定返回 null。 */
    public static Item getItemForDrinkId(String drinkId) {
        RegistrySupplier<Item> sup = DRINK_TO_SUPPLIER.get(drinkId);
        return sup != null ? sup.get() : null;
    }
}
