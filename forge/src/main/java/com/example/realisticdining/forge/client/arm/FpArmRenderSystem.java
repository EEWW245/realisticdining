package com.example.realisticdining.forge.client.arm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.example.realisticdining.forge.client.arm.drink.DrinkAnimHandler;
import com.example.realisticdining.forge.client.arm.drink.DrinkAnimRegistry;
import com.example.realisticdining.forge.client.arm.drink.DrinkAnimState;
import com.example.realisticdining.forge.network.ConsumeDrinkPacket;
import com.example.realisticdining.forge.network.ConsumeRicePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

public class FpArmRenderSystem {

    private static final LeftArmAnimatable LEFT_ARM_ANIMATABLE = LeftArmAnimatable.getInstance();
    private static final RightArmAnimatable RIGHT_ARM_ANIMATABLE = RightArmAnimatable.getInstance();
    private static final LeftArmModel LEFT_ARM_MODEL = new LeftArmModel();
    private static final RightArmModel RIGHT_ARM_MODEL = new RightArmModel();

    private static LeftArmRenderer leftArmRenderer;
    private static RightArmRenderer rightArmRenderer;
    private static boolean consumePacketSent = false;
    private static boolean startEatingPacketSent = false;

    private static boolean shouldRenderEatRice = false;
    private static boolean shouldRenderLeftHandRice = false;
    private static boolean shouldRenderDrink = false;
    private static boolean armRenderEnabled = true;

    // === v2.1.3+ pickup/putdown：主手切换检测 ===
    private static Item lastMainHandItem = null;
    private static Item pendingPickupItem = null;
    private static boolean firstMainHandCheck = true;

    public static void toggleArmRender() {
        armRenderEnabled = !armRenderEnabled;
        if (!armRenderEnabled) {
            resetEatRiceState();
            DrinkAnimRegistry.resetAll();
            lastMainHandItem = null;
            pendingPickupItem = null;
            firstMainHandCheck = true;
        }
    }

    /**
     * 触发饮用动画。按键调用，传入饮料 id（如 "mineral_water"、"milk_beer"）。
     * 在 {@link DrinkAnimRegistry} 中查找对应 handler 并触发。
     */
    public static void triggerDrink(String drinkId) {
        DrinkAnimHandler handler = DrinkAnimRegistry.byId(drinkId);
        if (handler == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        handler.trigger(mc.level.getGameTime());
    }

    /**
     * 根据玩家手中物品触发对应的饮用动画。
     * 检查双手：主手优先，主手为空或非饮料物品时检查副手。
     * 主手/副手为矿泉水/奶啤/薯片/能量棒（或附属注册的饮料）时，按 U 键触发对应动画。
     */
    public static void triggerDrinkForMainHand() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        // 主手优先
        ItemStack mainHand = mc.player.getMainHandItem();
        if (!mainHand.isEmpty()) {
            String drinkId = DrinkAnimRegistry.drinkIdForItem(mainHand.getItem());
            if (drinkId != null) {
                triggerDrink(drinkId);
                return;
            }
        }
        // 主手无匹配，检查副手
        ItemStack offHand = mc.player.getOffhandItem();
        if (!offHand.isEmpty()) {
            String drinkId = DrinkAnimRegistry.drinkIdForItem(offHand.getItem());
            if (drinkId != null) {
                triggerDrink(drinkId);
            }
        }
    }

    /** 是否有任意饮料动画正在播放（供 Mixin 判断是否屏蔽原版双手渲染）。 */
    public static boolean isDrinkPlaying() {
        return DrinkAnimRegistry.anyPlaying();
    }

    public static boolean shouldRenderDrink() {
        return shouldRenderDrink;
    }

    /**
     * 每帧更新所有饮用动画。任一饮料在播则渲染它，并设置 shouldRenderDrink 标志。
     * 动画刚自然结束时向服务端发送消耗请求（ConsumeDrinkPacket），由服务端校验主手物品并扣减。
     *
     * <p>v2.1.3+ 每帧检测主手物品变化，触发 pickup/putdown 动画。
     */
    public static void updateDrinkState(PoseStack poseStack, MultiBufferSource bufferSource,
                                         int packedLight, float partialTick) {
        shouldRenderDrink = false;
        if (!armRenderEnabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        double gameTime = mc.level.getGameTime();

        // v2.1.3+ 检测主手切换，触发 pickup/putdown
        tickMainHandChange(mc, gameTime);

        for (DrinkAnimHandler handler : DrinkAnimRegistry.all()) {
            if (handler.update(poseStack, bufferSource, packedLight, partialTick, gameTime)) {
                shouldRenderDrink = true;
            }
            if (handler.consumeJustFinished()) {
                ConsumeDrinkPacket.sendToServer(handler.id());
            }
        }

        // v2.1.3+ 检查 pendingPickup：所有 handler 都 IDLE 时触发排队的新物品 pickup
        if (pendingPickupItem != null && !DrinkAnimRegistry.anyPlaying()) {
            String drinkId = DrinkAnimRegistry.drinkIdForItem(pendingPickupItem);
            if (drinkId != null) {
                DrinkAnimHandler handler = DrinkAnimRegistry.byId(drinkId);
                if (handler != null && handler.isPickupConfigured()) {
                    handler.triggerPickup(gameTime);
                }
            }
            pendingPickupItem = null;
        }
    }

    /**
     * v2.1.3+ 检测主手物品变化，触发 pickup/putdown 动画。
     * <ul>
     *   <li>旧物品在 HOLD 态 → 触发 putdown（若未配置 putdown 则直接 reset）</li>
     *   <li>旧物品在 DRINK/PICKUP/PUTDOWN 态 → 强制 reset（动画中切物品，直接消失）</li>
     *   <li>新物品有 pickup 配置 → 触发 pickup；若被全局锁拒绝（旧 putdown 进行中）则排队 pendingPickupItem</li>
     * </ul>
     */
    private static void tickMainHandChange(Minecraft mc, double gameTime) {
        ItemStack mainHandStack = mc.player.getMainHandItem();
        Item current = mainHandStack.isEmpty() ? null : mainHandStack.getItem();

        // 第一次检查只记录，不触发（避免上线时自动播放 pickup）
        if (firstMainHandCheck) {
            lastMainHandItem = current;
            firstMainHandCheck = false;
            return;
        }

        if (current == lastMainHandItem) return;

        // 旧物品处理
        if (lastMainHandItem != null) {
            String oldDrinkId = DrinkAnimRegistry.drinkIdForItem(lastMainHandItem);
            if (oldDrinkId != null) {
                DrinkAnimHandler oldHandler = DrinkAnimRegistry.byId(oldDrinkId);
                if (oldHandler != null) {
                    DrinkAnimState.Phase oldPhase = oldHandler.phase();
                    if (oldPhase == DrinkAnimState.Phase.HOLD) {
                        oldHandler.triggerPutdown(gameTime);
                    } else if (oldPhase != DrinkAnimState.Phase.IDLE) {
                        oldHandler.reset();
                    }
                }
            }
        }

        // 新物品处理
        if (current != null) {
            String newDrinkId = DrinkAnimRegistry.drinkIdForItem(current);
            if (newDrinkId != null) {
                DrinkAnimHandler newHandler = DrinkAnimRegistry.byId(newDrinkId);
                if (newHandler != null && newHandler.isPickupConfigured()) {
                    if (!newHandler.triggerPickup(gameTime)) {
                        pendingPickupItem = current;
                    }
                }
            }
        }

        lastMainHandItem = current;
    }

    public static boolean isArmRenderEnabled() {
        return armRenderEnabled;
    }

    public static void triggerEatRiceAnimation() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack offHandItem = player.getOffhandItem();
        ItemStack mainHandItem = player.getMainHandItem();

        boolean hasRiceInOffHand = isCookedRice(offHandItem);
        boolean hasRiceInMainHand = isCookedRice(mainHandItem);
        boolean hasRice = hasRiceInOffHand || hasRiceInMainHand;
        boolean hasChopsticks = isChopsticks(mainHandItem);

        EatRiceState state = EatRiceState.getInstance();

        if (state.isEating()) {
            LEFT_ARM_ANIMATABLE.triggerBiteAnimation();
            RIGHT_ARM_ANIMATABLE.triggerBiteAnimation();
            if (mc.level != null) {
                state.startBite(mc.level.getGameTime());
            }
        } else if (hasRice && hasChopsticks) {
            if (!startEatingPacketSent) {
                ConsumeRicePacket.sendStartEatingToServer();
                startEatingPacketSent = true;
            }
            LEFT_ARM_ANIMATABLE.triggerBiteAnimation();
            RIGHT_ARM_ANIMATABLE.triggerBiteAnimation();
            if (mc.level != null) {
                state.startBite(mc.level.getGameTime());
            }
        }
    }

    public static void resetEatRiceState() {
        EatRiceState.getInstance().reset();
    }

    public static boolean shouldRenderEatRice() {
        return shouldRenderEatRice;
    }

    public static boolean shouldRenderLeftHandRice() {
        return shouldRenderLeftHandRice;
    }

    public static void updateState(PoseStack poseStack, MultiBufferSource bufferSource,
                                    int packedLight, float partialTick) {
        if (!armRenderEnabled) {
            shouldRenderEatRice = false;
            shouldRenderLeftHandRice = false;
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();

        boolean hasChopsticks = isChopsticks(mainHandItem);
        boolean hasRiceInOffHand = isCookedRice(offHandItem);
        boolean hasRiceInMainHand = isCookedRice(mainHandItem);
        boolean hasRice = hasRiceInOffHand || hasRiceInMainHand;

        EatRiceState state = EatRiceState.getInstance();
        if (mc.level != null) {
            state.update(mc.level.getGameTime());
        }

        if (hasRice && state.isFinished() && !state.shouldConsumeRice()) {
            state.reset();
            consumePacketSent = false;
            startEatingPacketSent = false;
        }

        if (state.shouldConsumeRice() && !consumePacketSent) {
            ConsumeRicePacket.sendConsumeToServer();
            consumePacketSent = true;
            state.onRiceConsumed();
        }

        if (state.shouldPlayEatSound()) {
            playEatSound(player);
            state.onEatSoundPlayed();
        }

        if (state.shouldApplySaturation()) {
            ConsumeRicePacket.sendApplySaturationToServer();
            state.onSaturationApplied();
        }

        shouldRenderEatRice = false;
        shouldRenderLeftHandRice = false;

        if (!hasRice) {
            return;
        }

        if (state.isAnimationPlaying()) {
            shouldRenderEatRice = true;

            if (leftArmRenderer == null) {
                leftArmRenderer = new LeftArmRenderer(LEFT_ARM_MODEL);
            }
            if (rightArmRenderer == null) {
                rightArmRenderer = new RightArmRenderer(RIGHT_ARM_MODEL);
            }

            poseStack.pushPose();
            leftArmRenderer.renderLeftArm(poseStack, LEFT_ARM_ANIMATABLE, bufferSource, packedLight, partialTick);
            poseStack.popPose();

            poseStack.pushPose();
            rightArmRenderer.renderRightArm(poseStack, RIGHT_ARM_ANIMATABLE, bufferSource, packedLight, partialTick);
            poseStack.popPose();
        } else {
            shouldRenderLeftHandRice = true;
            if (leftArmRenderer == null) {
                leftArmRenderer = new LeftArmRenderer(LEFT_ARM_MODEL);
            }
            poseStack.pushPose();
            leftArmRenderer.renderLeftArm(poseStack, LEFT_ARM_ANIMATABLE, bufferSource, packedLight, partialTick);
            poseStack.popPose();
        }
    }

    private static void playEatSound(Player player) {
        if (player instanceof LocalPlayer localPlayer) {
            player.getCommandSenderWorld().playSound(
                localPlayer,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EAT,
                SoundSource.PLAYERS,
                1.0F, 1.0F
            );
        }
    }

    private static boolean isChopsticks(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() == com.example.realisticdining.init.ModItems.CHOPSTICKS.get();
    }

    private static boolean isCookedRice(ItemStack stack) {
        return com.example.realisticdining.compat.KaleidoscopeCompat.isCookedRice(stack);
    }
}
