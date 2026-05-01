package com.realisticdining.neoforge.event;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class RicePlaceHandler {
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        
        if (event.getHand() != net.minecraft.world.InteractionHand.OFF_HAND) {
            return;
        }
        
        ItemStack heldItem = player.getItemInHand(event.getHand());
        
        if (KaleidoscopeCookeryCompat.isCookedRice(heldItem)) {
            if (level.getBlockState(pos.above()).getBlock() == Blocks.AIR) {
                level.setBlock(pos.above(), ModBlocks.RICE_BOWL.get().defaultBlockState(), 3);
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
