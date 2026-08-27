package com.realisticdining.common;

import com.realisticdining.registry.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 啤酒累积计数 + 醉酒 Buff 应用器。
 *
 * <p>玩家每喝一瓶啤酒时由 {@link DrinkConsumeHandler} 调用 {@link #onBeerDrunk}，
 * 用内存 Map 累积 counter（参考 {@code ServerEatingState} 跨平台方案，玩家下线后重置）：
 * <ul>
 *   <li>距上次喝啤酒 ≤ 60 秒（1200 ticks）→ counter += 1</li>
 *   <li>否则（已超过窗口）→ counter = 1</li>
 * </ul>
 *
 * <p>累积阈值映射：
 * <ul>
 *   <li>counter &lt; 2 → 不施加醉酒</li>
 *   <li>counter ∈ [2, 3] → 微醺（{@code tipsy}，持续 30 秒）</li>
 *   <li>counter ∈ [4, 5] → 中度醉酒（{@code drunk}，持续 60 秒）</li>
 *   <li>counter ≥ 6 → 重度醉酒（{@code wasted}，持续 90 秒）</li>
 * </ul>
 *
 * <p>3 个等级是 3 个独立 MobEffect，玩家升级时会先 remove 之前所有醉酒 effect，
 * 再 add 新等级的，避免叠加显示。
 * counter 不在 Buff 持续期间重置，Buff 过期后再次喝酒时按时间窗口自动重置。
 *
 * <p>1.21.1 vanilla 的 MobEffect API 改用 {@code Holder<MobEffect>}，
 * 通过 {@link BuiltInRegistries#MOB_EFFECT} 包装。
 */
public final class BeerDrinkTracker {

    /** 累积时间窗口：60 秒（1200 ticks） */
    private static final long WINDOW_TICKS = 60L * 20L;

    private static final Map<UUID, Integer> BEER_COUNT = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> BEER_LAST = new ConcurrentHashMap<>();

    private BeerDrinkTracker() {}

    /** 玩家喝完一瓶啤酒后调用：累积 counter + 给醉酒 Buff。 */
    public static void onBeerDrunk(ServerPlayer player) {
        long now = player.level().getGameTime();
        UUID id = player.getUUID();

        Long last = BEER_LAST.get(id);
        int counter;
        if (last == null || (now - last) > WINDOW_TICKS) {
            counter = 1;
        } else {
            counter = BEER_COUNT.getOrDefault(id, 0) + 1;
        }

        BEER_COUNT.put(id, counter);
        BEER_LAST.put(id, now);

        applyDrunk(player, counter);
    }

    private static void applyDrunk(ServerPlayer player, int counter) {
        if (counter < 2) return;

        Holder<MobEffect> effect;
        int duration;
        if (counter >= 6) {
            effect = holderOf(ModEffects.WASTED);  duration = 90 * 20;
        } else if (counter >= 4) {
            effect = holderOf(ModEffects.DRUNK);   duration = 60 * 20;
        } else {
            effect = holderOf(ModEffects.TIPSY);  duration = 30 * 20;
        }

        // remove 之前所有醉酒系列 effect，避免多个等级叠加显示
        player.removeEffect(holderOf(ModEffects.TIPSY));
        player.removeEffect(holderOf(ModEffects.DRUNK));
        player.removeEffect(holderOf(ModEffects.WASTED));

        player.addEffect(new MobEffectInstance(
                effect,
                duration,
                0,
                false,
                true,
                true
        ));
    }

    /** 1.21.1 vanilla API 需要通过注册表包装为 Holder。 */
    private static Holder<MobEffect> holderOf(java.util.function.Supplier<MobEffect> supplier) {
        MobEffect effect = supplier.get();
        ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect);
        if (id == null) {
            return Holder.direct(effect);
        }
        net.minecraft.resources.ResourceKey<MobEffect> key =
                net.minecraft.resources.ResourceKey.create(BuiltInRegistries.MOB_EFFECT.key(), id);
        return BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(key);
    }

    /** 玩家下线时调用，清除累积计数。 */
    public static void reset(UUID playerId) {
        BEER_COUNT.remove(playerId);
        BEER_LAST.remove(playerId);
    }
}
