package com.realisticdining.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义「醉酒」系列 MobEffect 基类。
 *
 * <p>注册 3 个独立 effect（{@code tipsy}/{@code drunk}/{@code wasted}）对应
 * 3 个等级（微醺/中度醉酒/重度醉酒），玩家在物品栏直接看到等级名称而非罗马数字。
 *
 * <p>{@link #level} 字段决定每秒叠加的原版辅助效果：
 * <ul>
 *   <li>level 0（微醺）：反胃 I</li>
 *   <li>level 1（中度醉酒）：反胃 I + 迟缓 I</li>
 *   <li>level 2（重度醉酒）：反胃 II + 迟缓 II + 挖掘疲劳 I</li>
 * </ul>
 *
 * <p>1.21.1 vanilla 的 MobEffect 用 {@link #shouldApplyEffectTickThisTick(int, int)} 决定何时调用
 * {@link #applyEffectTick}，返回 true 让 vanilla 每 tick 调用（实际由内部判断每秒刷新一次）。
 */
public class DrunkEffect extends MobEffect {

    private final int level;

    public DrunkEffect(int level) {
        // HARMFUL 让「/effect clear」和牛奶按负面处理；颜色用金棕色（RGB 230 165 35）模拟啤酒液
        super(MobEffectCategory.HARMFUL, 0xE6A523);
        this.level = Math.max(0, level);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        // 仅服务端处理，每秒刷新一次原版辅助效果
        if (entity.level().isClientSide) return true;
        if (entity.tickCount % 20 != 0) return true;

        // 反胃：所有等级都有，level 2 用 II
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, level >= 2 ? 1 : 0, false, false, false));
        // 迟缓：level >= 1 时叠加，level 2 用 II
        if (level >= 1) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, level >= 2 ? 1 : 0, false, false, false));
        }
        // 挖掘疲劳：level >= 2 时叠加
        if (level >= 2) {
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, false, false, false));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // 让 vanilla 每 tick 都调用 applyEffectTick（内部判断每秒刷新一次原版效果）
        return true;
    }
}
