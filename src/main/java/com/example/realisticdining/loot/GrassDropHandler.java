package com.example.realisticdining.loot;

import com.example.realisticdining.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.example.realisticdining.realisticdining;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

@Mod.EventBusSubscriber(modid = realisticdining.MODID)
public class GrassDropHandler {
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == Blocks.GRASS || 
            event.getState().getBlock() == Blocks.TALL_GRASS ||
            event.getState().getBlock() == Blocks.FERN) {
            
            if (event.getPlayer() != null) {
                ItemStack tool = event.getPlayer().getMainHandItem();
                // 只有使用剪刀才能获得葱种子
                if (tool.getItem() == Items.SHEARS) {
                    if (event.getLevel() instanceof ServerLevel serverLevel) {
                        if (serverLevel.random.nextFloat() < 0.45f) {
                            BlockPos pos = event.getPos();
                            net.minecraft.world.Containers.dropItemStack(
                                serverLevel, 
                                pos.getX() + 0.5, 
                                pos.getY() + 0.5, 
                                pos.getZ() + 0.5, 
                                new ItemStack(ModItems.GREEN_ONION_SEEDS.get())
                            );
                        }
                    }
                }
            }
        }
    }
}
