package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.blocks.*;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import java.util.function.Supplier;

public class ModBlocks {
    private static final PlatformRegistry<Block> BLOCKS = ServiceHelper.getPlatformServices().createBlockRegistry(RealisticDining.MOD_ID);
    
    public static final Supplier<Block> WOK_BLOCK = register("wok_block", () -> new WokBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(3.0f).noOcclusion().noCollission()
    ));

    public static final Supplier<Block> WOK_YELLOW_STEAK = register("wok_yellow_steak", () -> new WokYellowSteakBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(3.0f).noOcclusion().noCollission()
    ));

    public static final Supplier<Block> WOK_FRIED_EGG = register("wok_fried_egg", () -> new WokFriedEggBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(3.0f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> PORK_PIECES = register("pork_pieces", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).sound(SoundType.SLIME_BLOCK).strength(0.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> CHOPPING_BOARD = register("chopping_board", () -> new ChoppingBoardBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> PORK_STAGE_0 = register("pork_stage_0", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_1 = register("pork_stage_1", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_2 = register("pork_stage_2", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_3 = register("pork_stage_3", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_4 = register("pork_stage_4", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_5 = register("pork_stage_5", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_6 = register("pork_stage_6", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final Supplier<Block> PORK_STAGE_7 = register("pork_stage_7", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    
    public static final Supplier<Block> ROAST_CHICKEN = register("roast_chicken", () -> new RoastChickenBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.WOOL).strength(0.5f).noOcclusion()
    ));
    
    public static final Supplier<Block> PLATE = register("plate", () -> new PlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> STIR_FRIED_PORK_CABBAGE_PLATE = register("stir_fried_pork_cabbage_plate", () -> new StirFriedPorkCabbagePlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> PEPPERY_CHICKEN_PLATE = register("peppery_chicken_plate", () -> new PepperyChickenPlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> STIR_FRIED_YELLOW_BEEF_PLATE = register("stir_fried_yellow_beef_plate", () -> new StirFriedYellowBeefPlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> RICE_BOWL = register("rice_bowl", () -> new RiceBowlBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));
    
    public static final Supplier<Block> GREEN_ONION_CROP = register("green_onion_crop", () -> new GreenOnionCropBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.GRASS).noCollission().randomTicks().strength(0.0f).noOcclusion()
    ));

    public static final Supplier<Block> TOMATO_POACHED_EGG_BLOCK = register("tomato_poached_egg_block", () -> new TomatoPoachedEggBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));

    public static final Supplier<Block> FRIED_RICE_EGG_BLOCK = register("fried_rice_egg_block", () -> new FriedRiceEggBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));

    public static final Supplier<Block> CORIANDER_CROP = register("coriander_crop", () -> new CorianderCropBlock(
            BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP)
    ));
    
    private static <T extends Block> Supplier<T> register(String name, Supplier<T> block) {
        return BLOCKS.register(new ResourceLocation(RealisticDining.MOD_ID, name), block);
    }
    
    public static void init() {
        // 注册由平台实现自动处理
    }
}
