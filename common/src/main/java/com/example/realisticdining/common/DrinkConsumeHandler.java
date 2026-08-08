package com.example.realisticdining.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * 饮料/零食消耗服务端处理（1.20.1 版本，使用 ItemStack NBT）。
 *
 * <p>客户端动画播完后发送 C2S 包，服务端在此处理：
 * <ul>
 *   <li>在玩家整个物品栏（主物品栏 + 副手）中查找匹配 drinkId 的物品</li>
 *   <li>瓶装饮料：读 NBT remaining_uses，递减；归 0 则 shrink(1)</li>
 *   <li>其他饮料/零食：直接 shrink(1)</li>
 *   <li>根据 {@link DrinkConsumeConfig.Entry} 恢复饱食度并施加 buff</li>
 * </ul>
 *
 * <p>修复：原实现只检查主手，玩家在动画播放期间切换物品会导致消耗失败。
 * 现在改为全物品栏查找，确保动画播完后总能找到对应物品消耗。
 */
public final class DrinkConsumeHandler {

    private static final String REMAINING_USES_KEY = "remaining_uses";

    private DrinkConsumeHandler() {}

    public static void handle(ServerPlayer player, String drinkId) {
        DrinkConsumeConfig.Entry entry = DrinkConsumeConfig.entry(drinkId);
        if (entry == null) return;

        // 在玩家所有物品栏中查找匹配 drinkId 的物品（不限于主手）
        ItemStack target = findDrinkItem(player, drinkId);
        if (target.isEmpty()) return;

        // 1. 消耗物品（一次性 or 基于 NBT 计数）
        consumeItem(target, entry.maxUses());

        // 2. 恢复饱食度
        if (entry.nutrition() > 0) {
            player.getFoodData().eat(entry.nutrition(), entry.saturation());
        }

        // 3. 施加 buff
        for (DrinkConsumeConfig.EffectSpec spec : entry.effects()) {
            player.addEffect(new MobEffectInstance(spec.effect(), spec.duration(), spec.amplifier()));
        }
    }

    /** 遍历主物品栏 + 副手，返回第一个匹配 drinkId 的物品堆栈。 */
    private static ItemStack findDrinkItem(ServerPlayer player, String drinkId) {
        Inventory inv = player.getInventory();
        // 主物品栏（含快捷栏，共 36 格）
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (matchesDrink(stack, drinkId)) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static boolean matchesDrink(ItemStack stack, String drinkId) {
        if (stack.isEmpty()) return false;
        String actualId = DrinkItemMapping.getDrinkIdForItem(stack.getItem());
        return drinkId.equals(actualId);
    }

    /** 根据最大使用次数消耗物品：1 次直接 shrink；多次则基于 NBT remaining_uses 递减。 */
    private static void consumeItem(ItemStack stack, int maxUses) {
        if (maxUses <= 1) {
            stack.shrink(1);
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();
        int remaining = tag.contains(REMAINING_USES_KEY) ? tag.getInt(REMAINING_USES_KEY) : maxUses;

        remaining--;
        if (remaining > 0) {
            tag.putInt(REMAINING_USES_KEY, remaining);
        } else {
            tag.remove(REMAINING_USES_KEY);
            if (tag.isEmpty()) stack.setTag(null);
            stack.shrink(1);
        }
    }

    /**
     * 动画播放中分时段触发饱食度（用于薯片等"一口一口吃"的零食）。
     *
     * <p>不消耗物品，仅按 entry.saturation() 增加 1 点 food level。
     * 物品消耗在动画结束时的 {@link #handle} 中处理。
     */
    public static void applyHunger(ServerPlayer player, String drinkId) {
        DrinkConsumeConfig.Entry entry = DrinkConsumeConfig.entry(drinkId);
        if (entry == null) return;
        // 每个 cue +1 点饱食度；saturation 取 entry.saturation()（薯片 0.0），避免叠加过多饱和度
        player.getFoodData().eat(1, entry.saturation());
    }
}
