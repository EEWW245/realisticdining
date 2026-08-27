package com.example.realisticdining.forge.client.pack;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.forge.network.PackFinishPacket;
import com.example.realisticdining.forge.network.PackSoundPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 材质包扩展物品的共用 GeoItem（Forge 1.20.1）。
 *
 * <p>所有材质包扩展物品共用一个 {@code pack_empty} GeoItem（不注册到创造栏，
 * 玩家永远拿不到）。渲染时通过 {@link PackCustomRenderer} 替换为各扩展物品的
 * 模型/材质/动画（路径由定义文件名派生）。
 *
 * <p>动画控制器只检测一个 {@code eat} 动画：
 * <ul>
 *   <li>按下 U 键 → {@link PackAnimationPacket} 触发 {@code triggerAnim("eat", "eat")}</li>
 *   <li>动画结尾的 {@code timeline: "finished;"} 自定义指令 → {@link #registerControllers}
 *       里的 {@code setCustomInstructionKeyframeHandler} 接收，执行：消耗物品 +
 *       {@code controller.stop()} + 解锁快捷栏</li>
 *   <li>{@code sound_effects} 关键帧 → {@code setSoundKeyframeHandler} 接收，按定义文件
 *       的 sounds 映射播放音效</li>
 * </ul>
 */
public class PackEmpty extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private ItemStack renderStack;

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

    public static void stopEatAnimation() {
        PackEmpty empty = PackItems.EMPTY_ITEM;
        if (empty == null) return;
        long instanceId = GeoItem.getId(empty.getRenderStack());
        AnimationController<?> controller = empty.getAnimatableInstanceCache()
                .getManagerForId(instanceId)
                .getAnimationControllers()
                .get("eat");
        if (controller != null) {
            controller.stop();
        }
    }

    /**
     * 当前正在渲染的物品 ID（由 {@link PackCustomRenderer} 在 renderByItem 时设置）。
     * <p>用于 {@link #registerControllers} 的谓词判断持物模式：
     * PICKUP 模式返回 CONTINUE 让 pickup 动画播放/定格，STATIC 模式返回 STOP。
     * <p>Minecraft 渲染线程单线程，无需同步。
     */
    private static String currentRenderItemId = null;

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
        AnimatableManager<?> manager =
                empty.getAnimatableInstanceCache().getManagerForId(instanceId);
        if (manager == null) return;
        AnimationController<?> controller = manager.getAnimationControllers().get("eat");
        if (controller != null) {
            controller.forceAnimationReset();
            controller.stop();
        }
        manager.tryTriggerAnimation("eat", "pickup");
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
            ResourceLocation itemIdRl = ForgeRegistries.ITEMS.getKey(PackAnimationLock.lockedStack.getItem());
            if (itemIdRl == null) return;
            ResourceLocation soundId = PackDefinitionManager.getSound(itemIdRl.toString(), keyFrames.getKeyframeData().getSound());
            if (soundId != null) {
                PackSoundPacket.sendToServer(soundId);
            }
        });

        // 自定义指令关键帧：检测 "finished" 触发消耗
        controller.setCustomInstructionKeyframeHandler(keyFrames -> {
            RealisticDining.LOGGER.info("[材质包扩展调试] 收到自定义指令关键帧：tick={}，instructions='{}'",
                    keyFrames.getKeyframeData().getStartTick(), keyFrames.getKeyframeData().getInstructions());
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
                    if (!mainHand.isEmpty()) {
                        ResourceLocation handId = ForgeRegistries.ITEMS.getKey(mainHand.getItem());
                        if (handId != null
                                && PackDefinitionManager.containsItem(handId.toString())
                                && PackDefinitionManager.getMode(handId.toString()) == PackMode.PICKUP) {
                            PackEmpty.triggerPickupAnimation();
                        }
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
