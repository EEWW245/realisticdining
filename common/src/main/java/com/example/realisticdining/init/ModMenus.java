package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
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

    private static <T extends MenuType<?>> Supplier<T> register(String name, Supplier<T> menu) {
        return MENUS.register(new ResourceLocation(RealisticDining.MOD_ID, name), menu);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
