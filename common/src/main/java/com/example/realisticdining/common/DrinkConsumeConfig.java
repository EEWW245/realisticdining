package com.example.realisticdining.common;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 饮料/零食消耗与回血配置。
 *
 * <p>每种 drinkId 对应一个 Entry：
 * <ul>
 *   <li>maxUses: 最大使用次数（瓶装饮料 2，其他 1）</li>
 *   <li>nutrition: 动画结束时一次性恢复的 food level 点数（薯片为 0，靠 hungerCues 分时段触发）</li>
 *   <li>saturation: 动画结束时一次性恢复的 saturation</li>
 *   <li>effects: 动画播完后施加的 MobEffect 列表</li>
 *   <li>hungerCues: 动画中分时段触发饱食度的秒数列表；每到该时间点 +1 food + 配置 saturation</li>
 * </ul>
 *
 * <p>1.20.1：剩余次数通过 ItemStack NBT (tag.remaining_uses) 存储。
 */
public final class DrinkConsumeConfig {

    /** effect 描述：effect、duration(ticks)、amplifier */
    public record EffectSpec(MobEffect effect, int duration, int amplifier) {}

    /**
     * clearHarmful: 动画结束清除一切负面效果（奶啤酒/豆浆，类似原版牛奶但只清负面、保留正面）。
     */
    public record Entry(int maxUses, int nutrition, float saturation, List<EffectSpec> effects, List<Double> hungerCues, boolean clearHarmful) {}

    /** 所有饮料通用的 buff：生命恢复 I 10s, 迅捷 I 20s */
    private static final List<EffectSpec> DRINK_BUFFS = List.of(
            new EffectSpec(MobEffects.REGENERATION, 10 * 20, 0),
            new EffectSpec(MobEffects.MOVEMENT_SPEED, 20 * 20, 0)
    );

    /** 运动饮料额外：力量 I 30s */
    private static final List<EffectSpec> SPORTS_DRINK_BUFFS = concat(DRINK_BUFFS,
            new EffectSpec(MobEffects.DAMAGE_BOOST, 30 * 20, 0));

    /** 能量棒 buffs：力量 I 30s, 迅捷 I 20s, 快速挖掘 I 30s */
    private static final List<EffectSpec> ENERGY_BAR_BUFFS = List.of(
            new EffectSpec(MobEffects.DAMAGE_BOOST, 30 * 20, 0),
            new EffectSpec(MobEffects.MOVEMENT_SPEED, 20 * 20, 0),
            new EffectSpec(MobEffects.DIG_SPEED, 30 * 20, 0)
    );

    /** 薯片分时段饱食度触发：动画第 3、4、5、6 秒各 +1 点饱食度（共 4 点） */
    private static final List<Double> CRISP_HUNGER_CUES = List.of(3.0, 4.0, 5.0, 6.0);

    private static final Map<String, Entry> ENTRIES = new HashMap<>();

    static {
        // 瓶装饮料（2 次，半点饱食度，饮料通用 buff）
        register("mineral_water", 2, 1, 0.5f, DRINK_BUFFS, List.of());
        register("peach_grapefruit_tea", 2, 1, 0.5f, DRINK_BUFFS, List.of());
        register("sports_drink", 2, 1, 0.5f, SPORTS_DRINK_BUFFS, List.of());
        register("iced_tea", 2, 1, 0.5f, DRINK_BUFFS, List.of());
        register("coconut_juice", 2, 1, 0.5f, DRINK_BUFFS, List.of());
        register("orange_juice", 2, 1, 0.5f, DRINK_BUFFS, List.of());
        // 罐装饮料（1 次，半点饱食度，饮料通用 buff）
        // 奶啤酒/豆浆（v2.2.5）：喝完清除一切负面效果（类似原版牛奶，但只清负面、保留正面）
        register("milk_beer", 1, 1, 0.5f, DRINK_BUFFS, List.of(), true);
        register("soymilk", 1, 1, 0.5f, DRINK_BUFFS, List.of(), true);
        register("cola", 1, 1, 0.5f, DRINK_BUFFS, List.of());
        register("beer", 1, 1, 0.5f, DRINK_BUFFS, List.of());
        // 薯片（1 次消耗，无 buff，饱食度靠 hungerCues 在 3/4/5/6 秒分时段触发）
        register("potato_chips", 1, 0, 0.0f, List.of(), CRISP_HUNGER_CUES);
        register("potato_chips_bbq", 1, 0, 0.0f, List.of(), CRISP_HUNGER_CUES);
        register("potato_chips_cucumber", 1, 0, 0.0f, List.of(), CRISP_HUNGER_CUES);
        register("potato_chips_tomato", 1, 0, 0.0f, List.of(), CRISP_HUNGER_CUES);
        register("crispy_fish_chips", 1, 0, 0.0f, List.of(), CRISP_HUNGER_CUES);
        // 饼干（1 次，6 点饱食度，无 buff；v2.2.5 由 2 点上调）
        register("cookie_bag", 1, 6, 0.6f, List.of(), List.of());
        register("cookie_bag_coconut_latte", 1, 6, 0.6f, List.of(), List.of());
        // 能量棒（1 次，6 点饱食度，能量棒 buffs）
        register("energy_bar", 1, 6, 1.0f, ENERGY_BAR_BUFFS, List.of());
        // 珍珠奶茶（1 次，1 点饱食度，清除负面 Buff，类似原版牛奶）
        register("pearl_milk_tea", 1, 1, 0.5f, DRINK_BUFFS, List.of(), true);
    }

    private DrinkConsumeConfig() {}

    private static void register(String id, int maxUses, int nutrition, float saturation, List<EffectSpec> effects, List<Double> hungerCues) {
        register(id, maxUses, nutrition, saturation, effects, hungerCues, false);
    }

    /** 带 clearHarmful 的注册重载（奶啤酒/豆浆）。 */
    private static void register(String id, int maxUses, int nutrition, float saturation, List<EffectSpec> effects, List<Double> hungerCues, boolean clearHarmful) {
        ENTRIES.put(id, new Entry(maxUses, nutrition, saturation, effects, hungerCues, clearHarmful));
    }

    private static List<EffectSpec> concat(List<EffectSpec> base, EffectSpec... more) {
        List<EffectSpec> list = new ArrayList<>(base);
        for (EffectSpec e : more) list.add(e);
        return List.copyOf(list);
    }

    /** 返回该 drinkId 对应的 Entry，无配置返回 null。 */
    public static Entry entry(String drinkId) {
        return ENTRIES.get(drinkId);
    }

    /** 返回该 drinkId 对应的最大使用次数，无配置返回 1。 */
    public static int maxUses(String drinkId) {
        Entry e = ENTRIES.get(drinkId);
        return e == null ? 1 : e.maxUses;
    }

    /** 是否为瓶装饮料（2 次）。 */
    public static boolean isBottle(String drinkId) {
        Entry e = ENTRIES.get(drinkId);
        return e != null && e.maxUses >= 2;
    }
}
