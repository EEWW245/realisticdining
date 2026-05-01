package com.realisticdining.neoforge;

import com.realisticdining.RealisticDining;
import com.realisticdining.neoforge.loot.ModLootModifiers;
import com.realisticdining.neoforge.registry.ModMenuTypes;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModBlocks;
import com.realisticdining.registry.ModCreativeModeTabs;
import com.realisticdining.registry.ModItems;
import com.realisticdining.registry.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(RealisticDining.MOD_ID)
public class RealisticDiningNeoForge {
    
    public RealisticDiningNeoForge(IEventBus modEventBus) {
        RealisticDining.init();
        
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModCreativeModeTabs.init();
        ModSounds.init();
        ModMenuTypes.register(modEventBus);
        
        modEventBus.register(ModLootModifiers.class);
        
        RealisticDining.LOGGER.info("[Realistic Dining] NeoForge initialization complete!");
    }
}
