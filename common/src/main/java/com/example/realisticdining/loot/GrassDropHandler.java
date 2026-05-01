package com.example.realisticdining.loot;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;

public class GrassDropHandler {
    
    public static void init() {
        ServiceHelper.getPlatformServices().registerBlockBreakHandler((level, pos, state, player) -> {
            if (level.isClientSide()) {
                return;
            }
            
            if (state.getBlock() == Blocks.GRASS || 
                state.getBlock() == Blocks.TALL_GRASS ||
                state.getBlock() == Blocks.FERN) {
                
                if (player != null) {
                    ItemStack tool = player.getMainHandItem();
                    if (tool.getItem() == Items.SHEARS) {
                        if (level instanceof ServerLevel serverLevel) {
                            if (serverLevel.random.nextFloat() < 0.45f) {
                                net.minecraft.world.Containers.dropItemStack(
                                    serverLevel, 
                                    pos.getX() + 0.5, 
                                    pos.getY() + 0.5, 
                                    pos.getZ() + 0.5, 
                                    new ItemStack(ModItems.GREEN_ONION_SEEDS.get())
                                );
                            }
                            if (serverLevel.random.nextFloat() < 0.45f) {
                                net.minecraft.world.Containers.dropItemStack(
                                    serverLevel, 
                                    pos.getX() + 0.5, 
                                    pos.getY() + 0.5, 
                                    pos.getZ() + 0.5, 
                                    new ItemStack(ModItems.CORIANDER_SEEDS.get())
                                );
                            }
                        }
                    }
                }
            }
        });
    }
}
