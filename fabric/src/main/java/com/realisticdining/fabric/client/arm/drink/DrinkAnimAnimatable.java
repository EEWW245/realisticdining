package com.realisticdining.fabric.client.arm.drink;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 饮用动画的单例 GeoAnimatable。
 *
 * <p>每个饮料一份独立实例（独立 controllerName + 独立 controller）。
 * 这样多个饮料即使共用同一个 geo/animation 文件，也能各自播放、互不干扰。
 *
 * <p>双模型模式下，Handler 会创建两个 Animatable 实例，分别用第一组/第二组
 * controllerName + triggerName + rawAnimationName 注册独立 controller。
 *
 * <p>触发时先 {@link AnimationController#forceAnimationReset()} +
 * {@link AnimationController#stop()} 再 {@link AnimatableManager#tryTriggerAnimation}，
 * 避免 controller 残留 RUNNING/TRANSITIONING 态导致下次按键只显示静态骨骼。
 *
 * <p>v2.1.3+ 支持 pickup/putdown 动画：
 * <ul>
 *   <li>pickle 动画用 {@link RawAnimation#thenPlayAndHold} 注册，播完后定格最后一帧（HOLD 态持物）</li>
 *   <li>putdown 动画用 {@link RawAnimation#thenPlay} 注册，正常播完后回 IDLE</li>
 *   <li>pickup/putdown 动画必须与 drink 动画在同一个 animation.json 文件内（GeckoLib GeoModel 限制）</li>
 *   <li>附属通过 {@link DrinkAnimRegistry#registerPickup} 配置后，调用 {@link #registerPickupAnim} 动态追加 trigger</li>
 * </ul>
 */
public class DrinkAnimAnimatable implements SingletonGeoAnimatable {

    private static final long INSTANCE_ID = 0L;

    /** pickup/putdown 的 trigger 名约定（在单个 controller 内唯一即可）。 */
    private static final String PICKUP_TRIGGER = "pickup";
    private static final String PUTDOWN_TRIGGER = "putdown";

    private final DrinkAnimConfig config;
    private final String controllerName;
    private final String triggerName;
    private final String rawAnimationName;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    /** 第一组 controller（默认）。 */
    public DrinkAnimAnimatable(DrinkAnimConfig config) {
        this(config, 1);
    }

    /**
     * 指定组的 controller。
     * @param group 1 = 第一组（controllerName/triggerName/rawAnimationName），2 = 第二组
     */
    public DrinkAnimAnimatable(DrinkAnimConfig config, int group) {
        this.config = config;
        if (group == 2) {
            this.controllerName = config.controllerName2();
            this.triggerName = config.triggerName2();
            this.rawAnimationName = config.rawAnimationName2();
        } else {
            this.controllerName = config.controllerName();
            this.triggerName = config.triggerName();
            this.rawAnimationName = config.rawAnimationName();
        }
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, controllerName, 0, state -> PlayState.CONTINUE)
                .triggerableAnim(triggerName,
                        RawAnimation.begin().thenPlay(rawAnimationName)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object object) {
        if (object instanceof Entity entity) {
            return entity.tickCount;
        }
        return Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getGameTime()
                : 0;
    }

    /**
     * 动态注册 pickup 动画 trigger。
     * <p>用 {@link RawAnimation#thenPlayAndHold} 让 pickup 播完后定格在最后一帧，
     * 这样 HOLD 态（持物等待）时 GeckoLib 自动保持姿态，无需额外循环动画。
     * @param pickupRawAnimName animation.json 内的 pickup 动画名（如 "animation.bottle.pickup"）
     */
    public void registerPickupAnim(String pickupRawAnimName) {
        AnimatableManager<?> manager = this.cache.getManagerForId(INSTANCE_ID);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get(controllerName);
        if (controller == null) return;
        controller.triggerableAnim(PICKUP_TRIGGER,
                RawAnimation.begin().thenPlayAndHold(pickupRawAnimName));
    }

    /**
     * 动态注册 putdown 动画 trigger。
     * <p>用 {@link RawAnimation#thenPlay} 正常播放，播完后 GeckoLib 回到默认姿态（骨骼归零）。
     * @param putdownRawAnimName animation.json 内的 putdown 动画名（如 "animation.bottle.putdown"）
     */
    public void registerPutdownAnim(String putdownRawAnimName) {
        AnimatableManager<?> manager = this.cache.getManagerForId(INSTANCE_ID);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get(controllerName);
        if (controller == null) return;
        controller.triggerableAnim(PUTDOWN_TRIGGER,
                RawAnimation.begin().thenPlay(putdownRawAnimName));
    }

    /** 触发 drink（食用）动画。 */
    public void triggerDrinkAnimation() {
        triggerAnimInternal(triggerName);
    }

    /** 触发 pickup（拿起）动画。须先 {@link #registerPickupAnim} 注册。 */
    public void triggerPickupAnimation() {
        triggerAnimInternal(PICKUP_TRIGGER);
    }

    /** 触发 putdown（放下）动画。须先 {@link #registerPutdownAnim} 注册。 */
    public void triggerPutdownAnimation() {
        triggerAnimInternal(PUTDOWN_TRIGGER);
    }

    private void triggerAnimInternal(String animTriggerName) {
        AnimatableManager<?> manager = this.cache.getManagerForId(INSTANCE_ID);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get(controllerName);
        if (controller != null) {
            controller.forceAnimationReset(); // needsAnimationReload = true，强制重建动画队列
            controller.stop();                // animationState = STOPPED，使 tryTriggerAnimation 能切到 TRANSITIONING
        }
        manager.tryTriggerAnimation(controllerName, animTriggerName);
    }
}
