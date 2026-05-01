package com.realisticdining.fabric.event;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.registry.ModBlocks;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class FriedRiceEggPlaceHandler {
    
    public static void register() {
        UseBlockCallback.EVENT.register((Player player, Level level, net.minecraft.world.InteractionHand hand, BlockHitResult hitResult) -> {
            if (hand != net.minecraft.world.InteractionHand.OFF_HAND) {
                return InteractionResult.PASS;
            }
            
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.isEmpty()) {
                return InteractionResult.PASS;
            }
            
            if (!KaleidoscopeCookeryCompat.isEggFriedRice(heldItem)) {
                return InteractionResult.PASS;
            }
            
            BlockPos pos = hitResult.getBlockPos();
            if (level.getBlockState(pos.above()).isAir()) {
                level.setBlock(pos.above(), ModBlocks.FRIED_RICE_EGG_BLOCK.get().defaultBlockState(), 3);
                level.playSound(null, pos.above(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            
            return InteractionResult.PASS;
        });
    }
}
