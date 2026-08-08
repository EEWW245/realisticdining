package com.realisticdining.menu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.inventory.MenuType;

public class EatRiceGuideMenuType {
    
    @ExpectPlatform
    public static MenuType<EatRiceGuideMenu> get() {
        throw new AssertionError();
    }
}
