package com.example.realisticdining.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RoastChickenBlockEntity extends BlockEntity {

    public RoastChickenBlockEntity(BlockPos pos, BlockState state) {
        super(com.example.realisticdining.init.ModBlockEntities.ROAST_CHICKEN.get(), pos, state);
    }
}
