package com.example.realisticdining.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WokRecipeSerializer implements RecipeSerializer<WokRecipe> {
    @Override
    public WokRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = json.has("group") ? json.get("group").getAsString() : "";
        JsonObject inputJson = json.getAsJsonObject("input");
        JsonObject outputJson = json.getAsJsonObject("output");
        
        ItemStack input = itemStackFromJson(inputJson);
        ItemStack output = itemStackFromJson(outputJson);
        int cookTime = json.has("cookTime") ? json.get("cookTime").getAsInt() : 200;
        
        return new WokRecipe(recipeId, group, input, output, cookTime);
    }
    
    private ItemStack itemStackFromJson(JsonObject json) {
        if (json == null || json.isJsonNull()) {
            return ItemStack.EMPTY;
        }
        
        String item = json.get("item").getAsString();
        int count = json.has("count") ? json.get("count").getAsInt() : 1;
        
        ResourceLocation itemId = new ResourceLocation(item);
        return new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId), count);
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
