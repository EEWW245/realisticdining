package com.example.realisticdining.commands;

import com.example.realisticdining.init.ModBlocks;
import com.example.realisticdining.init.ModItems;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CookingCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cooking")
            .then(Commands.literal("give")
                .then(Commands.argument("item", StringArgumentType.string())
                    .executes(context -> {
                        Player player = context.getSource().getPlayerOrException();
                        String itemName = StringArgumentType.getString(context, "item");
                        
                        switch (itemName) {
                            case "tomato":
                                player.addItem(new ItemStack(ModItems.TOMATO.get()));
                                player.sendSystemMessage(Component.literal("Given tomato"));
                                break;
                            case "onion":
                                player.addItem(new ItemStack(ModItems.ONION.get()));
                                player.sendSystemMessage(Component.literal("Given onion"));
                                break;
                            case "salad":
                                player.addItem(new ItemStack(ModItems.TOMATO_SALAD.get()));
                                player.sendSystemMessage(Component.literal("Given tomato salad"));
                                break;
                            case "wok":
                                player.addItem(new ItemStack(ModItems.WOK.get()));
                                player.sendSystemMessage(Component.literal("Given wok"));
                                break;
                            case "wok_block":
                                player.addItem(new ItemStack(ModBlocks.WOK_BLOCK.get()));
                                player.sendSystemMessage(Component.literal("Given wok block"));
                                break;
                            default:
                                player.sendSystemMessage(Component.literal("Unknown item: " + itemName));
                                break;
                        }
                        
                        return 1;
                    })
                )
            )
            .then(Commands.literal("test")
                .executes(context -> {
                    Player player = context.getSource().getPlayerOrException();
                    player.sendSystemMessage(Component.literal("Cooking mod test successful!"));
                    player.sendSystemMessage(Component.literal("Available commands: /cooking give [item]"));
                    player.sendSystemMessage(Component.literal("Available items: tomato, onion, salad, wok, wok_block"));
                    return 1;
                })
            )
        );
    }
}