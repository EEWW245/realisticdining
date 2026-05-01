package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.blockentities.ChoppingBoardBlockEntity;
import com.example.realisticdining.blockentities.RoastChickenBlockEntity;
import com.example.realisticdining.blockentities.WokBlockEntity;
import com.example.realisticdining.blockentities.WokFriedEggBlockEntity;
import com.example.realisticdining.blockentities.WokYellowSteakBlockEntity;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;

public class ModBlockEntities {
    private static final PlatformRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ServiceHelper.getPlatformServices().createBlockEntityRegistry(RealisticDining.MOD_ID);

    public static final Supplier<BlockEntityType<WokBlockEntity>> WOK = register("wok",
        () -> BlockEntityType.Builder.of(WokBlockEntity::new, ModBlocks.WOK_BLOCK.get()).build(null)
    );

    public static final Supplier<BlockEntityType<WokYellowSteakBlockEntity>> WOK_YELLOW_STEAK = register("wok_yellow_steak",
        () -> BlockEntityType.Builder.of(WokYellowSteakBlockEntity::new, ModBlocks.WOK_YELLOW_STEAK.get()).build(null)
    );

    public static final Supplier<BlockEntityType<WokFriedEggBlockEntity>> WOK_FRIED_EGG = register("wok_fried_egg",
        () -> BlockEntityType.Builder.of(WokFriedEggBlockEntity::new, ModBlocks.WOK_FRIED_EGG.get()).build(null)
    );

    public static final Supplier<BlockEntityType<ChoppingBoardBlockEntity>> CHOPPING_BOARD = register("chopping_board",
        () -> BlockEntityType.Builder.of(ChoppingBoardBlockEntity::new, ModBlocks.CHOPPING_BOARD.get()).build(null)
    );

    public static final Supplier<BlockEntityType<RoastChickenBlockEntity>> ROAST_CHICKEN = register("roast_chicken",
        () -> BlockEntityType.Builder.of(RoastChickenBlockEntity::new, ModBlocks.ROAST_CHICKEN.get()).build(null)
    );

    private static <T extends BlockEntityType<?>> Supplier<T> register(String name, Supplier<T> blockEntity) {
        return BLOCK_ENTITIES.register(new ResourceLocation(RealisticDining.MOD_ID, name), blockEntity);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
