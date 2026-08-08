package com.realisticdining.menu.fabric;

import com.realisticdining.fabric.registry.ModMenuTypes;
import com.realisticdining.menu.VendingMachineMenu;
import net.minecraft.world.inventory.MenuType;

public class VendingMachineMenuTypeImpl {

    public static MenuType<VendingMachineMenu> get() {
        return ModMenuTypes.VENDING_MACHINE_MENU;
    }
}
