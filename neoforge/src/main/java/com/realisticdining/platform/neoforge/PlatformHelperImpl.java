package com.realisticdining.platform.neoforge;

import com.realisticdining.menu.CookbookMenu;
import com.realisticdining.platform.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PlatformHelperImpl {
    
    public static PlatformHelper.Platform getPlatform() {
        return PlatformHelper.Platform.NEOFORGE;
    }

    public static boolean isModLoaded(String modId) {
        return net.neoforged.fml.ModList.get().isLoaded(modId);
    }

    public static void openCookbookMenu(ServerPlayer player) {
        MenuProvider provider = new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("食谱书");
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory inventory, net.minecraft.world.entity.player.Player player) {
                return new CookbookMenu(containerId, inventory, null);
            }
        };
        player.openMenu(provider);
    }
}
