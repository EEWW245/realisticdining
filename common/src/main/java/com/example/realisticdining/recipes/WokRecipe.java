package com.example.realisticdining.recipes;

import com.example.realisticdining.init.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WokRecipe implements Recipe<WokRecipeInput> {
    public static RecipeType<WokRecipe> WOK_RECIPE_TYPE;
    
    private final ResourceLocation id;
    private final String group;
    private final ItemStack input;
    private final ItemStack output;
    private final int cookTime;
    
    public WokRecipe(ResourceLocation id, String group, ItemStack input, ItemStack output, int cookTime) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.output = output;
        this.cookTime = cookTime;
    }
    
    public static void setType(RecipeType<WokRecipe> type) {
        WOK_RECIPE_TYPE = type;
    }
    
    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }
    
    @Override
    public @NotNull String getGroup() {
        return group;
    }
    
    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output;
    }
    
    @Override
    public boolean matches(@NotNull WokRecipeInput input, @NotNull Level level) {
        ItemStack inputStack = input.getItem(0);
        return !inputStack.isEmpty() && inputStack.getItem() == this.input.getItem();
    }
    
    @Override
    public @NotNull ItemStack assemble(@NotNull WokRecipeInput input, @NotNull RegistryAccess registryAccess) {
        return output.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.COOKING_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType() {
        if (WOK_RECIPE_TYPE != null) {
            return WOK_RECIPE_TYPE;
        }
        return ModRecipes.COOKING_TYPE.get();
    }
    
    public ItemStack getInput() {
        return input;
    }
    
    public int getCookTime() {
        return cookTime;
    }
}
