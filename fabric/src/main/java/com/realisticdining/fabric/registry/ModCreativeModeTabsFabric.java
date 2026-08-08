package com.realisticdining.fabric.registry;

import com.realisticdining.RealisticDining;
import com.realisticdining.registry.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabsFabric {
    
    public static void register() {
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.realisticdining"))
                .icon(() -> new ItemStack(ModItems.WOK.get()))
                .displayItems((params, output) -> {
                    // === 饮料（10 个） ===
                    output.accept(new ItemStack(ModItems.MINERAL_WATER.get()));
                    output.accept(new ItemStack(ModItems.MILK_BEER.get()));
                    output.accept(new ItemStack(ModItems.PEACH_GRAPEFRUIT_TEA.get()));
                    output.accept(new ItemStack(ModItems.SPORTS_DRINK.get()));
                    output.accept(new ItemStack(ModItems.ICED_TEA.get()));
                    output.accept(new ItemStack(ModItems.COCONUT_JUICE.get()));
                    output.accept(new ItemStack(ModItems.ORANGE_JUICE.get()));
                    output.accept(new ItemStack(ModItems.SOYMILK.get()));
                    output.accept(new ItemStack(ModItems.COLA.get()));
                    output.accept(new ItemStack(ModItems.BEER.get()));
                    // === 零食（9 个） ===
                    output.accept(new ItemStack(ModItems.CRISP.get()));
                    output.accept(new ItemStack(ModItems.POTATO_CHIPS.get()));
                    output.accept(new ItemStack(ModItems.POTATO_CHIPS_BBQ.get()));
                    output.accept(new ItemStack(ModItems.POTATO_CHIPS_CUCUMBER.get()));
                    output.accept(new ItemStack(ModItems.POTATO_CHIPS_TOMATO.get()));
                    output.accept(new ItemStack(ModItems.CRISPY_FISH_CHIPS.get()));
                    output.accept(new ItemStack(ModItems.ENERGY_BAR.get()));
                    output.accept(new ItemStack(ModItems.COOKIE_BAG.get()));
                    output.accept(new ItemStack(ModItems.COOKIE_BAG_COCONUT_LATTE.get()));
                    // === 食材 ===
                    output.accept(new ItemStack(ModItems.GREEN_ONION.get()));
                    output.accept(new ItemStack(ModItems.GREEN_ONION_SEEDS.get()));
                    output.accept(new ItemStack(ModItems.CORIANDER.get()));
                    output.accept(new ItemStack(ModItems.CORIANDER_SEEDS.get()));
                    output.accept(new ItemStack(ModItems.CHOPPED_GREEN_ONION.get()));
                    output.accept(new ItemStack(ModItems.CHOPPED_GREENS.get()));
                    output.accept(new ItemStack(ModItems.CHOPPED_PORK.get()));
                    output.accept(new ItemStack(ModItems.CHOPPED_CHICKEN.get()));
                    output.accept(new ItemStack(ModItems.CHOPPED_RED_PEPPER.get()));
                    output.accept(new ItemStack(ModItems.RESULT_CORIANDER.get()));
                    output.accept(new ItemStack(ModItems.BEEF_SLICE.get()));
                    output.accept(new ItemStack(ModItems.STIR_FRIED_PORK_CABBAGE.get()));
                    output.accept(new ItemStack(ModItems.PEPPERY_CHICKEN.get()));
                    output.accept(new ItemStack(ModItems.STIR_FRIED_YELLOW_BEEF.get()));
                    // === 工具/方块 ===
                    output.accept(new ItemStack(ModItems.WOK.get()));
                    output.accept(new ItemStack(ModItems.CHOPPING_BOARD.get()));
                    output.accept(new ItemStack(ModItems.VENDING_MACHINE.get()));
                    output.accept(new ItemStack(ModItems.SPATULA.get()));
                    output.accept(new ItemStack(ModItems.COOKBOOK.get()));
                    output.accept(new ItemStack(ModItems.EAT_RICE_GUIDE.get()));
                    output.accept(new ItemStack(ModItems.CHOPSTICKS.get()));
                    output.accept(new ItemStack(ModItems.PLATE.get()));
                    output.accept(new ItemStack(ModItems.RESULT_CHILI.get()));
                    output.accept(new ItemStack(ModItems.TOMATO_SLICE.get()));
                    output.accept(new ItemStack(ModItems.TOMATO_POACHED_EGG.get()));
                })
                .build();
        
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "realistic_dining_tab"),
                tab
        );
    }
}
