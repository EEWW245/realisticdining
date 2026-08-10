package com.realisticdining.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

/**
 * 服务端购买处理：扣除玩家金粒并给对应物品。
 * 在主线程上由各平台网络包 handler 调用。
 *
 * <p>每日库存：每个玩家对每种物品每日最多 6 次购买，不论从哪台售货机购买都共用同一额度，
 * 新一天自动补货。当库存用尽时给玩家发送"货物数量不足"提示。
 */
public final class VendingMachinePurchaseHandler {

    private VendingMachinePurchaseHandler() {}

    public static void handle(ServerPlayer player, ResourceLocation itemId) {
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == Items.AIR) return;

        // 1. 必须打开了售货机菜单才能购买（防止伪造网络包）
        if (!(player.containerMenu instanceof VendingMachineMenu)) {
            return;
        }

        // 2. 预检库存：仅查询不递增，剩余 0 则提示"货物数量不足"
        ServerLevel level = player.serverLevel();
        UUID playerUUID = player.getUUID();
        VendingMachineStockData stockData = VendingMachineStockData.get(level);
        if (stockData.getRemaining(level, playerUUID, itemId) <= 0) {
            player.sendSystemMessage(Component.translatable("gui.realisticdining.vending_machine.out_of_stock"));
            return;
        }

        int price = VendingMachinePrices.getPrice(itemId);
        var inventory = player.getInventory();

        // 3. 预检金粒数量
        int available = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(Items.GOLD_NUGGET)) {
                available += stack.getCount();
            }
        }
        if (available < price) {
            player.sendSystemMessage(Component.translatable("gui.realisticdining.vending_machine.insufficient_funds"));
            return;
        }

        // 4. 全部预检通过 → 记录库存（应当成功；若失败则中止交易）
        if (!stockData.tryPurchase(level, playerUUID, itemId)) {
            player.sendSystemMessage(Component.translatable("gui.realisticdining.vending_machine.out_of_stock"));
            return;
        }

        // 5. 扣金粒
        int toRemove = price;
        for (int i = 0; i < inventory.getContainerSize() && toRemove > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(Items.GOLD_NUGGET)) {
                int take = Math.min(stack.getCount(), toRemove);
                stack.shrink(take);
                toRemove -= take;
                if (stack.isEmpty()) {
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }
        }

        // 6. 给物品
        ItemStack purchased = new ItemStack(item, 1);
        if (!inventory.add(purchased)) {
            player.drop(purchased, false);
        }
    }
}
