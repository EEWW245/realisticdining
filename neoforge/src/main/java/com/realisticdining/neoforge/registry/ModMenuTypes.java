package com.realisticdining.neoforge.registry;

import com.realisticdining.menu.CookbookMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.realisticdining.RealisticDining.MOD_ID;

public class ModMenuTypes {
    
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.MENU, MOD_ID
    );
    
    public static final Supplier<MenuType<CookbookMenu>> COOKBOOK_MENU = MENU_TYPES.register(
            "cookbook_menu",
            () -> IMenuTypeExtension.create((containerId, inventory, buf) -> new CookbookMenu(containerId, inventory, buf))
    );
    
    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
