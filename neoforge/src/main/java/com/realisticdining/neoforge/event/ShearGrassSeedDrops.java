package com.realisticdining.neoforge.event;

import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class ShearGrassSeedDrops {

    private static final float DROP_CHANCE = 0.45f;

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        BlockState state = event.getState();
        boolean isGrass = state.getBlock() == Blocks.SHORT_GRASS ||
                state.getBlock() == Blocks.TALL_GRASS ||
                state.getBlock() == Blocks.FERN;

        if (!isGrass) {
            return;
        }

        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        ItemStack tool = player.getMainHandItem();
        if (tool.getItem() != Items.SHEARS) {
            return;
        }

        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();

        if (level.random.nextFloat() < DROP_CHANCE) {
            Containers.dropItemStack(
                    level,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    new ItemStack(ModItems.GREEN_ONION_SEEDS.get())
            );
        }
        if (level.random.nextFloat() < DROP_CHANCE) {
            Containers.dropItemStack(
                    level,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    new ItemStack(ModItems.CORIANDER_SEEDS.get())
            );
        }
    }
}
