package com.realisticdining.fabric.client.pack;

/**
 * 材质包扩展物品的持物模式（Fabric 1.21.1）。
 *
 * <ul>
 *   <li>{@link #STATIC}：拿到物品立即显示 3D 模型 + 程序化晃动（默认）。
 *       动画文件只需 1 个 {@code eat} 动画，U 键触发整段 eat 后 {@code finished;} 消耗。</li>
 *   <li>{@link #PICKUP}：拿到物品自动播放 pickup 动画 → 定格在最后一帧（持物姿态）。
 *       动画文件需 2 个动画：{@code pickup}（用 hold_on_last_frame 定格）+ {@code eat}。
 *       U 键触发 eat，{@code finished;} 消耗后若手中仍有物品则重新触发 pickup 定格。</li>
 * </ul>
 *
 * <p>由定义文件 JSON 的 {@code mode} 字段决定，缺省或无法识别时默认 {@link #STATIC}。
 */
public enum PackMode {
    STATIC,
    PICKUP;

    /**
     * 从字符串解析模式，{@code null}/空/无法识别时返回 {@link #STATIC}（默认静态模式）。
     *
     * @param s 模式字符串（"static" 或 "pickup"，大小写不敏感）
     * @return 对应的 {@link PackMode}
     */
    public static PackMode fromString(String s) {
        if (s == null || s.isBlank()) return STATIC;
        if ("pickup".equalsIgnoreCase(s)) return PICKUP;
        return STATIC;
    }
}
