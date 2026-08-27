package com.realisticdining.fabric.client.pack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.realisticdining.RealisticDining;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 材质包扩展物品定义加载器（Fabric 1.21.1）。
 *
 * <p>扫描 {@code assets/realisticdining/definitions/*.json} 加载所有扩展物品定义。
 * 加载时同时构建以下索引：
 * <ul>
 *   <li>{@code itemIdList}：物品 ID 字符串集合（供 mixin 快速判断是否拦截）</li>
 *   <li>{@code itemList}：Item 实例集合（供按键路由快速判断主手是否匹配）</li>
 *   <li>{@code nameByItemId}：物品 ID → 资源派生名（用于定位 model/texture/animation）</li>
 *   <li>{@code soundMappings}：物品 ID → (keyframe → soundId) 映射</li>
 *   <li>{@code invisibleMappings}：物品 ID → 隐藏骨骼集合</li>
 *   <li>{@code modeMappings}：物品 ID → 持物模式（static 或 pickup），mixin/hand-change-tracker 用</li>
 * </ul>
 */
public class PackDefinitionManager extends SimpleJsonResourceReloadListener
        implements IdentifiableResourceReloadListener {

    private static final Gson GSON = new Gson();
    public static final List<PackDefinition> DEFINITIONS = new ArrayList<>();
    /** 物品 ID 字符串集合，mixin 用 */
    public static final List<String> itemIdList = new ArrayList<>();
    /** Item 实例集合，按键路由用 */
    public static final List<Item> itemList = new ArrayList<>();
    /** 物品 ID → 资源派生名（由定义文件名决定） */
    public static final Map<String, String> nameByItemId = new HashMap<>();
    /** 物品 ID → (音效关键帧 → 音效 ID) */
    private static final Map<String, Map<String, String>> soundMappings = new HashMap<>();
    /** 物品 ID → 隐藏骨骼名集合（小写） */
    private static final Map<String, Set<String>> invisibleMappings = new HashMap<>();
    /** 物品 ID → 持物模式（static 或 pickup），mixin/hand-change-tracker 用 */
    private static final Map<String, PackMode> modeMappings = new HashMap<>();

    public PackDefinitionManager() {
        super(GSON, "definitions");
    }

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "pack_definitions");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManager, ProfilerFiller profiler) {
        DEFINITIONS.clear();
        itemIdList.clear();
        itemList.clear();
        nameByItemId.clear();
        soundMappings.clear();
        invisibleMappings.clear();
        modeMappings.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            // 只加载 realisticdining 命名空间的 definitions，过滤掉其他模组（如 Kaleidoscope ImmersiveEating 的 food 命名空间），
            // 否则会误加载其他模组的定义，导致 PackCustomRenderer 用 realisticdining 命名空间去加载不存在的模型而崩溃。
            if (!RealisticDining.MOD_ID.equals(entry.getKey().getNamespace())) {
                continue;
            }
            PackDefinition definition = GSON.fromJson(entry.getValue(), PackDefinition.class);
            DEFINITIONS.add(definition);

            // 定义文件 ResourceLocation 形如 realisticdining:definitions/my_snack
            // path 部分 "definitions/my_snack" 去前缀 "definitions/" 得到 "my_snack" 作为派生名
            String fileKey = entry.getKey().getPath();
            String name = fileKey.startsWith("definitions/")
                    ? fileKey.substring("definitions/".length())
                    : fileKey;

            String itemId = definition.getItem();
            if (itemId == null || itemId.isBlank()) {
                continue;
            }
            nameByItemId.put(itemId, name);
            itemIdList.add(itemId);

            // 1.21.1 用 BuiltInRegistries.ITEM 查询物品，ResourceLocation 用 parse 解析字符串
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                itemList.add(item);
            }

            if (definition.getSounds() != null) {
                soundMappings.computeIfAbsent(itemId, k -> new HashMap<>()).putAll(definition.getSounds());
            }
            if (definition.getInvisible() != null && !definition.getInvisible().isEmpty()) {
                Set<String> invisibleBones = invisibleMappings.computeIfAbsent(itemId, k -> new HashSet<>());
                for (String boneName : definition.getInvisible()) {
                    if (boneName != null && !boneName.isBlank()) {
                        invisibleBones.add(boneName.toLowerCase());
                    }
                }
            }
            modeMappings.put(itemId, definition.getMode());
        }

        RealisticDining.LOGGER.info("[材质包扩展] 已加载 {} 个扩展物品定义", DEFINITIONS.size());
    }

    public static String getDerivedName(String itemId) {
        return nameByItemId.get(itemId);
    }

    public static boolean containsItem(String itemId) {
        return nameByItemId.containsKey(itemId);
    }

    public static boolean containsItem(Item item) {
        return item != null && itemList.contains(item);
    }

    public static ResourceLocation getSound(String itemId, String keyframeName) {
        Map<String, String> itemSounds = soundMappings.get(itemId);
        if (itemSounds == null) return null;
        String soundId = itemSounds.get(keyframeName);
        return soundId == null || soundId.isBlank() ? null : ResourceLocation.parse(soundId);
    }

    public static boolean isInvisible(String itemId, String boneName) {
        Set<String> invisibleBones = invisibleMappings.get(itemId);
        return invisibleBones != null && boneName != null && invisibleBones.contains(boneName.toLowerCase());
    }

    /**
     * 查询物品的持物模式。未注册时返回 {@link PackMode#STATIC}（默认静态模式）。
     *
     * @param itemId 物品 ID 字符串（如 "minecraft:apple"）
     * @return 持物模式，未注册时返回 STATIC
     */
    public static PackMode getMode(String itemId) {
        PackMode mode = modeMappings.get(itemId);
        return mode == null ? PackMode.STATIC : mode;
    }
}
