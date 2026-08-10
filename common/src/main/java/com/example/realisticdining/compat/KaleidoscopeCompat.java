package com.example.realisticdining.compat;

import com.example.realisticdining.init.ModTags;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class KaleidoscopeCompat {

    public static final String MOD_ID = "kaleidoscope_cookery";

    private static Boolean loaded = null;
    private static Item lettuceItem = null;
    private static Item oilPotItem = null;
    private static Item oilItem = null;
    private static Block stoveBlock = null;
    private static Item redPepperItem = null;
    private static Item greenChiliItem = null;
    private static Item riceItem = null;

    public static boolean isLoaded() {
        if (loaded == null) {
            loaded = ServiceHelper.getPlatformServices().isModLoaded(MOD_ID);
            System.out.println("[RD-DEBUG] KaleidoscopeCompat.isLoaded() called, kaleidoscope_cookery=" + loaded);
        }
        return loaded;
    }

    public static Item getLettuce() {
        if (lettuceItem == null && isLoaded()) {
            lettuceItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "lettuce"));
        }
        return lettuceItem;
    }

    public static Item getOilPot() {
        if (oilPotItem == null && isLoaded()) {
            oilPotItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "oil_pot"));
        }
        return oilPotItem;
    }

    /**
     * 获取森罗物语的油脂（Oil）物品。
     * 油脂物品的功能和油壶一样：可作为炒锅的油源使用，每次消耗 1 个。
     */
    public static Item getOil() {
        if (oilItem == null && isLoaded()) {
            oilItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "oil"));
        }
        return oilItem;
    }

    /**
     * 检查物品是否是森罗物语的油脂（Oil）。
     * 油脂物品的功能和油壶一样，可作为炒锅的油源使用。
     */
    public static boolean isOil(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item oil = getOil();
        return oil != null && stack.getItem() == oil;
    }

    public static Block getStove() {
        if (stoveBlock == null && isLoaded()) {
            stoveBlock = net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(new ResourceLocation(MOD_ID, "stove"));
        }
        return stoveBlock;
    }

    public static boolean isOilPot(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item oilPot = getOilPot();
        return oilPot != null && stack.getItem() == oilPot;
    }

    public static boolean hasOil(ItemStack stack) {
        if (!isOilPot(stack)) {
            return false;
        }
        return stack.getOrCreateTag().getInt("oil_count") > 0;
    }

    public static int getOilCount(ItemStack stack) {
        if (!isOilPot(stack)) {
            return 0;
        }
        return stack.getOrCreateTag().getInt("oil_count");
    }

    public static void shrinkOilCount(ItemStack stack) {
        if (!isOilPot(stack)) {
            return;
        }
        int currentCount = getOilCount(stack);
        if (currentCount > 0) {
            stack.getOrCreateTag().putInt("oil_count", currentCount - 1);
        }
    }

    public static boolean isLettuce(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：覆盖森罗/农夫乐事/整合包统一物品
        if (stack.is(ModTags.Items.LETTUCE)) {
            return true;
        }
        // 旧版硬编码 fallback（森罗未注册到 Tag 时兜底）
        if (isLoaded()) {
            Item lettuce = getLettuce();
            if (lettuce != null && stack.getItem() == lettuce) {
                return true;
            }
        }
        return FarmersDelightCompat.isCabbage(stack);
    }

    public static boolean isStove(Block block) {
        if (!isLoaded() || block == null) {
            return false;
        }
        Block stove = getStove();
        return stove != null && block == stove;
    }

    public static boolean isStoveLit(Level level, BlockState state) {
        if (!isStove(state.getBlock())) {
            return false;
        }
        if (state.hasProperty(BlockStateProperties.LIT)) {
            return state.getValue(BlockStateProperties.LIT);
        }
        return false;
    }

    public static boolean isRedChili(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：覆盖森罗/农夫乐事/整合包统一物品
        if (stack.is(ModTags.Items.RED_CHILI)) {
            return true;
        }
        // 森罗硬编码 fallback
        if (isLoaded()) {
            Item redPepper = getRedPepper();
            if (redPepper != null && stack.getItem() == redPepper) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGreenChili(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：覆盖森罗/整合包统一物品
        if (stack.is(ModTags.Items.GREEN_CHILI)) {
            return true;
        }
        // 森罗硬编码 fallback
        if (isLoaded()) {
            Item greenChili = getGreenChili();
            if (greenChili != null && stack.getItem() == greenChili) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRice(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：覆盖森罗/农夫乐事/整合包统一物品
        if (stack.is(ModTags.Items.RICE)) {
            return true;
        }
        // 森罗硬编码 fallback
        if (isLoaded()) {
            Item rice = getRice();
            if (rice != null && stack.getItem() == rice) {
                return true;
            }
        }
        // 农夫乐事熟米饭(cooked_rice)与森罗米饭功能等价
        return FarmersDelightCompat.isCookedRice(stack);
    }

    public static boolean isCookedRice(ItemStack stack) {
        return isRice(stack);
    }

    private static Item getRice() {
        if (riceItem == null && isLoaded()) {
            riceItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "cooked_rice"));
        }
        return riceItem;
    }

    public static ItemStack getRiceItem() {
        if (riceItem == null && isLoaded()) {
            riceItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "cooked_rice"));
        }
        if (riceItem != null) {
            return new ItemStack(riceItem);
        }
        // 森罗未加载时回退到农夫乐事米饭
        return FarmersDelightCompat.getCookedRiceItem();
    }

    private static Item getRedPepper() {
        if (redPepperItem == null && isLoaded()) {
            redPepperItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "red_chili"));
        }
        return redPepperItem;
    }

    private static Item getGreenChili() {
        if (greenChiliItem == null && isLoaded()) {
            greenChiliItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "green_chili"));
        }
        return greenChiliItem;
    }

    private static Item tomatoItem = null;

    private static Item getTomato() {
        if (tomatoItem == null && isLoaded()) {
            tomatoItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "tomato"));
        }
        return tomatoItem;
    }

    public static boolean isTomato(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：覆盖森罗/农夫乐事/整合包统一物品
        if (stack.is(ModTags.Items.TOMATO)) {
            return true;
        }
        // 森罗硬编码 fallback
        if (isLoaded()) {
            Item tomato = getTomato();
            if (tomato != null && stack.getItem() == tomato) {
                return true;
            }
        }
        // 农夫乐事番茄与森罗番茄功能等价
        return FarmersDelightCompat.isTomato(stack);
    }

    private static Item eggFriedRiceItem = null;

    public static Item getEggFriedRice() {
        if (eggFriedRiceItem == null && isLoaded()) {
            eggFriedRiceItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(MOD_ID, "egg_fried_rice"));
        }
        return eggFriedRiceItem;
    }

    public static boolean isEggFriedRice(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        // Tag 优先：覆盖森罗/整合包统一物品
        if (stack.is(ModTags.Items.EGG_FRIED_RICE)) {
            return true;
        }
        // 森罗硬编码 fallback
        if (isLoaded()) {
            Item eggFriedRice = getEggFriedRice();
            if (eggFriedRice != null && stack.getItem() == eggFriedRice) {
                return true;
            }
        }
        return false;
    }
}
