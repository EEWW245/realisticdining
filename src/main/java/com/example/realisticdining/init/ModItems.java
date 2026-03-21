package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import com.example.realisticdining.items.PlaceableFoodItem;
import com.example.realisticdining.items.EdibleChopsticksItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, realisticdining.MODID);

    // ================= 食材 =================
    public static final RegistryObject<Item> VEGETABLE_OIL = ITEMS.register("vegetable_oil",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAPA_CABBAGE = ITEMS.register("napa_cabbage",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION = ITEMS.register("green_onion",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ONION = ITEMS.register("onion",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_SEEDS = ITEMS.register("green_onion_seeds", () -> new ItemNameBlockItem(ModBlocks.GREEN_ONION_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_0 = ITEMS.register("green_onion_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_1 = ITEMS.register("green_onion_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_2 = ITEMS.register("green_onion_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_3 = ITEMS.register("green_onion_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_4 = ITEMS.register("green_onion_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_5 = ITEMS.register("green_onion_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_6 = ITEMS.register("green_onion_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_7 = ITEMS.register("green_onion_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_8 = ITEMS.register("green_onion_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_9 = ITEMS.register("green_onion_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_10 = ITEMS.register("green_onion_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_11 = ITEMS.register("green_onion_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION_STAGE_12 = ITEMS.register("green_onion_stage_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOPPED_GREEN_ONION = ITEMS.register("chopped_green_onion", () -> new Item(new Item.Properties()));
    // 注册大白菜的 5 个切割阶段 (0-4)
    public static final RegistryObject<Item> NAPA_CABBAGE_STAGE_0 = ITEMS.register("napa_cabbage_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAPA_CABBAGE_STAGE_1 = ITEMS.register("napa_cabbage_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAPA_CABBAGE_STAGE_2 = ITEMS.register("napa_cabbage_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAPA_CABBAGE_STAGE_3 = ITEMS.register("napa_cabbage_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NAPA_CABBAGE_STAGE_4 = ITEMS.register("napa_cabbage_stage_4", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> CHOPPED_GREENS = ITEMS.register("chopped_greens", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOPPED_PORK = ITEMS.register("chopped_pork", () -> new Item(new Item.Properties()));

    // ================= 食物 =================
    public static final RegistryObject<Item> STIR_FRIED_PORK_CABBAGE = ITEMS.register("stir_fried_pork_cabbage", () -> new PlaceableFoodItem(
            ModBlocks.STIR_FRIED_PORK_CABBAGE_PLATE.get(),
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(1.2f)
                            .meat()
                            .build())
    ));

    public static final RegistryObject<Item> PEPPERY_CHICKEN = ITEMS.register("peppery_chicken", () -> new PlaceableFoodItem(
            ModBlocks.PEPPERY_CHICKEN_PLATE.get(),
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(10)
                            .saturationMod(1.5f)
                            .meat()
                            .build())
    ));

    // ================= 方块物品与工具 =================

    // 炒锅
    public static final RegistryObject<Item> WOK = ITEMS.register("wok", () -> new BlockItem(
            ModBlocks.WOK_BLOCK.get(),
            new Item.Properties()
                    .stacksTo(1)
                    .defaultDurability(256)
    ));

    // 猪肉块 3D 物品
    public static final RegistryObject<Item> PORK_PIECES = ITEMS.register("pork_pieces", () -> new BlockItem(
            ModBlocks.PORK_PIECES.get(),
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.3f)
                    .meat()
                    .build())
    ));

    // 菜板物品注册
    public static final RegistryObject<Item> CHOPPING_BOARD = ITEMS.register("chopping_board", () -> new BlockItem(
            ModBlocks.CHOPPING_BOARD.get(),
            new Item.Properties()
    ));

    // 铲子物品注册
    public static final RegistryObject<Item> SPATULA = ITEMS.register("spatula", () -> new Item(new Item.Properties()
            .stacksTo(1)
    ));

    // 食谱书物品注册
    public static final RegistryObject<Item> COOKBOOK = ITEMS.register("cookbook", () -> new com.example.realisticdining.items.CookbookItem(new Item.Properties()
            .stacksTo(1)
    ));

    // 筷子物品注册
    public static final RegistryObject<Item> CHOPSTICKS = ITEMS.register("chopsticks", () -> new Item(new Item.Properties()
            .stacksTo(1)
    ));
    
    // 夹着菜的筷子（可食用）
    public static final RegistryObject<Item> CHOPSTICKS_PORK = ITEMS.register("chopsticks_pork", () -> new com.example.realisticdining.items.EdibleChopsticksItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.3f)
                    .meat()
                    .alwaysEat()
                    .build())
    ));

    public static final RegistryObject<Item> CHOPSTICKS_CABBAGE = ITEMS.register("chopsticks_cabbage", () -> new com.example.realisticdining.items.EdibleChopsticksItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(0.2f)
                    .alwaysEat()
                    .build())
    ));

    // ================= 筷子+辣子鸡物品（50个变体） =================
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_1 = ITEMS.register("chopsticks_peppery_chicken_1", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_2 = ITEMS.register("chopsticks_peppery_chicken_2", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_3 = ITEMS.register("chopsticks_peppery_chicken_3", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_4 = ITEMS.register("chopsticks_peppery_chicken_4", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_5 = ITEMS.register("chopsticks_peppery_chicken_5", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_6 = ITEMS.register("chopsticks_peppery_chicken_6", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_7 = ITEMS.register("chopsticks_peppery_chicken_7", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_8 = ITEMS.register("chopsticks_peppery_chicken_8", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_9 = ITEMS.register("chopsticks_peppery_chicken_9", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_10 = ITEMS.register("chopsticks_peppery_chicken_10", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_11 = ITEMS.register("chopsticks_peppery_chicken_11", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_12 = ITEMS.register("chopsticks_peppery_chicken_12", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_13 = ITEMS.register("chopsticks_peppery_chicken_13", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_14 = ITEMS.register("chopsticks_peppery_chicken_14", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_15 = ITEMS.register("chopsticks_peppery_chicken_15", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_16 = ITEMS.register("chopsticks_peppery_chicken_16", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_17 = ITEMS.register("chopsticks_peppery_chicken_17", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_18 = ITEMS.register("chopsticks_peppery_chicken_18", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_19 = ITEMS.register("chopsticks_peppery_chicken_19", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_20 = ITEMS.register("chopsticks_peppery_chicken_20", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_21 = ITEMS.register("chopsticks_peppery_chicken_21", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_22 = ITEMS.register("chopsticks_peppery_chicken_22", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_23 = ITEMS.register("chopsticks_peppery_chicken_23", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_24 = ITEMS.register("chopsticks_peppery_chicken_24", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_25 = ITEMS.register("chopsticks_peppery_chicken_25", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_26 = ITEMS.register("chopsticks_peppery_chicken_26", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_27 = ITEMS.register("chopsticks_peppery_chicken_27", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_28 = ITEMS.register("chopsticks_peppery_chicken_28", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_29 = ITEMS.register("chopsticks_peppery_chicken_29", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_30 = ITEMS.register("chopsticks_peppery_chicken_30", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_31 = ITEMS.register("chopsticks_peppery_chicken_31", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_32 = ITEMS.register("chopsticks_peppery_chicken_32", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_33 = ITEMS.register("chopsticks_peppery_chicken_33", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_34 = ITEMS.register("chopsticks_peppery_chicken_34", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));
    public static final RegistryObject<Item> CHOPSTICKS_PEPPERY_CHICKEN_35 = ITEMS.register("chopsticks_peppery_chicken_35", () -> new EdibleChopsticksItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build())));

    public static final RegistryObject<Item> CHOPSTICKS_RICE = ITEMS.register("chopsticks_rice", () -> new com.example.realisticdining.items.EdibleChopsticksItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.3f)
                    .alwaysEat()
                    .build())
    ));
    // ================= 炒锅专属：17 个动态翻炒虚拟物品 =================
    public static final RegistryObject<Item> WOK_IDLE = ITEMS.register("wok_idle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOK_BASE_1 = ITEMS.register("wok_base_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_2 = ITEMS.register("wok_base_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_3 = ITEMS.register("wok_base_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_4 = ITEMS.register("wok_base_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_5 = ITEMS.register("wok_base_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_6 = ITEMS.register("wok_base_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_7 = ITEMS.register("wok_base_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_8 = ITEMS.register("wok_base_8", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOK_FLY_1 = ITEMS.register("wok_fly_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_2 = ITEMS.register("wok_fly_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_3 = ITEMS.register("wok_fly_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_4 = ITEMS.register("wok_fly_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_5 = ITEMS.register("wok_fly_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_6 = ITEMS.register("wok_fly_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_7 = ITEMS.register("wok_fly_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_8 = ITEMS.register("wok_fly_8", () -> new Item(new Item.Properties()));

    // ================= Stage 2 (19~24次) Models (Base 1-8, Fly 1-8, Idle) =================
    public static final RegistryObject<Item> WOK_BASE_STAGE2_1 = ITEMS.register("wok_base_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_2 = ITEMS.register("wok_base_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_3 = ITEMS.register("wok_base_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_4 = ITEMS.register("wok_base_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_5 = ITEMS.register("wok_base_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_6 = ITEMS.register("wok_base_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_7 = ITEMS.register("wok_base_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE2_8 = ITEMS.register("wok_base_stage2_8", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOK_FLY_STAGE2_1 = ITEMS.register("wok_fly_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_2 = ITEMS.register("wok_fly_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_3 = ITEMS.register("wok_fly_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_4 = ITEMS.register("wok_fly_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_5 = ITEMS.register("wok_fly_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_6 = ITEMS.register("wok_fly_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_7 = ITEMS.register("wok_fly_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE2_8 = ITEMS.register("wok_fly_stage2_8", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> WOK_IDLE_STAGE2 = ITEMS.register("wok_idle_stage2", () -> new Item(new Item.Properties()));

    // ================= Stage 3 (25~30+次) Models (Base 1-8, Fly 1-8, Idle) =================
    public static final RegistryObject<Item> WOK_BASE_STAGE3_1 = ITEMS.register("wok_base_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_2 = ITEMS.register("wok_base_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_3 = ITEMS.register("wok_base_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_4 = ITEMS.register("wok_base_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_5 = ITEMS.register("wok_base_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_6 = ITEMS.register("wok_base_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_7 = ITEMS.register("wok_base_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_BASE_STAGE3_8 = ITEMS.register("wok_base_stage3_8", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOK_FLY_STAGE3_1 = ITEMS.register("wok_fly_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_2 = ITEMS.register("wok_fly_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_3 = ITEMS.register("wok_fly_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_4 = ITEMS.register("wok_fly_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_5 = ITEMS.register("wok_fly_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_6 = ITEMS.register("wok_fly_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_7 = ITEMS.register("wok_fly_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_FLY_STAGE3_8 = ITEMS.register("wok_fly_stage3_8", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> WOK_IDLE_STAGE3 = ITEMS.register("wok_idle_stage3", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOK_PORK_ONLY = ITEMS.register("wok_pork_only", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_ONLY = ITEMS.register("wok_chicken_only", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_RED_PEPPER_ONLY = ITEMS.register("wok_red_pepper_only", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_ONLY_3 = ITEMS.register("wok_chicken_only_3", () -> new Item(new Item.Properties()));

    // ================= 辣子鸡翻炒的 8 种物理变体 x 3 个阶段 =================
    // 阶段1 (0-18次翻炒)
    public static final RegistryObject<Item> WOK_CHICKEN_3_IDLE = ITEMS.register("wok_chicken_3_idle", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_1 = ITEMS.register("wok_chicken_3_base_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_2 = ITEMS.register("wok_chicken_3_base_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_3 = ITEMS.register("wok_chicken_3_base_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_4 = ITEMS.register("wok_chicken_3_base_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_5 = ITEMS.register("wok_chicken_3_base_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_6 = ITEMS.register("wok_chicken_3_base_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_7 = ITEMS.register("wok_chicken_3_base_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_8 = ITEMS.register("wok_chicken_3_base_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_1 = ITEMS.register("wok_chicken_3_fly_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_2 = ITEMS.register("wok_chicken_3_fly_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_3 = ITEMS.register("wok_chicken_3_fly_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_4 = ITEMS.register("wok_chicken_3_fly_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_5 = ITEMS.register("wok_chicken_3_fly_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_6 = ITEMS.register("wok_chicken_3_fly_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_7 = ITEMS.register("wok_chicken_3_fly_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_8 = ITEMS.register("wok_chicken_3_fly_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_9 = ITEMS.register("wok_chicken_3_base_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_10 = ITEMS.register("wok_chicken_3_base_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_11 = ITEMS.register("wok_chicken_3_base_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_12 = ITEMS.register("wok_chicken_3_base_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_13 = ITEMS.register("wok_chicken_3_base_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_14 = ITEMS.register("wok_chicken_3_base_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_15 = ITEMS.register("wok_chicken_3_base_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_16 = ITEMS.register("wok_chicken_3_base_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_17 = ITEMS.register("wok_chicken_3_base_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_18 = ITEMS.register("wok_chicken_3_base_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_19 = ITEMS.register("wok_chicken_3_base_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_20 = ITEMS.register("wok_chicken_3_base_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_21 = ITEMS.register("wok_chicken_3_base_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_22 = ITEMS.register("wok_chicken_3_base_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_23 = ITEMS.register("wok_chicken_3_base_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_24 = ITEMS.register("wok_chicken_3_base_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_25 = ITEMS.register("wok_chicken_3_base_25", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_26 = ITEMS.register("wok_chicken_3_base_26", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_27 = ITEMS.register("wok_chicken_3_base_27", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_28 = ITEMS.register("wok_chicken_3_base_28", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_29 = ITEMS.register("wok_chicken_3_base_29", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_30 = ITEMS.register("wok_chicken_3_base_30", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_9 = ITEMS.register("wok_chicken_3_fly_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_10 = ITEMS.register("wok_chicken_3_fly_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_11 = ITEMS.register("wok_chicken_3_fly_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_12 = ITEMS.register("wok_chicken_3_fly_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_13 = ITEMS.register("wok_chicken_3_fly_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_14 = ITEMS.register("wok_chicken_3_fly_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_15 = ITEMS.register("wok_chicken_3_fly_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_16 = ITEMS.register("wok_chicken_3_fly_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_17 = ITEMS.register("wok_chicken_3_fly_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_18 = ITEMS.register("wok_chicken_3_fly_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_19 = ITEMS.register("wok_chicken_3_fly_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_20 = ITEMS.register("wok_chicken_3_fly_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_21 = ITEMS.register("wok_chicken_3_fly_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_22 = ITEMS.register("wok_chicken_3_fly_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_23 = ITEMS.register("wok_chicken_3_fly_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_24 = ITEMS.register("wok_chicken_3_fly_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_25 = ITEMS.register("wok_chicken_3_fly_25", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_26 = ITEMS.register("wok_chicken_3_fly_26", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_27 = ITEMS.register("wok_chicken_3_fly_27", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_28 = ITEMS.register("wok_chicken_3_fly_28", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_29 = ITEMS.register("wok_chicken_3_fly_29", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_30 = ITEMS.register("wok_chicken_3_fly_30", () -> new Item(new Item.Properties()));
    // 阶段2 (19-24次翻炒)
    public static final RegistryObject<Item> WOK_CHICKEN_3_IDLE_STAGE2 = ITEMS.register("wok_chicken_3_idle_stage2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_1 = ITEMS.register("wok_chicken_3_base_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_2 = ITEMS.register("wok_chicken_3_base_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_3 = ITEMS.register("wok_chicken_3_base_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_4 = ITEMS.register("wok_chicken_3_base_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_5 = ITEMS.register("wok_chicken_3_base_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_6 = ITEMS.register("wok_chicken_3_base_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_7 = ITEMS.register("wok_chicken_3_base_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_8 = ITEMS.register("wok_chicken_3_base_stage2_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_9 = ITEMS.register("wok_chicken_3_base_stage2_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_10 = ITEMS.register("wok_chicken_3_base_stage2_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_11 = ITEMS.register("wok_chicken_3_base_stage2_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_12 = ITEMS.register("wok_chicken_3_base_stage2_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_13 = ITEMS.register("wok_chicken_3_base_stage2_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_14 = ITEMS.register("wok_chicken_3_base_stage2_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_15 = ITEMS.register("wok_chicken_3_base_stage2_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_16 = ITEMS.register("wok_chicken_3_base_stage2_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_17 = ITEMS.register("wok_chicken_3_base_stage2_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_18 = ITEMS.register("wok_chicken_3_base_stage2_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_19 = ITEMS.register("wok_chicken_3_base_stage2_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_20 = ITEMS.register("wok_chicken_3_base_stage2_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_21 = ITEMS.register("wok_chicken_3_base_stage2_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_22 = ITEMS.register("wok_chicken_3_base_stage2_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_23 = ITEMS.register("wok_chicken_3_base_stage2_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_24 = ITEMS.register("wok_chicken_3_base_stage2_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_25 = ITEMS.register("wok_chicken_3_base_stage2_25", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_26 = ITEMS.register("wok_chicken_3_base_stage2_26", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_27 = ITEMS.register("wok_chicken_3_base_stage2_27", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_28 = ITEMS.register("wok_chicken_3_base_stage2_28", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_29 = ITEMS.register("wok_chicken_3_base_stage2_29", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE2_30 = ITEMS.register("wok_chicken_3_base_stage2_30", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_1 = ITEMS.register("wok_chicken_3_fly_stage2_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_2 = ITEMS.register("wok_chicken_3_fly_stage2_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_3 = ITEMS.register("wok_chicken_3_fly_stage2_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_4 = ITEMS.register("wok_chicken_3_fly_stage2_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_5 = ITEMS.register("wok_chicken_3_fly_stage2_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_6 = ITEMS.register("wok_chicken_3_fly_stage2_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_7 = ITEMS.register("wok_chicken_3_fly_stage2_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_8 = ITEMS.register("wok_chicken_3_fly_stage2_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_9 = ITEMS.register("wok_chicken_3_fly_stage2_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_10 = ITEMS.register("wok_chicken_3_fly_stage2_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_11 = ITEMS.register("wok_chicken_3_fly_stage2_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_12 = ITEMS.register("wok_chicken_3_fly_stage2_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_13 = ITEMS.register("wok_chicken_3_fly_stage2_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_14 = ITEMS.register("wok_chicken_3_fly_stage2_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_15 = ITEMS.register("wok_chicken_3_fly_stage2_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_16 = ITEMS.register("wok_chicken_3_fly_stage2_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_17 = ITEMS.register("wok_chicken_3_fly_stage2_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_18 = ITEMS.register("wok_chicken_3_fly_stage2_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_19 = ITEMS.register("wok_chicken_3_fly_stage2_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_20 = ITEMS.register("wok_chicken_3_fly_stage2_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_21 = ITEMS.register("wok_chicken_3_fly_stage2_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_22 = ITEMS.register("wok_chicken_3_fly_stage2_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_23 = ITEMS.register("wok_chicken_3_fly_stage2_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_24 = ITEMS.register("wok_chicken_3_fly_stage2_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_25 = ITEMS.register("wok_chicken_3_fly_stage2_25", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_26 = ITEMS.register("wok_chicken_3_fly_stage2_26", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_27 = ITEMS.register("wok_chicken_3_fly_stage2_27", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_28 = ITEMS.register("wok_chicken_3_fly_stage2_28", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_29 = ITEMS.register("wok_chicken_3_fly_stage2_29", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE2_30 = ITEMS.register("wok_chicken_3_fly_stage2_30", () -> new Item(new Item.Properties()));
    // 阶段3 (25-30次翻炒)
    public static final RegistryObject<Item> WOK_CHICKEN_3_IDLE_STAGE3 = ITEMS.register("wok_chicken_3_idle_stage3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_1 = ITEMS.register("wok_chicken_3_base_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_2 = ITEMS.register("wok_chicken_3_base_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_3 = ITEMS.register("wok_chicken_3_base_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_4 = ITEMS.register("wok_chicken_3_base_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_5 = ITEMS.register("wok_chicken_3_base_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_6 = ITEMS.register("wok_chicken_3_base_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_7 = ITEMS.register("wok_chicken_3_base_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_8 = ITEMS.register("wok_chicken_3_base_stage3_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_9 = ITEMS.register("wok_chicken_3_base_stage3_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_10 = ITEMS.register("wok_chicken_3_base_stage3_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_11 = ITEMS.register("wok_chicken_3_base_stage3_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_12 = ITEMS.register("wok_chicken_3_base_stage3_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_13 = ITEMS.register("wok_chicken_3_base_stage3_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_14 = ITEMS.register("wok_chicken_3_base_stage3_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_15 = ITEMS.register("wok_chicken_3_base_stage3_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_16 = ITEMS.register("wok_chicken_3_base_stage3_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_17 = ITEMS.register("wok_chicken_3_base_stage3_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_18 = ITEMS.register("wok_chicken_3_base_stage3_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_19 = ITEMS.register("wok_chicken_3_base_stage3_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_20 = ITEMS.register("wok_chicken_3_base_stage3_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_21 = ITEMS.register("wok_chicken_3_base_stage3_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_22 = ITEMS.register("wok_chicken_3_base_stage3_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_23 = ITEMS.register("wok_chicken_3_base_stage3_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_24 = ITEMS.register("wok_chicken_3_base_stage3_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_25 = ITEMS.register("wok_chicken_3_base_stage3_25", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_26 = ITEMS.register("wok_chicken_3_base_stage3_26", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_27 = ITEMS.register("wok_chicken_3_base_stage3_27", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_28 = ITEMS.register("wok_chicken_3_base_stage3_28", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_29 = ITEMS.register("wok_chicken_3_base_stage3_29", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_BASE_STAGE3_30 = ITEMS.register("wok_chicken_3_base_stage3_30", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_1 = ITEMS.register("wok_chicken_3_fly_stage3_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_2 = ITEMS.register("wok_chicken_3_fly_stage3_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_3 = ITEMS.register("wok_chicken_3_fly_stage3_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_4 = ITEMS.register("wok_chicken_3_fly_stage3_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_5 = ITEMS.register("wok_chicken_3_fly_stage3_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_6 = ITEMS.register("wok_chicken_3_fly_stage3_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_7 = ITEMS.register("wok_chicken_3_fly_stage3_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_8 = ITEMS.register("wok_chicken_3_fly_stage3_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_9 = ITEMS.register("wok_chicken_3_fly_stage3_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_10 = ITEMS.register("wok_chicken_3_fly_stage3_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_11 = ITEMS.register("wok_chicken_3_fly_stage3_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_12 = ITEMS.register("wok_chicken_3_fly_stage3_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_13 = ITEMS.register("wok_chicken_3_fly_stage3_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_14 = ITEMS.register("wok_chicken_3_fly_stage3_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_15 = ITEMS.register("wok_chicken_3_fly_stage3_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_16 = ITEMS.register("wok_chicken_3_fly_stage3_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_17 = ITEMS.register("wok_chicken_3_fly_stage3_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_18 = ITEMS.register("wok_chicken_3_fly_stage3_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_19 = ITEMS.register("wok_chicken_3_fly_stage3_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_20 = ITEMS.register("wok_chicken_3_fly_stage3_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_21 = ITEMS.register("wok_chicken_3_fly_stage3_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_22 = ITEMS.register("wok_chicken_3_fly_stage3_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_23 = ITEMS.register("wok_chicken_3_fly_stage3_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_24 = ITEMS.register("wok_chicken_3_fly_stage3_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_25 = ITEMS.register("wok_chicken_3_fly_stage3_25", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_26 = ITEMS.register("wok_chicken_3_fly_stage3_26", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_27 = ITEMS.register("wok_chicken_3_fly_stage3_27", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_28 = ITEMS.register("wok_chicken_3_fly_stage3_28", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_29 = ITEMS.register("wok_chicken_3_fly_stage3_29", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOK_CHICKEN_3_FLY_STAGE3_30 = ITEMS.register("wok_chicken_3_fly_stage3_30", () -> new Item(new Item.Properties()));

    // ================= 猪肉切割的 26 个物理阶段 =================
    public static final RegistryObject<Item> PORK_STAGE_0 = ITEMS.register("pork_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_1 = ITEMS.register("pork_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_2 = ITEMS.register("pork_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_3 = ITEMS.register("pork_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_4 = ITEMS.register("pork_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_5 = ITEMS.register("pork_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_6 = ITEMS.register("pork_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_7 = ITEMS.register("pork_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_8 = ITEMS.register("pork_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_9 = ITEMS.register("pork_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_10 = ITEMS.register("pork_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_11 = ITEMS.register("pork_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_12 = ITEMS.register("pork_stage_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_13 = ITEMS.register("pork_stage_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_14 = ITEMS.register("pork_stage_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_15 = ITEMS.register("pork_stage_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_16 = ITEMS.register("pork_stage_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_17 = ITEMS.register("pork_stage_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_18 = ITEMS.register("pork_stage_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_19 = ITEMS.register("pork_stage_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_20 = ITEMS.register("pork_stage_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_21 = ITEMS.register("pork_stage_21", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_22 = ITEMS.register("pork_stage_22", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_23 = ITEMS.register("pork_stage_23", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_24 = ITEMS.register("pork_stage_24", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PORK_STAGE_25 = ITEMS.register("pork_stage_25", () -> new Item(new Item.Properties()));

    // ================= 鸡肉切割的 21 个阶段 (0-20) =================
    public static final RegistryObject<Item> CHICKEN_STAGE_0 = ITEMS.register("chicken_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_1 = ITEMS.register("chicken_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_2 = ITEMS.register("chicken_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_3 = ITEMS.register("chicken_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_4 = ITEMS.register("chicken_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_5 = ITEMS.register("chicken_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_6 = ITEMS.register("chicken_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_7 = ITEMS.register("chicken_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_8 = ITEMS.register("chicken_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_9 = ITEMS.register("chicken_stage_9", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_10 = ITEMS.register("chicken_stage_10", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_11 = ITEMS.register("chicken_stage_11", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_12 = ITEMS.register("chicken_stage_12", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_13 = ITEMS.register("chicken_stage_13", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_14 = ITEMS.register("chicken_stage_14", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_15 = ITEMS.register("chicken_stage_15", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_16 = ITEMS.register("chicken_stage_16", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_17 = ITEMS.register("chicken_stage_17", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_18 = ITEMS.register("chicken_stage_18", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_19 = ITEMS.register("chicken_stage_19", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHICKEN_STAGE_20 = ITEMS.register("chicken_stage_20", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOPPED_CHICKEN = ITEMS.register("chopped_chicken", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationMod(0.5f)
                    .meat()
                    .build())
    ));

    public static final RegistryObject<Item> RED_PEPPER_STAGE_0 = ITEMS.register("red_pepper_stage_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_1 = ITEMS.register("red_pepper_stage_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_2 = ITEMS.register("red_pepper_stage_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_3 = ITEMS.register("red_pepper_stage_3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_4 = ITEMS.register("red_pepper_stage_4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_5 = ITEMS.register("red_pepper_stage_5", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_6 = ITEMS.register("red_pepper_stage_6", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_7 = ITEMS.register("red_pepper_stage_7", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_PEPPER_STAGE_8 = ITEMS.register("red_pepper_stage_8", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHOPPED_RED_PEPPER = ITEMS.register("chopped_red_pepper", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TOMATO_SALAD = ITEMS.register("tomato_salad", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(4)
                    .saturationMod(0.5f)
                    .build())
    ));

    public static final RegistryObject<Item> ROAST_CHICKEN = ITEMS.register("roast_chicken", () -> new BlockItem(
            ModBlocks.ROAST_CHICKEN.get(),
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(6)
                    .saturationMod(0.8f)
                    .meat()
                    .build())
    ));

    public static final RegistryObject<Item> PLATE = ITEMS.register("plate", () -> new BlockItem(
            ModBlocks.PLATE.get(),
            new Item.Properties()
    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
