package com.realisticdining.items;

import com.realisticdining.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EdibleChopsticksItem extends Item {

    public EdibleChopsticksItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            if (!level.isClientSide) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return new ItemStack(ModItems.CHOPSTICKS.get());
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
