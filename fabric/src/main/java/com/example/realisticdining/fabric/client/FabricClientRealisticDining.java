package com.example.realisticdining.fabric.client;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.client.renderer.ChoppingBoardRenderer;
import com.example.realisticdining.client.renderer.RoastChickenRenderer;
import com.example.realisticdining.client.renderer.SnackDisplayRenderer;
import com.example.realisticdining.client.renderer.WokFriedEggRenderer;
import com.example.realisticdining.client.renderer.WokRenderer;
import com.example.realisticdining.client.renderer.WokYellowSteakRenderer;
import com.example.realisticdining.client.screen.CookbookScreen;
import com.example.realisticdining.client.screen.EatRiceGuideScreen;
import com.example.realisticdining.client.screen.VendingMachineScreen;
import com.example.realisticdining.fabric.client.pack.PackClientEvents;
import com.example.realisticdining.fabric.client.pack.PackClientSetup;
import com.example.realisticdining.fabric.events.SnackTooltipHandler;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.init.ModMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;

public class FabricClientRealisticDining implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RealisticDining.LOGGER.info("========================================");
        RealisticDining.LOGGER.info("[烤鸡调试] Fabric 客户端初始化");
        RealisticDining.LOGGER.info("========================================");

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORIANDER_CROP.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREEN_ONION_CROP.get(), RenderType.cutout());

        BlockEntityRendererRegistry.register(ModBlockEntities.WOK.get(), WokRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.WOK_YELLOW_STEAK.get(), WokYellowSteakRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.WOK_FRIED_EGG.get(), WokFriedEggRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.CHOPPING_BOARD.get(), ChoppingBoardRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.ROAST_CHICKEN.get(), RoastChickenRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.SNACK_DISPLAY.get(), SnackDisplayRenderer::new);

        ModKeybinds.register();

        SnackTooltipHandler.register();

        MenuScreens.register(ModMenus.EAT_RICE_GUIDE_MENU.get(), EatRiceGuideScreen::new);
        MenuScreens.register(ModMenus.COOKBOOK_MENU.get(), CookbookScreen::new);
        MenuScreens.register(ModMenus.VENDING_MACHINE_MENU.get(), VendingMachineScreen::new);

        // 材质包扩展：注册客户端 tick 事件 + 资源重载监听器
        PackClientEvents.register();
        PackClientSetup.register();

        RealisticDining.LOGGER.info("[烤鸡调试] Fabric 渲染器注册完成");
    }
}
