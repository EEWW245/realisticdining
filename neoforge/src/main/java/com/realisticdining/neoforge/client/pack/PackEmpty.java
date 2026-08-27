package com.realisticdining.neoforge.client.pack;

import com.realisticdining.neoforge.network.PackFinishPacket;
import com.realisticdining.neoforge.network.PackSoundPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 材质包扩展物品的共用 GeoItem（NeoForge 1.21.1）。
 *
 * <p>所有材质包扩展物品共用一个 {@code pack_empty} GeoItem（不注册到创造栏，
 * 玩家永远拿不到）。渲染时通过 {@link PackCustomRenderer} 替换为各扩展物品的
 * 模型/材质/动画（路径由定义文件名派生）。
 *
 * <p>动画控制器（名为 {@code eat}）支持两种持物模式，由定义文件 {@code mode} 字段决定：
 * <ul>
 *   <li>STATIC（默认）：拿到物品立即显示 3D 模型 + 程序化晃动（{@link PackHeldItemMotion}）。
 *       谓词返回 STOP，控制器不接管动画。</li>
 *   <li>PICKUP：拿到物品自动播放 pickup 动画 → GeckoLib {@code hold_on_last_frame} 定格。
 *       谓词返回 CONTINUE 让 pickup 动画播放并定格在持物姿态。</li>
 * </ul>
 *
 * <p>U 键流程（两种模式通用）：
 * <ul>
 *   <li>按下 U 键 → {@link com.realisticdining.neoforge.client.pack.PackKeyRouter}
 *       本地调用 {@link #triggerEatAnimation} 触发 {@code triggerAnim("eat", "eat")}</li>
 *   <li>动画结尾的 {@code timeline: "finished;"} 自定义指令 → {@link #registerControllers}
 *       里的 {@code setCustomInstructionKeyframeHandler} 接收，执行：消耗物品 +
 *       {@code controller.stop()} + 解锁快捷栏</li>
 *   <li>{@code sound_effects} 关键帧 → {@code setSoundKeyframeHandler} 接收，按定义文件
 *       的 sounds 映射播放音效</li>
 *   <li>PICKUP 模式：finished 后若主手仍有该物品（堆叠 > 1），重新触发 pickup 定格，
 *       视觉无缝衔接</li>
 * </ul>
 */
public class PackEmpty extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private ItemStack renderStack;

    /**
     * 当前正在渲染的物品 ID（由 {@link PackCustomRenderer} 在 renderByItem 时设置）。
     * <p>用于 {@link #registerControllers} 的谓词判断持物模式：
     * PICKUP 模式返回 CONTINUE 让 pickup 动画播放/定格，STATIC 模式返回 STOP。
     * <p>Minecraft 渲染线程单线程，无需同步。
     */
    private static String currentRenderItemId = null;

    /** 由渲染器在每次 renderByItem 入口设置当前渲染的物品 ID。 */
    public static void setCurrentRenderItemId(String itemId) {
        currentRenderItemId = itemId;
    }

    /**
     * 客户端本地触发 pickup 动画（PICKUP 模式专用）。
     * <p>由 {@link PackHandChangeTracker} 检测到主手切到扩展物品时调用。
     * pickup 动画用 {@code thenPlayAndHold} 注册，播完后 GeckoLib 原生定格在最后一帧。
     */
    public static void triggerPickupAnimation() {
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null) return;
        long instanceId = GeoItem.getId(empty.getRenderStack());
        AnimatableInstanceCache cache = empty.getAnimatableInstanceCache();
        if (cache == null) return;
        AnimatableManager<?> manager = cache.getManagerForId(instanceId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.forceAnimationReset();
            controller.stop();
        }
        manager.tryTriggerAnimation("eat", "pickup");
    }

    public PackEmpty(Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    /** 用于渲染的 ItemStack（每次返回同一个，避免反复 new）。 */
    public ItemStack getRenderStack() {
        if (renderStack == null) {
            renderStack = new ItemStack(this);
        }
        return renderStack;
    }

    /** 客户端本地触发 eat 动画。 */
    public static void triggerEatAnimation() {
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null) return;
        long instanceId = GeoItem.getId(empty.getRenderStack());
        AnimatableInstanceCache cache = empty.getAnimatableInstanceCache();
        if (cache == null) return;
        AnimatableManager<?> manager = cache.getManagerForId(instanceId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.forceAnimationReset();
            controller.stop();
        }
        manager.tryTriggerAnimation("eat", "eat");
    }

    public static void stopEatAnimation() {
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null) return;
        long instanceId = GeoItem.getId(empty.getRenderStack());
        AnimatableInstanceCache cache = empty.getAnimatableInstanceCache();
        if (cache == null) return;
        AnimatableManager<?> manager = cache.getManagerForId(instanceId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.stop();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<PackEmpty> controller = new AnimationController<>(this, "eat", 5, state -> {
            // PICKUP 模式：CONTINUE 让 pickup 动画播放 + hold_on_last_frame 定格在持物姿态
            // STATIC 模式：STOP 让 PackHeldItemMotion 接管程序化晃动渲染
            if (currentRenderItemId != null
                    && PackDefinitionManager.getMode(currentRenderItemId) == PackMode.PICKUP) {
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
        // eat 动画：U 键触发，播完自然结束（finished 指令触发消耗）
        controller.triggerableAnim("eat", RawAnimation.begin().thenPlay("eat"));
        // pickup 动画：拿到物品自动触发，播完后 hold_on_last_frame 定格在最后一帧
        controller.triggerableAnim("pickup", RawAnimation.begin().thenPlayAndHold("pickup"));

        // 音效关键帧：按定义文件的 sounds 映射查音效，发到服务端播放
        controller.setSoundKeyframeHandler(keyFrames -> {
            if (PackAnimationLock.lockedStack == null) return;
            ResourceLocation itemIdRl = BuiltInRegistries.ITEM.getKey(PackAnimationLock.lockedStack.getItem());
            if (itemIdRl == null) return;
            ResourceLocation soundId = PackDefinitionManager.getSound(itemIdRl.toString(), keyFrames.getKeyframeData().getSound());
            if (soundId != null) {
                PackSoundPacket.sendToServer(soundId);
            }
        });

        // 自定义指令关键帧：检测 "finished" 触发消耗
        controller.setCustomInstructionKeyframeHandler(keyFrames -> {
            if (keyFrames.getKeyframeData().getInstructions().contains("finished")) {
                if (PackAnimationLock.lockedStack != null) {
                    // 发包让服务端真正消耗主手物品
                    PackFinishPacket.sendToServer(InteractionHand.MAIN_HAND);
                }
                controller.stop();
                PackAnimationLock.unlock();
                // PICKUP 模式：eat 播完后若主手仍有该物品（堆叠 > 1），重新触发 pickup 定格
                // 服务端消耗是异步的，但客户端主手此时仍有物品（堆叠未减），
                // 立即触发 pickup 视觉无缝衔接；若堆叠 = 1 则下一帧主手变空，
                // PackHandChangeTracker 会检测到并停止动画
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    ItemStack mainHand = mc.player.getMainHandItem();
                    // 仅当主手仍有该物品（堆叠 > 1）才重新触发 pickup 定格；
                    // 堆叠 == 1 时服务端即将消耗光，重新触发会导致定格在初始帧 + 主手锁定
                    if (!mainHand.isEmpty() && mainHand.getCount() > 1) {
                        ResourceLocation handId = BuiltInRegistries.ITEM.getKey(mainHand.getItem());
                        if (handId != null
                                && PackDefinitionManager.containsItem(handId.toString())
                                && PackDefinitionManager.getMode(handId.toString()) == PackMode.PICKUP) {
                            PackEmpty.triggerPickupAnimation();
                        }
                    } else {
                        // 堆叠耗尽：清空渲染物品 ID，让控制器谓词返回 STOP，
                        // 否则 currentRenderItemId 残留导致动画定格在初始帧、主手无法恢复
                        PackEmpty.setCurrentRenderItemId(null);
                    }
                }
            }
        });

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
