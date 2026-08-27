package com.realisticdining.fabric.event;

import com.mojang.serialization.MapCodec;
import com.realisticdining.RealisticDining;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * 随机概率条件：每次开箱评估时，以「30%~50% 区间随机抽取」的概率通过。
 *
 * <p>用于给所有原版箱子战利品表追加零食/饮料时控制整体出现概率，
 * 命中后由外层 LootPool 的 18 种零食等权重 + 数量 1~2 决定具体产出。
 */
public class RandomChanceCondition implements LootItemCondition {

    public static final MapCodec<RandomChanceCondition> CODEC = MapCodec.unit(RandomChanceCondition::new);

    /** 必须在注册表冻结前（mod 初始化阶段）由 {@link #register()} 显式注册，不能靠静态字段懒加载。 */
    private static LootItemConditionType TYPE;

    private static final float MIN_CHANCE = 0.30f;
    private static final float CHANCE_RANGE = 0.20f;

    /** 在 onInitialize 阶段调用，注册 loot condition type（注册表冻结前）。 */
    public static void register() {
        TYPE = Registry.register(
                BuiltInRegistries.LOOT_CONDITION_TYPE,
                ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "random_chance_30_50"),
                new LootItemConditionType(CODEC)
        );
    }

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        // 30%~50% 区间随机抽取概率
        float chance = MIN_CHANCE + context.getRandom().nextFloat() * CHANCE_RANGE;
        return context.getRandom().nextFloat() < chance;
    }

    public static LootItemCondition.Builder builder() {
        return () -> new RandomChanceCondition();
    }
}
