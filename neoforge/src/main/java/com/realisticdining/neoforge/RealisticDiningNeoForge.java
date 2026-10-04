package com.realisticdining.neoforge;

import com.realisticdining.RealisticDining;
import com.realisticdining.neoforge.client.pack.PackItems;
import com.realisticdining.neoforge.loot.FoodConfigReloadListener;
import com.realisticdining.neoforge.loot.ModLootModifiers;
import com.realisticdining.neoforge.network.ApplyHungerPacket;
import com.realisticdining.neoforge.network.ConsumeRicePacket;
import com.realisticdining.neoforge.network.ConsumeDrinkPacket;
import com.realisticdining.neoforge.network.PackAnimationPacket;
import com.realisticdining.neoforge.network.PackFinishPacket;
import com.realisticdining.neoforge.network.PackSoundPacket;
import com.realisticdining.neoforge.network.VendingMachinePurchasePacket;
import com.realisticdining.neoforge.registry.ModMenuTypes;
import com.realisticdining.registry.ModBlockEntities;
import com.realisticdining.registry.ModBlocks;
import com.realisticdining.registry.ModCreativeModeTabs;
import com.realisticdining.registry.ModEffects;
import com.realisticdining.registry.ModItems;
import com.realisticdining.registry.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(RealisticDining.MOD_ID)
public class RealisticDiningNeoForge {

    public RealisticDiningNeoForge(IEventBus modEventBus) {
        RealisticDining.init();

        ModBlocks.init();
        ModItems.init();
        ModEffects.init();
        ModBlockEntities.init();
        ModCreativeModeTabs.init();
        ModSounds.init();
        ModMenuTypes.register(modEventBus);

        // 材质包扩展物品注册（pack_empty GeoItem 载体）
        PackItems.register(modEventBus);

        modEventBus.register(ModLootModifiers.class);

        // 食物配置数据包 ReloadListener：扫描 data/<任意 ns>/foods/drinks/*.json 和 dishes/*.json
        // 玩家可写数据包覆盖任意饮料/零食/菜品的饱食度、饱和度、buff
        // 注意：AddReloadListenerEvent 是 game bus 事件，必须注册到 NeoForge.EVENT_BUS，
        // 不能注册到 modEventBus（mod bus 只接受 IModBusEvent 子类），否则会在 mod 加载阶段抛
        // IllegalArgumentException: Listener for event class ...AddReloadListenerEvent takes an argument
        // that is not a subtype of the base type interface ...IModBusEvent
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) ->
                event.addListener(new FoodConfigReloadListener()));

        modEventBus.addListener(ConsumeRicePacket::register);
        modEventBus.addListener(ConsumeDrinkPacket::register);
        modEventBus.addListener(ApplyHungerPacket::register);
        modEventBus.addListener(VendingMachinePurchasePacket::register);
        // 材质包扩展网络包（C2S 触发动画 + 消耗 + 音效）
        modEventBus.addListener(PackAnimationPacket::register);
        modEventBus.addListener(PackFinishPacket::register);
        modEventBus.addListener(PackSoundPacket::register);

        RealisticDining.LOGGER.info("[Realistic Dining] NeoForge initialization complete!");
    }
}
