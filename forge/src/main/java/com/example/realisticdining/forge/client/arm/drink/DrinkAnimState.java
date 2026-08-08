package com.example.realisticdining.forge.client.arm.drink;

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
 *   <li>gameTime：判定阶段结束，与 GeckoLib 动画推进一致</li>
 *   <li>realTimeMillis：音效 cue 触发判定，TPS 低时也能准点</li>
 * </ul>
 */
public class DrinkAnimState {

    public enum Phase { IDLE, PICKUP, HOLD, DRINK, PUTDOWN }

    private final double drinkDuration;
    private double pickupDuration = 0;
    private double putdownDuration = 0;
    private boolean pickupConfigured = false;

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

    public boolean isPickupConfigured() { return pickupConfigured; }
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
            phase = Phase.DRINK;
            phaseStartTime = gameTime;
            phaseStartRealTimeMillis = Util.getMillis();
            drinkJustFinished = false;
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

    /** 1.20.1 GeckoLib：用 currentGameTime 推进状态机。 */
    public void update(double currentGameTime) {
        if (phase == Phase.IDLE || phase == Phase.HOLD) return;
        if (phaseStartTime < 0) return;

        double elapsed = (currentGameTime - phaseStartTime) / 20.0;
        switch (phase) {
            case PICKUP:
                if (elapsed >= pickupDuration) {
                    phase = Phase.HOLD;
                    phaseStartTime = -1;
                }
                break;
            case DRINK:
                if (elapsed >= drinkDuration) {
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

    /** 当前阶段已播放秒数（基于 gameTime，HOLD/IDLE 返回 0）。 */
    public double elapsed(double currentGameTime) {
        if (phase == Phase.IDLE || phase == Phase.HOLD || phaseStartTime < 0) return 0.0;
        return (currentGameTime - phaseStartTime) / 20.0;
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
    }
}
