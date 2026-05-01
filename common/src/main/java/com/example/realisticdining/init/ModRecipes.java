package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.recipes.CookingRecipe;
import com.example.realisticdining.recipes.CookingRecipeSerializer;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import java.util.function.Supplier;

public class ModRecipes {
    private static final PlatformRegistry<RecipeSerializer<?>> SERIALIZERS =
            ServiceHelper.getPlatformServices().createRecipeSerializerRegistry(RealisticDining.MOD_ID);

    private static final PlatformRegistry<RecipeType<?>> TYPES =
            ServiceHelper.getPlatformServices().createRegistry(net.minecraft.core.registries.Registries.RECIPE_TYPE, RealisticDining.MOD_ID);

    public static final Supplier<RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER =
            registerSerializer("cooking", CookingRecipeSerializer::new);

    public static final Supplier<RecipeType<CookingRecipe>> COOKING_TYPE =
            registerType("cooking", () -> new RecipeType<CookingRecipe>() {
                @Override
                public String toString() {
                    return "cooking";
                }
            });

    private static <T extends RecipeSerializer<?>> Supplier<T> registerSerializer(String name, Supplier<T> serializer) {
        return SERIALIZERS.register(new ResourceLocation(RealisticDining.MOD_ID, name), serializer);
    }

    private static <T extends RecipeType<?>> Supplier<T> registerType(String name, Supplier<T> type) {
        return TYPES.register(new ResourceLocation(RealisticDining.MOD_ID, name), type);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
