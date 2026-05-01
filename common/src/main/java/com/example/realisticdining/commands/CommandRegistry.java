package com.example.realisticdining.commands;

import com.example.realisticdining.platform.ServiceHelper;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandRegistry {
    
    public static void init() {
        ServiceHelper.getPlatformServices().registerCommand(dispatcher -> {
            CookingCommand.register(dispatcher);
        });
    }
}
