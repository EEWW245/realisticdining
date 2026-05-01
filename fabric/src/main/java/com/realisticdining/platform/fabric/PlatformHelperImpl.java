package com.realisticdining.platform.fabric;

import com.realisticdining.platform.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PlatformHelperImpl {
    
    public static PlatformHelper.Platform getPlatform() {
        return PlatformHelper.Platform.FABRIC;
    }

    public static boolean isModLoaded(String modId) {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
    }

    public static void openCookbookMenu(ServerPlayer player) {
        MenuProvider provider = new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("食谱书");
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory inventory, net.minecraft.world.entity.player.Player player) {
                return new com.realisticdining.menu.CookbookMenu(containerId, inventory, null);
            }
        };
        player.openMenu(provider);
    }
}
