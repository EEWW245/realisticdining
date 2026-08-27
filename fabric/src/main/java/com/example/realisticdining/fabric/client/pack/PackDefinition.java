package com.example.realisticdining.fabric.client.pack;

import java.util.List;
import java.util.Map;

/**
 * 材质包扩展物品定义（Fabric 1.20.1）
 *
 * <p>极简结构，仅 4 个字段，路径全部由定义文件名自动派生：
 * <ul>
 *   <li>{@code item}：要绑定的物品 ID（任意 mod 的物品都行，如 minecraft:apple）</li>
 *   <li>{@code mode}：持物模式（可选，"static" 默认 或 "pickup"），
 *       见 {@link PackMode}。static 模式立即显示 3D 模型 + 程序化晃动；
 *       pickup 模式拿到物品自动播放 pickup 动画并定格在持物姿态。</li>
 *   <li>{@code invisible}：持物状态隐藏的骨骼名（可选，空数组或不写表示不隐藏）</li>
 *   <li>{@code sounds}：音效关键帧 → 音效 ID 映射（可选，如 {"eating": "realisticdining:eat"})</li>
 * </ul>
 *
 * <p>资源路径约定（约定优于配置）：
 * <ul>
 *   <li>模型：{@code realisticdining:geo/{name}.geo.json}</li>
 *   <li>材质：{@code realisticdining:textures/item/{name}.png}</li>
 *   <li>动画：{@code realisticdining:animations/{name}.animation.json}</li>
 *   <li>动画名：static 模式固定 {@code eat}；pickup 模式需 {@code pickup} + {@code eat} 两个</li>
 * </ul>
 * 其中 {@code name} 由定义文件名（去 .json 后缀）决定，与 {@code item} 字段无关，避免同名冲突。
 */
public class PackDefinition {
    private String item;
    /** 持物模式字符串（"static" 或 "pickup"），null/空时默认 static。 */
    private String mode;
    private Map<String, String> sounds;
    private List<String> invisible;

    public String getItem() {
        return item;
    }

    /** 解析持物模式，null/空/无法识别时返回 {@link PackMode#STATIC}（默认）。 */
    public PackMode getMode() {
        return PackMode.fromString(mode);
    }

    public Map<String, String> getSounds() {
        return sounds;
    }

    public List<String> getInvisible() {
        return invisible;
    }
}
