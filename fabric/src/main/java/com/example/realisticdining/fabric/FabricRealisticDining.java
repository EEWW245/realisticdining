package com.example.realisticdining.fabric;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.fabric.client.pack.PackItems;
import com.example.realisticdining.fabric.events.FabricEventHandler;
import com.example.realisticdining.fabric.events.SnackDisplayPlaceHandler;
import com.example.realisticdining.fabric.loot.RandomChanceCondition;
import com.example.realisticdining.fabric.loot.SnackLootInjector;
import com.example.realisticdining.fabric.network.ApplyHungerPacket;
import com.example.realisticdining.fabric.network.ConsumeRicePacket;
import com.example.realisticdining.fabric.network.ConsumeDrinkPacket;
import com.example.realisticdining.fabric.network.PackAnimationPacket;
import com.example.realisticdining.fabric.network.PackFinishPacket;
import com.example.realisticdining.fabric.network.PackSoundPacket;
import com.example.realisticdining.fabric.network.VendingMachinePurchasePacket;
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
        SnackDisplayPlaceHandler.register();
        // 战利品注入：往所有原版箱子追加零食/饮料
        // 先注册自定义 loot condition type（必须在注册表冻结前）
        RandomChanceCondition.register();
        SnackLootInjector.register();
        ConsumeRicePacket.registerServer();
        ConsumeDrinkPacket.registerServer();
        ApplyHungerPacket.registerServer();
        VendingMachinePurchasePacket.registerServer();
        // 材质包扩展：注册共用 pack_empty GeoItem（同步赋值 EMPTY_ITEM，服务端 handler 也能访问）
        PackItems.register();
        // 材质包扩展网络包
        PackAnimationPacket.registerServer();
        PackFinishPacket.registerServer();
        PackSoundPacket.registerServer();
        LOGGER.info("Realistic Dining Fabric mod initialized");
    }
}
