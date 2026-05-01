package com.example.realisticdining.compat;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FoodCompatibilityManager {
    
    public static boolean isFood(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.getItem().isEdible();
    }

    public static ItemStack processFood(ItemStack input) {
        if (input.is(Items.PORKCHOP)) {
            return new ItemStack(com.example.realisticdining.init.ModItems.CHOPPED_PORK.get());
        } else if (input.is(Items.CHICKEN)) {
            return new ItemStack(com.example.realisticdining.init.ModItems.CHOPPED_CHICKEN.get());
        } else if (KaleidoscopeCompat.isLettuce(input)) {
            return new ItemStack(com.example.realisticdining.init.ModItems.CHOPPED_GREENS.get());
        } else if (KaleidoscopeCompat.isRedChili(input)) {
            return new ItemStack(com.example.realisticdining.init.ModItems.CHOPPED_RED_PEPPER.get());
        }
        return ItemStack.EMPTY;
    }
}
