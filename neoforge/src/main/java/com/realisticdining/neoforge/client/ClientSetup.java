package com.realisticdining.neoforge.client;

import com.realisticdining.menu.CookbookScreen;
import com.realisticdining.neoforge.client.renderer.ChoppingBoardRenderer;
import com.realisticdining.neoforge.client.renderer.WokRenderer;
import com.realisticdining.neoforge.registry.ModMenuTypes;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = "realisticdining", bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CHOPPING_BOARD.get(), ChoppingBoardRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOK.get(), WokRenderer::new);
    }
    
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COOKBOOK_MENU.get(), CookbookScreen::new);
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHOPPING_BOARD.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOK_BLOCK.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOK_YELLOW_STEAK.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOK_FRIED_EGG.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RICE_BOWL.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CORIANDER_CROP.get(), RenderType.cutout());
        });
    }
}
