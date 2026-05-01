package com.example.realisticdining.platform;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import java.util.function.Supplier;
import java.util.function.Consumer;

public interface PlatformServices {
    <T> PlatformRegistry<T> createRegistry(ResourceKey<? extends Registry<T>> registryKey, String modId);
    
    default PlatformRegistry<Block> createBlockRegistry(String modId) {
        return createRegistry(Registries.BLOCK, modId);
    }
    
    default PlatformRegistry<Item> createItemRegistry(String modId) {
        return createRegistry(Registries.ITEM, modId);
    }
    
    default PlatformRegistry<BlockEntityType<?>> createBlockEntityRegistry(String modId) {
        return createRegistry(Registries.BLOCK_ENTITY_TYPE, modId);
    }
    
    default PlatformRegistry<SoundEvent> createSoundEventRegistry(String modId) {
        return createRegistry(Registries.SOUND_EVENT, modId);
    }
    
    default PlatformRegistry<MenuType<?>> createMenuRegistry(String modId) {
        return createRegistry(Registries.MENU, modId);
    }
    
    default PlatformRegistry<RecipeSerializer<?>> createRecipeSerializerRegistry(String modId) {
        return createRegistry(Registries.RECIPE_SERIALIZER, modId);
    }
    
    default PlatformRegistry<CreativeModeTab> createCreativeModeTabRegistry(String modId) {
        return createRegistry(Registries.CREATIVE_MODE_TAB, modId);
    }
    
    boolean isModLoaded(String modId);
    
    String getPlatformName();
    
    <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> blockEntityType, BlockEntityRendererProvider<T> rendererProvider);
    
    interface RightClickBlockHandler {
        InteractionResult onRightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockPos hitPos);
    }
    
    void registerRightClickBlockHandler(RightClickBlockHandler handler);
    
    interface BlockBreakHandler {
        void onBlockBreak(LevelAccessor level, BlockPos pos, BlockState state, Player player);
    }
    
    void registerBlockBreakHandler(BlockBreakHandler handler);
    
    void registerCommand(Consumer<CommandDispatcher<CommandSourceStack>> commandRegister);
}
