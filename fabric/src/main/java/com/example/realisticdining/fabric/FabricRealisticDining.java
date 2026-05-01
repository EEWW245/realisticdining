package com.example.realisticdining.fabric;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.fabric.events.FabricEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

public class FabricRealisticDining implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FabricRealisticDining.class);

    @Override
    public void onInitialize() {
        LOGGER.info("Realistic Dining Fabric mod initializing");
        RealisticDining.init();
        FabricEventHandler.register();
        LOGGER.info("Realistic Dining Fabric mod initialized");
    }
}
