package com.realisticdining.menu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.inventory.MenuType;

public class VendingMachineMenuType {

    @ExpectPlatform
    public static MenuType<VendingMachineMenu> get() {
        throw new AssertionError();
    }
}
