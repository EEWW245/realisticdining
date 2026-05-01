package com.example.realisticdining.fabric.platform;

import com.example.realisticdining.platform.PlatformRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.CreativeModeTab;
import java.util.function.Supplier;

public class FabricPlatformRegistry<T> implements PlatformRegistry<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String modId;
    
    public FabricPlatformRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = registryKey;
        this.modId = modId;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <R extends T> Supplier<R> register(ResourceLocation location, Supplier<R> supplier) {
        R value = supplier.get();
        
        // 根据注册表类型使用正确的注册方法
        if (registryKey.equals(Registries.BLOCK)) {
            Registry.register(BuiltInRegistries.BLOCK, location, (Block) value);
        } else if (registryKey.equals(Registries.ITEM)) {
            Registry.register(BuiltInRegistries.ITEM, location, (Item) value);
        } else if (registryKey.equals(Registries.BLOCK_ENTITY_TYPE)) {
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, location, (BlockEntityType<?>) value);
        } else if (registryKey.equals(Registries.SOUND_EVENT)) {
            Registry.register(BuiltInRegistries.SOUND_EVENT, location, (SoundEvent) value);
        } else if (registryKey.equals(Registries.MENU)) {
            Registry.register(BuiltInRegistries.MENU, location, (MenuType<?>) value);
        } else if (registryKey.equals(Registries.RECIPE_SERIALIZER)) {
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, location, (RecipeSerializer<?>) value);
        } else if (registryKey.equals(Registries.RECIPE_TYPE)) {
            Registry.register(BuiltInRegistries.RECIPE_TYPE, location, (RecipeType<?>) value);
        } else if (registryKey.equals(Registries.CREATIVE_MODE_TAB)) {
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, location, (CreativeModeTab) value);
        } else {
            throw new IllegalArgumentException("Unsupported registry: " + registryKey);
        }
        
        return () -> value;
    }
    
    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }
    
    @Override
    public String getModId() {
        return modId;
    }
}
