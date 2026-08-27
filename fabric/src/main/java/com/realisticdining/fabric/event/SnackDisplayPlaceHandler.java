package com.realisticdining.fabric.event;

import com.realisticdining.blockentities.SnackDisplayBlockEntity;
import com.realisticdining.blocks.SnackDisplayBlock;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.common.SnackItemRegistry;
import com.realisticdining.fabric.client.pack.PackDefinitionManager;
import com.realisticdining.registry.ModBlocks;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Fabric 端：手持零食/饮料右键地面/桌子自动创建展示台（v2.2.0+）。
 *
 * <p>玩家主手持零食/饮料右键任意方块：
 * <ul>
 *   <li>目标是展示台本身 → PASS，交给方块的 useItemOn（放入槽位）</li>
 *   <li>目标是森罗物语桌子（kaleidoscope_cookery:table_*）→ 在桌面上方一格放置展示台
 *       （若上方已有展示台则直接放入下一槽），拦截森罗物语 2D 物品贴图放置</li>
 *   <li>否则在点击面外侧创建新展示台（facing = 玩家朝向），物品进入槽位 0（远左）</li>
 * </ul>
 *
 * <p>双端执行（客户端预测 + 服务端权威），与 {@link RicePlaceHandler} 模式一致。
 */
public class SnackDisplayPlaceHandler {

    /** 森罗物语 mod ID */
    private static final String KALEIDOSCOPE_COOKERY_MODID = "kaleidoscope_cookery";

    public static void register() {
        UseBlockCallback.EVENT.register((Player player, Level level, InteractionHand hand, BlockHitResult hitResult) -> {
            if (hand != InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }
            ItemStack held = player.getItemInHand(hand);
            if (held.isEmpty() || !SnackItemRegistry.isSnackItem(held.getItem())) {
                return InteractionResult.PASS;
            }

            // v2.3.0+ 防无限刷：喝动画播放期间禁止右键地面/桌子放置展示台。
            // 双端一致返回 FAIL：客户端 FAIL 阻止原版 Block.use()（防止森罗桌子先放 2D 贴图），
            // 服务端 FAIL 权威拒绝。
            if (ServerEatingState.isEating(player.getUUID())) {
                return InteractionResult.FAIL;
            }

            BlockPos hitPos = hitResult.getBlockPos();
            // 右键已有展示台：交给方块自身交互（放入槽位）
            if (level.getBlockState(hitPos).getBlock() == ModBlocks.SNACK_DISPLAY.get()) {
                return InteractionResult.PASS;
            }

            // 右键森罗物语桌子：在桌面上方一格放置展示台
            if (isKaleidoscopeTable(level.getBlockState(hitPos))) {
                InteractionResult tableResult = tryPlaceOnTable(player, level, held, hand, hitPos);
                if (tableResult == InteractionResult.PASS) {
                    // 桌子上方放不下 → 兜底触发饮用动画
                    triggerDrinkFallback(level);
                }
                return tableResult;
            }

            // 放置位置 = 点击面外侧，必须可替换；只能放在上表面（点击顶面）上，与火把/牌子等原版方块一致，
            // 点击墙壁侧面（东西南北）不放，兜底触发饮用动画。
            Direction face = hitResult.getDirection();
            if (face != Direction.UP) {
                triggerDrinkFallback(level);
                return InteractionResult.PASS;
            }
            BlockPos placePos = hitPos.relative(face);
            if (!level.getBlockState(placePos).canBeReplaced()) {
                // 位置放不下 → 兜底触发饮用动画
                triggerDrinkFallback(level);
                return InteractionResult.PASS;
            }

            BlockState state = ModBlocks.SNACK_DISPLAY.get().defaultBlockState()
                    .setValue(SnackDisplayBlock.FACING, player.getDirection());
            level.setBlock(placePos, state, 3);

            if (level.getBlockEntity(placePos) instanceof SnackDisplayBlockEntity be) {
                be.tryPlace(player, held, hand);
            }
            return InteractionResult.SUCCESS;
        });

        // 对准空气右键：主手持零食/饮料 或 材质包扩展物品时触发饮用动画（仅客户端）
        UseItemCallback.EVENT.register((Player player, Level level, InteractionHand hand) -> {
            if (!level.isClientSide) {
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
            if (hand != InteractionHand.MAIN_HAND) {
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
            ItemStack held = player.getItemInHand(hand);
            if (held.isEmpty()) {
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
            // 主模组零食/饮料：兜底触发饮用动画
            if (SnackItemRegistry.isSnackItem(held.getItem())) {
                triggerDrinkFallback(level);
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
            // 材质包扩展物品：饮用键=右键时，consumeClick 可能因原版 keyUse 冲突检测不到，
            // 这里通过 UseItemCallback 兜底触发材质包动画
            if (isPackItem(held) && com.realisticdining.fabric.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) {
                com.realisticdining.fabric.client.ModKeybinds.triggerDrinkPressed();
            }
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        });
    }

    /**
     * 放置失败时兜底触发饮用动画（仅客户端执行）。
     * <p>仅当「饮用键」被改绑为鼠标右键时才触发，避免与默认 U 键冲突。
     */
    private static void triggerDrinkFallback(Level level) {
        if (!level.isClientSide) return;
        if (!com.realisticdining.fabric.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) return;
        com.realisticdining.fabric.client.ModKeybinds.triggerDrinkPressed();
    }

    /** 判断物品是否是材质包扩展物品（Pack）。 */
    private static boolean isPackItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && PackDefinitionManager.containsItem(id.toString());
    }

    /**
     * 在森罗物语桌子上方放置/写入展示台。
     * <ul>
     *   <li>上方已有展示台 → 直接复用，放入下一槽</li>
     *   <li>上方可替换（空气/草等）→ 放置新展示台，facing = 玩家朝向，物品进入槽位 0</li>
     *   <li>上方不可替换 → PASS</li>
     * </ul>
     */
    private static InteractionResult tryPlaceOnTable(Player player, Level level, ItemStack held,
                                                     InteractionHand hand, BlockPos tablePos) {
        BlockPos abovePos = tablePos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (aboveState.getBlock() == ModBlocks.SNACK_DISPLAY.get()) {
            if (level.getBlockEntity(abovePos) instanceof SnackDisplayBlockEntity be) {
                be.tryPlace(player, held, hand);
            }
            return InteractionResult.SUCCESS;
        }

        if (!aboveState.canBeReplaced()) {
            return InteractionResult.PASS;
        }

        BlockState state = ModBlocks.SNACK_DISPLAY.get().defaultBlockState()
                .setValue(SnackDisplayBlock.FACING, player.getDirection());
        level.setBlock(abovePos, state, 3);

        if (level.getBlockEntity(abovePos) instanceof SnackDisplayBlockEntity be) {
            be.tryPlace(player, held, hand);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 判断方块是否是森罗物语桌子（kaleidoscope_cookery:table_*）。
     */
    private static boolean isKaleidoscopeTable(BlockState state) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return key != null
                && key.getNamespace().equals(KALEIDOSCOPE_COOKERY_MODID)
                && key.getPath().startsWith("table");
    }
}
