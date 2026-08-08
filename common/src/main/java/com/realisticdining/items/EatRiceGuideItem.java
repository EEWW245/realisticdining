package com.realisticdining.items;

import com.realisticdining.RealisticDining;
import com.realisticdining.platform.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EatRiceGuideItem extends Item {
    
    private static final Component GUIDE_TITLE = Component.literal("进食动画说明书");
    
    public EatRiceGuideItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        RealisticDining.LOGGER.info("EatRiceGuideItem.use() called - isClientSide: {}", level.isClientSide);
        
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        
        if (player instanceof ServerPlayer serverPlayer) {
            RealisticDining.LOGGER.info("Opening eat rice guide menu for player: {}", serverPlayer.getName().getString());
            PlatformHelper.openEatRiceGuideMenu(serverPlayer);
        }
        
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        
        RealisticDining.LOGGER.info("EatRiceGuideItem.useOn() called - isClientSide: {}", level.isClientSide);
        
        if (player == null) {
            return InteractionResult.PASS;
        }
        
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        
        if (player instanceof ServerPlayer serverPlayer) {
            RealisticDining.LOGGER.info("Opening eat rice guide menu for player: {}", serverPlayer.getName().getString());
            PlatformHelper.openEatRiceGuideMenu(serverPlayer);
        }
        
        return InteractionResult.CONSUME;
    }
}
