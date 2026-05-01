package com.example.realisticdining.fabric.platform;

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
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class FabricPlatformServices implements PlatformServices {
    @Override
    public <T> PlatformRegistry<T> createRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new FabricPlatformRegistry<>(registryKey, modId);
    }
    
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
    
    @Override
    public String getPlatformName() {
        return "fabric";
    }
    
    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
    }
    
    @Override
    public void registerRightClickBlockHandler(PlatformServices.RightClickBlockHandler handler) {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (hand != InteractionHand.OFF_HAND) {
                return InteractionResult.PASS;
            }
            
            BlockPos pos = hitResult.getBlockPos();
            return handler.onRightClickBlock(player, hand, pos, pos);
        });
    }
    
    @Override
    public void registerBlockBreakHandler(PlatformServices.BlockBreakHandler handler) {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            handler.onBlockBreak(world, pos, state, player);
        });
    }
    
    @Override
    public void registerCommand(Consumer<CommandDispatcher<CommandSourceStack>> commandRegister) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            commandRegister.accept(dispatcher);
        });
    }
}
