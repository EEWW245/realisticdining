package com.example.realisticdining.forge.client.arm.drink;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

/**
 * 循环音效：用于 bag_rustle 等"动画期间持续播放"的音效。
 *
 * <p>1.20.1 中 AbstractSoundInstance 字段可变，tick() 中更新 x/y/z 跟随玩家。
 * 动画结束/重置时由 DrinkAnimHandler 调用 stopLoop() 停止。
 */
public class DrinkLoopSound extends AbstractTickableSoundInstance {
    private boolean stopped = false;

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

    public void stopLoop() {
        stopped = true;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }
}
