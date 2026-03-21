package com.example.realisticdining.commands;

import com.example.realisticdining.realisticdining;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = realisticdining.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CookingCommand.register(event.getDispatcher());
    }
}