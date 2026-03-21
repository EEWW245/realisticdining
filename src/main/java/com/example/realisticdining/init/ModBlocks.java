package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import com.example.realisticdining.blocks.ChoppingBoardBlock;
import com.example.realisticdining.blocks.GreenOnionCropBlock;
import com.example.realisticdining.blocks.PlateBlock;
import com.example.realisticdining.blocks.RiceBowlBlock;
import com.example.realisticdining.blocks.RoastChickenBlock;
import com.example.realisticdining.blocks.StirFriedPorkCabbagePlateBlock;
import com.example.realisticdining.blocks.PepperyChickenPlateBlock;
import com.example.realisticdining.blocks.WokBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, realisticdining.MODID);

    // ================= 烹饪工具 =================
    public static final RegistryObject<Block> WOK_BLOCK = BLOCKS.register("wok_block", () -> new WokBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(3.0f).noOcclusion().noCollission()
    ));

    // ================= 可以放置的食材方块 =================
    public static final RegistryObject<Block> PORK_PIECES = BLOCKS.register("pork_pieces", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).sound(SoundType.SLIME_BLOCK).strength(0.5f).noOcclusion().noCollission()
    ));

    // 菜板方块注册 - 无碰撞箱
    public static final RegistryObject<Block> CHOPPING_BOARD = BLOCKS.register("chopping_board", () -> new ChoppingBoardBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.5f).noOcclusion().noCollission()
    ));

    // 猪肉切割阶段方块（仅用于渲染，不可放置）
    public static final RegistryObject<Block> PORK_STAGE_0 = BLOCKS.register("pork_stage_0", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_1 = BLOCKS.register("pork_stage_1", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_2 = BLOCKS.register("pork_stage_2", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_3 = BLOCKS.register("pork_stage_3", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_4 = BLOCKS.register("pork_stage_4", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_5 = BLOCKS.register("pork_stage_5", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_6 = BLOCKS.register("pork_stage_6", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));
    public static final RegistryObject<Block> PORK_STAGE_7 = BLOCKS.register("pork_stage_7", () -> new Block(
            BlockBehaviour.Properties.of().air().noCollission().noLootTable()
    ));

    public static final RegistryObject<Block> ROAST_CHICKEN = BLOCKS.register("roast_chicken", () -> new RoastChickenBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.WOOL).strength(0.5f).noOcclusion()
    ));

    public static final RegistryObject<Block> PLATE = BLOCKS.register("plate", () -> new PlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));

    public static final RegistryObject<Block> STIR_FRIED_PORK_CABBAGE_PLATE = BLOCKS.register("stir_fried_pork_cabbage_plate", () -> new StirFriedPorkCabbagePlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));

    public static final RegistryObject<Block> PEPPERY_CHICKEN_PLATE = BLOCKS.register("peppery_chicken_plate", () -> new PepperyChickenPlateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));

    public static final RegistryObject<Block> RICE_BOWL = BLOCKS.register("rice_bowl", () -> new RiceBowlBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.STONE).strength(0.5f).noOcclusion().noCollission()
    ));

    // ================= 作物 =================
    public static final RegistryObject<Block> GREEN_ONION_CROP = BLOCKS.register("green_onion_crop", () -> new GreenOnionCropBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.GRASS).noCollission().randomTicks().strength(0.0f).noOcclusion()
    ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
