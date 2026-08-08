package com.example.realisticdining.fabric.client.arm.drink;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;

/**
 * 饮用动画音效触发点。
 *
 * <p>持有 {@link Supplier<SoundEvent>} 而非 {@link SoundEvent} 本身，
 * 避免在 {@link DrinkAnimRegistry} 静态初始化时调用 {@code Supplier.get()}
 * 拿到 null（registry 尚未填充时）。真正播放时才通过 {@link #resolve()} 懒解析。
 *
 * @param soundSupplier 音效事件供应者（懒解析）
 * @param timeSeconds   触发时间（秒，动画起点为 0）
 * @param looping       是否循环播放（动画期间持续，动画结束/重置时停止）
 */
public record DrinkSoundCue(
        Supplier<SoundEvent> soundSupplier,
        double timeSeconds,
        boolean looping
) {
    public static DrinkSoundCue once(Supplier<SoundEvent> sound, double timeSeconds) {
        return new DrinkSoundCue(sound, timeSeconds, false);
    }

    public static DrinkSoundCue loop(Supplier<SoundEvent> sound, double timeSeconds) {
        return new DrinkSoundCue(sound, timeSeconds, true);
    }

    /** 懒解析 SoundEvent；若供应者尚未就绪返回 null。 */
    public SoundEvent resolve() {
        return soundSupplier.get();
    }
}
