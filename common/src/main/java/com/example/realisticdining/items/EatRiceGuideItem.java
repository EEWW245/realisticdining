package com.example.realisticdining.items;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EatRiceGuideItem extends Item {
    
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
            ServiceHelper.getPlatformServices().openEatRiceGuideMenu(serverPlayer);
        }
        
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal("§7右键打开说明书"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
