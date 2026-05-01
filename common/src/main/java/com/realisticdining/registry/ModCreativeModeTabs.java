package com.realisticdining.registry;

import com.realisticdining.RealisticDining;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(RealisticDining.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> REALISTIC_DINING_TAB = TABS.register("realistic_dining_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.realisticdining"))
                    .icon(() -> new ItemStack(ModItems.WOK.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.GREEN_ONION.get());
                        output.accept(ModItems.GREEN_ONION_SEEDS.get());
                        output.accept(ModItems.CORIANDER.get());
                        output.accept(ModItems.CORIANDER_SEEDS.get());
                        output.accept(ModItems.CHOPPED_GREEN_ONION.get());
                        output.accept(ModItems.CHOPPED_GREENS.get());
                        output.accept(ModItems.CHOPPED_PORK.get());
                        output.accept(ModItems.CHOPPED_CHICKEN.get());
                        output.accept(ModItems.CHOPPED_RED_PEPPER.get());
                        output.accept(ModItems.RESULT_CORIANDER.get());
                        output.accept(ModItems.BEEF_SLICE.get());
                        output.accept(ModItems.STIR_FRIED_PORK_CABBAGE.get());
                        output.accept(ModItems.PEPPERY_CHICKEN.get());
                        output.accept(ModItems.STIR_FRIED_YELLOW_BEEF.get());
                        output.accept(ModItems.ROAST_CHICKEN.get());
                        output.accept(ModItems.WOK.get());
                        output.accept(ModItems.CHOPPING_BOARD.get());
                        output.accept(ModItems.SPATULA.get());
                        output.accept(ModItems.COOKBOOK.get());
                        output.accept(ModItems.CHOPSTICKS.get());
                        output.accept(ModItems.PLATE.get());
                        output.accept(ModItems.RESULT_CHILI.get());
                        output.accept(ModItems.TOMATO_SLICE.get());
                        output.accept(ModItems.TOMATO_POACHED_EGG.get());
                    })
                    .build());

    public static void init() {
        TABS.register();
    }
}
