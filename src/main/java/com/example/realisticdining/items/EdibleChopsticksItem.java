package com.example.realisticdining.items;

import com.example.realisticdining.init.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EdibleChopsticksItem extends Item {
    private final int nutrition;
    private final float saturationModifier;

    public EdibleChopsticksItem(Properties properties) {
        this(properties, 2, 0.3f);
    }

    public EdibleChopsticksItem(Properties properties, int nutrition, float saturationModifier) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturationModifier)
                .alwaysEat()
                .build()));
        this.nutrition = nutrition;
        this.saturationModifier = saturationModifier;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide && entity instanceof Player) {
            super.finishUsingItem(stack, world, entity);
            return new ItemStack(ModItems.CHOPSTICKS.get());
        }
        return super.finishUsingItem(stack, world, entity);
    }
}
