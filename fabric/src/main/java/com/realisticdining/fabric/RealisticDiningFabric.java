package com.realisticdining.fabric;

import com.realisticdining.RealisticDining;
import com.realisticdining.fabric.event.CorianderSeedsLootHandler;
import com.realisticdining.fabric.event.FriedRiceEggPlaceHandler;
import com.realisticdining.fabric.event.GreenOnionSeedsLootHandler;
import com.realisticdining.fabric.event.RicePlaceHandler;
import com.realisticdining.fabric.network.ApplyHungerPacket;
import com.realisticdining.fabric.network.ConsumeRicePacket;
import com.realisticdining.fabric.network.ConsumeDrinkPacket;
import com.realisticdining.fabric.network.VendingMachinePurchasePacket;
import com.realisticdining.fabric.registry.ModCreativeModeTabsFabric;
import com.realisticdining.fabric.registry.ModMenuTypes;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModBlocks;
import com.realisticdining.registry.ModItems;
import com.realisticdining.registry.ModSounds;
import net.fabricmc.api.ModInitializer;

public class RealisticDiningFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        RealisticDining.init();
        
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModCreativeModeTabsFabric.register();
        ModSounds.init();
        
        ModMenuTypes.register();
        
        RicePlaceHandler.register();
        FriedRiceEggPlaceHandler.register();
        GreenOnionSeedsLootHandler.register();
        CorianderSeedsLootHandler.register();
        
        ConsumeRicePacket.registerServer();
        ConsumeDrinkPacket.registerServer();
        ApplyHungerPacket.registerServer();
        VendingMachinePurchasePacket.registerServer();

        RealisticDining.LOGGER.info("[Realistic Dining] Fabric initialization complete!");
    }
}
