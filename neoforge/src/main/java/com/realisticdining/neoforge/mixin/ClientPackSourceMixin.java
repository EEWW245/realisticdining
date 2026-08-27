package com.realisticdining.neoforge.mixin;

import com.realisticdining.neoforge.client.pack.sound.PackDynamicPack;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 客户端资源包列表注入（NeoForge 1.21.1）。
 *
 * <p>在 {@link ClientPackSource#populatePackList} 末尾追加
 * {@link PackDynamicPack} 作为内置资源包，让模组运行时能为材质包扩展
 * 自动生成 sounds.json 事件定义，使任意独立音效可被播放。
 */
@Mixin(ClientPackSource.class)
public class ClientPackSourceMixin {

    @Inject(method = "populatePackList", at = @At("TAIL"))
    private void realisticdining$addDynamicPack(BiConsumer<String, Function<String, Pack>> consumer, CallbackInfo ci) {
        consumer.accept("realisticdining_dynamic", id -> Pack.readMetaAndCreate(
                new PackLocationInfo(id,
                        Component.literal("RealisticDining Dynamic Sounds"),
                        PackSource.BUILT_IN,
                        Optional.empty()),
                new Pack.ResourcesSupplier() {
                    @Override
                    public PackResources openPrimary(PackLocationInfo locationInfo) {
                        return new PackDynamicPack();
                    }

                    @Override
                    public PackResources openFull(PackLocationInfo locationInfo, Pack.Metadata metadata) {
                        return new PackDynamicPack();
                    }
                },
                PackType.CLIENT_RESOURCES,
                new PackSelectionConfig(true, Pack.Position.TOP, false)
        ));
    }
}
