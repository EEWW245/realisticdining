package com.realisticdining.menu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.inventory.MenuType;

public class CookbookMenuType {
    
    @ExpectPlatform
    public static MenuType<CookbookMenu> get() {
        throw new AssertionError();
    }
}
