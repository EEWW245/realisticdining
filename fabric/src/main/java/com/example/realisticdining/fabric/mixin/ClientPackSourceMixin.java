package com.example.realisticdining.fabric.mixin;

import com.example.realisticdining.fabric.client.pack.sound.PackDynamicPack;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 客户端资源包列表注入（Fabric 1.20.1）。
 *
 * <p>在 {@link ClientPackSource#populatePackList} 末尾追加
 * {@link PackDynamicPack} 作为内置资源包，让模组运行时能为材质包扩展
 * 自动生成 sounds.json 事件定义，使任意独立音效可被播放。
 */
@Mixin(ClientPackSource.class)
public class ClientPackSourceMixin {

    @Inject(method = "populatePackList", at = @At("TAIL"))
    private void realisticdining$addDynamicPack(BiConsumer<String, Function<String, Pack>> consumer, CallbackInfo ci) {
        consumer.accept("realisticdining_dynamic", id -> Pack.readMetaAndCreate(id,
                Component.literal("RealisticDining Dynamic Sounds"),
                true,
                packId -> new PackDynamicPack(),
                PackType.CLIENT_RESOURCES,
                Pack.Position.TOP,
                PackSource.BUILT_IN
        ));
    }
}
