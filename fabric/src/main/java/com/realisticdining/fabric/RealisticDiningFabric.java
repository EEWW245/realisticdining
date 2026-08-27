package com.realisticdining.fabric;

import com.realisticdining.RealisticDining;
import com.realisticdining.common.ServerEatingState;
import com.realisticdining.fabric.event.CorianderSeedsLootHandler;
import com.realisticdining.fabric.event.FriedRiceEggPlaceHandler;
import com.realisticdining.fabric.event.GreenOnionSeedsLootHandler;
import com.realisticdining.fabric.event.RandomChanceCondition;
import com.realisticdining.fabric.event.RicePlaceHandler;
import com.realisticdining.fabric.event.SnackDisplayPlaceHandler;
import com.realisticdining.fabric.event.SnackLootInjector;
import com.realisticdining.fabric.client.pack.PackItems;
import com.realisticdining.fabric.network.ApplyHungerPacket;
import com.realisticdining.fabric.network.ConsumeRicePacket;
import com.realisticdining.fabric.network.ConsumeDrinkPacket;
import com.realisticdining.fabric.network.PackAnimationPacket;
import com.realisticdining.fabric.network.PackFinishPacket;
import com.realisticdining.fabric.network.PackSoundPacket;
import com.realisticdining.fabric.network.VendingMachinePurchasePacket;
import com.realisticdining.fabric.registry.ModCreativeModeTabsFabric;
import com.realisticdining.fabric.registry.ModMenuTypes;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModBlocks;
import com.realisticdining.registry.ModEffects;
import com.realisticdining.registry.ModItems;
import com.realisticdining.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class RealisticDiningFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        RealisticDining.init();
        
        ModBlocks.init();
        ModItems.init();
        ModEffects.init();
        ModBlockEntities.init();
        ModCreativeModeTabsFabric.register();
        ModSounds.init();
        
        ModMenuTypes.register();

        // 材质包扩展：在注册表冻结前（onInitialize 阶段）注册共用 pack_empty GeoItem
        PackItems.register();
        
        RicePlaceHandler.register();
        FriedRiceEggPlaceHandler.register();
        GreenOnionSeedsLootHandler.register();
        CorianderSeedsLootHandler.register();
        SnackDisplayPlaceHandler.register();
        // 战利品注入：往所有原版箱子追加零食/饮料（30%~50% 区间随机概率，1~2 个）
        // 先注册自定义 loot condition type（必须在注册表冻结前）
        RandomChanceCondition.register();
        SnackLootInjector.register();
        
        ConsumeRicePacket.registerServer();
        ConsumeDrinkPacket.registerServer();
        ApplyHungerPacket.registerServer();
        VendingMachinePurchasePacket.registerServer();
        // 材质包扩展网络包
        PackAnimationPacket.registerServer();
        PackFinishPacket.registerServer();
        PackSoundPacket.registerServer();

        // v2.3.0+ 玩家退出世界时清除 eating 状态（防状态卡死兜底）
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerEatingState.reset(handler.getPlayer().getUUID());
        });

        RealisticDining.LOGGER.info("[Realistic Dining] Fabric initialization complete!");
    }
}
