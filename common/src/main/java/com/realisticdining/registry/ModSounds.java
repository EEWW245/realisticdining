package com.realisticdining.registry;

import com.realisticdining.RealisticDining;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(RealisticDining.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> POUR_OIL = SOUNDS.register("pour_oil",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "pour_oil")));

    public static final RegistrySupplier<SoundEvent> SAUTE = SOUNDS.register("saute",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "saute")));

    public static final RegistrySupplier<SoundEvent> MEAT_IN_THE_POT = SOUNDS.register("meat_in_the_pot",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "meat_in_the_pot")));

    public static final RegistrySupplier<SoundEvent> GREEN_VEGETABLES_IN_THE_POT = SOUNDS.register("green_vegetables_in_the_pot",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "green_vegetables_in_the_pot")));

    public static void init() {
        SOUNDS.register();
    }
}
