package com.example.realisticdining;

import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.init.ModBlockEntities;
import com.example.realisticdining.init.ModCreativeModeTabs;
import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.init.ModMenus;
import com.example.realisticdining.init.ModRecipes;
import com.example.realisticdining.init.ModSounds;
import com.example.realisticdining.loot.ModLootModifiers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(realisticdining.MODID)
public class realisticdining {
    public static final String MODID = "realisticdining";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public realisticdining() {
        LOGGER.info("========================================");
        LOGGER.info("[烤鸡调试] realisticdining 构造函数开始执行");
        LOGGER.info("========================================");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册物品、方块、方块实体、菜单、创造模式标签页、声音
        ModItems.register(modEventBus);
        LOGGER.info("[烤鸡调试] ModItems 注册完成");
        ModBlocks.register(modEventBus);
        LOGGER.info("[烤鸡调试] ModBlocks 注册完成");
        ModBlockEntities.register(modEventBus);
        LOGGER.info("[烤鸡调试] ModBlockEntities 注册完成");
        ModMenus.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModSounds.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModRecipes.register(modEventBus);

        // 注册事件
        modEventBus.addListener(this::setup);

        // 注册事件总线
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("========================================");
        LOGGER.info("[烤鸡调试] Cooking Mod 初始化完成！");
        LOGGER.info("========================================");
    }

    private void setup(final FMLCommonSetupEvent event) {
        // 初始化食物兼容性管理器
        event.enqueueWork(() -> {
            com.example.realisticdining.compat.FoodCompatibilityManager.init();
        });
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        // 可以在这里添加 Forge 事件监听器
    }
}