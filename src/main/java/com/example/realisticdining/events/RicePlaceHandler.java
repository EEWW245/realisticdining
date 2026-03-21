package com.example.realisticdining.events;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RicePlaceHandler {
    
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack heldItem = player.getItemInHand(event.getHand());
        
        // 主模组米饭右键地面生成米饭碗
        if (KaleidoscopeCompat.isRice(heldItem)) {
            if (world.getBlockState(pos.above()).getBlock() == Blocks.AIR) {
                world.setBlock(pos.above(), ModBlocks.RICE_BOWL.get().defaultBlockState(), 3);
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
