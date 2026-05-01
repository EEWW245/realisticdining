package com.example.realisticdining.events;

import com.example.realisticdining.blocks.FriedRiceEggBlock;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FriedRiceEggPlaceHandler {

    public static boolean onPlayerRightClick(Player player, Level level, InteractionHand hand) {
        if (level.isClientSide) {
            return false;
        }

        ItemStack offhandItem = player.getOffhandItem();

        if (!KaleidoscopeCompat.isEggFriedRice(offhandItem)) {
            return false;
        }

        HitResult hitResult = player.pick(5.0, 0.0f, false);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        BlockPos hitPos = blockHitResult.getBlockPos();
        Direction direction = blockHitResult.getDirection();
        BlockPos placePos = hitPos.relative(direction);

        if (!level.getBlockState(placePos).isAir()) {
            return false;
        }

        BlockState placeState = ModBlocks.FRIED_RICE_EGG_BLOCK.get().defaultBlockState();
        level.setBlock(placePos, placeState, 3);
        level.playSound(null, placePos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!player.isCreative()) {
            offhandItem.shrink(1);
        }

        return true;
    }
}
