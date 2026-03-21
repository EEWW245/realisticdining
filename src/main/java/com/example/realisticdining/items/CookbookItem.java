package com.example.realisticdining.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CookbookItem extends Item {
    
    public CookbookItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            ItemStack writtenBook = createWrittenCookbook();
            player.setItemInHand(hand, writtenBook);
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.success(stack);
    }
    
    private ItemStack createWrittenCookbook() {
        ItemStack book = new ItemStack(net.minecraft.world.item.Items.WRITTEN_BOOK);
        CompoundTag tag = new CompoundTag();
        
        tag.putString("title", "真实用餐模组食谱书");
        tag.putString("author", "Realistic Dining");
        tag.putBoolean("resolved", true);
        
        ListTag pages = new ListTag();
        
        // 第1页：模组介绍
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 真实用餐模组 ===\n\n§f欢迎来到真实用餐模组！\n\n§0本模组与森罗物语厨房联动，为你带来真实的烹饪体验。")
        )));
        
        // 第2页：主模组物品
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 森罗物语厨房物品 ===\n\n§0需要使用的物品：\n§0- 油壶：倒油\n§0- 红辣椒：切好的辣椒\n§0- 生菜：切好的青菜\n§0- 米饭：生成米饭碗\n§0- 炉灶：热源")
        )));
        
        // 第3页：种植系统
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 种植系统 ===\n\n§0获取葱种子：\n§0- 用剪刀左键破坏草\n§0- 45%概率获得葱种子\n\n§0种植：\n§0- 右键耕地种植\n§0- 生长速度与小麦相同")
        )));
        
        // 第4页：切割系统
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 刀具切割 ===\n\n§0生猪肉 → 切好的猪肉\n§0生鸡肉 → 切好的鸡肉\n§0葱 → 切好的葱\n§0红辣椒 → 切好的辣椒\n§0生菜 → 切好的青菜\n\n§0用刀具右键菜板切割")
        )));
        
        // 第5页：炒锅使用
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 炒锅使用 ===\n\n§0步骤：\n§0- 放在点燃的炉灶上\n§0- 用油壶倒入植物油\n§0- 放入切好的食材\n§0- 用锅铲翻炒30次\n§0- 等待40秒后用碗出锅")
        )));
        
        // 第6页：辣子鸡配方
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 辣子鸡配方 ===\n\n§0需要材料：\n§0- 切好的鸡肉\n§0- 切好的红辣椒\n§0- 切好的葱\n\n§0步骤：\n§0- 倒油→放鸡肉→放辣椒→放葱\n§0- 翻炒30次→等待40秒")
        )));
        
        // 第7页：猪肉炒菜配方
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 猪肉炒菜配方 ===\n\n§0需要材料：\n§0- 切好的猪肉\n§0- 切好的青菜\n\n§0步骤：\n§0- 倒油→放猪肉→放青菜\n§0- 翻炒30次→等待40秒")
        )));
        
        // 第8页：筷子使用
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 筷子使用 ===\n\n§0筷子可以夹取食物：\n§0- 右键盘子夹菜\n§0- 右键空气吃菜\n§0- 饱食度满时也能吃\n\n§0筷子是可重复使用的！")
        )));
        
        // 第9页：米饭碗
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 米饭碗 ===\n\n§0如何获得：\n§0- 主模组米饭右键地面\n\n§0用途：\n§0- 可以用筷子夹米饭吃")
        )));
        
        // 第10页：烤鸡
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 烤鸡 ===\n\n§0使用方法：\n§0- 右键放置在地上\n§0- 空手右键吃掉\n\n§0烤鸡是可以食用的！")
        )));
        
        tag.put("pages", pages);
        book.setTag(tag);
        
        return book;
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal("§7右键打开食谱"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
