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
        
        // 第 1 页：模组介绍
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 真实用餐模组 ===\n\n§f欢迎来到真实用餐模组！\n\n§0本模组与森罗物语厨房联动，为你带来真实的烹饪体验。\n\n§0本模组新增多种食材、\n§0烹饪工具和完整的烹饪流程。")
        )));
        
        // 第 2 页：森罗物语厨房物品
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 森罗物语厨房物品 ===\n\n§0需要使用的物品：\n\n§0• 油壶：倒油\n§0• 红辣椒：切好的辣椒\n§0• 生菜：切好的青菜\n§0• 米饭物品：生成米饭模型\n§0• 炉灶：热源\n§0• 番茄：切成番茄片\n§0• 青辣椒：切成青辣椒片\n§0• 蛋炒饭物品：生成蛋炒饭模型")
        )));
        
        // 第 3 页：种植系统
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 种植系统 ===\n\n§0获取葱种子：\n§0• 用剪刀左键破坏草\n§0• 45% 概率获得葱种子\n\n§0获取香菜种子：\n§0• 用剪刀左键破坏草\n§0• 45% 概率获得香菜种子\n§0• 两种种子可同时掉落\n\n§0种植：\n§0• 右键耕地种植\n§0• 生长速度与小麦相同\n§0• 完全成熟后收获")
        )));
        
        // 第 4 页：刀具切割（基础食材）
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 刀具切割（基础食材）===\n\n§0生猪肉 → 切好的猪肉（25 刀）\n§0生鸡肉 → 切好的鸡肉（20 刀）\n§0葱 → 切好的葱（12 刀）\n§0红辣椒 → 切好的红辣椒（8 刀）\n§0生菜 → 切好的青菜（4 刀）\n\n§0切割方法：\n§0• 食材放在菜板上\n§0• 用刀具右键切割\n§0• 切到最后一刀后\n§0• 空手右键收集成品\n§0• 刀具右键不会获得")
        )));
        
        // 第 5 页：刀具切割（进阶食材）
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 刀具切割（进阶食材）===\n\n§0青辣椒 → 青辣椒片（8 刀）\n§0香菜 → 香菜段（7 刀）\n§0原版牛肉 → 牛肉片（9 刀）\n§0番茄 → 番茄片（4 刀）\n\n§0切割方法：\n§0• 食材放在菜板上\n§0• 用刀具右键切割\n§0• 切到最后一刀后\n§0• 空手右键收集成品\n§0• 刀具右键不会获得")
        )));
        
        // 第 6 页：炒锅使用
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 炒锅使用 ===\n\n§0基础步骤：\n\n§01. 放在点燃的炉灶上\n§02. 用油壶倒入植物油\n§03. 放入切好的食材\n§04. 用铲子翻炒 30 次\n§05. 等待 40 秒后用碗出锅\n\n§0注意：\n§0• 40 秒后超过 20 秒会烧焦\n§0• 烧焦后生成木炭\n§0• 铲子可放在炒锅上")
        )));
        
        // 第 7 页：辣子鸡配方
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 辣子鸡配方 ===\n\n§0需要材料：\n§0• 切好的鸡肉\n§0• 切好的红辣椒\n§0• 切好的葱\n\n§0步骤：\n§0倒油→放鸡肉→放辣椒→放葱\n§0翻炒 30 次→等待 40 秒\n\n§0用碗出锅，超时 20 秒烧焦")
        )));
        
        // 第 8 页：小炒黄牛肉配方
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 小炒黄牛肉配方 ===\n\n§0需要材料：\n§0• 牛肉片\n§0• 青辣椒片\n§0• 切好的红辣椒\n§0• 香菜段\n\n§0步骤：\n§0倒油→放牛肉片→放青辣椒片→\n§0放切好的红辣椒→放香菜段\n§0翻炒 30 次→等待 40 秒\n\n§0用碗出锅，40 秒后超过 20 秒烧焦")
        )));
        
        // 第 9 页：猪肉炒菜配方
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 猪肉炒菜配方 ===\n\n§0需要材料：\n§0• 切好的猪肉\n§0• 切好的青菜\n\n§0步骤：\n§0倒油→放切好猪肉→放切好的青菜\n§0翻炒 30 次→等待 40 秒\n\n§0用碗出锅，40 秒后超过 20 秒烧焦")
        )));
        
        // 第 10 页：番茄荷包蛋配方
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 番茄荷包蛋配方 ===\n\n§0需要材料：\n§0• 鸡蛋\n§0• 番茄片\n\n§0步骤：\n§0倒油→放鸡蛋→放番茄片\n§0翻炒 6 次→等待 25 秒\n\n§0用碗出锅，25 秒后超过 20 秒烧焦")
        )));
        
        // 第 11 页：蛋炒饭制作
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 蛋炒饭制作 ===\n\n§0制作方法：\n\n§0• 使用森罗物语厨房的\n§0  蛋炒饭物品\n\n§0• 副手持有蛋炒饭物品\n\n§0• 右键地面生成\n§0  蛋炒饭模型\n\n§0• 用筷子食用\n\n§0无需炒锅，直接放置")
        )));
        
        // 第 12 页：米饭放置
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 米饭放置 ===\n\n§0获得米饭碗：\n\n§0• 使用森罗物语厨房的\n§0  米饭物品\n\n§0• 副手持有米饭物品\n\n§0• 副手右键地面生成\n§0  米饭模型\n\n§0• 可以用筷子夹米饭吃\n\n§0可放置在任意地面")
        )));
        
        // 第 13 页：筷子使用（基础用法）
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 筷子使用（基础）===\n\n§0筷子可以夹取食物：\n\n§0• 右键盘子夹菜\n§0• 右键空气吃菜\n§0• 饱食度满时也能吃\n§0• 筷子可重复使用\n\n§0吃完整盘菜有额外\n§0奖励 buff 时长")
        )));
        
        // 第 14 页：筷子使用（特殊用法）
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 筷子使用（特殊）===\n\n§0特殊用法：\n\n§0• 可以右键放在空碗上\n§0  空手右键收回\n\n§0• 可以右键放在空盘子上\n§0  空手右键收回\n\n§0• 可以装饰美观")
        )));
        
        // 第 15 页：铲子使用
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 铲子使用 ===\n\n§0铲子用途：\n\n§0• 翻炒锅中食材\n§0• 需要翻炒 30 次\n§0• 某些菜品 6 次即可\n§0不用担心，到了翻炒特定次数会有提示\n\n§0特殊功能：\n\n§0• 可以放在炒锅上\n§0• 作为装饰美观\n§0• 方便随时取用\n\n§0可重复使用")
        )));
        
        // 第 16 页：烤鸡
        pages.add(StringTag.valueOf(Component.Serializer.toJson(
            Component.literal("§6=== 烤鸡 ===\n\n§0使用方法：\n\n§0• 右键放置在地上\n§0• 空手右键吃掉\n\n§0烤鸡是可以食用的！\n\n§6⚠️ Fabric 端注意：\n§0Fabric 版本暂时没有\n§0烤鸡功能，敬请期待！")
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
