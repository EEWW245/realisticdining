package com.example.realisticdining.forge.platform;

import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.PlatformServices;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.server.level.ServerLevel;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class ForgePlatformServices implements PlatformServices {
    private final Map<ResourceKey<?>, ForgePlatformRegistry<?>> registries = new HashMap<>();
    private final List<PlatformServices.RightClickBlockHandler> rightClickHandlers = new ArrayList<>();
    private final List<PlatformServices.BlockBreakHandler> blockBreakHandlers = new ArrayList<>();
    private final List<Consumer<CommandDispatcher<CommandSourceStack>>> commandRegisters = new ArrayList<>();
    private boolean isRegistered = false;
    
    @Override
    public <T> PlatformRegistry<T> createRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        @SuppressWarnings("unchecked")
        ForgePlatformRegistry<T> registry = (ForgePlatformRegistry<T>) registries.get(registryKey);
        if (registry == null) {
            registry = new ForgePlatformRegistry<>(registryKey, modId);
            registries.put(registryKey, registry);
        }
        return registry;
    }
    
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
    
    @Override
    public String getPlatformName() {
        return "forge";
    }
    
    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
    }
    
    @Override
    public void registerRightClickBlockHandler(PlatformServices.RightClickBlockHandler handler) {
        rightClickHandlers.add(handler);
    }
    
    @Override
    public void registerBlockBreakHandler(PlatformServices.BlockBreakHandler handler) {
        blockBreakHandlers.add(handler);
    }
    
    @Override
    public void registerCommand(Consumer<CommandDispatcher<CommandSourceStack>> commandRegister) {
        commandRegisters.add(commandRegister);
    }
    
    public List<PlatformServices.RightClickBlockHandler> getRightClickHandlers() {
        return rightClickHandlers;
    }
    
    public List<PlatformServices.BlockBreakHandler> getBlockBreakHandlers() {
        return blockBreakHandlers;
    }
    
    public void registerAll(net.minecraftforge.eventbus.api.IEventBus modEventBus) {
        if (isRegistered) return;
        isRegistered = true;

        for (ForgePlatformRegistry<?> registry : registries.values()) {
            registry.register(modEventBus);
        }
        
        MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) -> {
            if (!rightClickHandlers.isEmpty()) {
                if (event.getHand() != InteractionHand.OFF_HAND) {
                    return;
                }
                
                Player player = event.getEntity();
                InteractionHand hand = event.getHand();
                BlockPos pos = event.getPos();
                InteractionResult result = InteractionResult.PASS;
                
                for (PlatformServices.RightClickBlockHandler handler : rightClickHandlers) {
                    result = handler.onRightClickBlock(player, hand, pos, pos);
                    if (result != InteractionResult.PASS) {
                        event.setCancellationResult(result);
                        event.setCanceled(true);
                        break;
                    }
                }
            }
        });
        
        MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.event.level.BlockEvent.BreakEvent event) -> {
            if (!blockBreakHandlers.isEmpty()) {
                Player player = event.getPlayer();
                LevelAccessor level = event.getLevel();
                BlockPos pos = event.getPos();
                BlockState state = event.getState();
                
                for (PlatformServices.BlockBreakHandler handler : blockBreakHandlers) {
                    handler.onBlockBreak(level, pos, state, player);
                }
            }
        });
        
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            for (Consumer<CommandDispatcher<CommandSourceStack>> register : commandRegisters) {
                register.accept(event.getDispatcher());
            }
        });
    }
}
