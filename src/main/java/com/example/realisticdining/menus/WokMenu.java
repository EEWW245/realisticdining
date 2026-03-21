package com.example.realisticdining.menus;

import com.example.realisticdining.blockentities.WokBlockEntity;
import com.example.realisticdining.init.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WokMenu extends AbstractContainerMenu {
    private final WokBlockEntity blockEntity;
    private final ContainerData data;
    
    public WokMenu(int id, Inventory inventory, WokBlockEntity blockEntity) {
        super(ModMenus.WOK.get(), id);
        this.blockEntity = blockEntity;
        this.data = new SimpleContainerData(4);
        blockEntity.startOpen(inventory.player);
        
        // 食材槽
        this.addSlot(new Slot(blockEntity, 0, 56, 17));
        // 结果槽
        this.addSlot(new Slot(blockEntity, 1, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        
        // 玩家物品栏
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // 玩家快捷栏
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
        
        this.addDataSlots(data);
    }
    
    public WokMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, getBlockEntity(inventory, buf));
    }
    
    private static WokBlockEntity getBlockEntity(Inventory inventory, FriendlyByteBuf buf) {
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (blockEntity instanceof WokBlockEntity) {
            return (WokBlockEntity) blockEntity;
        }
        throw new IllegalStateException("Block entity is not a WokBlockEntity!");
    }
    
    @Override
    public boolean stillValid(Player player) {
        return this.blockEntity.stillValid(player);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (index == 1) {
                if (!this.moveItemStackTo(itemStack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack1, itemStack);
            } else if (index != 0) {
                if (isIngredient(itemStack1)) {
                    if (!this.moveItemStackTo(itemStack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.moveItemStackTo(itemStack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemStack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, 2, 38, false)) {
                return ItemStack.EMPTY;
            }
            
            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, itemStack1);
        }
        
        return itemStack;
    }
    
    private boolean isIngredient(ItemStack stack) {
        // 使用兼容性管理器检查是否为可烹饪的食材
        return com.example.realisticdining.compat.FoodCompatibilityManager.isFood(stack);
    }
    
    @Override
    public void removed(Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }
    
    public WokBlockEntity getBlockEntity() {
        return blockEntity;
    }
    
    public int getCookProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}