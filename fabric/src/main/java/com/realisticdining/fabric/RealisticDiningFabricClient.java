package com.realisticdining.fabric;

import com.realisticdining.fabric.client.renderer.ChoppingBoardRenderer;
import com.realisticdining.fabric.client.renderer.FriedRiceEggRenderer;
import com.realisticdining.fabric.client.renderer.TomatoPoachedEggRenderer;
import com.realisticdining.fabric.client.renderer.WokFriedEggRenderer;
import com.realisticdining.fabric.client.renderer.WokRenderer;
import com.realisticdining.fabric.client.renderer.WokYellowSteakRenderer;
import com.realisticdining.fabric.registry.ModMenuTypes;
import com.realisticdining.menu.CookbookScreen;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

public class RealisticDiningFabricClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHOPPING_BOARD.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOK_BLOCK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOK_YELLOW_STEAK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOK_FRIED_EGG.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RICE_BOWL.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORIANDER_CROP.get(), RenderType.cutout());
        
        BlockEntityRendererRegistry.register(ModBlockEntities.CHOPPING_BOARD.get(), ChoppingBoardRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.WOK.get(), WokRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.WOK_YELLOW_STEAK.get(), WokYellowSteakRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.WOK_FRIED_EGG.get(), WokFriedEggRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TOMATO_POACHED_EGG.get(), TomatoPoachedEggRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.FRIED_RICE_EGG.get(), FriedRiceEggRenderer::new);
        
        MenuScreens.register(ModMenuTypes.COOKBOOK_MENU, CookbookScreen::new);
    }
}
