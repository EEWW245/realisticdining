package com.realisticdining.items;

import com.realisticdining.RealisticDining;
import com.realisticdining.menu.CookbookMenu;
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

public class CookbookItem extends Item {
    
    private static final Component COOKBOOK_TITLE = Component.literal("食谱书");
    
    public CookbookItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        RealisticDining.LOGGER.info("CookbookItem.use() called - isClientSide: {}", level.isClientSide);
        
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        
        if (player instanceof ServerPlayer serverPlayer) {
            RealisticDining.LOGGER.info("Opening cookbook menu for player: {}", serverPlayer.getName().getString());
            PlatformHelper.openCookbookMenu(serverPlayer);
        }
        
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        
        RealisticDining.LOGGER.info("CookbookItem.useOn() called - isClientSide: {}", level.isClientSide);
        
        if (player == null) {
            return InteractionResult.PASS;
        }
        
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        
        if (player instanceof ServerPlayer serverPlayer) {
            RealisticDining.LOGGER.info("Opening cookbook menu for player: {}", serverPlayer.getName().getString());
            PlatformHelper.openCookbookMenu(serverPlayer);
        }
        
        return InteractionResult.CONSUME;
    }
}
