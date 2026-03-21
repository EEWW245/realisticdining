package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import com.example.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.example.realisticdining.blockentities.RoastChickenBlockEntity;
import com.example.realisticdining.blockentities.WokBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, realisticdining.MODID);
    
    public static final RegistryObject<BlockEntityType<WokBlockEntity>> WOK = BLOCK_ENTITIES.register("wok", 
        () -> BlockEntityType.Builder.of(WokBlockEntity::new, ModBlocks.WOK_BLOCK.get()).build(null)
    );
    
    public static final RegistryObject<BlockEntityType<ChoppingBoardBlockEntity>> CHOPPING_BOARD = BLOCK_ENTITIES.register("chopping_board", 
        () -> BlockEntityType.Builder.of(ChoppingBoardBlockEntity::new, ModBlocks.CHOPPING_BOARD.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<RoastChickenBlockEntity>> ROAST_CHICKEN = BLOCK_ENTITIES.register("roast_chicken",
        () -> BlockEntityType.Builder.of(RoastChickenBlockEntity::new, ModBlocks.ROAST_CHICKEN.get()).build(null)
    );
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
