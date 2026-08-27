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

    public static final RegistrySupplier<SoundEvent> EAT_RICE = SOUNDS.register("eat_rice",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "eat_rice")));

    // === 饮用动画音效 ===
    // 薯片
    public static final RegistrySupplier<SoundEvent> DRINK_CRISP_BAG_PICKUP = registerDrink("drink.crisp.bag_pickup");
    public static final RegistrySupplier<SoundEvent> DRINK_CRISP_BAG_OPEN = registerDrink("drink.crisp.bag_open");
    public static final RegistrySupplier<SoundEvent> DRINK_CRISP_CRUNCH = registerDrink("drink.crisp.crisp_crunch");
    // 矿泉水
    public static final RegistrySupplier<SoundEvent> DRINK_WATER_CAP_OFF = registerDrink("drink.water.cap_off");
    public static final RegistrySupplier<SoundEvent> DRINK_WATER_GULP = registerDrink("drink.water.water_gulp");
    public static final RegistrySupplier<SoundEvent> DRINK_WATER_AHH = registerDrink("drink.water.water_ahh");
    // 能量棒
    public static final RegistrySupplier<SoundEvent> DRINK_ENERGYBAR_WRAPPER_PICKUP = registerDrink("drink.energybar.wrapper_pickup");
    public static final RegistrySupplier<SoundEvent> DRINK_ENERGYBAR_WRAPPER_OPEN = registerDrink("drink.energybar.wrapper_open");
    public static final RegistrySupplier<SoundEvent> DRINK_ENERGYBAR_BAR_CHEW = registerDrink("drink.energybar.bar_chew");
    // 罐装
    public static final RegistrySupplier<SoundEvent> DRINK_CAN_PULL_TAB = registerDrink("drink.can.pull_tab");
    public static final RegistrySupplier<SoundEvent> DRINK_CAN_GULP = registerDrink("drink.can.can_gulp");
    public static final RegistrySupplier<SoundEvent> DRINK_CAN_AHH = registerDrink("drink.can.can_ahh");
    // 珍珠奶茶
    public static final RegistrySupplier<SoundEvent> DRINK_MILKTEA_STRAW_INSERT = registerDrink("drink.milktea.straw_insert");
    public static final RegistrySupplier<SoundEvent> DRINK_MILKTEA_GULP = registerDrink("drink.milktea.gulp");

    private static RegistrySupplier<SoundEvent> registerDrink(String path) {
        return SOUNDS.register(path,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, path)));
    }

    public static void init() {
        SOUNDS.register();
    }
}
