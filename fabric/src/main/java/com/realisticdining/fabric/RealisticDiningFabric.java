package com.realisticdining.fabric;

import com.realisticdining.RealisticDining;
import com.realisticdining.fabric.event.CorianderSeedsLootHandler;
import com.realisticdining.fabric.event.FriedRiceEggPlaceHandler;
import com.realisticdining.fabric.event.GreenOnionSeedsLootHandler;
import com.realisticdining.fabric.event.RicePlaceHandler;
import com.realisticdining.fabric.registry.ModCreativeModeTabsFabric;
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
        
        RicePlaceHandler.register();
        FriedRiceEggPlaceHandler.register();
        GreenOnionSeedsLootHandler.register();
        CorianderSeedsLootHandler.register();
        
        RealisticDining.LOGGER.info("[Realistic Dining] Fabric initialization complete!");
    }
}
