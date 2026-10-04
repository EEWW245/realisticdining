package com.example.realisticdining.fabric.client.pack;

/**
 * eat 动画播完自动消耗监视器（Fabric 1.20.1）。
 *
 * <p><b>目的：</b>材质包动画文件不再必须手写 {@code timeline: "finished;"} 指令。
 * 本监视器在客户端 tick 中轮询当前物品的 eat 触发动画状态，GeckoLib 播完触发动画
 * 会清空 {@code triggeredAnimation}（{@code isPlayingTriggeredAnimation} 翻转为 false），
 * 即"动画结束"信号——不依赖动画文件里的任何标记。
 *
 * <p><b>与旧指令机制的关系（两者共存，向后兼容）：</b>
 * <ul>
 *   <li>旧材质包写了 {@code finished} 指令：指令在动画内部，一定早于或等于动画结束
 *       时刻触发，触发时即消耗并 {@code unlock()} → 本监视器检测到未锁定直接跳过，
 *       不会双消耗</li>
 *   <li>新材质包未写指令：只有本监视器生效，动画播完后自动消耗</li>
 * </ul>
 *
 * <p><b>状态机：</b>eat 锁定后动画可能不是立即开始（服务端 triggerAnim 回包有网络延迟，
 * fabric 1.20.1 的 eat 由服务端同步触发），故先等待观测到"正在播放"再等待"播放完毕"：
 * {@code WAITING_START → PLAYING → 播完 → finishEatAnimation}。
 * 若长时间（100 tick）未开始播放（触发包丢失/动画名写错），仅解锁不消耗，防死锁。
 */
public final class PackEatFinishWatcher {

    /** 已观测到 eat 动画开始播放（进入 PLAYING 阶段）。 */
    private static boolean seenPlaying = false;
    /** WAITING_START 阶段已等待的 tick 数（超时保护）。 */
    private static int waitStartTicks = 0;

    /** 动画开始的最长等待 tick 数（超过视为触发失败，仅解锁防死锁）。 */
    private static final int MAX_WAIT_START_TICKS = 100;

    private PackEatFinishWatcher() {
    }

    /** 每客户端 tick 由 {@link PackClientEvents} 调用。 */
    public static void tick() {
        if (!PackAnimationLock.isLocked()) {
            reset();
            return;
        }
        String itemId = PackEmpty.getCurrentRenderItemId();
        if (itemId == null) return;

        if (!seenPlaying) {
            // 等待动画开始（容忍网络触发延迟）
            if (PackEmpty.isEatAnimationPlaying(itemId)) {
                com.example.realisticdining.RealisticDining.LOGGER.info("[RD诊断] Watcher: 动画开始播放(itemId={}) → PLAYING", itemId);
                seenPlaying = true;
                waitStartTicks = 0;
            } else if (++waitStartTicks > MAX_WAIT_START_TICKS) {
                // 长时间未开始：触发包丢失或动画名写错，仅解锁防死锁（不消耗）
                com.example.realisticdining.RealisticDining.LOGGER.info("[RD诊断] Watcher: 等待动画开始超时({}tick) → 仅解锁", MAX_WAIT_START_TICKS);
                PackAnimationLock.unlock();
                reset();
            }
            return;
        }
        // PLAYING：仍在播 → 等待；翻转为 false → 播完，执行消耗
        if (PackEmpty.isEatAnimationPlaying(itemId)) return;
        com.example.realisticdining.RealisticDining.LOGGER.info("[RD诊断] Watcher: 动画播完(itemId={}) → finishEatAnimation", itemId);
        PackEmpty.finishEatAnimation(itemId);
        reset();
    }

    private static void reset() {
        seenPlaying = false;
        waitStartTicks = 0;
    }
}
