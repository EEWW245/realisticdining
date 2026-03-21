package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import com.example.realisticdining.menus.WokMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType; // 引入 Forge 的菜单类型扩展
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, realisticdining.MODID);

    // 使用 IForgeMenuType.create 替换原来的 Builder
    public static final RegistryObject<MenuType<WokMenu>> WOK = MENUS.register("wok",
            () -> IForgeMenuType.create(WokMenu::new)
    );

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}