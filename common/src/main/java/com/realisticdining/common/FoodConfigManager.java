package com.realisticdining.common;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食物配置管理器（数据包可覆盖）。
 *
 * <p>本类是模组食物 buff/effect 描述类型的「公共类型来源」：
 * <ul>
 *   <li>{@link EffectSpec} - effect 描述（effect Holder、duration、amplifier）</li>
 *   <li>{@link DrinkConfig} - 饮料/零食覆盖配置（与 DrinkConsumeConfig.Entry 字段一致）</li>
 * </ul>
 *
 * <p>数据包路径：
 * <ul>
 *   <li>饮料/零食：{@code data/realisticdining/foods/drinks/<drinkId>.json}</li>
 * </ul>
 *
 * <p>查询流程：先查 override 表；没有则返回 null，调用方使用代码默认值。
 * 数据包缺失或 JSON 解析失败时，模组行为完全不变。
 *
 * <p>注：菜品（米饭、筷子等）的饱食度/饱和度/buff 不支持数据包覆盖，
 * 仅饮料/零食支持数据包覆盖。
 */
public final class FoodConfigManager {

    /** effect 描述：effect Holder、duration(ticks)、amplifier（DrinkConsumeConfig 也用这个类型） */
    public record EffectSpec(Holder<MobEffect> effect, int duration, int amplifier) {}

    /** 饮料/零食覆盖配置（与 DrinkConsumeConfig.Entry 字段一致） */
    public record DrinkConfig(int maxUses, int nutrition, float saturation,
                              List<EffectSpec> effects, List<Double> hungerCues, boolean clearHarmful) {}

    private static final Map<String, DrinkConfig> DRINK_OVERRIDES = new HashMap<>();

    private FoodConfigManager() {}

    public static void putDrinkOverride(String drinkId, DrinkConfig config) {
        DRINK_OVERRIDES.put(drinkId, config);
    }

    public static DrinkConfig getDrinkOverride(String drinkId) {
        return DRINK_OVERRIDES.get(drinkId);
    }

    /** ReloadListener 在加载新数据前先清空 override 表 */
    public static void clearOverrides() {
        DRINK_OVERRIDES.clear();
    }

    /** 解析 ResourceLocation → Holder<MobEffect>，找不到返回 null */
    public static Holder<MobEffect> resolveEffect(ResourceLocation id) {
        MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(id);
        if (effect == null) return null;
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
    }
}
