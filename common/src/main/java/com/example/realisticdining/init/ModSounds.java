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

    // === 饮用动画音效 ===
    // 薯片
    public static final Supplier<SoundEvent> DRINK_CRISP_BAG_PICKUP = registerDrink("drink.crisp.bag_pickup");
    public static final Supplier<SoundEvent> DRINK_CRISP_BAG_OPEN = registerDrink("drink.crisp.bag_open");
    public static final Supplier<SoundEvent> DRINK_CRISP_CRUNCH = registerDrink("drink.crisp.crisp_crunch");
    // 矿泉水
    public static final Supplier<SoundEvent> DRINK_WATER_CAP_OFF = registerDrink("drink.water.cap_off");
    public static final Supplier<SoundEvent> DRINK_WATER_GULP = registerDrink("drink.water.water_gulp");
    public static final Supplier<SoundEvent> DRINK_WATER_AHH = registerDrink("drink.water.water_ahh");
    // 能量棒
    public static final Supplier<SoundEvent> DRINK_ENERGYBAR_WRAPPER_PICKUP = registerDrink("drink.energybar.wrapper_pickup");
    public static final Supplier<SoundEvent> DRINK_ENERGYBAR_WRAPPER_OPEN = registerDrink("drink.energybar.wrapper_open");
    public static final Supplier<SoundEvent> DRINK_ENERGYBAR_BAR_CHEW = registerDrink("drink.energybar.bar_chew");
    // 罐装
    public static final Supplier<SoundEvent> DRINK_CAN_PULL_TAB = registerDrink("drink.can.pull_tab");
    public static final Supplier<SoundEvent> DRINK_CAN_GULP = registerDrink("drink.can.can_gulp");
    public static final Supplier<SoundEvent> DRINK_CAN_AHH = registerDrink("drink.can.can_ahh");
    // 珍珠奶茶
    public static final Supplier<SoundEvent> DRINK_MILKTEA_STRAW_INSERT = registerDrink("drink.milktea.straw_insert");
    public static final Supplier<SoundEvent> DRINK_MILKTEA_GULP = registerDrink("drink.milktea.gulp");

    private static <T extends SoundEvent> Supplier<T> register(String name, Supplier<T> sound) {
        return SOUND_EVENTS.register(new ResourceLocation(RealisticDining.MOD_ID, name), sound);
    }

    private static Supplier<SoundEvent> registerDrink(String path) {
        return SOUND_EVENTS.register(new ResourceLocation(RealisticDining.MOD_ID, path),
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RealisticDining.MOD_ID, path)));
    }

    public static void init() {
        // 注册由平台实现自动处理
    }
}
