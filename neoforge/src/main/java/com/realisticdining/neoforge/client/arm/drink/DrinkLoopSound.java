package com.realisticdining.neoforge.client.arm.drink;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

/**
 * 循环音效：用于 bag_rustle 等"动画期间持续播放"的音效。
 *
 * <p>1.21.1 中 AbstractTickableSoundInstance.stop() 为 protected final，
 * 故通过 public stopLoop() 暴露停止能力，由 DrinkAnimHandler 在动画结束时调用。
 * 字段 looping/x/y/z/volume/pitch 仍可变，tick() 中更新 x/y/z 跟随玩家。
 */
public class DrinkLoopSound extends AbstractTickableSoundInstance {

    public DrinkLoopSound(SoundEvent sound, SoundSource source) {
        super(sound, source, RandomSource.create());
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
        }
    }

    @Override
    public void tick() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            stopLoop();
            return;
        }
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    /** 公开停止入口：内部调用父类 protected final stop()。 */
    public void stopLoop() {
        stop();
    }
}
