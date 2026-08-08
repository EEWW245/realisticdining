package com.example.realisticdining.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

public class PlaceableFoodItem extends Item {

    private final Block placeBlock;
    private final IntegerProperty bitesProperty;
    private final String nbtKey;

    public PlaceableFoodItem(Block placeBlock, IntegerProperty bitesProperty, String nbtKey, Properties properties) {
        super(properties);
        this.placeBlock = placeBlock;
        this.bitesProperty = bitesProperty;
        this.nbtKey = nbtKey;
    }

    public static int getBitesFromStack(ItemStack stack, String nbtKey) {
        if (stack.hasTag() && stack.getTag().contains(nbtKey)) {
            return stack.getTag().getInt(nbtKey);
        }
        return 0;
    }

    public static void setBitesToStack(ItemStack stack, String nbtKey, int bites) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(nbtKey, bites);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos placePos = blockpos.relative(direction);

        if (level.getBlockState(placePos).isAir()) {
            BlockState blockstate = placeBlock.defaultBlockState();
            
            if (bitesProperty != null) {
                int bites = getBitesFromStack(context.getItemInHand(), nbtKey);
                if (bites > 0 && blockstate.hasProperty(bitesProperty)) {
                    blockstate = blockstate.setValue(bitesProperty, bites);
                }
            }
            
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
