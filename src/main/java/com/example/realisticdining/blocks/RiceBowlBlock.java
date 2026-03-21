package com.example.realisticdining.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.compat.KaleidoscopeCompat;

public class RiceBowlBlock extends Block {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    public static final VoxelShape SHAPE = Shapes.box(4.0 / 16.0, 0.0, 4.0 / 16.0, 12.0 / 16.0, 4.0 / 16.0, 12.0 / 16.0);

    public RiceBowlBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        int bites = state.getValue(BITES);
        if (bites == 0) {
            if (!level.isClientSide && KaleidoscopeCompat.isLoaded()) {
                ItemStack rice = KaleidoscopeCompat.getRiceItem();
                if (!rice.isEmpty()) {
                    popResource(level, pos, rice);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        
        if (heldItem.is(ModItems.CHOPSTICKS.get())) {
            int currentBites = state.getValue(BITES);
            
            if (currentBites < 15) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state.setValue(BITES, currentBites + 1), 3);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                    
                    ItemStack riceOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_RICE.get());
                    player.setItemInHand(hand, riceOnChopsticks);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                if (!level.isClientSide) {
                    level.destroyBlock(pos, false);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                    
                    ItemStack riceOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_RICE.get());
                    player.setItemInHand(hand, riceOnChopsticks);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
    }
}
