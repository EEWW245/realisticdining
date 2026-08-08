package com.realisticdining.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VendingMachineMenu extends AbstractContainerMenu {

    /** 服务端记录的方块坐标，用于库存统计；客户端为 null（不需要渲染）。 */
    @Nullable
    private final BlockPos pos;

    public VendingMachineMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, (BlockPos) null);
    }

    public VendingMachineMenu(int containerId, Inventory inventory, @Nullable BlockPos pos) {
        super(VendingMachineMenuType.get(), containerId);
        this.pos = pos;
    }

    public VendingMachineMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        super(VendingMachineMenuType.get(), containerId);
        this.pos = null;
    }

    @Nullable
    public BlockPos getPos() {
        return pos;
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
