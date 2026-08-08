package com.realisticdining.fabric.registry;

import com.realisticdining.menu.CookbookMenu;
import com.realisticdining.menu.EatRiceGuideMenu;
import com.realisticdining.menu.VendingMachineMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;

public class ModMenuTypes {

    public static final MenuType<CookbookMenu> COOKBOOK_MENU;
    public static final MenuType<EatRiceGuideMenu> EAT_RICE_GUIDE_MENU;
    public static final MenuType<VendingMachineMenu> VENDING_MACHINE_MENU;

    static {
        COOKBOOK_MENU = new MenuType<>(CookbookMenu::new, FeatureFlags.DEFAULT_FLAGS);
        EAT_RICE_GUIDE_MENU = new MenuType<>(EatRiceGuideMenu::new, FeatureFlags.DEFAULT_FLAGS);
        VENDING_MACHINE_MENU = new MenuType<>(VendingMachineMenu::new, FeatureFlags.DEFAULT_FLAGS);

        Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath("realisticdining", "cookbook_menu"),
                COOKBOOK_MENU
        );
        Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath("realisticdining", "eat_rice_guide_menu"),
                EAT_RICE_GUIDE_MENU
        );
        Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath("realisticdining", "vending_machine_menu"),
                VENDING_MACHINE_MENU
        );
    }

    public static void register() {
    }
}
