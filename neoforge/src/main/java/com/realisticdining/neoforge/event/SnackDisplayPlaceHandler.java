package com.realisticdining.neoforge.event;

import com.realisticdining.blockentities.SnackDisplayBlockEntity;
import com.realisticdining.blocks.SnackDisplayBlock;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.common.SnackItemRegistry;
import com.realisticdining.registry.ModBlocks;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * NeoForge 端：手持零食/饮料右键地面/桌子自动创建展示台（v2.2.0+）。
 *
 * <p>玩家主手持零食/饮料右键任意方块：
 * <ul>
 *   <li>目标是展示台本身 → 不取消，交给方块的 useItemOn（放入槽位）</li>
 *   <li>目标是森罗物语桌子（kaleidoscope_cookery:table_*）→ 在桌面上方一格放置展示台
 *       （若上方已有展示台则直接放入下一槽），拦截森罗物语 2D 物品贴图放置</li>
 *   <li>否则在点击面外侧创建新展示台（facing = 玩家朝向），物品进入槽位 0（远左）</li>
 * </ul>
 *
 * <p>双端执行（RightClickBlock 双端触发）：客户端必须取消事件，否则原版 Block.use()
 * 会先执行，导致森罗物语桌子先在客户端放置 2D 物品贴图（即使服务端取消也来不及）。
 */
@EventBusSubscriber(modid = com.realisticdining.RealisticDining.MOD_ID)
public class SnackDisplayPlaceHandler {

    /** 森罗物语 mod ID */
    private static final String KALEIDOSCOPE_COOKERY_MODID = "kaleidoscope_cookery";

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        if (hand != InteractionHand.MAIN_HAND) return;

        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty() || !SnackItemRegistry.isSnackItem(held.getItem())) return;

        // v2.3.0+ 防无限刷：动画播放期间禁止右键地面/桌子放置展示台
        if (ServerEatingState.isEating(player.getUUID())) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);
            return;
        }

        BlockPos hitPos = event.getPos();
        // 右键已有展示台：交给方块自身交互（放入槽位）
        if (level.getBlockState(hitPos).getBlock() == ModBlocks.SNACK_DISPLAY.get()) return;

        // 右键森罗物语桌子：在桌面上方一格放置展示台
        if (isKaleidoscopeTable(level.getBlockState(hitPos))) {
            if (tryPlaceOnTable(player, level, held, hand, hitPos)) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            } else {
                // 桌子上方放不下 → 兜底触发饮用动画
                triggerDrinkFallback(level);
            }
            return;
        }

        // 放置位置 = 点击面外侧，必须可替换；只能放在上表面（点击顶面）上，与火把/牌子等原版方块一致，
        // 点击墙壁侧面（东西南北）不放，兜底触发饮用动画。
        Direction face = event.getFace();
        if (face == null) return;
        if (face != Direction.UP) {
            triggerDrinkFallback(level);
            return;
        }
        BlockPos placePos = hitPos.relative(face);
        if (!level.getBlockState(placePos).canBeReplaced()) {
            // 位置放不下 → 兜底触发饮用动画
            triggerDrinkFallback(level);
            return;
        }

        BlockState state = ModBlocks.SNACK_DISPLAY.get().defaultBlockState()
                .setValue(SnackDisplayBlock.FACING, player.getDirection());
        level.setBlock(placePos, state, 3);

        if (level.getBlockEntity(placePos) instanceof SnackDisplayBlockEntity be) {
            be.tryPlace(player, held, hand);
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /** 对准空气右键：主手持零食/饮料时直接触发饮用动画（仅客户端） */
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        if (!level.isClientSide) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        ItemStack held = event.getEntity().getItemInHand(event.getHand());
        if (held.isEmpty() || !SnackItemRegistry.isSnackItem(held.getItem())) return;
        triggerDrinkFallback(level);
    }

    /**
     * 放置失败时兜底触发饮用动画（仅客户端执行）。
     * <p>仅当「饮用键」被改绑为鼠标右键时才触发，避免与默认 U 键冲突。
     * <p>用 FMLEnvironment.dist.isClient() 隔离，避免服务端加载客户端类。
     */
    private static void triggerDrinkFallback(Level level) {
        if (!level.isClientSide) return;
        if (!FMLEnvironment.dist.isClient()) return;
        if (!com.realisticdining.neoforge.client.ModKeybinds.isDrinkKeyBoundToRightMouse()) return;
        com.realisticdining.neoforge.client.ModKeybinds.triggerDrinkPressed();
    }

    /**
     * 在森罗物语桌子上方放置/写入展示台。返回 true 表示已处理（应取消事件）。
     */
    private static boolean tryPlaceOnTable(Player player, Level level, ItemStack held,
                                          InteractionHand hand, BlockPos tablePos) {
        BlockPos abovePos = tablePos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (aboveState.getBlock() == ModBlocks.SNACK_DISPLAY.get()) {
            if (level.getBlockEntity(abovePos) instanceof SnackDisplayBlockEntity be) {
                be.tryPlace(player, held, hand);
            }
            return true;
        }

        if (!aboveState.canBeReplaced()) {
            return false;
        }

        BlockState state = ModBlocks.SNACK_DISPLAY.get().defaultBlockState()
                .setValue(SnackDisplayBlock.FACING, player.getDirection());
        level.setBlock(abovePos, state, 3);

        if (level.getBlockEntity(abovePos) instanceof SnackDisplayBlockEntity be) {
            be.tryPlace(player, held, hand);
        }
        return true;
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
