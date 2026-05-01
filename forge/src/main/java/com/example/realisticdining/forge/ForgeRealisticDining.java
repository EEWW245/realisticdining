package com.example.realisticdining.forge;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.client.renderer.ChoppingBoardRenderer;
import com.example.realisticdining.client.renderer.RoastChickenRenderer;
import com.example.realisticdining.client.renderer.WokFriedEggRenderer;
import com.example.realisticdining.client.renderer.WokRenderer;
import com.example.realisticdining.client.renderer.WokYellowSteakRenderer;
import com.example.realisticdining.forge.platform.ForgePlatformServices;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RealisticDining.MOD_ID)
public class ForgeRealisticDining {
    public ForgeRealisticDining() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RealisticDining.init();

        ForgePlatformServices forgeServices = (ForgePlatformServices) ServiceHelper.getPlatformServices();
        forgeServices.registerAll(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ForgeRealisticDining::onClientSetup);
            modEventBus.addListener(ForgeRealisticDining::registerEntityRenderers);
        });
    }

    private static void onClientSetup(final FMLClientSetupEvent event) {
        RealisticDining.LOGGER.info("========================================");
        RealisticDining.LOGGER.info("[烤鸡调试] Forge 客户端初始化");
        RealisticDining.LOGGER.info("========================================");

        event.enqueueWork(() -> {
            net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(ModBlocks.CORIANDER_CROP.get(), net.minecraft.client.renderer.RenderType.cutout());
        });
    }

    private static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.WOK.get(), WokRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOK_YELLOW_STEAK.get(), WokYellowSteakRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOK_FRIED_EGG.get(), WokFriedEggRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CHOPPING_BOARD.get(), ChoppingBoardRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ROAST_CHICKEN.get(), RoastChickenRenderer::new);

        RealisticDining.LOGGER.info("[烤鸡调试] Forge 渲染器注册完成");
    }
}
