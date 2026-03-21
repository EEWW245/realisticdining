package com.example.realisticdining.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

// 关键点：这里必须 implements Recipe<SimpleContainer>
public class CookingRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;

    // 构造函数：每个配方都需要一个唯一的 ID
    public CookingRecipe(ResourceLocation id) {
        this.id = id;
    }

    // 1. 判断容器（炒锅）里的物品是否符合这个配方
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        // 这里以后写判断逻辑，比如 container.getItem(0) 是不是你需要的食材
        return false;
    }

    // 2. 制作出成品（消耗食材，生成结果）
    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY; // 以后改成返回你的成品，比如 return new ItemStack(ModItems.FRIED_RICE.get());
    }

    // 3. 检查合成空间是否足够（对于炒锅这种固定容器通常直接返回 true）
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    // 4. 获取配方的理论输出结果（用于在 JEI 这样的配方模组中显示）
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    // 5. 获取配方 ID
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    // 6. 返回配方的序列化器（也就是你之前写的那个类）
    @Override
    public RecipeSerializer<?> getSerializer() {
        // 之后这里要返回：ModRecipes.COOKING_SERIALIZER.get();
        return null;
    }

    // 7. 返回配方的类型
    @Override
    public RecipeType<?> getType() {
        // 之后这里要返回：ModRecipes.COOKING_TYPE.get();
        return null;
    }
}