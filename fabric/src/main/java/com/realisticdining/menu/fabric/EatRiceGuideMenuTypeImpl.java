package com.realisticdining.menu.fabric;

import com.realisticdining.fabric.registry.ModMenuTypes;
import com.realisticdining.menu.EatRiceGuideMenu;
import net.minecraft.world.inventory.MenuType;

public class EatRiceGuideMenuTypeImpl {
    
    public static MenuType<EatRiceGuideMenu> get() {
        return ModMenuTypes.EAT_RICE_GUIDE_MENU;
    }
}
