package com.realisticdining.platform.neoforge;

import com.realisticdining.menu.EatRiceGuideMenu;
import com.realisticdining.menu.VendingMachineMenu;
import com.realisticdining.neoforge.network.VendingMachinePurchasePacket;
import com.realisticdining.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
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
                return new com.realisticdining.menu.CookbookMenu(containerId, inventory, null);
            }
        };
        player.openMenu(provider);
    }

    public static void openEatRiceGuideMenu(ServerPlayer player) {
        MenuProvider provider = new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("进食动画说明书");
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory inventory, net.minecraft.world.entity.player.Player player) {
                return new EatRiceGuideMenu(containerId, inventory, null);
            }
        };
        player.openMenu(provider);
    }

    public static void openVendingMachineMenu(ServerPlayer player, BlockPos pos) {
        MenuProvider provider = new SimpleMenuProvider(
                (containerId, inventory, p) -> new VendingMachineMenu(containerId, inventory, pos),
                Component.translatable("block.realisticdining.vending_machine")
        );
        player.openMenu(provider);
    }

    public static void sendVendingPurchase(ResourceLocation itemId) {
        VendingMachinePurchasePacket.sendPurchaseToServer(itemId);
    }

    public static void sendDrinkConsume(String drinkId, boolean startEating) {
        com.realisticdining.neoforge.network.ConsumeDrinkPacket.sendConsumeToServer(drinkId, startEating);
    }
}
