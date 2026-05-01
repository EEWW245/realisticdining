package com.realisticdining.fabric.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CookbookMenuData {
    
    public static final StreamCodec<RegistryFriendlyByteBuf, CookbookMenuData> PACKET_CODEC = StreamCodec.of(
        (buf, data) -> {
            // 不需要写入任何数据
        },
        buf -> {
            // 不需要读取任何数据
            return new CookbookMenuData();
        }
    );
    
    public CookbookMenuData() {
        // 空的构造函数，因为我们不需要传输额外数据
    }
}
