package com.realisticdining.fabric.event;

import com.realisticdining.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric 1.21.1 战利品表注入：往所有原版箱子（minecraft:chests/*）追加随机零食/饮料。
 *
 * <p>每次开箱以「30%~50% 区间随机抽取」的概率（由 {@link RandomChanceCondition} 控制）
 * 追加 1~2 个零食/饮料，18 种零食/饮料平等随机（等权重）。
 */
public class SnackLootInjector {

    /** 18 个可饮用/可放置的零食/饮料（与 SnackItemRegistry 一致）。 */
    private static final List<Supplier<Item>> SNACKS = List.of(
            ModItems.MINERAL_WATER,
            ModItems.MILK_BEER,
            ModItems.ENERGY_BAR,
            ModItems.PEACH_GRAPEFRUIT_TEA,
            ModItems.SPORTS_DRINK,
            ModItems.ICED_TEA,
            ModItems.COCONUT_JUICE,
            ModItems.ORANGE_JUICE,
            ModItems.SOYMILK,
            ModItems.COLA,
            ModItems.BEER,
            ModItems.POTATO_CHIPS,
            ModItems.POTATO_CHIPS_BBQ,
            ModItems.POTATO_CHIPS_CUCUMBER,
            ModItems.POTATO_CHIPS_TOMATO,
            ModItems.CRISPY_FISH_CHIPS,
            ModItems.COOKIE_BAG,
            ModItems.COOKIE_BAG_COCONUT_LATTE,
            ModItems.PEARL_MILK_TEA
    );

    private static final int SNACK_WEIGHT = 1;

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!"minecraft".equals(key.location().getNamespace())
                    || !key.location().getPath().startsWith("chests/")) {
                return;
            }

            LootPool.Builder pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    // 30%~50% 区间随机概率：命中才出现零食
                    .when(RandomChanceCondition.builder());

            // 18 种零食等权重
            for (Supplier<Item> snack : SNACKS) {
                pool.add(LootItem.lootTableItem(snack.get())
                        .setWeight(SNACK_WEIGHT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))));
            }
            tableBuilder.withPool(pool);
        });
    }
}
