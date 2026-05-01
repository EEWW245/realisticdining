package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import java.util.function.Supplier;

public class ModCreativeModeTabs {
    private static final PlatformRegistry<CreativeModeTab> CREATIVE_MODE_TABS =
            ServiceHelper.getPlatformServices().createCreativeModeTabRegistry(RealisticDining.MOD_ID);

    public static final Supplier<CreativeModeTab> COOKING_TAB = register("cooking_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.realisticdining.cooking_tab"))
                    .icon(() -> new ItemStack(ModItems.WOK.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GREEN_ONION.get());
                        output.accept(ModItems.GREEN_ONION_SEEDS.get());
                        output.accept(ModItems.CORIANDER.get());
                        output.accept(ModItems.CORIANDER_SEEDS.get());
                        output.accept(ModItems.CHOPPED_GREENS.get());
                        output.accept(ModItems.CHOPPED_PORK.get());
                        output.accept(ModItems.CHOPPED_CHICKEN.get());
                        output.accept(ModItems.CHOPPED_GREEN_ONION.get());
                        output.accept(ModItems.CHOPPED_RED_PEPPER.get());
                        output.accept(ModItems.RESULT_CORIANDER.get());
                        output.accept(ModItems.BEEF_SLICE.get());
                        output.accept(ModItems.RESULT_CHILI.get());
                        output.accept(ModItems.TOMATO_SLICE.get());
                        output.accept(ModItems.TOMATO_POACHED_EGG.get());
                        output.accept(ModItems.STIR_FRIED_PORK_CABBAGE.get());
                        output.accept(ModItems.PEPPERY_CHICKEN.get());
                        output.accept(ModItems.STIR_FRIED_YELLOW_BEEF.get());

                        if (!"fabric".equals(ServiceHelper.getPlatformServices().getPlatformName())) {
                            output.accept(ModItems.ROAST_CHICKEN.get());
                        }

                        output.accept(ModItems.WOK.get());
                        output.accept(ModItems.CHOPPING_BOARD.get());
                        output.accept(ModItems.SPATULA.get());
                        output.accept(ModItems.CHOPSTICKS.get());
                        output.accept(ModItems.PLATE.get());
                        output.accept(ModItems.COOKBOOK.get());
                    })
                    .build()
    );

    private static <T extends CreativeModeTab> Supplier<T> register(String name, Supplier<T> tab) {
        return CREATIVE_MODE_TABS.register(new ResourceLocation(RealisticDining.MOD_ID, name), tab);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
