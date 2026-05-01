package com.example.realisticdining.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

public interface PlatformRegistry<T> {
    <R extends T> Supplier<R> register(ResourceLocation location, Supplier<R> supplier);
    
    ResourceKey<? extends Registry<T>> getRegistryKey();
    
    String getModId();
}
