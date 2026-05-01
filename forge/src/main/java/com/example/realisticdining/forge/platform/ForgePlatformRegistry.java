package com.example.realisticdining.forge.platform;

import com.example.realisticdining.platform.PlatformRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ForgePlatformRegistry<T> implements PlatformRegistry<T> {
    private final DeferredRegister<T> deferredRegister;
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String modId;
    
    public ForgePlatformRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = registryKey;
        this.modId = modId;
        this.deferredRegister = DeferredRegister.create(registryKey, modId);
    }
    
    @Override
    public <R extends T> Supplier<R> register(ResourceLocation location, Supplier<R> supplier) {
        RegistryObject<R> registryObject = deferredRegister.register(location.getPath(), supplier);
        return registryObject;
    }
    
    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }
    
    @Override
    public String getModId() {
        return modId;
    }
    
    public void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
        deferredRegister.register(eventBus);
    }
}
