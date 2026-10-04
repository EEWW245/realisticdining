package com.realisticdining.neoforge.client.pack;

import com.realisticdining.neoforge.network.PackFinishPacket;
import com.realisticdining.neoforge.network.PackSoundPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 材质包扩展物品的共用 GeoItem（NeoForge 1.21.1）。
 *
 * <p>所有材质包扩展物品共用一个 {@code pack_empty} GeoItem（不注册到创造栏，
 * 玩家永远拿不到）。渲染时通过 {@link PackCustomRenderer} 替换为各扩展物品的
 * 模型/材质/动画（路径由定义文件名派生）。
 *
 * <p><b>动画状态按物品隔离（修复切换物品手臂乱跑的根治方案）：</b>
 * GeckoLib 的 {@code SingletonAnimatableInstanceCache} 按实例 id（{@code GeoItem.getId(stack)}）
 * 区分 {@link AnimatableManager}。若所有物品共用同一 id（无 id 组件时为 {@code Long.MAX_VALUE}），
 * 则共享同一个 manager——controller、骨骼快照表、动画插值队列全部互相污染，
 * A 物品 hold_on_last_frame 定格姿态会成为 B 物品 pickup 动画的过渡起点 → 手臂乱跑。
 *
 * <p>本类为每个 itemId 分配一个稳定且独立的 id（{@link #getIdForItem}），渲染时传入
 * 附加了该 id 的代理 stack（{@link #getRenderStackFor}），触发/停止动画时也按同一 id
 * 定位 manager。每个物品独享一整套动画状态，任何共享污染从根上消除。
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
 *       发包到服务端，由服务端 {@code triggerAnim} 同步触发 {@code eat}</li>
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

    /**
     * 每个 itemId 的渲染代理 stack 缓存（附加 GeckoLib 独立 animatable id 组件）。
     * 渲染线程单线程访问，ConcurrentHashMap 仅作防御。
     */
    private static final Map<String, ItemStack> RENDER_STACKS = new ConcurrentHashMap<>();

    /**
     * per-item id 的 base：落在 long 高位区间（2^62 起），与 GeckoLib 计数器分配的
     * 真实 stack id（从 0 递增的小整数）以及无 id 时的 {@code Long.MAX_VALUE} 完全隔离。
     */
    private static final long PACK_ID_BASE = 0x4000000000000000L;

    /**
     * 当前正在渲染的物品 ID（由 {@link PackCustomRenderer} 在 renderByItem 时设置）。
     * <p>用于 {@link #registerControllers} 的谓词判断持物模式：
     * PICKUP 模式返回 CONTINUE 让 pickup 动画播放/定格，STATIC 模式返回 STOP。
     * <p>Minecraft 渲染线程单线程，无需同步。
     */
    private static String currentRenderItemId = null;

    public PackEmpty(Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    /**
     * 为指定扩展物品分配一个独立且稳定的 GeckoLib animatable id。
     * <p>同一 itemId 在客户端/服务端、整个会话期间恒定一致，保证：
     * <ul>
     *   <li>渲染（{@code GeoItemRenderer} 按 {@code GeoItem.getId(currentItemStack)} 取 manager）</li>
     *   <li>本地触发（{@link #triggerPickupAnimation} 等）</li>
     *   <li>服务端同步触发（{@code triggerAnim} 网络包按 instanceId 定位客户端 manager）</li>
     * </ul>
     * 三条链路始终作用于同一个 {@link AnimatableManager}；不同物品则各自独享，
     * 动画状态（骨骼快照、插值队列、controller 状态机）互不可见。
     */
    public static long getIdForItem(String itemId) {
        return PACK_ID_BASE | (itemId.hashCode() & 0xFFFFFFFFL);
    }

    /**
     * 获取（或创建）指定物品的渲染代理 stack：{@code new ItemStack(pack_empty)}
     * 并附加 GeckoLib {@code STACK_ANIMATABLE_ID} 组件 = {@link #getIdForItem}。
     * <p>渲染入口（{@code PackItemRendererMixin}）必须传此 stack 给
     * {@code renderByItem}，GeckoLib 才能按 per-item id 定位独立 manager。
     */
    public static ItemStack getRenderStackFor(String itemId) {
        return RENDER_STACKS.computeIfAbsent(itemId, key -> {
            ItemStack stack = new ItemStack(PackItems.EMPTY_ITEM);
            stack.set(GeckoLibConstants.STACK_ANIMATABLE_ID_COMPONENT.get(), getIdForItem(key));
            return stack;
        });
    }

    /** 由渲染器在每次 renderByItem 入口设置当前渲染的物品 ID。 */
    public static void setCurrentRenderItemId(String itemId) {
        currentRenderItemId = itemId;
    }

    /** 当前正在渲染的物品 ID（无渲染时为 null）。 */
    public static String getCurrentRenderItemId() {
        return currentRenderItemId;
    }

    /** 按物品 id 取其专属 {@link AnimatableManager}（不存在则由 GeckoLib 自动创建）。 */
    private static AnimatableManager<?> managerFor(String itemId) {
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null || itemId == null) return null;
        AnimatableInstanceCache cache = empty.getAnimatableInstanceCache();
        if (cache == null) return null;
        return cache.getManagerForId(getIdForItem(itemId));
    }

    /**
     * 客户端本地触发 pickup 动画（PICKUP 模式专用）。
     * <p>由 {@link PackHandChangeTracker} 检测到主手切到扩展物品时调用。
     * pickup 动画用 {@code thenPlayAndHold} 注册，播完后 GeckoLib 原生定格在最后一帧。
     * <p>manager 按 itemId 隔离：新物品的 manager 是全新状态（或该物品自己的历史状态），
     * 上一个物品的定格姿态、插值队列残留等永远无法污染本次过渡 → 手臂不乱跑。
     */
    public static void triggerPickupAnimation(String itemId) {
        AnimatableManager<?> manager = managerFor(itemId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.forceAnimationReset();
            controller.stop();
        }
        manager.tryTriggerAnimation("eat", "pickup");
    }

    /** 客户端本地触发 eat 动画。 */
    public static void triggerEatAnimation(String itemId) {
        AnimatableManager<?> manager = managerFor(itemId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.forceAnimationReset();
            controller.stop();
        }
        manager.tryTriggerAnimation("eat", "eat");
    }

    /** 停止指定物品的定格动画（只影响该物品专属的 manager）。 */
    public static void stopEatAnimation(String itemId) {
        AnimatableManager<?> manager = managerFor(itemId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.stop();
        }
    }

    /**
     * 当前物品的 eat 触发动画是否正在播放。
     * <p>供 {@link PackEatFinishWatcher} 轮询：GeckoLib 在触发动画播完后清空
     * {@code triggeredAnimation}，使本方法从 true 翻转为 false——即"动画结束"信号，
     * 不依赖动画文件里的任何标记（如 timeline finished 指令）。
     */
    public static boolean isEatAnimationPlaying(String itemId) {
        AnimatableManager<?> manager = managerFor(itemId);
        if (manager == null) return false;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        return controller != null && controller.isPlayingTriggeredAnimation();
    }

    /**
     * eat 动画播完后的统一收尾：消耗物品 + 停止定格 + 解锁快捷栏 + PICKUP 重新定格。
     * <p>两个入口共用（逻辑一致，保证行为统一）：
     * <ul>
     *   <li>材质包动画里写了 {@code timeline: "finished;"} 指令（旧约定，向后兼容）→
     *       {@link #registerControllers} 的 custom instruction handler</li>
     *   <li>材质包未写指令 → {@link PackEatFinishWatcher} 检测到动画播完自动调用</li>
     * </ul>
     */
    public static void finishEatAnimation(String itemId) {
        AnimatableManager<?> manager = managerFor(itemId);
        AnimationController<?> controller = manager == null ? null : manager.getAnimationControllers().get("eat");
        if (PackAnimationLock.lockedStack != null) {
            // 发包让服务端真正消耗主手物品
            PackFinishPacket.sendToServer(InteractionHand.MAIN_HAND);
        }
        if (controller != null) {
            controller.stop();
        }
        // 停止原版使用状态：创造模式下右键空气可能触发原版 Item.use → SUCCESS → usingItem 状态，
        // 该状态会阻止玩家用数字键/滚轮切换槽位，导致"主手被锁"的体验。
        // 动画播完后必须 stopUsingItem，确保解锁后槽位可自由切换。
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.isUsingItem()) {
            mc.player.stopUsingItem();
        }
        PackAnimationLock.unlock();
        // PICKUP 模式：eat 播完后若主手仍有该物品（堆叠 > 1），重新触发 pickup 定格
        // 服务端消耗是异步的，但客户端主手此时仍有物品（堆叠未减），
        // 立即触发 pickup 视觉无缝衔接；若堆叠 = 1 则下一帧主手变空，
        // PackHandChangeTracker 会检测到并停止动画
        if (mc.player != null) {
            ItemStack mainHand = mc.player.getMainHandItem();
            // 仅当主手仍有该物品（堆叠 > 1）才重新触发 pickup 定格；
            // 堆叠 == 1 时服务端即将消耗光，重新触发会导致定格在初始帧 + 主手锁定
            if (!mainHand.isEmpty() && mainHand.getCount() > 1) {
                ResourceLocation handId = BuiltInRegistries.ITEM.getKey(mainHand.getItem());
                if (handId != null
                        && PackDefinitionManager.containsItem(handId.toString())
                        && PackDefinitionManager.getMode(handId.toString()) == PackMode.PICKUP) {
                    PackEmpty.triggerPickupAnimation(handId.toString());
                }
            } else {
                // 堆叠耗尽：清空渲染物品 ID，让控制器谓词返回 STOP，
                // 否则 currentRenderItemId 残留导致动画定格在初始帧、主手无法恢复
                PackEmpty.setCurrentRenderItemId(null);
            }
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

        // 自定义指令关键帧：材质包写了 "finished" 指令（旧约定，可选）时的消耗入口；
        // 未写指令的材质包由 PackEatFinishWatcher 在动画播完后自动消耗
        controller.setCustomInstructionKeyframeHandler(keyFrames -> {
            if (keyFrames.getKeyframeData().getInstructions().contains("finished")) {
                PackEmpty.finishEatAnimation(currentRenderItemId);
            }
        });

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
