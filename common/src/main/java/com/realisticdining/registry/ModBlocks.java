package com.realisticdining.registry;

import com.realisticdining.RealisticDining;
import com.realisticdining.blocks.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(RealisticDining.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> CHOPPING_BOARD = BLOCKS.register("chopping_board",
            () -> new ChoppingBoardBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 3.0f)
                    .noOcclusion()));

    public static final RegistrySupplier<Block> WOK_BLOCK = BLOCKS.register("wok_block",
            () -> new WokBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f, 4.0f)
                    .noOcclusion()));

    public static final RegistrySupplier<Block> GREEN_ONION_CROP = BLOCKS.register("green_onion_crop",
            () -> new GreenOnionCropBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final RegistrySupplier<Block> ROAST_CHICKEN = BLOCKS.register("roast_chicken",
            () -> new RoastChickenBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> PLATE = BLOCKS.register("plate",
            () -> new PlateBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> STIR_FRIED_PORK_CABBAGE_PLATE = BLOCKS.register("stir_fried_pork_cabbage_plate",
            () -> new StirFriedPorkCabbagePlateBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> PEPPERY_CHICKEN_PLATE = BLOCKS.register("peppery_chicken_plate",
            () -> new PepperyChickenPlateBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> RICE_BOWL = BLOCKS.register("rice_bowl",
            () -> new RiceBowlBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> PORK_PIECES = BLOCKS.register("pork_pieces",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> WOK_YELLOW_STEAK = BLOCKS.register("wok_yellow_steak",
            () -> new WokYellowSteakBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> WOK_FRIED_EGG = BLOCKS.register("wok_fried_egg",
            () -> new WokFriedEggBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> STIR_FRIED_YELLOW_BEEF_PLATE = BLOCKS.register("stir_fried_yellow_beef_plate",
            () -> new StirFriedYellowBeefPlateBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> TOMATO_POACHED_EGG_BLOCK = BLOCKS.register("tomato_poached_egg_block",
            () -> new TomatoPoachedEggBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> FRIED_RICE_EGG_BLOCK = BLOCKS.register("fried_rice_egg_block",
            () -> new FriedRiceEggBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .noOcclusion()
                    .noCollission()));

    public static final RegistrySupplier<Block> CORIANDER_CROP = BLOCKS.register("coriander_crop",
            () -> new CorianderCropBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)));

    public static void init() {
        BLOCKS.register();
    }
}
