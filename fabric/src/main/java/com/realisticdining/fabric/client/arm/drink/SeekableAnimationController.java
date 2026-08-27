package com.realisticdining.fabric.client.arm.drink;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;

/**
 * 可冻结/解冻的动画控制器（v2.1.6+ 持物前缀模式核心）。
 *
 * <p>GeckoLib 原生没有"暂停在指定帧"的 API：
 * <ul>
 *   <li>{@code setAnimationSpeed(0)} 是把动画位置按速度缩放（位置恒为 0，跳回第 0 帧），不是暂停</li>
 *   <li>{@code thenPlayAndHold} 只能停在动画自然结尾，无法停在中间指定帧</li>
 * </ul>
 *
 * <p>本类通过覆盖 {@link #adjustTick} 实现"冻结在指定动画位置"：
 * <ul>
 *   <li>{@link #freezeAt(double)}：冻结后每帧恒定返回指定位置 → 画面定格在该帧</li>
 *   <li>{@link #unfreeze()}：设置 {@code tickOffset = 最近渲染时间 − 冻结位置}，
 *       恢复后动画位置从冻结帧无缝继续增长到结尾</li>
 * </ul>
 *
 * <p>数学原理：正常播放时动画位置 = {@code seekTime − tickOffset}；
 * 解冻时令 {@code tickOffset = lastSeekTime − frozenTick}，则下一帧位置 =
 * {@code frozenTick + (seekTime − lastSeekTime)}，即冻结帧 + 新经过的时间，无缝续播。
 */
public class SeekableAnimationController<T extends GeoAnimatable> extends AnimationController<T> {

    /** 冻结的动画位置（ticks = 秒 × 20）；null 表示未冻结。 */
    private Double frozenTick = null;
    /** 最近一次渲染帧的 seekTime（解冻时计算 tickOffset 的基准）。 */
    private double lastSeekTime = 0;

    public SeekableAnimationController(T animatable, String name, int transitionTickTime,
                                       AnimationStateHandler<T> animationHandler) {
        super(animatable, name, transitionTickTime, animationHandler);
    }

    @Override
    protected double adjustTick(double tick) {
        this.lastSeekTime = tick;
        if (this.frozenTick != null) {
            return this.frozenTick;
        }
        return super.adjustTick(tick);
    }

    /** 冻结在指定动画位置（ticks = 秒 × 20）。 */
    public void freezeAt(double animPosTicks) {
        this.frozenTick = animPosTicks;
    }

    /** 解冻并从冻结帧继续播放。未冻结时无操作。 */
    public void unfreeze() {
        if (this.frozenTick == null) return;
        this.tickOffset = this.lastSeekTime - this.frozenTick;
        this.frozenTick = null;
    }

    /** 清除冻结状态（配合 forceAnimationReset + stop 完整重置时使用）。 */
    public void clearFrozen() {
        this.frozenTick = null;
    }

    /**
     * v2.1.7+ 彻底重置 controller 状态。
     *
     * <p>GeckoLib 的 {@code forceAnimationReset + stop + tryTriggerAnimation} 标准重置链无法清除
     * {@code triggeredAnimation} / {@code currentAnimation} / {@code currentRawAnimation} 残留：
     * <ul>
     *   <li>动画自然播完后，{@code triggeredAnimation} 不为 null，{@code hasAnimationFinished()} 因
     *       {@code currentRawAnimation} 仍指向触发动画而返回 false</li>
     *   <li>下次 {@code tryTriggerAnimation} 同一个 trigger 时，{@code setAnimation} 检测到
     *       {@code rawAnimation.equals(currentRawAnimation)} 且 {@code needsAnimationReload} 已被消费为 false，
     *       走 {@code stop()} 分支，动画只播 1 帧就被打断（"切走再切回/喝完再拿没动画"现象）</li>
     * </ul>
     *
     * <p>本方法手动清空所有残留字段，确保下次 {@code tryTriggerAnimation} 时 {@code setAnimation}
     * 一定走重建队列分支，从头播放。
     */
    public void fullReset() {
        this.frozenTick = null;
        this.triggeredAnimation = null;
        this.currentAnimation = null;
        this.currentRawAnimation = null;
        this.animationQueue.clear();
        this.needsAnimationReload = true;
        this.shouldResetTick = true;
        this.animationState = State.STOPPED;
        this.justStartedTransition = false;
        this.isJustStarting = false;
    }
}
