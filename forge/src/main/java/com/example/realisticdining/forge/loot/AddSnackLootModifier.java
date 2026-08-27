package com.example.realisticdining.forge.loot;

import com.example.realisticdining.init.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * 往所有原版箱子（minecraft:chests/*）追加随机零食/饮料的全局战利品修改器（Forge 1.20.1）。
 *
 * <p>conditions 为空（无条件），作用于所有战利品表；在 {@link #doApply} 里判断
 * {@code context.getQueriedLootTableId()} 是否以 {@code minecraft:chests/} 开头，
 * 命中则追加 1~2 个随机零食/饮料。
 */
public class AddSnackLootModifier extends LootModifier {

    public static final Codec<AddSnackLootModifier> CODEC = RecordCodecBuilder.create(
            instance -> codecStart(instance).apply(instance, AddSnackLootModifier::new));

    /** 18 个可饮用/可放置的零食/饮料（与 SnackItemRegistry 一致）。 */
    private static final List<Supplier<Item>> SNACKS = List.of(
            ModItems.MINERAL_WATER,
            ModItems.MILK_BEER,
            ModItems.ENERGY_BAR,
            ModItems.PEACH_GRAPEFRUIT_TEA,
            ModItems.SPORTS_DRINK,
            ModItems.ICED_TEA,
            ModItems.COCONUT_JUICE,
            ModItems.ORANGE_JUICE,
            ModItems.SOYMILK,
            ModItems.COLA,
            ModItems.BEER,
            ModItems.POTATO_CHIPS,
            ModItems.POTATO_CHIPS_BBQ,
            ModItems.POTATO_CHIPS_CUCUMBER,
            ModItems.POTATO_CHIPS_TOMATO,
            ModItems.CRISPY_FISH_CHIPS,
            ModItems.COOKIE_BAG,
            ModItems.COOKIE_BAG_COCONUT_LATTE,
            ModItems.PEARL_MILK_TEA
    );

    public AddSnackLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // 只作用于原版箱子战利品表（minecraft:chests/*）
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (tableId == null || !"minecraft".equals(tableId.getNamespace())
                || !tableId.getPath().startsWith("chests/")) {
            return generatedLoot;
        }

        // 30%~50% 区间随机抽取概率，命中则追加 1~2 个随机零食/饮料
        float chance = 0.30f + context.getRandom().nextFloat() * 0.20f;
        if (context.getRandom().nextFloat() > chance) {
            return generatedLoot;
        }

        int count = 1 + context.getRandom().nextInt(2); // 1 或 2
        for (int i = 0; i < count; i++) {
            Supplier<Item> snack = SNACKS.get(context.getRandom().nextInt(SNACKS.size()));
            generatedLoot.add(new ItemStack(snack.get()));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
