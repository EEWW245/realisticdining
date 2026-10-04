package com.example.realisticdining.events;

import com.example.realisticdining.ServerEatingState;
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
            if (hand != InteractionHand.OFF_HAND) {
                return InteractionResult.PASS;
            }
            
            Level world = player.level();
            ItemStack heldItem = player.getItemInHand(hand);
            
            if (KaleidoscopeCompat.isRice(heldItem)) {
                if (ServerEatingState.isEating(player.getUUID())) {
                    return InteractionResult.FAIL;
                }
                
                if (world.getBlockState(pos.above()).getBlock() == Blocks.AIR) {
                    // 客户端只返回 SUCCESS 阻止原版，服务端权威放置 + 消耗（避免双端重复 setBlock/shrink）
                    if (!world.isClientSide) {
                        world.setBlock(pos.above(), ModBlocks.RICE_BOWL.get().defaultBlockState(), 3);
                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }
}
