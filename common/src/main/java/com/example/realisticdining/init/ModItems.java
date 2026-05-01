package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.items.PlaceableFoodItem;
import com.example.realisticdining.items.EdibleChopsticksItem;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import java.util.function.Supplier;

public class ModItems {
    private static final PlatformRegistry<Item> ITEMS = ServiceHelper.getPlatformServices().createItemRegistry(RealisticDining.MOD_ID);

    public static final Supplier<Item> VEGETABLE_OIL = register("vegetable_oil",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NAPA_CABBAGE = register("napa_cabbage",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION = register("green_onion",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO = register("tomato",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ONION = register("onion",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_SEEDS = register("green_onion_seeds", () -> new ItemNameBlockItem(ModBlocks.GREEN_ONION_CROP.get(), new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_0 = register("green_onion_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_1 = register("green_onion_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_2 = register("green_onion_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_3 = register("green_onion_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_4 = register("green_onion_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_5 = register("green_onion_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_6 = register("green_onion_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_7 = register("green_onion_stage_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_8 = register("green_onion_stage_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_9 = register("green_onion_stage_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_10 = register("green_onion_stage_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_11 = register("green_onion_stage_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_ONION_STAGE_12 = register("green_onion_stage_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHOPPED_GREEN_ONION = register("chopped_green_onion", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NAPA_CABBAGE_STAGE_0 = register("napa_cabbage_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NAPA_CABBAGE_STAGE_1 = register("napa_cabbage_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NAPA_CABBAGE_STAGE_2 = register("napa_cabbage_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NAPA_CABBAGE_STAGE_3 = register("napa_cabbage_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NAPA_CABBAGE_STAGE_4 = register("napa_cabbage_stage_4", () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> CHOPPED_GREENS = register("chopped_greens", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHOPPED_PORK = register("chopped_pork", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> STIR_FRIED_PORK_CABBAGE = register("stir_fried_pork_cabbage", () -> new PlaceableFoodItem(
            ModBlocks.STIR_FRIED_PORK_CABBAGE_PLATE.get(),
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(1.2f)
                            .meat()
                            .build())
    ));

    public static final Supplier<Item> PEPPERY_CHICKEN = register("peppery_chicken", () -> new PlaceableFoodItem(
            ModBlocks.PEPPERY_CHICKEN_PLATE.get(),
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(10)
                            .saturationMod(1.5f)
                            .meat()
                            .build())
    ));

    public static final Supplier<Item> STIR_FRIED_YELLOW_BEEF = register("stir_fried_yellow_beef", () -> new PlaceableFoodItem(
            ModBlocks.STIR_FRIED_YELLOW_BEEF_PLATE.get(),
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(10)
                            .saturationMod(1.5f)
                            .meat()
                            .build())
    ));

    public static final Supplier<Item> WOK = register("wok", () -> new BlockItem(
            ModBlocks.WOK_BLOCK.get(),
            new Item.Properties()
                    .stacksTo(1)
                    .defaultDurability(256)
    ));

    public static final Supplier<Item> PORK_PIECES = register("pork_pieces", () -> new BlockItem(
            ModBlocks.PORK_PIECES.get(),
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.3f)
                    .meat()
                    .build())
    ));

    public static final Supplier<Item> CHOPPING_BOARD = register("chopping_board", () -> new BlockItem(
            ModBlocks.CHOPPING_BOARD.get(),
            new Item.Properties()
    ));

    public static final Supplier<Item> SPATULA = register("spatula", () -> new Item(new Item.Properties()
            .stacksTo(1)
    ));

    public static final Supplier<Item> COOKBOOK = register("cookbook", () -> new com.example.realisticdining.items.CookbookItem(new Item.Properties()
            .stacksTo(1)
    ));

    public static final Supplier<Item> CHOPSTICKS = register("chopsticks", () -> new Item(new Item.Properties()
            .stacksTo(1)
    ));
    
    public static final Supplier<Item> CHOPSTICKS_PORK = register("chopsticks_pork", () -> new com.example.realisticdining.items.EdibleChopsticksItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.3f)
                    .meat()
                    .alwaysEat()
                    .build())
    ));

    public static final Supplier<Item> CHOPSTICKS_CABBAGE = register("chopsticks_cabbage", () -> new com.example.realisticdining.items.EdibleChopsticksItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(0.2f)
                    .alwaysEat()
                    .build())
    ));

    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_1 = register("chopsticks_peppery_chicken_1", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_2 = register("chopsticks_peppery_chicken_2", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_3 = register("chopsticks_peppery_chicken_3", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_4 = register("chopsticks_peppery_chicken_4", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_5 = register("chopsticks_peppery_chicken_5", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_6 = register("chopsticks_peppery_chicken_6", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_7 = register("chopsticks_peppery_chicken_7", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_8 = register("chopsticks_peppery_chicken_8", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_9 = register("chopsticks_peppery_chicken_9", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_10 = register("chopsticks_peppery_chicken_10", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_11 = register("chopsticks_peppery_chicken_11", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_12 = register("chopsticks_peppery_chicken_12", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_13 = register("chopsticks_peppery_chicken_13", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_14 = register("chopsticks_peppery_chicken_14", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_15 = register("chopsticks_peppery_chicken_15", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_16 = register("chopsticks_peppery_chicken_16", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_17 = register("chopsticks_peppery_chicken_17", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_18 = register("chopsticks_peppery_chicken_18", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_19 = register("chopsticks_peppery_chicken_19", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_20 = register("chopsticks_peppery_chicken_20", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_21 = register("chopsticks_peppery_chicken_21", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_22 = register("chopsticks_peppery_chicken_22", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_23 = register("chopsticks_peppery_chicken_23", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_24 = register("chopsticks_peppery_chicken_24", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_25 = register("chopsticks_peppery_chicken_25", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_26 = register("chopsticks_peppery_chicken_26", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_27 = register("chopsticks_peppery_chicken_27", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_28 = register("chopsticks_peppery_chicken_28", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_29 = register("chopsticks_peppery_chicken_29", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_30 = register("chopsticks_peppery_chicken_30", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_31 = register("chopsticks_peppery_chicken_31", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_32 = register("chopsticks_peppery_chicken_32", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_33 = register("chopsticks_peppery_chicken_33", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_34 = register("chopsticks_peppery_chicken_34", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final Supplier<Item> CHOPSTICKS_PEPPERY_CHICKEN_35 = register("chopsticks_peppery_chicken_35", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));

    public static final Supplier<Item> CHOPSTICKS_YELLOW_BEEF = register("chopsticks_yellow_beef", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().alwaysEat().build())));

    public static final Supplier<Item> CHOPSTICKS_RICE = register("chopsticks_rice", () -> new com.example.realisticdining.items.EdibleChopsticksItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.3f)
                    .alwaysEat()
                    .build())
    ));

    public static final Supplier<Item> WOK_IDLE = register("wok_idle", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_1 = register("wok_base_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_2 = register("wok_base_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_3 = register("wok_base_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_4 = register("wok_base_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_5 = register("wok_base_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_6 = register("wok_base_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_7 = register("wok_base_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_8 = register("wok_base_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_1 = register("wok_fly_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_2 = register("wok_fly_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_3 = register("wok_fly_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_4 = register("wok_fly_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_5 = register("wok_fly_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_6 = register("wok_fly_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_7 = register("wok_fly_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_8 = register("wok_fly_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_1 = register("wok_base_stage2_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_2 = register("wok_base_stage2_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_3 = register("wok_base_stage2_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_4 = register("wok_base_stage2_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_5 = register("wok_base_stage2_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_6 = register("wok_base_stage2_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_7 = register("wok_base_stage2_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE2_8 = register("wok_base_stage2_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_1 = register("wok_fly_stage2_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_2 = register("wok_fly_stage2_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_3 = register("wok_fly_stage2_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_4 = register("wok_fly_stage2_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_5 = register("wok_fly_stage2_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_6 = register("wok_fly_stage2_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_7 = register("wok_fly_stage2_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE2_8 = register("wok_fly_stage2_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_IDLE_STAGE2 = register("wok_idle_stage2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_1 = register("wok_base_stage3_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_2 = register("wok_base_stage3_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_3 = register("wok_base_stage3_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_4 = register("wok_base_stage3_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_5 = register("wok_base_stage3_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_6 = register("wok_base_stage3_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_7 = register("wok_base_stage3_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_BASE_STAGE3_8 = register("wok_base_stage3_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_1 = register("wok_fly_stage3_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_2 = register("wok_fly_stage3_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_3 = register("wok_fly_stage3_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_4 = register("wok_fly_stage3_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_5 = register("wok_fly_stage3_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_6 = register("wok_fly_stage3_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_7 = register("wok_fly_stage3_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_FLY_STAGE3_8 = register("wok_fly_stage3_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_IDLE_STAGE3 = register("wok_idle_stage3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_PORK_ONLY = register("wok_pork_only", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_ONLY = register("wok_chicken_only", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_RED_PEPPER_ONLY = register("wok_red_pepper_only", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_ONLY_3 = register("wok_chicken_only_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_IDLE = register("wok_chicken_3_idle", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_1 = register("wok_chicken_3_base_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_2 = register("wok_chicken_3_base_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_3 = register("wok_chicken_3_base_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_4 = register("wok_chicken_3_base_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_5 = register("wok_chicken_3_base_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_6 = register("wok_chicken_3_base_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_7 = register("wok_chicken_3_base_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_8 = register("wok_chicken_3_base_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_1 = register("wok_chicken_3_fly_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_2 = register("wok_chicken_3_fly_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_3 = register("wok_chicken_3_fly_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_4 = register("wok_chicken_3_fly_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_5 = register("wok_chicken_3_fly_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_6 = register("wok_chicken_3_fly_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_7 = register("wok_chicken_3_fly_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_8 = register("wok_chicken_3_fly_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_9 = register("wok_chicken_3_base_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_10 = register("wok_chicken_3_base_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_11 = register("wok_chicken_3_base_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_12 = register("wok_chicken_3_base_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_13 = register("wok_chicken_3_base_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_14 = register("wok_chicken_3_base_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_15 = register("wok_chicken_3_base_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_16 = register("wok_chicken_3_base_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_17 = register("wok_chicken_3_base_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_18 = register("wok_chicken_3_base_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_19 = register("wok_chicken_3_base_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_20 = register("wok_chicken_3_base_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_21 = register("wok_chicken_3_base_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_22 = register("wok_chicken_3_base_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_23 = register("wok_chicken_3_base_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_24 = register("wok_chicken_3_base_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_25 = register("wok_chicken_3_base_25", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_26 = register("wok_chicken_3_base_26", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_27 = register("wok_chicken_3_base_27", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_28 = register("wok_chicken_3_base_28", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_29 = register("wok_chicken_3_base_29", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_30 = register("wok_chicken_3_base_30", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_9 = register("wok_chicken_3_fly_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_10 = register("wok_chicken_3_fly_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_11 = register("wok_chicken_3_fly_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_12 = register("wok_chicken_3_fly_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_13 = register("wok_chicken_3_fly_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_14 = register("wok_chicken_3_fly_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_15 = register("wok_chicken_3_fly_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_16 = register("wok_chicken_3_fly_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_17 = register("wok_chicken_3_fly_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_18 = register("wok_chicken_3_fly_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_19 = register("wok_chicken_3_fly_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_20 = register("wok_chicken_3_fly_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_21 = register("wok_chicken_3_fly_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_22 = register("wok_chicken_3_fly_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_23 = register("wok_chicken_3_fly_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_24 = register("wok_chicken_3_fly_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_25 = register("wok_chicken_3_fly_25", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_26 = register("wok_chicken_3_fly_26", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_27 = register("wok_chicken_3_fly_27", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_28 = register("wok_chicken_3_fly_28", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_29 = register("wok_chicken_3_fly_29", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_30 = register("wok_chicken_3_fly_30", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_IDLE_STAGE2 = register("wok_chicken_3_idle_stage2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_1 = register("wok_chicken_3_base_stage2_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_2 = register("wok_chicken_3_base_stage2_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_3 = register("wok_chicken_3_base_stage2_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_4 = register("wok_chicken_3_base_stage2_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_5 = register("wok_chicken_3_base_stage2_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_6 = register("wok_chicken_3_base_stage2_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_7 = register("wok_chicken_3_base_stage2_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_8 = register("wok_chicken_3_base_stage2_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_9 = register("wok_chicken_3_base_stage2_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_10 = register("wok_chicken_3_base_stage2_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_11 = register("wok_chicken_3_base_stage2_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_12 = register("wok_chicken_3_base_stage2_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_13 = register("wok_chicken_3_base_stage2_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_14 = register("wok_chicken_3_base_stage2_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_15 = register("wok_chicken_3_base_stage2_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_16 = register("wok_chicken_3_base_stage2_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_17 = register("wok_chicken_3_base_stage2_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_18 = register("wok_chicken_3_base_stage2_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_19 = register("wok_chicken_3_base_stage2_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_20 = register("wok_chicken_3_base_stage2_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_21 = register("wok_chicken_3_base_stage2_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_22 = register("wok_chicken_3_base_stage2_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_23 = register("wok_chicken_3_base_stage2_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_24 = register("wok_chicken_3_base_stage2_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_25 = register("wok_chicken_3_base_stage2_25", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_26 = register("wok_chicken_3_base_stage2_26", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_27 = register("wok_chicken_3_base_stage2_27", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_28 = register("wok_chicken_3_base_stage2_28", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_29 = register("wok_chicken_3_base_stage2_29", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE2_30 = register("wok_chicken_3_base_stage2_30", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_1 = register("wok_chicken_3_fly_stage2_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_2 = register("wok_chicken_3_fly_stage2_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_3 = register("wok_chicken_3_fly_stage2_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_4 = register("wok_chicken_3_fly_stage2_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_5 = register("wok_chicken_3_fly_stage2_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_6 = register("wok_chicken_3_fly_stage2_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_7 = register("wok_chicken_3_fly_stage2_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_8 = register("wok_chicken_3_fly_stage2_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_9 = register("wok_chicken_3_fly_stage2_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_10 = register("wok_chicken_3_fly_stage2_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_11 = register("wok_chicken_3_fly_stage2_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_12 = register("wok_chicken_3_fly_stage2_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_13 = register("wok_chicken_3_fly_stage2_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_14 = register("wok_chicken_3_fly_stage2_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_15 = register("wok_chicken_3_fly_stage2_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_16 = register("wok_chicken_3_fly_stage2_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_17 = register("wok_chicken_3_fly_stage2_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_18 = register("wok_chicken_3_fly_stage2_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_19 = register("wok_chicken_3_fly_stage2_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_20 = register("wok_chicken_3_fly_stage2_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_21 = register("wok_chicken_3_fly_stage2_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_22 = register("wok_chicken_3_fly_stage2_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_23 = register("wok_chicken_3_fly_stage2_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_24 = register("wok_chicken_3_fly_stage2_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_25 = register("wok_chicken_3_fly_stage2_25", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_26 = register("wok_chicken_3_fly_stage2_26", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_27 = register("wok_chicken_3_fly_stage2_27", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_28 = register("wok_chicken_3_fly_stage2_28", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_29 = register("wok_chicken_3_fly_stage2_29", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE2_30 = register("wok_chicken_3_fly_stage2_30", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_IDLE_STAGE3 = register("wok_chicken_3_idle_stage3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_1 = register("wok_chicken_3_base_stage3_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_2 = register("wok_chicken_3_base_stage3_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_3 = register("wok_chicken_3_base_stage3_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_4 = register("wok_chicken_3_base_stage3_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_5 = register("wok_chicken_3_base_stage3_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_6 = register("wok_chicken_3_base_stage3_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_7 = register("wok_chicken_3_base_stage3_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_8 = register("wok_chicken_3_base_stage3_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_9 = register("wok_chicken_3_base_stage3_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_10 = register("wok_chicken_3_base_stage3_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_11 = register("wok_chicken_3_base_stage3_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_12 = register("wok_chicken_3_base_stage3_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_13 = register("wok_chicken_3_base_stage3_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_14 = register("wok_chicken_3_base_stage3_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_15 = register("wok_chicken_3_base_stage3_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_16 = register("wok_chicken_3_base_stage3_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_17 = register("wok_chicken_3_base_stage3_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_18 = register("wok_chicken_3_base_stage3_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_19 = register("wok_chicken_3_base_stage3_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_20 = register("wok_chicken_3_base_stage3_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_21 = register("wok_chicken_3_base_stage3_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_22 = register("wok_chicken_3_base_stage3_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_23 = register("wok_chicken_3_base_stage3_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_24 = register("wok_chicken_3_base_stage3_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_25 = register("wok_chicken_3_base_stage3_25", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_26 = register("wok_chicken_3_base_stage3_26", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_27 = register("wok_chicken_3_base_stage3_27", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_28 = register("wok_chicken_3_base_stage3_28", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_29 = register("wok_chicken_3_base_stage3_29", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_BASE_STAGE3_30 = register("wok_chicken_3_base_stage3_30", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_1 = register("wok_chicken_3_fly_stage3_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_2 = register("wok_chicken_3_fly_stage3_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_3 = register("wok_chicken_3_fly_stage3_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_4 = register("wok_chicken_3_fly_stage3_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_5 = register("wok_chicken_3_fly_stage3_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_6 = register("wok_chicken_3_fly_stage3_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_7 = register("wok_chicken_3_fly_stage3_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_8 = register("wok_chicken_3_fly_stage3_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_9 = register("wok_chicken_3_fly_stage3_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_10 = register("wok_chicken_3_fly_stage3_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_11 = register("wok_chicken_3_fly_stage3_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_12 = register("wok_chicken_3_fly_stage3_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_13 = register("wok_chicken_3_fly_stage3_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_14 = register("wok_chicken_3_fly_stage3_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_15 = register("wok_chicken_3_fly_stage3_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_16 = register("wok_chicken_3_fly_stage3_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_17 = register("wok_chicken_3_fly_stage3_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_18 = register("wok_chicken_3_fly_stage3_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_19 = register("wok_chicken_3_fly_stage3_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_20 = register("wok_chicken_3_fly_stage3_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_21 = register("wok_chicken_3_fly_stage3_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_22 = register("wok_chicken_3_fly_stage3_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_23 = register("wok_chicken_3_fly_stage3_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_24 = register("wok_chicken_3_fly_stage3_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_25 = register("wok_chicken_3_fly_stage3_25", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_26 = register("wok_chicken_3_fly_stage3_26", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_27 = register("wok_chicken_3_fly_stage3_27", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_28 = register("wok_chicken_3_fly_stage3_28", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_29 = register("wok_chicken_3_fly_stage3_29", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WOK_CHICKEN_3_FLY_STAGE3_30 = register("wok_chicken_3_fly_stage3_30", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PORK_STAGE_0 = register("pork_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_1 = register("pork_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_2 = register("pork_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_3 = register("pork_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_4 = register("pork_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_5 = register("pork_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_6 = register("pork_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_7 = register("pork_stage_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_8 = register("pork_stage_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_9 = register("pork_stage_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_10 = register("pork_stage_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_11 = register("pork_stage_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_12 = register("pork_stage_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_13 = register("pork_stage_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_14 = register("pork_stage_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_15 = register("pork_stage_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_16 = register("pork_stage_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_17 = register("pork_stage_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_18 = register("pork_stage_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_19 = register("pork_stage_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_20 = register("pork_stage_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_21 = register("pork_stage_21", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_22 = register("pork_stage_22", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_23 = register("pork_stage_23", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_24 = register("pork_stage_24", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PORK_STAGE_25 = register("pork_stage_25", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CHICKEN_STAGE_0 = register("chicken_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_1 = register("chicken_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_2 = register("chicken_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_3 = register("chicken_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_4 = register("chicken_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_5 = register("chicken_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_6 = register("chicken_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_7 = register("chicken_stage_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_8 = register("chicken_stage_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_9 = register("chicken_stage_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_10 = register("chicken_stage_10", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_11 = register("chicken_stage_11", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_12 = register("chicken_stage_12", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_13 = register("chicken_stage_13", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_14 = register("chicken_stage_14", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_15 = register("chicken_stage_15", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_16 = register("chicken_stage_16", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_17 = register("chicken_stage_17", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_18 = register("chicken_stage_18", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_19 = register("chicken_stage_19", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHICKEN_STAGE_20 = register("chicken_stage_20", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHOPPED_CHICKEN = register("chopped_chicken", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationMod(0.5f)
                    .meat()
                    .build())
    ));

    public static final Supplier<Item> RED_PEPPER_STAGE_0 = register("red_pepper_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_1 = register("red_pepper_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_2 = register("red_pepper_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_3 = register("red_pepper_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_4 = register("red_pepper_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_5 = register("red_pepper_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_6 = register("red_pepper_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_7 = register("red_pepper_stage_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RED_PEPPER_STAGE_8 = register("red_pepper_stage_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CHOPPED_RED_PEPPER = register("chopped_red_pepper", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BEEF_SLICE = register("beef_slice", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI = register("green_chili", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_CHILI_STAGE_0 = register("green_chili_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_1 = register("green_chili_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_2 = register("green_chili_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_3 = register("green_chili_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_4 = register("green_chili_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_5 = register("green_chili_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_6 = register("green_chili_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_7 = register("green_chili_stage_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GREEN_CHILI_STAGE_8 = register("green_chili_stage_8", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RESULT_CHILI = register("result_chili", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RESULT_CORIANDER = register("result_coriander", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> TOMATO_SALAD = register("tomato_salad", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.5f)
                    .build())
    ));

    public static final Supplier<Item> ROAST_CHICKEN = register("roast_chicken", () -> new BlockItem(
            ModBlocks.ROAST_CHICKEN.get(),
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(0.8f)
                    .meat()
                    .build())
    ));

    public static final Supplier<Item> PLATE = register("plate", () -> new BlockItem(
            ModBlocks.PLATE.get(),
            new Item.Properties()
    ));

    public static final Supplier<Item> CORIANDER = register("coriander", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK = register("yellow_steak", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CORIANDER_STAGE_0 = register("coriander_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_1 = register("coriander_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_2 = register("coriander_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_3 = register("coriander_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_4 = register("coriander_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_5 = register("coriander_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_6 = register("coriander_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CORIANDER_STAGE_7 = register("coriander_stage_7", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_STEAK_STAGE_0 = register("yellow_steak_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_1 = register("yellow_steak_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_2 = register("yellow_steak_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_3 = register("yellow_steak_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_4 = register("yellow_steak_stage_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_5 = register("yellow_steak_stage_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_6 = register("yellow_steak_stage_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_7 = register("yellow_steak_stage_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_8 = register("yellow_steak_stage_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> YELLOW_STEAK_STAGE_9 = register("yellow_steak_stage_9", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> TOMATO_SLICE = register("tomato_slice", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> TOMATO_STAGE_0 = register("tomato_stage_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_STAGE_1 = register("tomato_stage_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_STAGE_2 = register("tomato_stage_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_STAGE_3 = register("tomato_stage_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_STAGE_4 = register("tomato_stage_4", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> TOMATO_POACHED_EGG = register("tomato_poached_egg",
            () -> new PlaceableFoodItem(ModBlocks.TOMATO_POACHED_EGG_BLOCK.get(),
                    new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(1.2f)
                            .build())));

    public static final Supplier<Item> CHOPSTICKS_EGG = register("chopsticks_egg",
            () -> new com.example.realisticdining.items.OneBiteChopsticksItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> TOMATO_POACHED_EGG_MODEL_0 = register("tomato_poached_egg_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_POACHED_EGG_MODEL_1 = register("tomato_poached_egg_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_POACHED_EGG_MODEL_2 = register("tomato_poached_egg_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOMATO_POACHED_EGG_MODEL_3 = register("tomato_poached_egg_3", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> FRIED_RICE_EGG = register("fried_rice_egg",
            () -> new PlaceableFoodItem(ModBlocks.FRIED_RICE_EGG_BLOCK.get(),
                    new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.3f)
                            .build())));

    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_0 = register("fried_rice_egg_0", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_1 = register("fried_rice_egg_1", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_2 = register("fried_rice_egg_2", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_3 = register("fried_rice_egg_3", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_4 = register("fried_rice_egg_4", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_5 = register("fried_rice_egg_5", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_6 = register("fried_rice_egg_6", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_7 = register("fried_rice_egg_7", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_8 = register("fried_rice_egg_8", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_9 = register("fried_rice_egg_9", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FRIED_RICE_EGG_MODEL_10 = register("fried_rice_egg_10", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CORIANDER_SEEDS = register("coriander_seeds", () -> new ItemNameBlockItem(
            ModBlocks.CORIANDER_CROP.get(),
            new Item.Properties()
    ));

    public static final Supplier<Item> WOK_FRIED_EGG_ITEM = register("wok_fried_egg", () -> new BlockItem(
            ModBlocks.WOK_FRIED_EGG.get(),
            new Item.Properties().stacksTo(1)
    ));

    private static <T extends Item> Supplier<T> register(String name, Supplier<T> item) {
        return ITEMS.register(new ResourceLocation(RealisticDining.MOD_ID, name), item);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
