package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, realisticdining.MODID);

    public static final RegistryObject<CreativeModeTab> COOKING_TAB = CREATIVE_MODE_TABS.register("cooking_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.realisticdining.cooking_tab"))
                    .icon(() -> new ItemStack(ModItems.WOK.get()))
                    .displayItems((parameters, output) -> {
                        // --- 基础蔬菜 ---
                        output.accept(ModItems.CHOPPED_GREENS.get());
                        output.accept(ModItems.CHOPPED_PORK.get());
                        output.accept(ModItems.GREEN_ONION.get());
                        output.accept(ModItems.GREEN_ONION_SEEDS.get());
                        output.accept(ModItems.CHOPPED_GREEN_ONION.get());
                        output.accept(ModItems.CHOPPED_RED_PEPPER.get());

                        // --- 烹饪成品 ---
                        output.accept(ModItems.STIR_FRIED_PORK_CABBAGE.get());
                        output.accept(ModItems.PEPPERY_CHICKEN.get());

                        // --- 工具与设备 ---
                        output.accept(ModItems.WOK.get());
                        output.accept(ModItems.CHOPPING_BOARD.get());
                        output.accept(ModItems.SPATULA.get());
                        output.accept(ModItems.CHOPSTICKS.get());
                        
                        // --- 食谱书 ---
                        output.accept(ModItems.COOKBOOK.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
