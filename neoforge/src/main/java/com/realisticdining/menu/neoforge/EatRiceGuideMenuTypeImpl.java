package com.realisticdining.menu.neoforge;

import com.realisticdining.menu.EatRiceGuideMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.realisticdining.RealisticDining.MOD_ID;

public class EatRiceGuideMenuTypeImpl {
    
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.MENU, MOD_ID
    );
    
    public static final Supplier<MenuType<EatRiceGuideMenu>> EAT_RICE_GUIDE_MENU = MENU_TYPES.register(
            "eat_rice_guide_menu",
            () -> IMenuTypeExtension.create((containerId, inventory, buf) -> new EatRiceGuideMenu(containerId, inventory, buf))
    );
    
    public static MenuType<EatRiceGuideMenu> get() {
        return EAT_RICE_GUIDE_MENU.get();
    }
    
    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
