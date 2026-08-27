package com.realisticdining.fabric.client.arm.drink;

import net.minecraft.Util;

/**
 * 饮用动画多阶段状态机（v2.1.3+）。
 *
 * <p>五阶段：
 * <ul>
 *   <li>{@link Phase#IDLE}：无动画，玩家未持物或持物但无 pickup 配置</li>
 *   <li>{@link Phase#PICKUP}：播放"拿起"动画，播完进 HOLD（定格最后一帧）</li>
 *   <li>{@link Phase#HOLD}：持物等待，定格在 pickup 最后一帧，等待 U 键或切物品</li>
 *   <li>{@link Phase#DRINK}：播放"食用"动画，播完回 IDLE 并触发消耗包</li>
 *   <li>{@link Phase#PUTDOWN}：播放"放下"动画，播完回 IDLE</li>
 * </ul>
 *
 * <p>状态流转：
 * <pre>
 *   IDLE ──拿到物品──▶ PICKUP ──播完──▶ HOLD ──按U键──▶ DRINK ──播完──▶ IDLE
 *                                    └──切物品──▶ PUTDOWN ──播完──▶ IDLE
 *   IDLE ──按U键(无pickup配置)──▶ DRINK ──播完──▶ IDLE   （旧饮料兼容路径）
 * </pre>
 *
 * <p>PICKUP 中按 U 键：排队（pendingDrink），PICKUP 播完进 HOLD 时自动触发 DRINK。
 *
 * <p>时间源双轨制（v2.0.7+ 修复音画不同步）：
 * <ul>
 *   <li>gameTime + partialTick：1.21.1 GeckoLib 4.7+ 高精度判定阶段结束</li>
 *   <li>realTimeMillis：音效 cue 触发判定，TPS 低时也能准点</li>
 * </ul>
 */
public class DrinkAnimState {

    public enum Phase { IDLE, PICKUP, HOLD, DRINK, PUTDOWN }

    private final double drinkDuration;
    private double pickupDuration = 0;
    private double putdownDuration = 0;
    private boolean pickupConfigured = false;
    // === v2.1.4+ 持物前缀模式 ===
    private double holdDuration = 0;
    private boolean holdPrefixConfigured = false;
    // === v2.1.6+ DRINK 起始偏移 ===
    // HOLD 续播时 = holdDuration（动画从冻结帧继续，剩余时长 = duration - holdDuration）；
    // IDLE 新播时 = 0（完整时长 duration）。
    private double drinkStartOffset = 0;

    private Phase phase = Phase.IDLE;
    private double phaseStartTime = -1;        // gameTime
    private long phaseStartRealTimeMillis = 0L;
    private boolean pendingDrink = false;       // PICKUP 中按 U 键排队
    private boolean drinkJustFinished = false;  // DRINK 自然结束标志

    public DrinkAnimState(double drinkDuration) {
        this.drinkDuration = drinkDuration;
    }

    /**
     * 配置 pickup/putdown 时长。
     * 由 {@link DrinkAnimHandler#configurePickup} 在附属调用 registerPickup 时触发。
     * 未配置时该 handler 走旧路径（IDLE ↔ DRINK，无 PICKUP/HOLD/PUTDOWN）。
     */
    public void configurePickup(double pickupDur, double putdownDur) {
        this.pickupDuration = pickupDur;
        this.putdownDuration = putdownDur;
        this.pickupConfigured = true;
    }

    /**
     * v2.1.4+ 配置持物前缀模式。
     * <p>启用后，物品进入主手时播放 drink 动画的 0~holdDuration 秒并定格（PICKUP→HOLD），
     * U 键触发后从 holdDuration 继续播放到结尾（DRINK）。
     * 与 {@link #configurePickup} 互斥；两者都配置时 holdPrefix 优先。
     */
    public void configureHoldPrefix(double holdDur) {
        this.holdDuration = holdDur;
        this.holdPrefixConfigured = holdDur > 0;
    }

    /** 是否配置了任意形式的持物（独立 pickup 动画 或 持物前缀模式）。 */
    public boolean isPickupConfigured() { return pickupConfigured || holdPrefixConfigured; }

    /** v2.1.4+ 是否启用了持物前缀模式。 */
    public boolean isHoldPrefixConfigured() { return holdPrefixConfigured; }

    /** v2.1.4+ 持物前缀时长（秒）。 */
    public double holdDuration() { return holdDuration; }
    public Phase phase() { return phase; }

    /** 开始 PICKUP 阶段。仅在 IDLE 态有效。 */
    public boolean startPickup(double gameTime) {
        if (phase != Phase.IDLE) return false;
        phase = Phase.PICKUP;
        phaseStartTime = gameTime;
        phaseStartRealTimeMillis = Util.getMillis();
        pendingDrink = false;
        return true;
    }

    /**
     * 开始 DRINK 阶段。
     * - PICKUP 态：排队 pendingDrink，PICKUP 播完进 HOLD 时自动触发
     * - HOLD 态：立即开始 DRINK
     * - IDLE 态：立即开始 DRINK（旧饮料兼容路径）
     * - DRINK/PUTDOWN 态：拒绝
     * @return true 表示已立即开始 DRINK；false 表示已排队或被拒绝
     */
    public boolean startDrink(double gameTime) {
        if (phase == Phase.PICKUP) {
            pendingDrink = true;
            return false;
        }
        if (phase == Phase.HOLD || phase == Phase.IDLE) {
            boolean fromHold = (phase == Phase.HOLD);
            phase = Phase.DRINK;
            phaseStartTime = gameTime;
            phaseStartRealTimeMillis = Util.getMillis();
            drinkJustFinished = false;
            // v2.1.6+ 持物前缀模式：HOLD 续播时动画从冻结帧继续（偏移 = holdDuration），
            // IDLE 新播时从头开始（偏移 = 0）。音效 cue 与结束判定都基于动画时间轴。
            this.drinkStartOffset = (fromHold && holdPrefixConfigured) ? holdDuration : 0;
            return true;
        }
        return false;
    }

    /** 开始 PUTDOWN 阶段。仅在 HOLD 态有效。 */
    public boolean startPutdown(double gameTime) {
        if (phase != Phase.HOLD) return false;
        phase = Phase.PUTDOWN;
        phaseStartTime = gameTime;
        phaseStartRealTimeMillis = Util.getMillis();
        return true;
    }

    /** 1.21.1 GeckoLib 4.7+：用 currentGameTime + partialTick 提高精度（判定阶段结束）。 */
    public void update(double currentGameTime, float partialTick) {
        if (phase == Phase.IDLE || phase == Phase.HOLD) return;
        if (phaseStartTime < 0) return;

        double preciseTime = currentGameTime + partialTick;
        double elapsed = (preciseTime - phaseStartTime) / 20.0;
        switch (phase) {
            case PICKUP:
                // v2.1.4+ 持物前缀模式用 holdDuration；旧 pickup 模式用 pickupDuration
                double pickupEnd = holdPrefixConfigured ? holdDuration : pickupDuration;
                if (elapsed >= pickupEnd) {
                    phase = Phase.HOLD;
                    phaseStartTime = -1;
                }
                break;
            case DRINK:
                // v2.1.6+ 动画时间轴位置 = 已播放秒数 + drinkStartOffset；
                // HOLD 续播时从 holdDuration 起算，IDLE 新播时从 0 起算，到达总时长结束
                if (elapsed + drinkStartOffset >= drinkDuration) {
                    phase = Phase.IDLE;
                    phaseStartTime = -1;
                    drinkJustFinished = true;
                }
                break;
            case PUTDOWN:
                if (elapsed >= putdownDuration) {
                    phase = Phase.IDLE;
                    phaseStartTime = -1;
                }
                break;
            default:
                break;
        }
    }

    /**
     * PICKUP 播完进 HOLD 时调用：检查是否有排队的 DRINK 请求。
     * @return true 表示有排队，handler 应立即触发 DRINK 动画
     */
    public boolean consumePendingDrink() {
        if (pendingDrink) {
            pendingDrink = false;
            return true;
        }
        return false;
    }

    /** DRINK 自然结束标志（供 handler 轮询触发消耗包）。 */
    public boolean consumeDrinkFinished() {
        if (drinkJustFinished) {
            drinkJustFinished = false;
            return true;
        }
        return false;
    }

    /** 当前阶段已播放秒数（基于 gameTime + partialTick，HOLD/IDLE 返回 0）。 */
    public double elapsed(double currentGameTime, float partialTick) {
        if (phase == Phase.IDLE || phase == Phase.HOLD || phaseStartTime < 0) return 0.0;
        double preciseTime = currentGameTime + partialTick;
        return (preciseTime - phaseStartTime) / 20.0;
    }

    /**
     * v2.1.6+ 当前在 drink 动画时间轴上的位置（秒）。
     * <p>DRINK 阶段 = 已播放秒数 + drinkStartOffset（HOLD 续播时为 holdDuration，IDLE 新播时为 0），
     * 让音效 cue / 饥饿 cue 时间轴与原动画一致。
     */
    public double elapsedInAnimation(double currentGameTime, float partialTick) {
        double e = elapsed(currentGameTime, partialTick);
        if (phase == Phase.DRINK) {
            return e + drinkStartOffset;
        }
        return e;
    }

    /** v2.1.6+ 当前 DRINK 的动画时间轴起始偏移（秒）。 */
    public double drinkStartOffset() {
        return drinkStartOffset;
    }

    /** 当前阶段已播放秒数（基于真实时间，供音效 cue 用）。 */
    public double elapsedRealSeconds() {
        if (phase == Phase.IDLE || phase == Phase.HOLD || phaseStartTime < 0) return 0.0;
        return (Util.getMillis() - phaseStartRealTimeMillis) / 1000.0;
    }

    /**
     * 是否在播放动画（PICKUP/DRINK/PUTDOWN）。
     * HOLD 不算"播放"（是持物等待），但 {@link #isActive()} 会覆盖 HOLD。
     */
    public boolean isPlaying() {
        return phase == Phase.PICKUP || phase == Phase.DRINK || phase == Phase.PUTDOWN;
    }

    /** 是否在 HOLD 态（持物等待）。 */
    public boolean isHolding() {
        return phase == Phase.HOLD;
    }

    /** 是否处于任何非 IDLE 态（PICKUP/HOLD/DRINK/PUTDOWN）。供 Mixin 判断屏蔽原版双手。 */
    public boolean isActive() {
        return phase != Phase.IDLE;
    }

    public void reset() {
        phase = Phase.IDLE;
        phaseStartTime = -1;
        phaseStartRealTimeMillis = 0L;
        pendingDrink = false;
        drinkJustFinished = false;
        drinkStartOffset = 0;
    }
}
