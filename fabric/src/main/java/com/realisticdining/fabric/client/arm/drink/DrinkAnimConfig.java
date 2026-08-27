package com.realisticdining.fabric.client.arm.drink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/**
 * 饮用动画配置。
 *
 * <p>每种饮料（矿泉水、奶啤、王老吉、苏打水等）注册一份配置，包括：
 * <ul>
 *   <li>id：唯一标识，用于按键 → triggerDrink(id) 调用</li>
 *   <li>duration：动画时长（秒），状态机据此判断何时结束</li>
 *   <li>geoPath / animationPath / rawAnimationName：动画类型资源</li>
 *   <li>texturePath：该饮料的瓶子/罐子贴图</li>
 *   <li>controllerName：GeckoLib AnimationController 名，每个饮料独立</li>
 *   <li>triggerName：triggerableAnim 注册名（drink）</li>
 *   <li>boneTextures：骨骼名 → 贴图路径映射（可选），支持一个模型多张贴图</li>
 * </ul>
 *
 * <p>同一种动画类型（geo + animation）可被多个饮料复用，仅贴图不同。
 * 例如：矿泉水与苏打水共用 BOTTLE 动画类型，奶啤与王老吉共用 CAN 动画类型。
 *
 * <p>双模型模式（v2.0.7+）：传入第二组 geo/animation/texture/controller/trigger 时，
 * Handler 会同时持有两套 model/animatable/renderer，每帧依次渲染两个独立 geo 模型，
 * 类似吃米饭动画的"左手碗+右手筷子"双模型架构。第二组字段全部为 null 时退化为单模型模式。
 */
public record DrinkAnimConfig(
        String modId,
        String id,
        double duration,
        String geoPath,
        String animationPath,
        String rawAnimationName,
        String texturePath,
        String controllerName,
        String triggerName,
        List<DrinkSoundCue> soundCues,
        Map<String, String> boneTextures,
        // === 双模型字段（v2.0.7+，全部为 null 时退化为单模型模式） ===
        String geoPath2,
        String animationPath2,
        String rawAnimationName2,
        String texturePath2,
        String controllerName2,
        String triggerName2,
        Map<String, String> boneTextures2,
        // === v2.1.4+ 持物前缀模式 ===
        // holdDuration > 0 时启用：物品进入主手时自动播放 drink 动画的 0~holdDuration 秒并定格（HOLD 态），
        // U 键触发后从 holdDuration 继续播放到结尾。无需独立的 pickup 动画文件。
        double holdDuration,
        // 持物阶段（PICKUP/HOLD）是否隐藏 Left Arm 骨骼（瓶装/罐装/薯片等需要隐藏左臂的物品传 true）。
        boolean hideLeftArm
) {
    /** 由各 Handler 自己持有 ResourceLocation，避免每次渲染重复构造。 */
    public ResourceLocation textureResource() {
        return ResourceLocation.fromNamespaceAndPath(modId, texturePath);
    }

    /** 第二组模型的贴图 ResourceLocation；双模型模式下不应为 null。 */
    public ResourceLocation textureResource2() {
        return texturePath2 == null ? null : ResourceLocation.fromNamespaceAndPath(modId, texturePath2);
    }

    /** 将 boneTextures 中的路径解析为带 modId 命名空间的 ResourceLocation。空 Map 表示无骨骼级贴图覆盖。 */
    public Map<String, ResourceLocation> boneTextureResources() {
        Map<String, ResourceLocation> result = new HashMap<>();
        if (boneTextures == null) return result;
        for (Map.Entry<String, String> e : boneTextures.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) continue;
            result.put(e.getKey(), ResourceLocation.fromNamespaceAndPath(modId, e.getValue()));
        }
        return result;
    }

    /** 第二组模型的骨骼贴图 ResourceLocation 映射。 */
    public Map<String, ResourceLocation> boneTextureResources2() {
        Map<String, ResourceLocation> result = new HashMap<>();
        if (boneTextures2 == null) return result;
        for (Map.Entry<String, String> e : boneTextures2.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) continue;
            result.put(e.getKey(), ResourceLocation.fromNamespaceAndPath(modId, e.getValue()));
        }
        return result;
    }

    /** 是否启用双模型模式（第二组 geo 路径非空即视为启用）。 */
    public boolean isDualModel() {
        return geoPath2 != null && !geoPath2.isEmpty();
    }
}
