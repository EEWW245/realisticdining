package com.realisticdining.registry;

import com.realisticdining.RealisticDining;
import com.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.realisticdining.blockentities.FriedRiceEggBlockEntity;
import com.realisticdining.blockentities.TomatoPoachedEggBlockEntity;
import com.realisticdining.blockentities.WokBlockEntity;
import com.realisticdining.blockentities.WokFriedEggBlockEntity;
import com.realisticdining.blockentities.WokYellowSteakBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(RealisticDining.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<ChoppingBoardBlockEntity>> CHOPPING_BOARD = BLOCK_ENTITIES.register("chopping_board",
            () -> BlockEntityType.Builder.of(ChoppingBoardBlockEntity::new, ModBlocks.CHOPPING_BOARD.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<WokBlockEntity>> WOK = BLOCK_ENTITIES.register("wok",
            () -> BlockEntityType.Builder.of(WokBlockEntity::new, ModBlocks.WOK_BLOCK.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<WokYellowSteakBlockEntity>> WOK_YELLOW_STEAK = BLOCK_ENTITIES.register("wok_yellow_steak",
            () -> BlockEntityType.Builder.of(WokYellowSteakBlockEntity::new, ModBlocks.WOK_YELLOW_STEAK.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<WokFriedEggBlockEntity>> WOK_FRIED_EGG = BLOCK_ENTITIES.register("wok_fried_egg",
            () -> BlockEntityType.Builder.of(WokFriedEggBlockEntity::new, ModBlocks.WOK_FRIED_EGG.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<TomatoPoachedEggBlockEntity>> TOMATO_POACHED_EGG = BLOCK_ENTITIES.register("tomato_poached_egg",
            () -> BlockEntityType.Builder.of(TomatoPoachedEggBlockEntity::new, ModBlocks.TOMATO_POACHED_EGG_BLOCK.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<FriedRiceEggBlockEntity>> FRIED_RICE_EGG = BLOCK_ENTITIES.register("fried_rice_egg",
            () -> BlockEntityType.Builder.of(FriedRiceEggBlockEntity::new, ModBlocks.FRIED_RICE_EGG_BLOCK.get()).build(null));

    public static void init() {
        BLOCK_ENTITIES.register();
    }
}
