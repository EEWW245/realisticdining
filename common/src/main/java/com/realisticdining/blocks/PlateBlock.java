package com.realisticdining.blocks;

import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlateBlock extends Block {

    public static final BooleanProperty HAS_CHOPSTICKS = BooleanProperty.create("has_chopsticks");
    private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D);

    public PlateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_CHOPSTICKS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_CHOPSTICKS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide) {
            popResource(level, pos, new ItemStack(ModItems.PLATE.get()));
            if (state.getValue(HAS_CHOPSTICKS)) {
                popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (state.getValue(HAS_CHOPSTICKS)) {
            level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, false), 3);
            ItemStack chopsticks = new ItemStack(ModItems.CHOPSTICKS.get());
            if (!player.getInventory().add(chopsticks)) {
                player.drop(chopsticks, false);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (!state.getValue(HAS_CHOPSTICKS) && stack.is(ModItems.CHOPSTICKS.get())) {
            if (!player.isCreative()) stack.shrink(1);
            level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, true), 3);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
