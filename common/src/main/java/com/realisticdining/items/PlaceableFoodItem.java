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

public class PlaceableFoodItem extends BlockItem {

    private final IntegerProperty bitesProperty;
    private final String nbtKey;

    public PlaceableFoodItem(Block block, IntegerProperty bitesProperty, String nbtKey, Properties properties) {
        super(block, properties);
        this.bitesProperty = bitesProperty;
        this.nbtKey = nbtKey;
    }

    public static int getBitesFromStack(ItemStack stack, String nbtKey) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains(nbtKey)) {
                return tag.getInt(nbtKey);
            }
        }
        return 0;
    }

    public static void setBitesToStack(ItemStack stack, String nbtKey, int bites) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(nbtKey, bites);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        if (bitesProperty != null) {
            int bites = getBitesFromStack(context.getItemInHand(), nbtKey);
            if (bites > 0 && state.hasProperty(bitesProperty)) {
                state = state.setValue(bitesProperty, bites);
            }
        }
        return super.placeBlock(context, state);
    }
}
