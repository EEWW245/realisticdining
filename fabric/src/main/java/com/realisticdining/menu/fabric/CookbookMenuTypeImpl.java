package com.realisticdining.menu.fabric;

import com.realisticdining.fabric.registry.ModMenuTypes;
import com.realisticdining.menu.CookbookMenu;
import net.minecraft.world.inventory.MenuType;

public class CookbookMenuTypeImpl {
    
    public static MenuType<CookbookMenu> get() {
        return ModMenuTypes.COOKBOOK_MENU;
    }
}
