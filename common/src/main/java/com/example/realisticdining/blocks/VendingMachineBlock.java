package com.example.realisticdining.blocks;

import com.example.realisticdining.menu.VendingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 自动售货机方块：玩家右键已放置的方块时打开购买 GUI。
 * 不持有 BlockEntity，库存视为无限（每次购买独立扣金粒+给物品）。
 * 掉落物由 JSON 战利品表 data/realisticdining/loot_tables/blocks/vending_machine.json 提供。
 */
public class VendingMachineBlock extends Block {

    // 模型高度约 25 像素，给一个接近实际形状的碰撞箱（10×24×10，居中）
    private static final VoxelShape SHAPE = Shapes.box(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);

    public VendingMachineBlock(Properties properties) {
        super(properties);
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
