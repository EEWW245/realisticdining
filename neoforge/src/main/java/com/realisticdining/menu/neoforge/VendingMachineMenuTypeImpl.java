package com.realisticdining.menu.neoforge;

import com.realisticdining.neoforge.registry.ModMenuTypes;
import com.realisticdining.menu.VendingMachineMenu;
import net.minecraft.world.inventory.MenuType;

public class VendingMachineMenuTypeImpl {

    public static MenuType<VendingMachineMenu> get() {
        return ModMenuTypes.VENDING_MACHINE_MENU.get();
    }
}
