package com.realisticdining.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class PlaceableFoodItem extends BlockItem {

    public PlaceableFoodItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }
}
