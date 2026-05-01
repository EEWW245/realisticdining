package com.example.realisticdining.init;

import com.example.realisticdining.RealisticDining;
import com.example.realisticdining.platform.PlatformRegistry;
import com.example.realisticdining.platform.ServiceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import java.util.function.Supplier;

public class ModSounds {
    private static final PlatformRegistry<SoundEvent> SOUND_EVENTS = ServiceHelper.getPlatformServices().createSoundEventRegistry(RealisticDining.MOD_ID);

    public static final Supplier<SoundEvent> POUR_OIL = register("pour_oil",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RealisticDining.MOD_ID, "pour_oil")));

    public static final Supplier<SoundEvent> SAUTE = register("saute",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RealisticDining.MOD_ID, "saute")));

    public static final Supplier<SoundEvent> MEAT_IN_THE_POT = register("meat_in_the_pot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RealisticDining.MOD_ID, "meat_in_the_pot")));

    public static final Supplier<SoundEvent> GREEN_VEGETABLES_IN_THE_POT = register("green_vegetables_in_the_pot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RealisticDining.MOD_ID, "green_vegetables_in_the_pot")));

    private static <T extends SoundEvent> Supplier<T> register(String name, Supplier<T> sound) {
        return SOUND_EVENTS.register(new ResourceLocation(RealisticDining.MOD_ID, name), sound);
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
