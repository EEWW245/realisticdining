package com.realisticdining.fabric.event;

import com.realisticdining.registry.ModItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CorianderSeedsLootHandler {

    private static final float DROP_CHANCE = 0.45f;

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide()) {
                return;
            }

            boolean isGrass = state.getBlock() == Blocks.SHORT_GRASS ||
                    state.getBlock() == Blocks.TALL_GRASS ||
                    state.getBlock() == Blocks.FERN;

            if (!isGrass) {
                return;
            }

            ItemStack tool = player.getMainHandItem();
            if (tool.getItem() != Items.SHEARS) {
                return;
            }

            Level level = (Level) world;
            if (level.random.nextFloat() < DROP_CHANCE) {
                Containers.dropItemStack(
                        level,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        new ItemStack(ModItems.CORIANDER_SEEDS.get())
                );
            }
        });
    }
}
