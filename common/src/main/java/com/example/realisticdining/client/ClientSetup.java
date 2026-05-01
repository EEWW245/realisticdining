package com.example.realisticdining.client;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.client.renderer.ChoppingBoardRenderer;
import com.example.realisticdining.client.renderer.RoastChickenRenderer;
import com.example.realisticdining.client.renderer.WokFriedEggRenderer;
import com.example.realisticdining.client.renderer.WokRenderer;
import com.example.realisticdining.client.renderer.WokYellowSteakRenderer;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.platform.ServiceHelper;

public class ClientSetup {

    public static void init() {
        RealisticDining.LOGGER.info("========================================");
        RealisticDining.LOGGER.info("[烤鸡调试] ClientSetup 初始化");
        RealisticDining.LOGGER.info("========================================");

        ServiceHelper.getPlatformServices().registerBlockEntityRenderer(ModBlockEntities.WOK, WokRenderer::new);
        ServiceHelper.getPlatformServices().registerBlockEntityRenderer(ModBlockEntities.WOK_YELLOW_STEAK, WokYellowSteakRenderer::new);
        ServiceHelper.getPlatformServices().registerBlockEntityRenderer(ModBlockEntities.WOK_FRIED_EGG, WokFriedEggRenderer::new);
        ServiceHelper.getPlatformServices().registerBlockEntityRenderer(ModBlockEntities.CHOPPING_BOARD, ChoppingBoardRenderer::new);
        ServiceHelper.getPlatformServices().registerBlockEntityRenderer(ModBlockEntities.ROAST_CHICKEN, RoastChickenRenderer::new);

        RealisticDining.LOGGER.info("[烤鸡调试] 渲染层设置完成");
    }
}
