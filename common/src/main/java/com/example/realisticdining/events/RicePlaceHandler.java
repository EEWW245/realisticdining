package com.example.realisticdining.events;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class RicePlaceHandler {
    
    public static void init() {
        ServiceHelper.getPlatformServices().registerRightClickBlockHandler((player, hand, pos, hitPos) -> {
            Level world = player.level();
            ItemStack heldItem = player.getItemInHand(hand);
            
            if (KaleidoscopeCompat.isRice(heldItem)) {
                if (world.getBlockState(pos.above()).getBlock() == Blocks.AIR) {
                    world.setBlock(pos.above(), ModBlocks.RICE_BOWL.get().defaultBlockState(), 3);
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }
}
