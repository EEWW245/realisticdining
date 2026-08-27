package com.realisticdining.registry;

import com.realisticdining.RealisticDining;
import com.realisticdining.effects.DrunkEffect;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

/**
 * 模组自定义 MobEffect 注册表。
 *
 * <p>注册 3 个独立「醉酒」系列 effect，分别对应不同等级：
 * <ul>
 *   <li>{@code realisticdining:tipsy} - 微醺（level 0）</li>
 *   <li>{@code realisticdining:drunk} - 中度醉酒（level 1）</li>
 *   <li>{@code realisticdining:wasted} - 重度醉酒（level 2）</li>
 * </ul>
 *
 * <p>3 个 effect 共用 {@code drunk.png} 图标（assets/realisticdining/textures/mob_effect/drunk.png），
 * 通过各自名称区分等级。
 */
public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(RealisticDining.MOD_ID, Registries.MOB_EFFECT);

    /** 微醺（level 0）：反胃 I */
    public static final RegistrySupplier<MobEffect> TIPSY = EFFECTS.register("tipsy", () -> new DrunkEffect(0));
    /** 中度醉酒（level 1）：反胃 I + 迟缓 I */
    public static final RegistrySupplier<MobEffect> DRUNK = EFFECTS.register("drunk", () -> new DrunkEffect(1));
    /** 重度醉酒（level 2）：反胃 II + 迟缓 II + 挖掘疲劳 I */
    public static final RegistrySupplier<MobEffect> WASTED = EFFECTS.register("wasted", () -> new DrunkEffect(2));

    public static void init() {
        EFFECTS.register();
    }
}
