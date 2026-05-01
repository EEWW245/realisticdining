package com.realisticdining.neoforge.loot;

import com.realisticdining.RealisticDining;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = RealisticDining.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModLootModifiers {

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, helper -> {
            helper.register(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "green_onion_seeds_from_grass"),
                    GreenOnionSeedsModifier.CODEC);
            helper.register(ResourceLocation.fromNamespaceAndPath(RealisticDining.MOD_ID, "coriander_seeds_from_grass"),
                    CorianderSeedsModifier.CODEC);
        });
    }
}
