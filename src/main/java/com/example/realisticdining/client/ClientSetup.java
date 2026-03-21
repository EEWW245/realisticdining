package com.example.realisticdining.client;

import com.example.realisticdining.realisticdining;
import com.example.realisticdining.client.renderer.ChoppingBoardRenderer;
import com.example.realisticdining.client.renderer.WokRenderer;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.obj.ObjLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = realisticdining.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    
    static {
        realisticdining.LOGGER.info("========================================");
        realisticdining.LOGGER.info("[烤鸡调试] ClientSetup 类被加载");
        realisticdining.LOGGER.info("========================================");
    }
    
    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        realisticdining.LOGGER.info("[烤鸡调试] 正在注册OBJ加载器...");
        event.register("obj", ObjLoader.INSTANCE);
        realisticdining.LOGGER.info("[烤鸡调试] OBJ加载器注册完成");
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.WOK.get(), WokRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CHOPPING_BOARD.get(), ChoppingBoardRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOK_BLOCK.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHOPPING_BOARD.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ROAST_CHICKEN.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GREEN_ONION_CROP.get(), RenderType.cutout());
            realisticdining.LOGGER.info("[烤鸡调试] 渲染层设置完成");
        });
    }
}
