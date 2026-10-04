package com.example.realisticdining.fabric.loot;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.common.DrinkConsumeConfig;
import com.example.realisticdining.common.FoodConfigManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Fabric 1.20.1 食物配置数据包 ReloadListener。
 *
 * <p>扫描 {@code data/<任何命名空间>/foods/drinks/<drinkId>.json}，
 * 解析 JSON 后覆盖代码默认值。数据包缺失或 JSON 解析失败时，模组行为完全不变。
 *
 * <p>仅支持覆盖饮料/零食（包括薯片、罐头、辣条、尖叫饮料等），不支持菜品（米饭、筷子等）。
 */
public class FoodConfigReloadListener extends SimpleJsonResourceReloadListener
        implements IdentifiableResourceReloadListener {

    private static final Gson GSON = new Gson();

    public FoodConfigReloadListener() {
        super(GSON, "foods");
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(RealisticDining.MOD_ID, "food_config");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        // 清空 override，重置为代码默认值
        FoodConfigManager.clearOverrides();
        DrinkConsumeConfig.resetToDefaults();

        int drinkCount = 0;
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation id = entry.getKey();
            String path = id.getPath();
            JsonElement json = entry.getValue();
            try {
                if (path.startsWith("drinks/")) {
                    String drinkId = path.substring("drinks/".length());
                    FoodConfigManager.DrinkConfig cfg = parseDrink(json);
                    if (cfg != null) {
                        FoodConfigManager.putDrinkOverride(drinkId, cfg);
                        DrinkConsumeConfig.applyOverride(drinkId, cfg);
                        drinkCount++;
                    }
                }
            } catch (Exception e) {
                RealisticDining.LOGGER.error("[Realistic Dining] Failed to parse food config {}: {}", id, e.getMessage());
            }
        }
        RealisticDining.LOGGER.info("[Realistic Dining] Loaded food overrides: {} drinks", drinkCount);
    }

    private FoodConfigManager.DrinkConfig parseDrink(JsonElement json) {
        if (!json.isJsonObject()) return null;
        JsonObject obj = json.getAsJsonObject();
        int maxUses = obj.has("max_uses") ? obj.get("max_uses").getAsInt() : 1;
        int nutrition = obj.has("nutrition") ? obj.get("nutrition").getAsInt() : 0;
        float saturation = obj.has("saturation") ? obj.get("saturation").getAsFloat() : 0.0f;
        List<FoodConfigManager.EffectSpec> effects = parseEffects(obj);
        List<Double> hungerCues = new ArrayList<>();
        if (obj.has("hunger_cues") && obj.get("hunger_cues").isJsonArray()) {
            for (JsonElement e : obj.getAsJsonArray("hunger_cues")) {
                hungerCues.add(e.getAsDouble());
            }
        }
        boolean clearHarmful = obj.has("clear_harmful") && obj.get("clear_harmful").getAsBoolean();
        return new FoodConfigManager.DrinkConfig(maxUses, nutrition, saturation, effects, hungerCues, clearHarmful);
    }

    private List<FoodConfigManager.EffectSpec> parseEffects(JsonObject obj) {
        List<FoodConfigManager.EffectSpec> list = new ArrayList<>();
        if (!obj.has("effects") || !obj.get("effects").isJsonArray()) return list;
        for (JsonElement e : obj.getAsJsonArray("effects")) {
            if (!e.isJsonObject()) continue;
            JsonObject eff = e.getAsJsonObject();
            if (!eff.has("effect") || !eff.get("effect").isJsonPrimitive()) continue;
            JsonPrimitive prim = eff.get("effect").getAsJsonPrimitive();
            String effectStr = prim.getAsString();
            ResourceLocation effectId = new ResourceLocation(effectStr);
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(effectId);
            if (effect == null) {
                RealisticDining.LOGGER.warn("[Realistic Dining] Unknown effect: {}", effectStr);
                continue;
            }
            int duration = eff.has("duration") ? eff.get("duration").getAsInt() : 0;
            int amplifier = eff.has("amplifier") ? eff.get("amplifier").getAsInt() : 0;
            list.add(new FoodConfigManager.EffectSpec(effect, duration, amplifier));
        }
        return list;
    }
}
