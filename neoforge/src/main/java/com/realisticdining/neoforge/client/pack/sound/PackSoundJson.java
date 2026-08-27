package com.realisticdining.neoforge.client.pack.sound;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.realisticdining.RealisticDining;
import com.realisticdining.neoforge.client.pack.PackDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 材质包扩展动态 sounds.json 生成器（NeoForge 1.21.1）。
 *
 * <p>扫描 {@code assets/realisticdining/definitions/*.json} 中所有定义的
 * {@code sounds} 字段，把每个值（如 {@code realisticdining:straw_insert}）的
 * path 部分（{@code straw_insert}）作为 sounds.json 事件名，自动产出：
 * <pre>
 * {
 *   "straw_insert": { "sounds": ["realisticdining:straw_insert"] },
 *   "can_gulp":     { "sounds": ["realisticdining:can_gulp"] }
 * }
 * </pre>
 *
 * <p>这样材质包扩展者只要在 definition 写 {@code sounds: {"xxx": "realisticdining:yyy"}}
 * 并把 {@code yyy.ogg} 放进 {@code assets/realisticdining/sounds/yyy.ogg}，
 * 模组运行时就能自动生成对应的 sounds.json 事件定义，让任意 SoundEvent ID 都能播放，
 * 无需事先在 ModSounds 注册表里注册。
 */
public class PackSoundJson {

    private static final Gson GSON = new Gson();

    public static JsonObject generate(List<PackDefinition> definitions) {
        JsonObject root = new JsonObject();

        for (PackDefinition definition : definitions) {
            if (definition == null || definition.getSounds() == null) {
                continue;
            }

            for (String soundId : definition.getSounds().values()) {
                if (soundId == null || soundId.isBlank()) {
                    continue;
                }
                ResourceLocation rl = ResourceLocation.parse(soundId);
                String name = rl.getPath(); // 例如 realisticdining:straw_insert → straw_insert
                addSound(root, name, soundId);
            }
        }

        return root;
    }

    public static JsonObject generate(ResourceManager resourceManager) {
        List<PackDefinition> definitions = new ArrayList<>();
        Map<ResourceLocation, Resource> resources = resourceManager.listResources("definitions",
                location -> RealisticDining.MOD_ID.equals(location.getNamespace())
                        && location.getPath().endsWith(".json"));

        for (Resource resource : resources.values()) {
            try (Reader reader = resource.openAsReader()) {
                PackDefinition definition = GSON.fromJson(reader, PackDefinition.class);
                if (definition != null) {
                    definitions.add(definition);
                }
            } catch (IOException exception) {
                throw new RuntimeException("Failed to read pack definition", exception);
            }
        }

        return generate(definitions);
    }

    private static void addSound(JsonObject root, String name, String soundId) {
        if (root.has(name)) {
            return;
        }
        JsonObject sound = new JsonObject();
        JsonArray sounds = new JsonArray();
        sounds.add(soundId);
        sound.add("sounds", sounds);
        root.add(name, sound);
    }
}
