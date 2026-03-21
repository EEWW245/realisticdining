package com.example.realisticdining.init;

import com.example.realisticdining.realisticdining;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, realisticdining.MODID);

    public static final RegistryObject<SoundEvent> POUR_OIL = SOUND_EVENTS.register("pour_oil",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(realisticdining.MODID, "pour_oil")));

    public static final RegistryObject<SoundEvent> SAUTE = SOUND_EVENTS.register("saute",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(realisticdining.MODID, "saute")));

    public static final RegistryObject<SoundEvent> MEAT_IN_THE_POT = SOUND_EVENTS.register("meat_in_the_pot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(realisticdining.MODID, "meat_in_the_pot")));

    public static final RegistryObject<SoundEvent> GREEN_VEGETABLES_IN_THE_POT = SOUND_EVENTS.register("green_vegetables_in_the_pot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(realisticdining.MODID, "green_vegetables_in_the_pot")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
