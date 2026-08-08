package com.realisticdining.neoforge.client.arm.drink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.realisticdining.RealisticDining;
import com.realisticdining.common.DrinkConsumeConfig;
import com.realisticdining.neoforge.network.ApplyHungerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import software.bernie.geckolib.animatable.GeoAnimatable;

/**
 * 饮用动画门面：组合 State + Animatable + Model + Renderer，并驱动音效。
 *
 * <p>v2.1.3+ 支持五阶段状态机：{@link DrinkAnimState.Phase#IDLE} →
 * {@link DrinkAnimState.Phase#PICKUP} → {@link DrinkAnimState.Phase#HOLD} →
 * {@link DrinkAnimState.Phase#DRINK} → {@link DrinkAnimState.Phase#IDLE}，
 * 以及切物品时的 {@link DrinkAnimState.Phase#PUTDOWN}。
 *
 * <p>触发入口：
 * <ul>
 *   <li>{@link #triggerPickup}：物品进入主手时由 FpArmRenderSystem 自动调用（需附属配置 pickup）</li>
 *   <li>{@link #trigger}（= drink）：U 键调用。PICKUP 中排队，HOLD/IDLE 中立即触发</li>
 *   <li>{@link #triggerPutdown}：物品切出主手时由 FpArmRenderSystem 自动调用</li>
 * </ul>
 *
 * <p>音效只在 DRINK 阶段推进（pickup/putdown 无音效 cue）。
 */
public class DrinkAnimHandler {

    private final DrinkAnimConfig config;
    private final DrinkAnimState state;
    private final DrinkAnimAnimatable animatable;
    private final DrinkAnimModel model;
    private DrinkAnimRenderer renderer;
    // === 双模型字段（仅 config.isDualModel() 时非 null） ===
    private final DrinkAnimAnimatable animatable2;
    private final DrinkAnimModel model2;
    private DrinkAnimRenderer renderer2;

    /** 与 config.soundCues() 一一对应，标记该 cue 是否已触发（避免重复播放）。 */
    private final boolean[] cueTriggered;
    /** 与 DrinkConsumeConfig.entry(id).hungerCues() 一一对应，标记该 hungerCue 是否已触发。 */
    private final boolean[] hungerCueTriggered;
    /** 当前动画期间活跃的循环音效实例，动画结束时统一 stop()。 */
    private final List<DrinkLoopSound> activeLoops = new ArrayList<>();
    /** 标记 DRINK 动画刚自然结束，供 FpArmRenderSystem 轮询并触发消耗包。 */
    private boolean justFinished = false;
    /** 是否配置了 putdown 动画（false 时切物品直接 reset，不播放下动画）。 */
    private boolean hasPutdownAnim = false;

    public DrinkAnimHandler(DrinkAnimConfig config) {
        this.config = config;
        this.state = new DrinkAnimState(config.duration());
        this.animatable = new DrinkAnimAnimatable(config);
        this.model = new DrinkAnimModel(config);
        if (config.isDualModel()) {
            this.animatable2 = new DrinkAnimAnimatable(config, 2);
            this.model2 = new DrinkAnimModel(config, 2);
        } else {
            this.animatable2 = null;
            this.model2 = null;
        }
        this.cueTriggered = new boolean[config.soundCues().size()];
        DrinkConsumeConfig.Entry entry = DrinkConsumeConfig.entry(config.id());
        int hungerCueCount = (entry != null && entry.hungerCues() != null) ? entry.hungerCues().size() : 0;
        this.hungerCueTriggered = new boolean[hungerCueCount];
    }

    public String id() {
        return config.id();
    }

    /** 是否处于任何非 IDLE 态（PICKUP/HOLD/DRINK/PUTDOWN）。供 anyPlaying/Mixin 判断。 */
    public boolean isPlaying() {
        return state.isActive();
    }

    /** 是否配置了 pickup 动画。 */
    public boolean isPickupConfigured() {
        return state.isPickupConfigured();
    }

    /** 当前状态机阶段（供 FpArmRenderSystem 切物品判断）。 */
    public DrinkAnimState.Phase phase() {
        return state.phase();
    }

    /**
     * 配置 pickup/putdown 动画。由 {@link DrinkAnimRegistry#registerPickup} 调用。
     * @param pickupRawAnimName pickup 动画名（animation.json 内）
     * @param pickupDuration    pickup 时长（秒）
     * @param putdownRawAnimName putdown 动画名（animation.json 内，可为 null 表示不播放下动画）
     * @param putdownDuration   putdown 时长（秒，putdownRawAnimName 为 null 时忽略）
     */
    public void configurePickup(String pickupRawAnimName, double pickupDuration,
                                String putdownRawAnimName, double putdownDuration) {
        state.configurePickup(pickupDuration, putdownDuration);
        animatable.registerPickupAnim(pickupRawAnimName);
        if (animatable2 != null) {
            animatable2.registerPickupAnim(pickupRawAnimName);
        }
        this.hasPutdownAnim = putdownRawAnimName != null && !putdownRawAnimName.isEmpty();
        if (hasPutdownAnim) {
            animatable.registerPutdownAnim(putdownRawAnimName);
            if (animatable2 != null) {
                animatable2.registerPutdownAnim(putdownRawAnimName);
            }
        }
    }

    /**
     * 触发 pickup 动画。仅在 IDLE 态且无其他动画在播时有效。
     * @return true 表示已开始；false 表示被全局锁拒绝（FpArmRenderSystem 会排队 pendingPickup）
     */
    public boolean triggerPickup(double gameTime) {
        if (!state.isPickupConfigured()) return false;
        if (state.phase() != DrinkAnimState.Phase.IDLE) return false;
        if (DrinkAnimRegistry.anyPlaying()) return false;
        state.startPickup(gameTime);
        animatable.triggerPickupAnimation();
        if (animatable2 != null) {
            animatable2.triggerPickupAnimation();
        }
        return true;
    }

    /**
     * U 键触发 drink 动画。
     * <ul>
     *   <li>PICKUP 态：排队 pendingDrink，PICKUP 播完进 HOLD 时自动触发 DRINK</li>
     *   <li>HOLD 态：立即触发 DRINK</li>
     *   <li>IDLE 态（无 pickup 配置或刚切物品）：检查全局锁后立即触发 DRINK</li>
     *   <li>DRINK/PUTDOWN 态：忽略</li>
     * </ul>
     */
    public void trigger(double gameTime) {
        DrinkAnimState.Phase phase = state.phase();
        if (phase == DrinkAnimState.Phase.DRINK || phase == DrinkAnimState.Phase.PUTDOWN) return;

        if (phase == DrinkAnimState.Phase.PICKUP) {
            // 排队：update 检测 PICKUP→HOLD 时自动触发
            state.startDrink(gameTime);
            return;
        }

        if (phase == DrinkAnimState.Phase.HOLD) {
            // HOLD → DRINK，无需全局锁（自己持物中）
            startDrinkInternal(gameTime);
            return;
        }

        // IDLE 态：检查全局锁
        if (DrinkAnimRegistry.anyPlaying()) return;
        startDrinkInternal(gameTime);
    }

    private void startDrinkInternal(double gameTime) {
        resetSoundState();
        justFinished = false;
        if (state.startDrink(gameTime)) {
            animatable.triggerDrinkAnimation();
            if (animatable2 != null) {
                animatable2.triggerDrinkAnimation();
            }
        }
    }

    /**
     * 触发 putdown 动画。仅在 HOLD 态有效。
     * 未配置 putdown 动画（putdownRawAnimName 为 null）时直接 reset，瞬间消失。
     */
    public void triggerPutdown(double gameTime) {
        if (!state.isPickupConfigured()) return;
        if (state.phase() != DrinkAnimState.Phase.HOLD) return;
        if (!hasPutdownAnim) {
            reset();
            return;
        }
        state.startPutdown(gameTime);
        animatable.triggerPutdownAnimation();
        if (animatable2 != null) {
            animatable2.triggerPutdownAnimation();
        }
    }

    /** 每帧更新：推进状态机，渲染当前阶段，DRINK 阶段推进音效。返回是否渲染了。 */
    public boolean update(PoseStack poseStack, MultiBufferSource bufferSource,
                          int packedLight, float partialTick, double gameTime) {
        DrinkAnimState.Phase phaseBefore = state.phase();
        state.update(gameTime, partialTick);
        DrinkAnimState.Phase phaseAfter = state.phase();

        // PICKUP → HOLD 转换：检查排队的 DRINK
        if (phaseBefore == DrinkAnimState.Phase.PICKUP && phaseAfter == DrinkAnimState.Phase.HOLD) {
            if (state.consumePendingDrink()) {
                startDrinkInternal(gameTime);
                phaseAfter = state.phase();
            }
        }

        // DRINK → IDLE 转换：DRINK 自然结束
        if (phaseBefore == DrinkAnimState.Phase.DRINK && phaseAfter == DrinkAnimState.Phase.IDLE) {
            stopLoopSounds();
            justFinished = true;
        }

        // 非 IDLE 都要渲染（PICKUP/HOLD/DRINK/PUTDOWN）
        if (phaseAfter == DrinkAnimState.Phase.IDLE) return false;

        // 仅 DRINK 阶段推进音效和饥饿 cue
        if (phaseAfter == DrinkAnimState.Phase.DRINK) {
            double elapsed = state.elapsed(gameTime, partialTick);
            double elapsedReal = state.elapsedRealSeconds();
            updateSounds(elapsedReal);
            updateHunger(elapsed);
        }

        if (renderer == null) {
            renderer = new DrinkAnimRenderer(model, config);
        }
        poseStack.pushPose();
        renderer.renderDrink(poseStack, (GeoAnimatable) animatable, bufferSource, packedLight, partialTick);
        poseStack.popPose();

        if (config.isDualModel() && model2 != null && animatable2 != null) {
            if (renderer2 == null) {
                renderer2 = new DrinkAnimRenderer(model2, config, 2);
            }
            poseStack.pushPose();
            renderer2.renderDrink(poseStack, (GeoAnimatable) animatable2, bufferSource, packedLight, partialTick);
            poseStack.popPose();
        }
        return true;
    }

    /**
     * 音效预触发提前量（秒）。
     * <p>400 模组整合包主线程卡顿时，SoundManager.play() 从调用到 OpenAL 实际开播
     * 有 100~300ms 入队延迟。提前 50ms 触发可抵消大部分入队延迟，让音效听感上准点。
     */
    private static final double SOUND_PRE_TRIGGER_SECONDS = 0.05;

    private void updateSounds(double elapsedReal) {
        List<DrinkSoundCue> cues = config.soundCues();
        for (int i = 0; i < cues.size(); i++) {
            DrinkSoundCue cue = cues.get(i);
            if (cueTriggered[i]) continue;
            if (elapsedReal < cue.timeSeconds() - SOUND_PRE_TRIGGER_SECONDS) continue;
            cueTriggered[i] = true;
            if (cue.looping()) {
                startLoopSound(cue);
            } else {
                playOneShot(cue);
            }
        }
    }

    private void updateHunger(double elapsed) {
        DrinkConsumeConfig.Entry entry = DrinkConsumeConfig.entry(config.id());
        if (entry == null || entry.hungerCues() == null || entry.hungerCues().isEmpty()) return;
        List<Double> cues = entry.hungerCues();
        for (int i = 0; i < cues.size(); i++) {
            if (hungerCueTriggered[i]) continue;
            double cueTime = cues.get(i);
            if (elapsed < cueTime) continue;
            hungerCueTriggered[i] = true;
            ApplyHungerPacket.sendToServer(config.id());
        }
    }

    private void startLoopSound(DrinkSoundCue cue) {
        SoundEvent sound = cue.resolve();
        if (sound == null) return;
        DrinkLoopSound inst = new DrinkLoopSound(sound, SoundSource.PLAYERS);
        Minecraft.getInstance().getSoundManager().play(inst);
        activeLoops.add(inst);
    }

    private void playOneShot(DrinkSoundCue cue) {
        SoundEvent sound = cue.resolve();
        if (sound == null) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        SimpleSoundInstance inst = new SimpleSoundInstance(
                sound, SoundSource.PLAYERS, 1.0F, 1.0F,
                RandomSource.create(),
                player.getX(), player.getY(), player.getZ());
        Minecraft.getInstance().getSoundManager().play(inst);
    }

    private void stopLoopSounds() {
        for (DrinkLoopSound s : activeLoops) {
            s.stopLoop();
        }
        activeLoops.clear();
    }

    private void resetSoundState() {
        stopLoopSounds();
        Arrays.fill(cueTriggered, false);
        Arrays.fill(hungerCueTriggered, false);
    }

    public void reset() {
        resetSoundState();
        justFinished = false;
        state.reset();
    }

    /**
     * 轮询动画是否刚自然结束。返回 true 表示刚结束（仅一次），同时清除标记。
     * FpArmRenderSystem 检测到 true 后发送消耗 C2S 包。
     */
    public boolean pollFinished() {
        if (!justFinished) return false;
        justFinished = false;
        return true;
    }
}
