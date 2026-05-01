package com.realisticdining.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CookbookMenu extends AbstractContainerMenu {
    
    private final Inventory inventory;
    
    // Client-side constructor (used by MenuType factory)
    public CookbookMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, null);
    }
    
    // Server-side constructor
    public CookbookMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        super(CookbookMenuType.get(), containerId);
        this.inventory = inventory;
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
