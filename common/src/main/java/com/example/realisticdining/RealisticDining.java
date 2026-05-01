package com.example.realisticdining;

import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModCreativeModeTabs;
import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.init.ModMenus;
import com.example.realisticdining.init.ModRecipes;
import com.example.realisticdining.init.ModSounds;
import com.example.realisticdining.loot.ModLootModifiers;
import com.example.realisticdining.loot.GrassDropHandler;
import com.example.realisticdining.events.RicePlaceHandler;
import com.example.realisticdining.commands.CommandRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealisticDining {
    public static final String MOD_ID = "realisticdining";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("========================================");
        LOGGER.info("[Realistic Dining] Initializing common module");
        LOGGER.info("========================================");

        ModBlocks.init();
        LOGGER.info("[Realistic Dining] ModBlocks registered");
        
        ModBlockEntities.init();
        LOGGER.info("[Realistic Dining] ModBlockEntities registered");
        
        ModItems.init();
        LOGGER.info("[Realistic Dining] ModItems registered");
        
        ModMenus.init();
        ModCreativeModeTabs.init();
        ModSounds.init();
        ModRecipes.init();
        
        ModLootModifiers.init();
        RicePlaceHandler.init();
        CommandRegistry.init();
        GrassDropHandler.init();

        LOGGER.info("========================================");
        LOGGER.info("[Realistic Dining] Common module initialized!");
        LOGGER.info("========================================");
    }
}
