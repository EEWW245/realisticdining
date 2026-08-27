package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.effects.DrunkEffect;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import java.util.function.Supplier;

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

    private static final PlatformRegistry<MobEffect> EFFECTS =
            ServiceHelper.getPlatformServices().createRegistry(Registries.MOB_EFFECT, RealisticDining.MOD_ID);

    /** 微醺（level 0）：反胃 I */
    public static final Supplier<MobEffect> TIPSY = register("tipsy", () -> new DrunkEffect(0));
    /** 中度醉酒（level 1）：反胃 I + 迟缓 I */
    public static final Supplier<MobEffect> DRUNK = register("drunk", () -> new DrunkEffect(1));
    /** 重度醉酒（level 2）：反胃 II + 迟缓 II + 挖掘疲劳 I */
    public static final Supplier<MobEffect> WASTED = register("wasted", () -> new DrunkEffect(2));

    private static <T extends MobEffect> Supplier<T> register(String name, Supplier<T> effect) {
        return EFFECTS.register(new ResourceLocation(RealisticDining.MOD_ID, name), effect);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
