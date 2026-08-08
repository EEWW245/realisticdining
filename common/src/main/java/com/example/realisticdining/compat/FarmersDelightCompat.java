package com.example.realisticdining.compat;

import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 农夫乐事(Farmer's Delight)兼容层。
 *
 * 仅暴露与森罗物语功能等价的三个食材：
 *  - cabbage      等价于森罗的 lettuce(可当白菜上菜板、当白菜丝入锅)
 *  - tomato       等价于森罗的 tomato(可上菜板切番茄片)
 *  - cooked_rice  等价于森罗的 cooked_rice(副手右键放置米饭碗)
 *
 * 判断入口统一走 {@link KaleidoscopeCompat} 的 isLettuce / isTomato / isRice,
 * 这样调用处无需感知具体来源模组。
 */
public class FarmersDelightCompat {

    public static final String MOD_ID = "farmersdelight";

    private static Boolean loaded = null;
    private static Item cabbageItem = null;
    private static Item tomatoItem = null;
    private static Item cookedRiceItem = null;

    public static boolean isLoaded() {
        if (loaded == null) {
            loaded = ServiceHelper.getPlatformServices().isModLoaded(MOD_ID);
        }
        return loaded;
    }

    private static Item getCabbage() {
        if (cabbageItem == null && isLoaded()) {
            cabbageItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "cabbage"));
        }
        return cabbageItem;
    }

    private static Item getTomato() {
        if (tomatoItem == null && isLoaded()) {
            tomatoItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "tomato"));
        }
        return tomatoItem;
    }

    private static Item getCookedRice() {
        if (cookedRiceItem == null && isLoaded()) {
            cookedRiceItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "cooked_rice"));
        }
        return cookedRiceItem;
    }

    public static boolean isCabbage(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item cabbage = getCabbage();
        return cabbage != null && stack.getItem() == cabbage;
    }

    public static boolean isTomato(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item tomato = getTomato();
        return tomato != null && stack.getItem() == tomato;
    }

    public static boolean isCookedRice(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item cookedRice = getCookedRice();
        return cookedRice != null && stack.getItem() == cookedRice;
    }

    public static ItemStack getCookedRiceItem() {
        Item cookedRice = getCookedRice();
        return cookedRice != null ? new ItemStack(cookedRice) : ItemStack.EMPTY;
    }
}
