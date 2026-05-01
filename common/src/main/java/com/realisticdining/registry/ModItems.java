package com.realisticdining.registry;

import com.realisticdining.RealisticDining;
import com.realisticdining.items.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(RealisticDining.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> VEGETABLE_OIL = ITEMS.register("vegetable_oil",
            () -> new Item(new Item.Properties().stacksTo(16)));
    
    public static final RegistrySupplier<Item> BEEF_SLICE = ITEMS.register("beef_slice",
            () -> new Item(new Item.Properties()));
    
    public static final RegistrySupplier<Item> NAPA_CABBAGE = ITEMS.register("napa_cabbage",
            () -> new Item(new Item.Properties()));
    
    public static final RegistrySupplier<Item> GREEN_ONION = ITEMS.register("green_onion",
            () -> new Item(new Item.Properties()));
    
    public static final RegistrySupplier<Item> GREEN_ONION_SEEDS = ITEMS.register("green_onion_seeds", 
            () -> new ItemNameBlockItem(ModBlocks.GREEN_ONION_CROP.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> RED_PEPPER = ITEMS.register("red_pepper",
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> CHOPPED_GREEN_ONION = ITEMS.register("chopped_green_onion", 
            () -> new Item(new Item.Properties()));
    
    public static final RegistrySupplier<Item> CHOPPED_GREENS = ITEMS.register("chopped_greens", 
            () -> new Item(new Item.Properties()));
    
    public static final RegistrySupplier<Item> CHOPPED_PORK = ITEMS.register("chopped_pork", 
            () -> new Item(new Item.Properties()));
    
    public static final RegistrySupplier<Item> CHOPPED_CHICKEN = ITEMS.register("chopped_chicken", 
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.5f).build())));

    public static final RegistrySupplier<Item> CHOPPED_RED_PEPPER = ITEMS.register("chopped_red_pepper", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> GREEN_CHILI = ITEMS.register("green_chili", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_0 = ITEMS.register("green_chili_stage_0", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_1 = ITEMS.register("green_chili_stage_1", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_2 = ITEMS.register("green_chili_stage_2", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_3 = ITEMS.register("green_chili_stage_3", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_4 = ITEMS.register("green_chili_stage_4", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_5 = ITEMS.register("green_chili_stage_5", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_6 = ITEMS.register("green_chili_stage_6", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_7 = ITEMS.register("green_chili_stage_7", 
            () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_CHILI_STAGE_8 = ITEMS.register("green_chili_stage_8", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> RESULT_CHILI = ITEMS.register("result_chili", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> CORIANDER = ITEMS.register("coriander", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> RESULT_CORIANDER = ITEMS.register("result_coriander", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> YELLOW_STEAK = ITEMS.register("yellow_steak", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> CORIANDER_STAGE_0 = ITEMS.register("coriander_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_1 = ITEMS.register("coriander_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_2 = ITEMS.register("coriander_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_3 = ITEMS.register("coriander_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_4 = ITEMS.register("coriander_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_5 = ITEMS.register("coriander_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_6 = ITEMS.register("coriander_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CORIANDER_STAGE_7 = ITEMS.register("coriander_stage_7", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_0 = ITEMS.register("yellow_steak_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_1 = ITEMS.register("yellow_steak_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_2 = ITEMS.register("yellow_steak_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_3 = ITEMS.register("yellow_steak_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_4 = ITEMS.register("yellow_steak_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_5 = ITEMS.register("yellow_steak_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_6 = ITEMS.register("yellow_steak_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_7 = ITEMS.register("yellow_steak_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_8 = ITEMS.register("yellow_steak_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOW_STEAK_STAGE_9 = ITEMS.register("yellow_steak_stage_9", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> STIR_FRIED_PORK_CABBAGE = ITEMS.register("stir_fried_pork_cabbage", 
            () -> new PlaceableFoodItem(ModBlocks.STIR_FRIED_PORK_CABBAGE_PLATE.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(1.2f).build())));

    public static final RegistrySupplier<Item> PEPPERY_CHICKEN = ITEMS.register("peppery_chicken", 
            () -> new PlaceableFoodItem(ModBlocks.PEPPERY_CHICKEN_PLATE.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationModifier(1.5f).build())));

    public static final RegistrySupplier<Item> STIR_FRIED_YELLOW_BEEF = ITEMS.register("stir_fried_yellow_beef", 
            () -> new PlaceableFoodItem(ModBlocks.STIR_FRIED_YELLOW_BEEF_PLATE.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationModifier(1.5f).build())));

    public static final RegistrySupplier<Item> WOK = ITEMS.register("wok", 
            () -> new BlockItem(ModBlocks.WOK_BLOCK.get(), new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> PORK_PIECES = ITEMS.register("pork_pieces", 
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> CHOPPING_BOARD = ITEMS.register("chopping_board", 
            () -> new BlockItem(ModBlocks.CHOPPING_BOARD.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> WOK_YELLOW_STEAK = ITEMS.register("wok_yellow_steak", 
            () -> new BlockItem(ModBlocks.WOK_YELLOW_STEAK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> SPATULA = ITEMS.register("spatula", 
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> COOKBOOK = ITEMS.register("cookbook", 
            () -> new CookbookItem(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> CHOPSTICKS = ITEMS.register("chopsticks", 
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> CHOPSTICKS_PORK = ITEMS.register("chopsticks_pork", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1)
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final RegistrySupplier<Item> CHOPSTICKS_CABBAGE = ITEMS.register("chopsticks_cabbage", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1)
                    .food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final RegistrySupplier<Item> CHOPSTICKS_RICE = ITEMS.register("chopsticks_rice", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1)
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_1 = ITEMS.register("chopsticks_peppery_chicken_1", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_2 = ITEMS.register("chopsticks_peppery_chicken_2", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_3 = ITEMS.register("chopsticks_peppery_chicken_3", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_4 = ITEMS.register("chopsticks_peppery_chicken_4", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_5 = ITEMS.register("chopsticks_peppery_chicken_5", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_6 = ITEMS.register("chopsticks_peppery_chicken_6", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_7 = ITEMS.register("chopsticks_peppery_chicken_7", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_8 = ITEMS.register("chopsticks_peppery_chicken_8", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_9 = ITEMS.register("chopsticks_peppery_chicken_9", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_10 = ITEMS.register("chopsticks_peppery_chicken_10", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_11 = ITEMS.register("chopsticks_peppery_chicken_11", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_12 = ITEMS.register("chopsticks_peppery_chicken_12", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_13 = ITEMS.register("chopsticks_peppery_chicken_13", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_14 = ITEMS.register("chopsticks_peppery_chicken_14", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_15 = ITEMS.register("chopsticks_peppery_chicken_15", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_16 = ITEMS.register("chopsticks_peppery_chicken_16", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_17 = ITEMS.register("chopsticks_peppery_chicken_17", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_18 = ITEMS.register("chopsticks_peppery_chicken_18", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_19 = ITEMS.register("chopsticks_peppery_chicken_19", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_20 = ITEMS.register("chopsticks_peppery_chicken_20", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_21 = ITEMS.register("chopsticks_peppery_chicken_21", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_22 = ITEMS.register("chopsticks_peppery_chicken_22", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_23 = ITEMS.register("chopsticks_peppery_chicken_23", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_24 = ITEMS.register("chopsticks_peppery_chicken_24", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_25 = ITEMS.register("chopsticks_peppery_chicken_25", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_26 = ITEMS.register("chopsticks_peppery_chicken_26", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_27 = ITEMS.register("chopsticks_peppery_chicken_27", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_28 = ITEMS.register("chopsticks_peppery_chicken_28", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_29 = ITEMS.register("chopsticks_peppery_chicken_29", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_30 = ITEMS.register("chopsticks_peppery_chicken_30", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_31 = ITEMS.register("chopsticks_peppery_chicken_31", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_32 = ITEMS.register("chopsticks_peppery_chicken_32", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_33 = ITEMS.register("chopsticks_peppery_chicken_33", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_34 = ITEMS.register("chopsticks_peppery_chicken_34", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final RegistrySupplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_35 = ITEMS.register("chopsticks_peppery_chicken_35", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final RegistrySupplier<Item> CHOPSTICKS_YELLOW_BEEF = ITEMS.register("chopsticks_yellow_beef", 
            () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final RegistrySupplier<Item> TOMATO_SLICE = ITEMS.register("tomato_slice",
            () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> TOMATO_STAGE_0 = ITEMS.register("tomato_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_STAGE_1 = ITEMS.register("tomato_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_STAGE_2 = ITEMS.register("tomato_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_STAGE_3 = ITEMS.register("tomato_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_STAGE_4 = ITEMS.register("tomato_stage_4", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> TOMATO_POACHED_EGG = ITEMS.register("tomato_poached_egg",
            () -> new PlaceableFoodItem(ModBlocks.TOMATO_POACHED_EGG_BLOCK.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(1.2f).build())));

    public static final RegistrySupplier<Item> CHOPSTICKS_EGG = ITEMS.register("chopsticks_egg",
            () -> new OneBiteChopsticksItem(new Item.Properties().stacksTo(1)
                    .food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));

    public static final RegistrySupplier<Item> TOMATO_POACHED_EGG_MODEL_0 = ITEMS.register("tomato_poached_egg_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_POACHED_EGG_MODEL_1 = ITEMS.register("tomato_poached_egg_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_POACHED_EGG_MODEL_2 = ITEMS.register("tomato_poached_egg_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TOMATO_POACHED_EGG_MODEL_3 = ITEMS.register("tomato_poached_egg_3", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> WOK_FRIED_EGG_ITEM = ITEMS.register("wok_fried_egg",
            () -> new BlockItem(ModBlocks.WOK_FRIED_EGG.get(), new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> ROAST_CHICKEN = ITEMS.register("roast_chicken", 
            () -> new BlockItem(ModBlocks.ROAST_CHICKEN.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8f).build())));

    public static final RegistrySupplier<Item> PLATE = ITEMS.register("plate", 
            () -> new BlockItem(ModBlocks.PLATE.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> PORK_STAGE_0 = ITEMS.register("pork_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_1 = ITEMS.register("pork_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_2 = ITEMS.register("pork_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_3 = ITEMS.register("pork_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_4 = ITEMS.register("pork_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_5 = ITEMS.register("pork_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_6 = ITEMS.register("pork_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_7 = ITEMS.register("pork_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_8 = ITEMS.register("pork_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_9 = ITEMS.register("pork_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_10 = ITEMS.register("pork_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_11 = ITEMS.register("pork_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_12 = ITEMS.register("pork_stage_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_13 = ITEMS.register("pork_stage_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_14 = ITEMS.register("pork_stage_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_15 = ITEMS.register("pork_stage_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_16 = ITEMS.register("pork_stage_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_17 = ITEMS.register("pork_stage_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_18 = ITEMS.register("pork_stage_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_19 = ITEMS.register("pork_stage_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_20 = ITEMS.register("pork_stage_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_21 = ITEMS.register("pork_stage_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_22 = ITEMS.register("pork_stage_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_23 = ITEMS.register("pork_stage_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_24 = ITEMS.register("pork_stage_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> PORK_STAGE_25 = ITEMS.register("pork_stage_25", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> CHICKEN_STAGE_0 = ITEMS.register("chicken_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_1 = ITEMS.register("chicken_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_2 = ITEMS.register("chicken_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_3 = ITEMS.register("chicken_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_4 = ITEMS.register("chicken_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_5 = ITEMS.register("chicken_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_6 = ITEMS.register("chicken_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_7 = ITEMS.register("chicken_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_8 = ITEMS.register("chicken_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_9 = ITEMS.register("chicken_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_10 = ITEMS.register("chicken_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_11 = ITEMS.register("chicken_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_12 = ITEMS.register("chicken_stage_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_13 = ITEMS.register("chicken_stage_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_14 = ITEMS.register("chicken_stage_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_15 = ITEMS.register("chicken_stage_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_16 = ITEMS.register("chicken_stage_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_17 = ITEMS.register("chicken_stage_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_18 = ITEMS.register("chicken_stage_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_19 = ITEMS.register("chicken_stage_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> CHICKEN_STAGE_20 = ITEMS.register("chicken_stage_20", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_0 = ITEMS.register("red_pepper_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_1 = ITEMS.register("red_pepper_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_2 = ITEMS.register("red_pepper_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_3 = ITEMS.register("red_pepper_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_4 = ITEMS.register("red_pepper_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_5 = ITEMS.register("red_pepper_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_6 = ITEMS.register("red_pepper_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_7 = ITEMS.register("red_pepper_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RED_PEPPER_STAGE_8 = ITEMS.register("red_pepper_stage_8", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_0 = ITEMS.register("napa_cabbage_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_1 = ITEMS.register("napa_cabbage_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_2 = ITEMS.register("napa_cabbage_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_3 = ITEMS.register("napa_cabbage_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_4 = ITEMS.register("napa_cabbage_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_5 = ITEMS.register("napa_cabbage_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_6 = ITEMS.register("napa_cabbage_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_7 = ITEMS.register("napa_cabbage_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_8 = ITEMS.register("napa_cabbage_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_9 = ITEMS.register("napa_cabbage_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_10 = ITEMS.register("napa_cabbage_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_11 = ITEMS.register("napa_cabbage_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_12 = ITEMS.register("napa_cabbage_stage_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_13 = ITEMS.register("napa_cabbage_stage_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_14 = ITEMS.register("napa_cabbage_stage_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_15 = ITEMS.register("napa_cabbage_stage_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_16 = ITEMS.register("napa_cabbage_stage_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_17 = ITEMS.register("napa_cabbage_stage_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_18 = ITEMS.register("napa_cabbage_stage_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_19 = ITEMS.register("napa_cabbage_stage_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_20 = ITEMS.register("napa_cabbage_stage_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_21 = ITEMS.register("napa_cabbage_stage_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_22 = ITEMS.register("napa_cabbage_stage_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_23 = ITEMS.register("napa_cabbage_stage_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_24 = ITEMS.register("napa_cabbage_stage_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_25 = ITEMS.register("napa_cabbage_stage_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_26 = ITEMS.register("napa_cabbage_stage_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_27 = ITEMS.register("napa_cabbage_stage_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_28 = ITEMS.register("napa_cabbage_stage_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_29 = ITEMS.register("napa_cabbage_stage_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_30 = ITEMS.register("napa_cabbage_stage_30", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_31 = ITEMS.register("napa_cabbage_stage_31", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_32 = ITEMS.register("napa_cabbage_stage_32", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_33 = ITEMS.register("napa_cabbage_stage_33", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_34 = ITEMS.register("napa_cabbage_stage_34", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_35 = ITEMS.register("napa_cabbage_stage_35", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_36 = ITEMS.register("napa_cabbage_stage_36", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_37 = ITEMS.register("napa_cabbage_stage_37", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_38 = ITEMS.register("napa_cabbage_stage_38", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_39 = ITEMS.register("napa_cabbage_stage_39", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_40 = ITEMS.register("napa_cabbage_stage_40", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_41 = ITEMS.register("napa_cabbage_stage_41", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_42 = ITEMS.register("napa_cabbage_stage_42", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_43 = ITEMS.register("napa_cabbage_stage_43", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_44 = ITEMS.register("napa_cabbage_stage_44", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_45 = ITEMS.register("napa_cabbage_stage_45", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_46 = ITEMS.register("napa_cabbage_stage_46", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_47 = ITEMS.register("napa_cabbage_stage_47", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_48 = ITEMS.register("napa_cabbage_stage_48", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_49 = ITEMS.register("napa_cabbage_stage_49", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_50 = ITEMS.register("napa_cabbage_stage_50", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_51 = ITEMS.register("napa_cabbage_stage_51", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_52 = ITEMS.register("napa_cabbage_stage_52", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_53 = ITEMS.register("napa_cabbage_stage_53", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_54 = ITEMS.register("napa_cabbage_stage_54", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_55 = ITEMS.register("napa_cabbage_stage_55", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_56 = ITEMS.register("napa_cabbage_stage_56", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_57 = ITEMS.register("napa_cabbage_stage_57", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_58 = ITEMS.register("napa_cabbage_stage_58", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_59 = ITEMS.register("napa_cabbage_stage_59", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_60 = ITEMS.register("napa_cabbage_stage_60", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_61 = ITEMS.register("napa_cabbage_stage_61", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_62 = ITEMS.register("napa_cabbage_stage_62", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_63 = ITEMS.register("napa_cabbage_stage_63", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_64 = ITEMS.register("napa_cabbage_stage_64", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_65 = ITEMS.register("napa_cabbage_stage_65", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_66 = ITEMS.register("napa_cabbage_stage_66", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_67 = ITEMS.register("napa_cabbage_stage_67", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_68 = ITEMS.register("napa_cabbage_stage_68", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_69 = ITEMS.register("napa_cabbage_stage_69", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> NAPA_CABBAGE_STAGE_70 = ITEMS.register("napa_cabbage_stage_70", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_0 = ITEMS.register("green_onion_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_1 = ITEMS.register("green_onion_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_2 = ITEMS.register("green_onion_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_3 = ITEMS.register("green_onion_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_4 = ITEMS.register("green_onion_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_5 = ITEMS.register("green_onion_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_6 = ITEMS.register("green_onion_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_7 = ITEMS.register("green_onion_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_8 = ITEMS.register("green_onion_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_9 = ITEMS.register("green_onion_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_10 = ITEMS.register("green_onion_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_11 = ITEMS.register("green_onion_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> GREEN_ONION_STAGE_12 = ITEMS.register("green_onion_stage_12", () -> new Item(new Item.Properties()));

    // ==================== 炒锅翻炒3D模型物品 ====================
    // 猪肉炒青菜 - 安静状态
    public static final RegistrySupplier<Item> WOK_IDLE = ITEMS.register("wok_idle", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_IDLE_STAGE2 = ITEMS.register("wok_idle_stage2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_IDLE_STAGE3 = ITEMS.register("wok_idle_stage3", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 只有猪肉
    public static final RegistrySupplier<Item> WOK_PORK_ONLY = ITEMS.register("wok_pork_only", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 锅底状态 (8种变体)
    public static final RegistrySupplier<Item> WOK_BASE_1 = ITEMS.register("wok_base_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_2 = ITEMS.register("wok_base_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_3 = ITEMS.register("wok_base_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_4 = ITEMS.register("wok_base_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_5 = ITEMS.register("wok_base_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_6 = ITEMS.register("wok_base_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_7 = ITEMS.register("wok_base_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_8 = ITEMS.register("wok_base_8", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 飞起状态 (8种变体)
    public static final RegistrySupplier<Item> WOK_FLY_1 = ITEMS.register("wok_fly_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_2 = ITEMS.register("wok_fly_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_3 = ITEMS.register("wok_fly_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_4 = ITEMS.register("wok_fly_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_5 = ITEMS.register("wok_fly_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_6 = ITEMS.register("wok_fly_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_7 = ITEMS.register("wok_fly_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_8 = ITEMS.register("wok_fly_8", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 第二阶段锅底
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_1 = ITEMS.register("wok_base_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_2 = ITEMS.register("wok_base_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_3 = ITEMS.register("wok_base_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_4 = ITEMS.register("wok_base_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_5 = ITEMS.register("wok_base_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_6 = ITEMS.register("wok_base_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_7 = ITEMS.register("wok_base_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE2_8 = ITEMS.register("wok_base_stage2_8", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 第二阶段飞起
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_1 = ITEMS.register("wok_fly_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_2 = ITEMS.register("wok_fly_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_3 = ITEMS.register("wok_fly_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_4 = ITEMS.register("wok_fly_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_5 = ITEMS.register("wok_fly_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_6 = ITEMS.register("wok_fly_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_7 = ITEMS.register("wok_fly_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE2_8 = ITEMS.register("wok_fly_stage2_8", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 第三阶段锅底
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_1 = ITEMS.register("wok_base_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_2 = ITEMS.register("wok_base_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_3 = ITEMS.register("wok_base_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_4 = ITEMS.register("wok_base_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_5 = ITEMS.register("wok_base_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_6 = ITEMS.register("wok_base_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_7 = ITEMS.register("wok_base_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_BASE_STAGE3_8 = ITEMS.register("wok_base_stage3_8", () -> new Item(new Item.Properties()));
    
    // 猪肉炒青菜 - 第三阶段飞起
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_1 = ITEMS.register("wok_fly_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_2 = ITEMS.register("wok_fly_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_3 = ITEMS.register("wok_fly_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_4 = ITEMS.register("wok_fly_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_5 = ITEMS.register("wok_fly_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_6 = ITEMS.register("wok_fly_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_7 = ITEMS.register("wok_fly_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_FLY_STAGE3_8 = ITEMS.register("wok_fly_stage3_8", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 只有鸡肉
    public static final RegistrySupplier<Item> WOK_CHICKEN_ONLY = ITEMS.register("wok_chicken_only", () -> new Item(new Item.Properties()));
    // 辣子鸡 - 鸡肉+红辣椒
    public static final RegistrySupplier<Item> WOK_RED_PEPPER_ONLY = ITEMS.register("wok_red_pepper_only", () -> new Item(new Item.Properties()));
    // 辣子鸡 - 鸡肉+红辣椒+葱
    public static final RegistrySupplier<Item> WOK_CHICKEN_ONLY_3 = ITEMS.register("wok_chicken_only_3", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 安静状态
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_IDLE = ITEMS.register("wok_chicken_3_idle", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_IDLE_STAGE2 = ITEMS.register("wok_chicken_3_idle_stage2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_IDLE_STAGE3 = ITEMS.register("wok_chicken_3_idle_stage3", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 锅底状态 (30种变体)
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_1 = ITEMS.register("wok_chicken_3_base_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_2 = ITEMS.register("wok_chicken_3_base_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_3 = ITEMS.register("wok_chicken_3_base_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_4 = ITEMS.register("wok_chicken_3_base_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_5 = ITEMS.register("wok_chicken_3_base_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_6 = ITEMS.register("wok_chicken_3_base_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_7 = ITEMS.register("wok_chicken_3_base_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_8 = ITEMS.register("wok_chicken_3_base_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_9 = ITEMS.register("wok_chicken_3_base_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_10 = ITEMS.register("wok_chicken_3_base_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_11 = ITEMS.register("wok_chicken_3_base_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_12 = ITEMS.register("wok_chicken_3_base_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_13 = ITEMS.register("wok_chicken_3_base_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_14 = ITEMS.register("wok_chicken_3_base_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_15 = ITEMS.register("wok_chicken_3_base_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_16 = ITEMS.register("wok_chicken_3_base_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_17 = ITEMS.register("wok_chicken_3_base_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_18 = ITEMS.register("wok_chicken_3_base_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_19 = ITEMS.register("wok_chicken_3_base_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_20 = ITEMS.register("wok_chicken_3_base_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_21 = ITEMS.register("wok_chicken_3_base_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_22 = ITEMS.register("wok_chicken_3_base_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_23 = ITEMS.register("wok_chicken_3_base_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_24 = ITEMS.register("wok_chicken_3_base_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_25 = ITEMS.register("wok_chicken_3_base_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_26 = ITEMS.register("wok_chicken_3_base_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_27 = ITEMS.register("wok_chicken_3_base_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_28 = ITEMS.register("wok_chicken_3_base_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_29 = ITEMS.register("wok_chicken_3_base_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_30 = ITEMS.register("wok_chicken_3_base_30", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 飞起状态 (30种变体)
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_1 = ITEMS.register("wok_chicken_3_fly_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_2 = ITEMS.register("wok_chicken_3_fly_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_3 = ITEMS.register("wok_chicken_3_fly_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_4 = ITEMS.register("wok_chicken_3_fly_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_5 = ITEMS.register("wok_chicken_3_fly_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_6 = ITEMS.register("wok_chicken_3_fly_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_7 = ITEMS.register("wok_chicken_3_fly_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_8 = ITEMS.register("wok_chicken_3_fly_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_9 = ITEMS.register("wok_chicken_3_fly_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_10 = ITEMS.register("wok_chicken_3_fly_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_11 = ITEMS.register("wok_chicken_3_fly_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_12 = ITEMS.register("wok_chicken_3_fly_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_13 = ITEMS.register("wok_chicken_3_fly_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_14 = ITEMS.register("wok_chicken_3_fly_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_15 = ITEMS.register("wok_chicken_3_fly_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_16 = ITEMS.register("wok_chicken_3_fly_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_17 = ITEMS.register("wok_chicken_3_fly_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_18 = ITEMS.register("wok_chicken_3_fly_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_19 = ITEMS.register("wok_chicken_3_fly_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_20 = ITEMS.register("wok_chicken_3_fly_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_21 = ITEMS.register("wok_chicken_3_fly_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_22 = ITEMS.register("wok_chicken_3_fly_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_23 = ITEMS.register("wok_chicken_3_fly_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_24 = ITEMS.register("wok_chicken_3_fly_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_25 = ITEMS.register("wok_chicken_3_fly_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_26 = ITEMS.register("wok_chicken_3_fly_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_27 = ITEMS.register("wok_chicken_3_fly_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_28 = ITEMS.register("wok_chicken_3_fly_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_29 = ITEMS.register("wok_chicken_3_fly_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_30 = ITEMS.register("wok_chicken_3_fly_30", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 第二阶段锅底 (30种变体)
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_1 = ITEMS.register("wok_chicken_3_base_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_2 = ITEMS.register("wok_chicken_3_base_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_3 = ITEMS.register("wok_chicken_3_base_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_4 = ITEMS.register("wok_chicken_3_base_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_5 = ITEMS.register("wok_chicken_3_base_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_6 = ITEMS.register("wok_chicken_3_base_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_7 = ITEMS.register("wok_chicken_3_base_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_8 = ITEMS.register("wok_chicken_3_base_stage2_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_9 = ITEMS.register("wok_chicken_3_base_stage2_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_10 = ITEMS.register("wok_chicken_3_base_stage2_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_11 = ITEMS.register("wok_chicken_3_base_stage2_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_12 = ITEMS.register("wok_chicken_3_base_stage2_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_13 = ITEMS.register("wok_chicken_3_base_stage2_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_14 = ITEMS.register("wok_chicken_3_base_stage2_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_15 = ITEMS.register("wok_chicken_3_base_stage2_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_16 = ITEMS.register("wok_chicken_3_base_stage2_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_17 = ITEMS.register("wok_chicken_3_base_stage2_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_18 = ITEMS.register("wok_chicken_3_base_stage2_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_19 = ITEMS.register("wok_chicken_3_base_stage2_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_20 = ITEMS.register("wok_chicken_3_base_stage2_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_21 = ITEMS.register("wok_chicken_3_base_stage2_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_22 = ITEMS.register("wok_chicken_3_base_stage2_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_23 = ITEMS.register("wok_chicken_3_base_stage2_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_24 = ITEMS.register("wok_chicken_3_base_stage2_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_25 = ITEMS.register("wok_chicken_3_base_stage2_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_26 = ITEMS.register("wok_chicken_3_base_stage2_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_27 = ITEMS.register("wok_chicken_3_base_stage2_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_28 = ITEMS.register("wok_chicken_3_base_stage2_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_29 = ITEMS.register("wok_chicken_3_base_stage2_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE2_30 = ITEMS.register("wok_chicken_3_base_stage2_30", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 第二阶段飞起 (30种变体)
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_1 = ITEMS.register("wok_chicken_3_fly_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_2 = ITEMS.register("wok_chicken_3_fly_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_3 = ITEMS.register("wok_chicken_3_fly_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_4 = ITEMS.register("wok_chicken_3_fly_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_5 = ITEMS.register("wok_chicken_3_fly_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_6 = ITEMS.register("wok_chicken_3_fly_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_7 = ITEMS.register("wok_chicken_3_fly_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_8 = ITEMS.register("wok_chicken_3_fly_stage2_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_9 = ITEMS.register("wok_chicken_3_fly_stage2_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_10 = ITEMS.register("wok_chicken_3_fly_stage2_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_11 = ITEMS.register("wok_chicken_3_fly_stage2_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_12 = ITEMS.register("wok_chicken_3_fly_stage2_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_13 = ITEMS.register("wok_chicken_3_fly_stage2_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_14 = ITEMS.register("wok_chicken_3_fly_stage2_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_15 = ITEMS.register("wok_chicken_3_fly_stage2_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_16 = ITEMS.register("wok_chicken_3_fly_stage2_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_17 = ITEMS.register("wok_chicken_3_fly_stage2_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_18 = ITEMS.register("wok_chicken_3_fly_stage2_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_19 = ITEMS.register("wok_chicken_3_fly_stage2_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_20 = ITEMS.register("wok_chicken_3_fly_stage2_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_21 = ITEMS.register("wok_chicken_3_fly_stage2_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_22 = ITEMS.register("wok_chicken_3_fly_stage2_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_23 = ITEMS.register("wok_chicken_3_fly_stage2_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_24 = ITEMS.register("wok_chicken_3_fly_stage2_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_25 = ITEMS.register("wok_chicken_3_fly_stage2_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_26 = ITEMS.register("wok_chicken_3_fly_stage2_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_27 = ITEMS.register("wok_chicken_3_fly_stage2_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_28 = ITEMS.register("wok_chicken_3_fly_stage2_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_29 = ITEMS.register("wok_chicken_3_fly_stage2_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE2_30 = ITEMS.register("wok_chicken_3_fly_stage2_30", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 第三阶段锅底 (30种变体)
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_1 = ITEMS.register("wok_chicken_3_base_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_2 = ITEMS.register("wok_chicken_3_base_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_3 = ITEMS.register("wok_chicken_3_base_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_4 = ITEMS.register("wok_chicken_3_base_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_5 = ITEMS.register("wok_chicken_3_base_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_6 = ITEMS.register("wok_chicken_3_base_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_7 = ITEMS.register("wok_chicken_3_base_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_8 = ITEMS.register("wok_chicken_3_base_stage3_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_9 = ITEMS.register("wok_chicken_3_base_stage3_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_10 = ITEMS.register("wok_chicken_3_base_stage3_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_11 = ITEMS.register("wok_chicken_3_base_stage3_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_12 = ITEMS.register("wok_chicken_3_base_stage3_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_13 = ITEMS.register("wok_chicken_3_base_stage3_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_14 = ITEMS.register("wok_chicken_3_base_stage3_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_15 = ITEMS.register("wok_chicken_3_base_stage3_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_16 = ITEMS.register("wok_chicken_3_base_stage3_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_17 = ITEMS.register("wok_chicken_3_base_stage3_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_18 = ITEMS.register("wok_chicken_3_base_stage3_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_19 = ITEMS.register("wok_chicken_3_base_stage3_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_20 = ITEMS.register("wok_chicken_3_base_stage3_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_21 = ITEMS.register("wok_chicken_3_base_stage3_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_22 = ITEMS.register("wok_chicken_3_base_stage3_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_23 = ITEMS.register("wok_chicken_3_base_stage3_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_24 = ITEMS.register("wok_chicken_3_base_stage3_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_25 = ITEMS.register("wok_chicken_3_base_stage3_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_26 = ITEMS.register("wok_chicken_3_base_stage3_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_27 = ITEMS.register("wok_chicken_3_base_stage3_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_28 = ITEMS.register("wok_chicken_3_base_stage3_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_29 = ITEMS.register("wok_chicken_3_base_stage3_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_BASE_STAGE3_30 = ITEMS.register("wok_chicken_3_base_stage3_30", () -> new Item(new Item.Properties()));
    
    // 辣子鸡 - 第三阶段飞起 (30种变体)
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_1 = ITEMS.register("wok_chicken_3_fly_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_2 = ITEMS.register("wok_chicken_3_fly_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_3 = ITEMS.register("wok_chicken_3_fly_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_4 = ITEMS.register("wok_chicken_3_fly_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_5 = ITEMS.register("wok_chicken_3_fly_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_6 = ITEMS.register("wok_chicken_3_fly_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_7 = ITEMS.register("wok_chicken_3_fly_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_8 = ITEMS.register("wok_chicken_3_fly_stage3_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_9 = ITEMS.register("wok_chicken_3_fly_stage3_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_10 = ITEMS.register("wok_chicken_3_fly_stage3_10", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_11 = ITEMS.register("wok_chicken_3_fly_stage3_11", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_12 = ITEMS.register("wok_chicken_3_fly_stage3_12", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_13 = ITEMS.register("wok_chicken_3_fly_stage3_13", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_14 = ITEMS.register("wok_chicken_3_fly_stage3_14", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_15 = ITEMS.register("wok_chicken_3_fly_stage3_15", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_16 = ITEMS.register("wok_chicken_3_fly_stage3_16", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_17 = ITEMS.register("wok_chicken_3_fly_stage3_17", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_18 = ITEMS.register("wok_chicken_3_fly_stage3_18", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_19 = ITEMS.register("wok_chicken_3_fly_stage3_19", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_20 = ITEMS.register("wok_chicken_3_fly_stage3_20", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_21 = ITEMS.register("wok_chicken_3_fly_stage3_21", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_22 = ITEMS.register("wok_chicken_3_fly_stage3_22", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_23 = ITEMS.register("wok_chicken_3_fly_stage3_23", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_24 = ITEMS.register("wok_chicken_3_fly_stage3_24", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_25 = ITEMS.register("wok_chicken_3_fly_stage3_25", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_26 = ITEMS.register("wok_chicken_3_fly_stage3_26", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_27 = ITEMS.register("wok_chicken_3_fly_stage3_27", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_28 = ITEMS.register("wok_chicken_3_fly_stage3_28", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_29 = ITEMS.register("wok_chicken_3_fly_stage3_29", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> WOK_CHICKEN_3_FLY_STAGE3_30 = ITEMS.register("wok_chicken_3_fly_stage3_30", () -> new Item(new Item.Properties()));

    public static RegistrySupplier<Item> getChopsticksPepperyChicken(int number) {
        return switch (number) {
            case 1 -> CHOPSTICKS_PEPPERY_CHICKEN_1;
            case 2 -> CHOPSTICKS_PEPPERY_CHICKEN_2;
            case 3 -> CHOPSTICKS_PEPPERY_CHICKEN_3;
            case 4 -> CHOPSTICKS_PEPPERY_CHICKEN_4;
            case 5 -> CHOPSTICKS_PEPPERY_CHICKEN_5;
            case 6 -> CHOPSTICKS_PEPPERY_CHICKEN_6;
            case 7 -> CHOPSTICKS_PEPPERY_CHICKEN_7;
            case 8 -> CHOPSTICKS_PEPPERY_CHICKEN_8;
            case 9 -> CHOPSTICKS_PEPPERY_CHICKEN_9;
            case 10 -> CHOPSTICKS_PEPPERY_CHICKEN_10;
            case 11 -> CHOPSTICKS_PEPPERY_CHICKEN_11;
            case 12 -> CHOPSTICKS_PEPPERY_CHICKEN_12;
            case 13 -> CHOPSTICKS_PEPPERY_CHICKEN_13;
            case 14 -> CHOPSTICKS_PEPPERY_CHICKEN_14;
            case 15 -> CHOPSTICKS_PEPPERY_CHICKEN_15;
            case 16 -> CHOPSTICKS_PEPPERY_CHICKEN_16;
            case 17 -> CHOPSTICKS_PEPPERY_CHICKEN_17;
            case 18 -> CHOPSTICKS_PEPPERY_CHICKEN_18;
            case 19 -> CHOPSTICKS_PEPPERY_CHICKEN_19;
            case 20 -> CHOPSTICKS_PEPPERY_CHICKEN_20;
            case 21 -> CHOPSTICKS_PEPPERY_CHICKEN_21;
            case 22 -> CHOPSTICKS_PEPPERY_CHICKEN_22;
            case 23 -> CHOPSTICKS_PEPPERY_CHICKEN_23;
            case 24 -> CHOPSTICKS_PEPPERY_CHICKEN_24;
            case 25 -> CHOPSTICKS_PEPPERY_CHICKEN_25;
            case 26 -> CHOPSTICKS_PEPPERY_CHICKEN_26;
            case 27 -> CHOPSTICKS_PEPPERY_CHICKEN_27;
            case 28 -> CHOPSTICKS_PEPPERY_CHICKEN_28;
            case 29 -> CHOPSTICKS_PEPPERY_CHICKEN_29;
            case 30 -> CHOPSTICKS_PEPPERY_CHICKEN_30;
            case 31 -> CHOPSTICKS_PEPPERY_CHICKEN_31;
            case 32 -> CHOPSTICKS_PEPPERY_CHICKEN_32;
            case 33 -> CHOPSTICKS_PEPPERY_CHICKEN_33;
            case 34 -> CHOPSTICKS_PEPPERY_CHICKEN_34;
            case 35 -> CHOPSTICKS_PEPPERY_CHICKEN_35;
            default -> CHOPSTICKS_PEPPERY_CHICKEN_1;
        };
    }

    public static final RegistrySupplier<Item> FRIED_RICE_EGG = ITEMS.register("fried_rice_egg",
            () -> new PlaceableFoodItem(ModBlocks.FRIED_RICE_EGG_BLOCK.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(1.2f).build())));

    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_0 = ITEMS.register("fried_rice_egg_0", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_1 = ITEMS.register("fried_rice_egg_1", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_2 = ITEMS.register("fried_rice_egg_2", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_3 = ITEMS.register("fried_rice_egg_3", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_4 = ITEMS.register("fried_rice_egg_4", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_5 = ITEMS.register("fried_rice_egg_5", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_6 = ITEMS.register("fried_rice_egg_6", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_7 = ITEMS.register("fried_rice_egg_7", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_8 = ITEMS.register("fried_rice_egg_8", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_9 = ITEMS.register("fried_rice_egg_9", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> FRIED_RICE_EGG_MODEL_10 = ITEMS.register("fried_rice_egg_10", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> CORIANDER_SEEDS = ITEMS.register("coriander_seeds", 
            () -> new ItemNameBlockItem(ModBlocks.CORIANDER_CROP.get(), new Item.Properties()));

    public static void init() {
        ITEMS.register();
    }
}
