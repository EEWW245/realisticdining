package com.realisticdining.fabric.client.arm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.fabric.client.arm.drink.DrinkAnimHandler;
import com.realisticdining.fabric.client.arm.drink.DrinkAnimRegistry;
import com.realisticdining.fabric.client.arm.drink.DrinkAnimState;
import com.realisticdining.fabric.network.ConsumeRicePacket;
import com.realisticdining.platform.PlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
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
    // === v2.1.8+ 喝完后剩余堆叠重新触发 PICKUP ===
    // DRINK 自然结束时记录饮料 id + 当前堆叠数 + remaining_uses；后续帧检测到服务端同步扣减后重新触发 PICKUP
    // 瓶装饮料 maxUses=2：第一次喝只改 NBT 不 shrink（count 不变，remaining_uses 减少），需同时检测两者
    private static String pendingPickupAfterDrinkId = null;
    private static int lastMainHandCount = 0;
    private static int lastMainHandRemainingUses = 0;
    // === v2.1.9+ 超时保护 ===
    // 同种饮料不同 stack 切换时，主手 stack 不是触发 DRINK 的那个，永远不会被服务端同步扣减，
    // 会导致 pendingPickupAfterDrinkId 死等。超时（10 tick = 0.5秒）后强制触发 PICKUP 避免卡死。
    private static double pendingPickupAfterDrinkSetTime = 0;

    public static void toggleArmRender() {
        armRenderEnabled = !armRenderEnabled;
        if (!armRenderEnabled) {
            resetEatRiceState();
            DrinkAnimRegistry.resetAll();
            lastMainHandItem = null;
            pendingPickupItem = null;
            firstMainHandCheck = true;
            pendingPickupAfterDrinkId = null;
            lastMainHandCount = 0;
            lastMainHandRemainingUses = 0;
            pendingPickupAfterDrinkSetTime = 0;
        }
    }

    /**
     * 触发饮用动画。按键（U）调用，传入饮料 id（如 "mineral_water"、"milk_beer"）。
     * 在 {@link DrinkAnimRegistry} 中查找对应 handler 并触发。
     *
     * <p>v2.3.0+ 防无限刷：U 键触发"真正喝"时才标记 eating 状态（客户端本地 + 服务端）。
     * PICKUP/HOLD 持物阶段不标记，否则拿着饮料期间永远无法右键放置展示台。
     */
    public static void triggerDrink(String drinkId) {
        DrinkAnimHandler handler = DrinkAnimRegistry.byId(drinkId);
        if (handler == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        handler.trigger(mc.level.getGameTime());

        // 标记"正在喝"：客户端本地（多人模式预测）+ 服务端（packet）
        // DRINK/PUTDOWN 态重复触发时 trigger() 内部忽略，重复发送无害（服务端覆盖同值）
        ServerEatingState.setEating(mc.player.getUUID(), true);
        PlatformHelper.sendDrinkConsume(drinkId, true);
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

    /**
     * v2.1.12+ 是否处于吃米饭激活状态（主手有筷子 + 主手或副手有米饭）。
     * <p>供 HeadMixin 判断是否需要接管 renderHandsWithItems：
     * 只有主手有筷子+副手有米饭时才需要 cancel + @Invoker（触发吃米饭动画）。
     * 副手有米饭但主手没筷子（如钻石剑）时不接管，让 HMI @Redirect 正常执行，
     * HMI 改进手臂+钻石剑正常显示。
     */
    public static boolean isEatRiceActive(Player player) {
        if (player == null) return false;
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        boolean hasRiceInOffHand = com.realisticdining.compat.KaleidoscopeCookeryCompat.isCookedRice(offHandItem);
        boolean hasRiceInMainHand = com.realisticdining.compat.KaleidoscopeCookeryCompat.isCookedRice(mainHandItem);
        boolean hasRice = hasRiceInOffHand || hasRiceInMainHand;
        boolean hasChopsticks = isChopsticks(mainHandItem);
        return hasRice && hasChopsticks;
    }

    /**
     * v2.1.4+ 主手或副手是否持有饮料/零食物品。
     * <p>供 Mixin 判断是否需要调用 {@link #updateDrinkState}：即使当前无动画在播（IDLE 态），
     * 只要手里有饮料物品就必须每帧调用 updateDrinkState，否则 {@code tickMainHandChange}
     * 不会执行，物品进入主手时无法触发起始（PICKUP）动画。
     *
     * <p>v2.1.7+ 加入重入保护与缓存：FirstPersonModel 的 PlayerMixin 会拦截
     * {@code player.getMainHandItem()} → {@code getItemBySlot}，并在拦截器内部回调
     * {@code LogicHandler.hideArmsAndItems}，从而再次进入本方法形成无限递归（StackOverflowError）。
     * 通过 {@code hasDrinkItemInHandReentry} 标志检测重入，递归时直接返回上一帧缓存值。
     */
    private static boolean hasDrinkItemInHandReentry = false;
    private static boolean cachedHasDrinkItemInHand = false;

    public static boolean hasDrinkItemInHand(Player player) {
        if (player == null) return false;
        // 重入时直接返回缓存，避免 FirstPersonModel 拦截 getMainHandItem 触发的递归
        if (hasDrinkItemInHandReentry) {
            return cachedHasDrinkItemInHand;
        }
        hasDrinkItemInHandReentry = true;
        try {
            ItemStack mainHand = player.getMainHandItem();
            boolean result = !mainHand.isEmpty()
                    && DrinkAnimRegistry.drinkIdForItem(mainHand.getItem()) != null;
            if (!result) {
                ItemStack offHand = player.getOffhandItem();
                result = !offHand.isEmpty()
                        && DrinkAnimRegistry.drinkIdForItem(offHand.getItem()) != null;
            }
            cachedHasDrinkItemInHand = result;
            return result;
        } finally {
            hasDrinkItemInHandReentry = false;
        }
    }

    public static boolean shouldRenderDrink() {
        return shouldRenderDrink;
    }

    /**
     * 每帧更新所有饮用动画。任一饮料在播则渲染它，并设置 shouldRenderDrink 标志。
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

        // v2.2.1 孤儿 HOLD 防御（对齐 NeoForge 端行为 + 支持副手）：
        // 仅 HOLD 态检查——DRINK/PICKUP/PUTDOWN 中切物品由 tickMainHandChange 正确处理
        // （PICKUP/PUTDOWN→reset，DRINK 态让其自然播完），不再一刀切 reset。
        // v2.1.11 曾扩展到所有非 IDLE 态，导致两个 bug：
        //   1) DRINK 动画播放中切物品被立即终止（NeoForge 端可自然播完）
        //   2) 副手持饮料按 U 触发动画后，主手无该饮料 → 下一帧被 reset，动画秒死
        // v2.2.1 物品检查扩展到双手：主手或副手任一手持有该饮料物品即视为"仍在持物"。
        ItemStack orphanMain = mc.player.getMainHandItem();
        ItemStack orphanOff = mc.player.getOffhandItem();
        Item orphanMainItem = orphanMain.isEmpty() ? null : orphanMain.getItem();
        Item orphanOffItem = orphanOff.isEmpty() ? null : orphanOff.getItem();
        String mainDrinkId = (orphanMainItem == null) ? null : DrinkAnimRegistry.drinkIdForItem(orphanMainItem);
        String offDrinkId = (orphanOffItem == null) ? null : DrinkAnimRegistry.drinkIdForItem(orphanOffItem);
        for (DrinkAnimHandler handler : DrinkAnimRegistry.all()) {
            if (handler.phase() == DrinkAnimState.Phase.HOLD) {
                boolean stillHeld = handler.id().equals(mainDrinkId) || handler.id().equals(offDrinkId);
                if (!stillHeld) {
                    handler.triggerPutdown(gameTime);
                }
            }
        }

        // v2.1.3+ 检查 pendingPickup：所有 handler 都 IDLE 时触发排队的新物品 pickup
        if (pendingPickupItem != null && !DrinkAnimRegistry.anyPlaying()) {
            // v2.1.9+ 排队触发前校验主手：排队等待期间玩家可能已切到其他物品（甚至非饮料），
            // 此时触发旧排队动画会导致"主手非饮料却定格饮料模型"的永久卡死
            ItemStack mainHand = mc.player.getMainHandItem();
            Item currentHandItem = mainHand.isEmpty() ? null : mainHand.getItem();
            if (currentHandItem == pendingPickupItem) {
                String drinkId = DrinkAnimRegistry.drinkIdForItem(pendingPickupItem);
                if (drinkId != null) {
                    DrinkAnimHandler handler = DrinkAnimRegistry.byId(drinkId);
                    if (handler != null && handler.isPickupConfigured()) {
                        handler.triggerPickup(gameTime);
                    }
                }
            }
            pendingPickupItem = null;
        }

        for (DrinkAnimHandler handler : DrinkAnimRegistry.all()) {
            if (handler.update(poseStack, bufferSource, packedLight, partialTick, gameTime)) {
                shouldRenderDrink = true;
            }
            // v2.x 提前消耗：DRINK 阶段提前量到达 → 提前发消耗 C2S 包（抵消网络+服务端延迟）
            if (handler.pollConsume()) {
                PlatformHelper.sendDrinkConsume(handler.id());
            }
            // 动画刚自然结束 → 清 eating 状态 + 记录剩余堆叠（等服务端扣减后重触发 PICKUP）
            if (handler.pollFinished()) {
                // v2.3.0+ 动画自然结束：清除客户端本地 eating 状态（packet 已带 startEating=false 清服务端）
                ServerEatingState.setEating(mc.player.getUUID(), false);
                // v2.1.8+ 喝完后剩余堆叠重新触发 PICKUP：记录当前堆叠数 + remaining_uses，等服务端同步扣减后检测
                ItemStack mainHand = mc.player.getMainHandItem();
                if (!mainHand.isEmpty()) {
                    if (handler.isConsumeSent()) {
                        // v2.x 提前消耗：服务端已在动画结束前扣减，物品状态已确定，直接重新 PICKUP，
                        // 不等"检测扣减变化"（否则掉进 0.5 秒超时兜底、闪现 2D 图标）
                        pendingPickupAfterDrinkId = null;
                        lastMainHandCount = mainHand.getCount();
                        lastMainHandRemainingUses = getRemainingUses(mainHand);
                        lastMainHandItem = mainHand.getItem();
                        if (!handler.triggerPickup(gameTime)) {
                            pendingPickupItem = mainHand.getItem();
                        }
                    } else {
                        pendingPickupAfterDrinkId = handler.id();
                        lastMainHandCount = mainHand.getCount();
                        lastMainHandRemainingUses = getRemainingUses(mainHand);
                        pendingPickupAfterDrinkSetTime = gameTime;
                    }
                }
            }
        }

        // v2.1.10+ 安全网：如果所有 handler 都 IDLE，强制 shouldRenderDrink=false
        // 防止任何异常路径导致 shouldRenderDrink 残留 true 而持续 cancel vanilla
        if (shouldRenderDrink && !DrinkAnimRegistry.anyPlaying()) {
            shouldRenderDrink = false;
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

        // v2.1.7+ 修复：第一次检查时若手里已持有饮料物品，直接触发 pickup（解决"进游戏时无起始动画"）
        if (firstMainHandCheck) {
            lastMainHandItem = current;
            firstMainHandCheck = false;
            if (current != null) {
                String drinkId = DrinkAnimRegistry.drinkIdForItem(current);
                if (drinkId != null) {
                    DrinkAnimHandler handler = DrinkAnimRegistry.byId(drinkId);
                    if (handler != null && handler.isPickupConfigured()) {
                        if (!handler.triggerPickup(gameTime)) {
                            pendingPickupItem = current;
                        }
                    }
                }
            }
            return;
        }

        // v2.1.8+ 喝完后剩余堆叠重新触发 PICKUP：等待服务端同步扣减后检测
        if (pendingPickupAfterDrinkId != null) {
            String currentDrinkId = (current == null) ? null : DrinkAnimRegistry.drinkIdForItem(current);
            if (currentDrinkId == null || !currentDrinkId.equals(pendingPickupAfterDrinkId)) {
                // 物品已切走或耗尽（堆叠降为 0）— 取消等待，走下方正常切物品逻辑
                pendingPickupAfterDrinkId = null;
                lastMainHandCount = 0;
                lastMainHandRemainingUses = 0;
            } else if (mainHandStack.getCount() < lastMainHandCount
                    || getRemainingUses(mainHandStack) < lastMainHandRemainingUses) {
                // 服务端已同步扣减（堆叠减少 或 瓶装第一次喝完 NBT remaining_uses 减少），重新触发 PICKUP
                pendingPickupAfterDrinkId = null;
                lastMainHandCount = mainHandStack.getCount();
                lastMainHandRemainingUses = getRemainingUses(mainHandStack);
                lastMainHandItem = current;
                DrinkAnimHandler handler = DrinkAnimRegistry.byId(currentDrinkId);
                if (handler != null && handler.isPickupConfigured()) {
                    if (!handler.triggerPickup(gameTime)) {
                        pendingPickupItem = current;
                    }
                }
                return;
            } else {
                // 服务端尚未同步扣减，继续等待
                // v2.1.9+ 超时保护：同种饮料不同 stack 切换时主手 stack 不是触发 DRINK 的那个，
                // 永远不会被服务端同步扣减。超时（10 tick = 0.5秒）后强制触发 PICKUP 避免死等。
                if (gameTime - pendingPickupAfterDrinkSetTime > 10) {
                    pendingPickupAfterDrinkId = null;
                    lastMainHandCount = mainHandStack.getCount();
                    lastMainHandRemainingUses = getRemainingUses(mainHandStack);
                    lastMainHandItem = current;
                    DrinkAnimHandler handler = DrinkAnimRegistry.byId(currentDrinkId);
                    if (handler != null && handler.isPickupConfigured()) {
                        if (!handler.triggerPickup(gameTime)) {
                            pendingPickupItem = current;
                        }
                    }
                }
                return;
            }
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
                    } else if (oldPhase == DrinkAnimState.Phase.PICKUP
                            || oldPhase == DrinkAnimState.Phase.PUTDOWN) {
                        // 仅 PICKUP/PUTDOWN 态强制中断；DRINK 态让其自然播完
                        oldHandler.reset();
                        // 切物品中断动画：只清客户端本地 eating 状态，不发消耗包。
                        // （修复：旧逻辑在此误发 sendDrinkConsume(false)，服务端会全物品栏
                        // 查找并真正消耗该饮料/零食，导致"切物品就消耗"的 bug。）
                        ServerEatingState.setEating(mc.player.getUUID(), false);
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

    /**
     * v2.1.8+ 读取瓶装饮料 remaining_uses（1.21.1 用 DataComponents.CUSTOM_DATA）。
     * 无 CUSTOM_DATA 时返回 {@link Integer#MAX_VALUE} 作为"满（未消耗过）"哨兵值，
     * 这样比较时任何实际 remaining（1、2...）都小于它，无需知道 maxUses。
     */
    private static int getRemainingUses(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        CustomData existing = stack.get(DataComponents.CUSTOM_DATA);
        if (existing != null) {
            CompoundTag tag = existing.copyTag();
            if (tag.contains("remaining_uses")) {
                return tag.getInt("remaining_uses");
            }
        }
        return Integer.MAX_VALUE;
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
            state.update(mc.level.getGameTime(), partialTick);
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
            // v2.1.13+ 不在 MAIN_HAND 的 updateState 里渲染 GeckoLib 左臂，
            // 改为在 OFF_HAND 分支渲染（renderLeftHandRice），避免干扰原版主手 renderArmWithItem
            shouldRenderLeftHandRice = true;
        }
    }

    /**
     * v2.1.13+ 渲染左手米饭模型（在 OFF_HAND 分支调用）。
     * 之前在 updateState（MAIN_HAND）里渲染，会干扰原版主手 renderArmWithItem，
     * 导致主手物品不显示。移到 OFF_HAND 分支后，主手走纯原版 renderArmWithItem。
     */
    public static void renderLeftHandRice(PoseStack poseStack, MultiBufferSource bufferSource,
                                          int packedLight, float partialTick) {
        if (!armRenderEnabled) return;
        if (!shouldRenderLeftHandRice) return;
        if (leftArmRenderer == null) {
            leftArmRenderer = new LeftArmRenderer(LEFT_ARM_MODEL);
        }
        poseStack.pushPose();
        leftArmRenderer.renderLeftArm(poseStack, LEFT_ARM_ANIMATABLE, bufferSource, packedLight, partialTick);
        poseStack.popPose();
    }

    private static void playEatSound(Player player) {
        if (player instanceof LocalPlayer localPlayer) {
            player.level().playSound(
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
        return stack.getItem() == com.realisticdining.registry.ModItems.CHOPSTICKS.get();
    }

    private static boolean isCookedRice(ItemStack stack) {
        return com.realisticdining.compat.KaleidoscopeCookeryCompat.isCookedRice(stack);
    }
}
