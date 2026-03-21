package com.example.realisticdining.compat;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.food.FoodProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FoodCompatibilityManager {
    private static final Map<Item, Function<ItemStack, ItemStack>> FOOD_PROCESSORS = new HashMap<>();
    
    // 初始化兼容性管理器
    public static void init() {
        // 注册默认的食物处理器
        registerDefaultProcessors();
    }
    
    // 注册默认的食物处理器
    private static void registerDefaultProcessors() {
        // 原版食物处理器
        registerFoodProcessor(Items.APPLE, stack -> stack.copy());
        registerFoodProcessor(Items.BREAD, stack -> stack.copy());
        registerFoodProcessor(Items.CARROT, stack -> stack.copy());
        registerFoodProcessor(Items.POTATO, stack -> stack.copy());
        registerFoodProcessor(Items.COOKED_BEEF, stack -> stack.copy());
        registerFoodProcessor(Items.COOKED_CHICKEN, stack -> stack.copy());
        // 可以添加更多原版食物
    }
    
    // 注册食物处理器
    public static void registerFoodProcessor(Item food, Function<ItemStack, ItemStack> processor) {
        FOOD_PROCESSORS.put(food, processor);
    }
    
    // 检测物品是否为食物
    public static boolean isFood(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        // 检查是否有食物属性
        Item item = stack.getItem();
        FoodProperties foodProperties = item.getFoodProperties();
        if (foodProperties != null) {
            return true;
        }
        
        // 检查是否在处理器列表中
        return FOOD_PROCESSORS.containsKey(item);
    }
    
    // 处理食物，生成烹饪结果
    public static ItemStack processFood(ItemStack input) {
        if (input.isEmpty() || !isFood(input)) {
            return ItemStack.EMPTY;
        }
        
        Item item = input.getItem();
        
        // 检查是否有专门的处理器
        if (FOOD_PROCESSORS.containsKey(item)) {
            return FOOD_PROCESSORS.get(item).apply(input);
        }
        
        // 默认处理器：返回输入物品的拷贝
        return input.copy();
    }
    
    // 获取食物的营养值
    public static int getNutrition(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        
        FoodProperties foodProperties = stack.getItem().getFoodProperties();
        if (foodProperties != null) {
            return foodProperties.getNutrition();
        }
        
        return 0;
    }
    
    // 获取食物的饱和度
    public static float getSaturation(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0f;
        }
        
        FoodProperties foodProperties = stack.getItem().getFoodProperties();
        if (foodProperties != null) {
            return foodProperties.getSaturationModifier();
        }
        
        return 0.0f;
    }
}