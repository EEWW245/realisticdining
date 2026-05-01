package com.realisticdining.neoforge.client;

import com.realisticdining.RealisticDining;
import com.realisticdining.neoforge.client.renderer.FriedRiceEggRenderer;
import com.realisticdining.neoforge.client.renderer.TomatoPoachedEggRenderer;
import com.realisticdining.neoforge.client.renderer.WokFriedEggRenderer;
import com.realisticdining.neoforge.client.renderer.WokYellowSteakRenderer;
import com.realisticdining.registry.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RealisticDining.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoForgeClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(ModBlockEntities.WOK_YELLOW_STEAK.get(), WokYellowSteakRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.WOK_FRIED_EGG.get(), WokFriedEggRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.TOMATO_POACHED_EGG.get(), TomatoPoachedEggRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.FRIED_RICE_EGG.get(), FriedRiceEggRenderer::new);
    }
}
