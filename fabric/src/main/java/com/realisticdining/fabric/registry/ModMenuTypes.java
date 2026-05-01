package com.realisticdining.fabric.registry;

import com.realisticdining.menu.CookbookMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;

public class ModMenuTypes {
    
    public static final MenuType<CookbookMenu> COOKBOOK_MENU;
    
    static {
        COOKBOOK_MENU = new MenuType<>(CookbookMenu::new, FeatureFlags.DEFAULT_FLAGS);

        Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath("realisticdining", "cookbook_menu"),
                COOKBOOK_MENU
        );
    }

    public static void register() {
    }
}