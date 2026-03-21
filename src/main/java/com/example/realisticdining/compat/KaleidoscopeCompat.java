package com.example.realisticdining.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class KaleidoscopeCompat {

    public static final String MOD_ID = "kaleidoscope_cookery";

    private static boolean loaded = false;
    private static Item lettuceItem = null;
    private static Item oilPotItem = null;
    private static Block stoveBlock = null;
    private static Item redPepperItem = null;
    private static Item riceItem = null;

    public static boolean isLoaded() {
        if (!loaded) {
            loaded = ModList.get().isLoaded(MOD_ID);
        }
        return loaded;
    }

    public static Item getLettuce() {
        if (lettuceItem == null && isLoaded()) {
            lettuceItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID, "lettuce"));
        }
        return lettuceItem;
    }

    public static Item getOilPot() {
        if (oilPotItem == null && isLoaded()) {
            oilPotItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID, "oil_pot"));
        }
        return oilPotItem;
    }

    public static Block getStove() {
        if (stoveBlock == null && isLoaded()) {
            stoveBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(MOD_ID, "stove"));
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
            return 00;
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
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item lettuce = getLettuce();
        return lettuce != null && stack.getItem() == lettuce;
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
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item redPepper = getRedPepper();
        return redPepper != null && stack.getItem() == redPepper;
    }

    public static boolean isRice(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) {
            return false;
        }
        Item rice = getRice();
        return rice != null && stack.getItem() == rice;
    }

    private static Item getRice() {
        if (riceItem == null && isLoaded()) {
            // Cooked Rice
            riceItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID, "cooked_rice"));
        }
        return riceItem;
    }

    public static ItemStack getRiceItem() {
        if (riceItem == null && isLoaded()) {
            riceItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID, "cooked_rice"));
        }
        return riceItem != null ? new ItemStack(riceItem) : ItemStack.EMPTY;
    }

    private static Item getRedPepper() {
        if (redPepperItem == null && isLoaded()) {
            // Red Chili
            redPepperItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID, "red_chili"));
        }
        return redPepperItem;
    }
}
