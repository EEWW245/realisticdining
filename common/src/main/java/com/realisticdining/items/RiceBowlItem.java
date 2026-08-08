package com.realisticdining.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RiceBowlItem extends BlockItem {

    private final IntegerProperty bitesProperty;

    public RiceBowlItem(Block block, IntegerProperty bitesProperty, Properties properties) {
        super(block, properties);
        this.bitesProperty = bitesProperty;
    }

    public static int getBitesFromStack(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("RiceBowlBites")) {
                return tag.getInt("RiceBowlBites");
            }
        }
        return 0;
    }

    public static void setBitesToStack(ItemStack stack, int bites) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("RiceBowlBites", bites);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        int bites = getBitesFromStack(context.getItemInHand());
        if (bites > 0 && state.hasProperty(bitesProperty)) {
            state = state.setValue(bitesProperty, bites);
        }
        return super.placeBlock(context, state);
    }
}
