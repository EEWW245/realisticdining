package com.example.realisticdining.blocks;

import com.example.realisticdining.menu.VendingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 自动售货机方块：玩家右键已放置的方块时打开购买 GUI。
 * 不持有 BlockEntity，库存视为无限（每次购买独立扣金粒+给物品）。
 * 掉落物由 JSON 战利品表 data/realisticdining/loot_tables/blocks/vending_machine.json 提供。
 *
 * <p>v2.2.5：带 FACING 水平朝向，放置时正面朝向玩家（此前固定朝南）。
 * 模型正面（logo/出货口）默认朝南，blockstate 以 south 为 y=0 基准。
 */
public class VendingMachineBlock extends HorizontalDirectionalBlock {

    // 模型高度约 25 像素，给一个接近实际形状的碰撞箱（10×24×10，居中）
    private static final VoxelShape SHAPE = Shapes.box(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);

    public VendingMachineBlock(Properties properties) {
        super(properties);
        // 1.20.1 的 HorizontalDirectionalBlock 不会自动注册 FACING 属性，
        // 必须由子类重写 createBlockStateDefinition 注册，否则放置时
        // getStateForPlacement 的 setValue(FACING, ...) 会抛 IllegalArgumentException（已修复的线上崩溃）
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        // 正面朝向放置的玩家
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level,
                                          @NotNull BlockPos pos, @NotNull Player player,
                                          @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuProvider provider = new SimpleMenuProvider(
                    (containerId, inventory, p) -> new VendingMachineMenu(containerId, inventory, pos),
                    Component.translatable("block.realisticdining.vending_machine")
            );
            serverPlayer.openMenu(provider);
        }
        return InteractionResult.SUCCESS;
    }
}
