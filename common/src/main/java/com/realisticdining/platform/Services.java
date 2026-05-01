package com.realisticdining.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class Services {
    
    @ExpectPlatform
    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BlockEntityType<?>> Supplier<T> registerBlockEntityType(String name, Supplier<T> blockEntityType) {
        throw new AssertionError();
    }
}
