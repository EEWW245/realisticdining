package com.realisticdining.compat;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class KaleidoscopeCookeryCompat {

    public static final String MOD_ID = "kaleidoscope_cookery";

    // 主模组物品ID
    public static final String RED_CHILI = "red_chili";
    public static final String GREEN_CHILI = "green_chili";
    public static final String LETTUCE = "lettuce";
    public static final String COOKED_RICE = "cooked_rice";
    public static final String OIL = "oil";
    public static final String OIL_POT = "oil_pot";
    public static final String STOVE = "stove";
    public static final String TOMATO = "tomato";

    /**
     * 检查是否安装了主模组
     */
    public static boolean isModLoaded() {
        return BuiltInRegistries.ITEM.keySet().stream()
                .anyMatch(loc -> loc.getNamespace().equals(MOD_ID));
    }

    /**
     * 获取主模组的物品
     */
    public static Item getItem(String itemId) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);
        return BuiltInRegistries.ITEM.get(loc);
    }

    /**
     * 获取主模组的炉灶方块
     */
    public static Block getStove() {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, STOVE);
        return BuiltInRegistries.BLOCK.get(loc);
    }

    /**
     * 检查方块是否是主模组的炉灶
     */
    public static boolean isStove(Block block) {
        if (!isModLoaded() || block == null) {
            return false;
        }
        Block stove = getStove();
        return stove != null && block == stove;
    }

    /**
     * 检查炉灶是否点燃
     */
    public static boolean isStoveLit(Level level, BlockState state) {
        if (!isStove(state.getBlock())) {
            return false;
        }
        if (state.hasProperty(BlockStateProperties.LIT)) {
            return state.getValue(BlockStateProperties.LIT);
        }
        return false;
    }

    /**
     * 检查物品是否是主模组的红辣椒
     */
    public static boolean isRedChili(ItemStack stack) {
        return isItem(stack, RED_CHILI);
    }

    /**
     * 检查物品是否是主模组的青辣椒
     */
    public static boolean isGreenChili(ItemStack stack) {
        return isItem(stack, GREEN_CHILI);
    }

    /**
     * 检查物品是否是主模组的生菜
     */
    public static boolean isLettuce(ItemStack stack) {
        return isItem(stack, LETTUCE);
    }

    /**
     * 检查物品是否是主模组的米饭
     */
    public static boolean isCookedRice(ItemStack stack) {
        return isItem(stack, COOKED_RICE);
    }

    /**
     * 检查物品是否是主模组的油
     */
    public static boolean isOil(ItemStack stack) {
        return isItem(stack, OIL);
    }

    /**
     * 检查物品是否是主模组的番茄
     */
    public static boolean isTomato(ItemStack stack) {
        return isItem(stack, TOMATO);
    }

    /**
     * 检查物品是否是主模组的油壶
     */
    public static boolean isOilPot(ItemStack stack) {
        if (!isModLoaded() || stack.isEmpty()) {
            return false;
        }
        Item oilPot = getItem(OIL_POT);
        return oilPot != null && stack.getItem() == oilPot;
    }

    /**
     * 获取油壶数据组件类型
     */
    private static DataComponentType<Integer> getOilPotOilCountComponent() {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, "oil_pot_oil_count");
        return (DataComponentType<Integer>) BuiltInRegistries.DATA_COMPONENT_TYPE.get(loc);
    }

    /**
     * 检查油壶是否有油
     */
    public static boolean hasOil(ItemStack stack) {
        if (!isOilPot(stack)) {
            return false;
        }
        DataComponentType<Integer> oilCountComponent = getOilPotOilCountComponent();
        if (oilCountComponent == null) {
            return false;
        }
        int count = stack.getOrDefault(oilCountComponent, 0);
        return count > 0;
    }

    /**
     * 获取油壶中的油量
     */
    public static int getOilCount(ItemStack stack) {
        if (!isOilPot(stack)) {
            return 0;
        }
        DataComponentType<Integer> oilCountComponent = getOilPotOilCountComponent();
        if (oilCountComponent == null) {
            return 0;
        }
        return stack.getOrDefault(oilCountComponent, 0);
    }

    /**
     * 减少油壶中的油量
     */
    public static void shrinkOilCount(ItemStack stack) {
        if (!isOilPot(stack)) {
            return;
        }
        DataComponentType<Integer> oilCountComponent = getOilPotOilCountComponent();
        if (oilCountComponent == null) {
            return;
        }
        int currentCount = getOilCount(stack);
        if (currentCount > 0) {
            stack.set(oilCountComponent, currentCount - 1);
        }
    }

    public static final String EGG_FRIED_RICE = "egg_fried_rice";

    public static boolean isEggFriedRice(ItemStack stack) {
        return isItem(stack, EGG_FRIED_RICE);
    }

    public static Item getEggFriedRice() {
        return getItem(EGG_FRIED_RICE);
    }

    private static boolean isItem(ItemStack stack, String itemId) {
        if (stack.isEmpty()) return false;
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, itemId);
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(loc);
    }
}
