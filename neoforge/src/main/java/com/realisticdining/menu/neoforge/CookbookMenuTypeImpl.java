package com.realisticdining.menu.neoforge;

import com.realisticdining.neoforge.registry.ModMenuTypes;
import com.realisticdining.menu.CookbookMenu;
import net.minecraft.world.inventory.MenuType;

public class CookbookMenuTypeImpl {
    
    public static MenuType<CookbookMenu> get() {
        return ModMenuTypes.COOKBOOK_MENU.get();
    }
}
