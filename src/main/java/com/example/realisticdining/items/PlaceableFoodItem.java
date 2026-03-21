package com.example.realisticdining.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

public class PlaceableFoodItem extends Item {

    private final Block placeBlock;

    public PlaceableFoodItem(Block placeBlock, Properties properties) {
        super(properties);
        this.placeBlock = placeBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos placePos = blockpos.relative(direction);

        if (level.getBlockState(placePos).isAir()) {
            BlockState blockstate = placeBlock.defaultBlockState();
            level.setBlock(placePos, blockstate, 3);
            level.playSound(null, placePos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!context.getPlayer().isCreative()) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
