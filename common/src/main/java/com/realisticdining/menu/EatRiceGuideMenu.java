package com.realisticdining.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EatRiceGuideMenu extends AbstractContainerMenu {
    
    private final Inventory inventory;
    
    public EatRiceGuideMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, null);
    }
    
    public EatRiceGuideMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        super(EatRiceGuideMenuType.get(), containerId);
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
