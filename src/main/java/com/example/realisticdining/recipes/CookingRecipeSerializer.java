package com.example.realisticdining.recipes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

// 这里的泛型 T 必须继承自你的 CookingRecipe（而你的 CookingRecipe 应该实现了 Recipe<?> 接口）
public class CookingRecipeSerializer<T extends CookingRecipe> implements RecipeSerializer<T> {

    @Override
    public T fromJson(ResourceLocation recipeId, com.google.gson.JsonObject json) {
        // 从 JSON 加载食谱的逻辑将写在这里
        // 例如解析输入食材 (Ingredient) 和输出结果 (ItemStack)
        return null;
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        // 从服务器同步到客户端网络时读取数据的逻辑
        return null;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        // 将配方数据写入网络发给客户端的逻辑
    }
}