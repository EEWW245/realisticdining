package com.example.realisticdining.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class WokRecipeSerializer implements RecipeSerializer<WokRecipe> {
    @Override
    public WokRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = json.has("group") ? json.get("group").getAsString() : "";
        ItemStack input = CraftingHelper.getItemStack(json.getAsJsonObject("input"), true);
        ItemStack output = CraftingHelper.getItemStack(json.getAsJsonObject("output"), true);
        int cookTime = json.has("cookTime") ? json.get("cookTime").getAsInt() : 200;
        
        return new WokRecipe(recipeId, group, input, output, cookTime);
    }
    
    @Override
    public WokRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        ItemStack input = buffer.readItem();
        ItemStack output = buffer.readItem();
        int cookTime = buffer.readInt();
        
        return new WokRecipe(recipeId, group, input, output, cookTime);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, WokRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeItem(recipe.getInput());
        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeInt(recipe.getCookTime());
    }
}