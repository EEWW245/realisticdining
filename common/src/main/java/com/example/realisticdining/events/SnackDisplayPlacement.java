package com.example.realisticdining.events;

import com.example.realisticdining.ServerEatingState;
import com.example.realisticdining.blocks.SnackDisplayBlock;
import com.example.realisticdining.blockentities.SnackDisplayBlockEntity;
import com.example.realisticdining.common.SnackItemRegistry;
import com.example.realisticdining.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 手持零食/饮料右键地面/桌子自动创建展示台（v2.2.0+）。
 *
 * <p>玩家主手持零食/饮料右键任意方块时：
 * <ul>
 *   <li>若目标是展示台本身 → PASS，交给 {@link SnackDisplayBlock#use} 放入槽位</li>
 *   <li>若目标是森罗物语桌子（kaleidoscope_cookery:table_*）→ 在桌面上方一格放置展示台
 *       （若上方已有展示台则直接放入下一槽），拦截森罗物语的 2D 物品贴图放置行为</li>
 *   <li>否则在点击面外侧放置一个新展示台（facing = 玩家朝向，与 BlockItem 放置一致），
 *       并把手中物品作为第一件放入槽位 0（远左）</li>
 * </ul>
 *
 * <p>由各平台事件处理器调用（Fabric UseBlockCallback / Forge RightClickBlock），
 * 双端执行（客户端预测 + 服务端权威），与 {@link RicePlaceHandler} 模式一致。
 */
public final class SnackDisplayPlacement {

    /** 森罗物语 mod ID */
    private static final String KALEIDOSCOPE_COOKERY_MODID = "kaleidoscope_cookery";

    private SnackDisplayPlacement() {}

    public static InteractionResult tryPlace(Player player, Level level, InteractionHand hand,
                                             BlockPos hitPos, Direction hitFace) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty() || !SnackItemRegistry.isSnackItem(held.getItem())) {
            return InteractionResult.PASS;
        }

        // v2.3.0+ 防无限刷：喝动画播放期间（ServerEatingState=true）禁止右键地面/桌子放置展示台。
        // 否则动画中放置会让物品进入展示台（NBT remaining_uses 保留），动画结束时服务端找不到
        // 主手饮料而静默 return，玩家从展示台取回 = 净消耗 0 = 无限刷。
        // 双端一致返回 FAIL：客户端 FAIL 阻止原版 Block.use()（防止森罗桌子先放 2D 贴图），
        // 服务端 FAIL 权威拒绝。
        if (ServerEatingState.isEating(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        // 右键已有展示台：交给方块自身交互（放入槽位）
        if (level.getBlockState(hitPos).getBlock() == ModBlocks.SNACK_DISPLAY.get()) {
            return InteractionResult.PASS;
        }

        // 右键森罗物语桌子：在桌面上方一格放置展示台（若上方已有展示台则放入下一槽）
        if (isKaleidoscopeTable(level.getBlockState(hitPos))) {
            return tryPlaceOnTable(player, level, held, hand, hitPos);
        }

        // 右键空地：只能放在上表面（点击顶面）上，与火把/牌子等原版方块一致；
        // 点击墙壁侧面（东西南北）不放，返回 PASS（交由事件处理器兜底触发饮用动画）。
        if (hitFace != Direction.UP) {
            return InteractionResult.PASS;
        }
        BlockPos placePos = hitPos.relative(hitFace);
        if (!level.getBlockState(placePos).canBeReplaced()) {
            return InteractionResult.PASS;
        }

        BlockState state = ModBlocks.SNACK_DISPLAY.get().defaultBlockState()
                .setValue(SnackDisplayBlock.FACING, player.getDirection());
        level.setBlock(placePos, state, 3);

        if (level.getBlockEntity(placePos) instanceof SnackDisplayBlockEntity be) {
            be.tryPlace(player, held, hand);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 在森罗物语桌子上方放置/写入展示台。
     * <ul>
     *   <li>上方已有展示台 → 直接复用，放入下一槽</li>
     *   <li>上方可替换（空气/草等）→ 放置新展示台，facing = 玩家朝向，物品进入槽位 0</li>
     *   <li>上方不可替换 → PASS，让原版处理（避免破坏桌子上的其他方块）</li>
     * </ul>
     */
    private static InteractionResult tryPlaceOnTable(Player player, Level level, ItemStack held,
                                                     InteractionHand hand, BlockPos tablePos) {
        BlockPos abovePos = tablePos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        // 桌子上方已有展示台 → 直接放入下一槽
        if (aboveState.getBlock() == ModBlocks.SNACK_DISPLAY.get()) {
            if (level.getBlockEntity(abovePos) instanceof SnackDisplayBlockEntity be) {
                be.tryPlace(player, held, hand);
            }
            return InteractionResult.SUCCESS;
        }

        // 桌子上方必须可替换
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
     * 森罗物语有 11 种木种的桌子，都共享 TableBlock 类，但 mod ID 一致、path 都以 "table" 开头。
     */
    private static boolean isKaleidoscopeTable(BlockState state) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return key != null
                && key.getNamespace().equals(KALEIDOSCOPE_COOKERY_MODID)
                && key.getPath().startsWith("table");
    }
}
