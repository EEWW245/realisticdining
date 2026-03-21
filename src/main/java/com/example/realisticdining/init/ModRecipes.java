package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import com.example.realisticdining.recipes.CookingRecipe;
import com.example.realisticdining.recipes.CookingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    // 1. 注册配方序列化器 (Serializer)
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, realisticdining.MODID);

    // 2. 注册配方类型 (Type)
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, realisticdining.MODID);

    // 注册炒锅配方的序列化器
    public static final RegistryObject<RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER =
            SERIALIZERS.register("cooking", CookingRecipeSerializer::new);

    // 注册炒锅配方的类型
    public static final RegistryObject<RecipeType<CookingRecipe>> COOKING_TYPE =
            TYPES.register("cooking", () -> new RecipeType<CookingRecipe>() {
                @Override
                public String toString() {
                    return "cooking";
                }
            });

    // 将这两个注册器挂载到总线上
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
