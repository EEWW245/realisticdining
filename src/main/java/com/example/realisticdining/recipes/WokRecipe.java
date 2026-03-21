package com.example.realisticdining.recipes;

import com.example.realisticdining.realisticdining;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class WokRecipe implements Recipe<WokRecipeInput> {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, realisticdining.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, realisticdining.MODID);
    
    public static final RegistryObject<RecipeType<WokRecipe>> WOK_RECIPE_TYPE = TYPES.register("wok_cooking", () -> new RecipeType<WokRecipe>() {
        @Override
        public String toString() {
            return new ResourceLocation(realisticdining.MODID, "wok_cooking").toString();
        }
    });
    
    public static final RegistryObject<RecipeSerializer<WokRecipe>> WOK_RECIPE_SERIALIZER = SERIALIZERS.register("wok_cooking", WokRecipeSerializer::new);
    
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
        return WOK_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType() {
        return WOK_RECIPE_TYPE.get();
    }
    
    public ItemStack getInput() {
        return input;
    }
    
    public int getCookTime() {
        return cookTime;
    }
    
    public static void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}