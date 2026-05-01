package com.example.realisticdining.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WokRecipeInput implements Container {
    private final ItemStack input;
    
    public WokRecipeInput(ItemStack input) {
        this.input = input;
    }
    
    @Override
    public int getContainerSize() {
        return 1;
    }
    
    @Override
    public @NotNull ItemStack getItem(int index) {
        return index == 0 ? input : ItemStack.EMPTY;
    }
    
    @Override
    public boolean isEmpty() {
        return input.isEmpty();
    }
    
    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
    }
    
    @Override
    public void clearContent() {
    }
    
    @Override
    public int getMaxStackSize() {
        return 64;
    }
    
    @Override
    public void setChanged() {
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}