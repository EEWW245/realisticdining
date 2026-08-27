package com.example.realisticdining.fabric.client.pack.sound;

import com.google.gson.JsonObject;
import com.example.realisticdining.RealisticDining;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * 材质包扩展动态资源包（Fabric 1.20.1）。
 *
 * <p>虚拟 {@link PackResources} 实现：注册到客户端资源包列表后，当游戏请求
 * {@code realisticdining:sounds.json} 时不读磁盘，而是调用
 * {@link PackSoundJson#generate} 动态生成 JSON 内容返回。
 *
 * <p>生成内容会聚合所有材质包扩展 definition 里的 sounds 字段，让材质包
 * 扩展者能自带任意独立音效，无需事先在 ModSounds 注册表里注册 SoundEvent。
 */
public class PackDynamicPack implements PackResources {

    private static final PackMetadataSection METADATA =
            new PackMetadataSection(Component.literal("Provides dynamic sounds for pack extensions."), 15);

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type != PackType.CLIENT_RESOURCES) {
            return null;
        }
        if (!location.getNamespace().equals(RealisticDining.MOD_ID)) {
            return null;
        }
        if (!location.getPath().equals("sounds.json")) {
            return null;
        }
        String content = createSoundsJson().toString();
        return () -> new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        if (type == PackType.CLIENT_RESOURCES) {
            return Set.of(RealisticDining.MOD_ID);
        }
        return Set.of();
    }

    @Override
    public void listResources(PackType type, String namespace, String prefix, ResourceOutput output) {
        if (type != PackType.CLIENT_RESOURCES) {
            return;
        }
        if (!namespace.equals(RealisticDining.MOD_ID)) {
            return;
        }
        if ("sounds.json".startsWith(prefix)) {
            output.accept(new ResourceLocation(RealisticDining.MOD_ID, "sounds.json"),
                    () -> {
                        JsonObject json = createSoundsJson();
                        return new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
                    }
            );
        }
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... path) {
        return null;
    }

    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        if (serializer == PackMetadataSection.TYPE) {
            return (T) METADATA;
        }
        return null;
    }

    @Override
    public String packId() {
        return "realisticdining_dynamic";
    }

    private static JsonObject createSoundsJson() {
        return PackSoundJson.generate(Minecraft.getInstance().getResourceManager());
    }

    @Override
    public void close() {
    }
}
