package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.menu.CookbookMenu;
import com.example.realisticdining.menu.EatRiceGuideMenu;
import com.example.realisticdining.menu.VendingMachineMenu;
import com.example.realisticdining.menus.WokMenu;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;
import java.util.function.Supplier;

public class ModMenus {
    private static final PlatformRegistry<MenuType<?>> MENUS = ServiceHelper.getPlatformServices().createMenuRegistry(RealisticDining.MOD_ID);

    public static final Supplier<MenuType<WokMenu>> WOK = register("wok",
            () -> new MenuType<>((id, inventory) -> new WokMenu(id, inventory), FeatureFlags.VANILLA_SET)
    );

    public static final Supplier<MenuType<EatRiceGuideMenu>> EAT_RICE_GUIDE_MENU = register("eat_rice_guide_menu",
            () -> new MenuType<>(EatRiceGuideMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<CookbookMenu>> COOKBOOK_MENU = register("cookbook_menu",
            () -> new MenuType<>(CookbookMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<VendingMachineMenu>> VENDING_MACHINE_MENU = register("vending_machine_menu",
            () -> new MenuType<>(VendingMachineMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private static <T extends MenuType<?>> Supplier<T> register(String name, Supplier<T> menu) {
        return MENUS.register(new ResourceLocation(RealisticDining.MOD_ID, name), menu);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
