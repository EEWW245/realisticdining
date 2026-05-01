package com.realisticdining.blocks;

import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public class RoastChickenBlock extends Block {

    public static final VoxelShape SHAPE = Shapes.box(2.0 / 16.0, 0.0, 1.0 / 16.0, 14.0 / 16.0, 8.0 / 16.0, 14.0 / 16.0);

    public RoastChickenBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        if (!level.isClientSide) {
            ItemStack chicken = new ItemStack(ModItems.ROAST_CHICKEN.get());
            popResource(level, pos, chicken);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            player.getFoodData().eat(6, 0.8f);
            world.destroyBlock(pos, false);
            popResource(world, pos, new ItemStack(Items.BOWL));
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return InteractionResult.SUCCESS;
    }
}
