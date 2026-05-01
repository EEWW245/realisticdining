package com.example.realisticdining.blocks;

import com.example.realisticdining.blockentities.WokFriedEggBlockEntity;
import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WokFriedEggBlock extends BaseEntityBlock {
    public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    
    private static final VoxelShape SHAPE = Shapes.or(
        Shapes.box(2.0/16.0, 0.0, 2.0/16.0, 14.0/16.0, 1.0/16.0, 14.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 2.0/16.0, 14.0/16.0, 3.0/16.0, 3.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 13.0/16.0, 14.0/16.0, 3.0/16.0, 14.0/16.0),
        Shapes.box(2.0/16.0, 1.0/16.0, 3.0/16.0, 3.0/16.0, 3.0/16.0, 13.0/16.0),
        Shapes.box(13.0/16.0, 1.0/16.0, 3.0/16.0, 14.0/16.0, 3.0/16.0, 13.0/16.0)
    );

    public WokFriedEggBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HAS_OIL, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(HAS_OIL, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WokFriedEggBlockEntity(pos, state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof WokFriedEggBlockEntity wokEntity)) {
            return InteractionResult.PASS;
        }

        if (wokEntity.isBurnt()) {
            level.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, state.getValue(FACING)));
            popResource(level, pos, new ItemStack(Items.CHARCOAL));
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (!wokEntity.hasTomatoSlice() && stack.is(ModItems.TOMATO_SLICE.get())) {
            if (!player.isCreative()) stack.shrink(1);
            wokEntity.setHasTomatoSlice(true);
            level.playSound(null, pos, ModSounds.GREEN_VEGETABLES_IN_THE_POT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (wokEntity.hasTomatoSlice() && stack.is(ModItems.SPATULA.get())) {
            if (!wokEntity.canStartStirring()) {
                int remaining = wokEntity.getRemainingWaitSeconds();
                player.displayClientMessage(Component.literal("还需等待 " + remaining + " 秒才能翻炒"), true);
                return InteractionResult.CONSUME;
            }

            if (wokEntity.isCurrentlyAnimating()) {
                return InteractionResult.CONSUME;
            }

            if (wokEntity.isCookingComplete()) {
                return InteractionResult.CONSUME;
            }

            if (wokEntity.isBurnt()) {
                return InteractionResult.CONSUME;
            }

            wokEntity.startStirAnimation();
            float randomPitch = 0.2F + level.random.nextFloat() * 1.6F;
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, randomPitch);

            int currentClick = wokEntity.getSpatulaClickCount();
            if (currentClick == 6) {
                player.displayClientMessage(Component.literal("已翻炒6次"), true);
            }
            return InteractionResult.SUCCESS;
        }

        if (stack.is(Items.BOWL)) {
            if (wokEntity.isCookingComplete()) {
                ItemStack dish = new ItemStack(ModItems.TOMATO_POACHED_EGG.get());
                if (!player.isCreative()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, dish);
                    } else {
                        if (!player.getInventory().add(dish)) {
                            player.drop(dish, false);
                        }
                    }
                } else {
                    if (!player.getInventory().add(dish)) {
                        player.drop(dish, false);
                    }
                }

                level.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, state.getValue(FACING)));
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            if (wokEntity.getSpatulaClickCount() >= 6 && !wokEntity.isCookingComplete()) {
                int remaining = wokEntity.getRemainingServeSeconds();
                if (remaining > 0) {
                    player.displayClientMessage(Component.literal("还剩" + remaining + "秒"), true);
                }
                return InteractionResult.CONSUME;
            }

            if (wokEntity.canGetSuspiciousStew()) {
                ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
                if (!player.isCreative()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, stew);
                    } else {
                        if (!player.getInventory().add(stew)) {
                            player.drop(stew, false);
                        }
                    }
                } else {
                    if (!player.getInventory().add(stew)) {
                        player.drop(stew, false);
                    }
                }

                level.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, state.getValue(FACING)));
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.displayClientMessage(Component.literal("翻炒不完整...获得了迷之炖菜"), true);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 20);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof WokFriedEggBlockEntity wokEntity) {
            if (wokEntity.isBurnt()) {
                level.setBlockAndUpdate(pos, ModBlocks.WOK_BLOCK.get().defaultBlockState().setValue(WokBlock.FACING, state.getValue(FACING)));
                popResource(level, pos, new ItemStack(Items.CHARCOAL));
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                return;
            }
        }
        level.scheduleTick(pos, this, 20);
    }
}
