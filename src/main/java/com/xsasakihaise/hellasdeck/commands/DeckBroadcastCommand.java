package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.DeckSessions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Toggles whether draws and lists are broadcast to the whole server.
 */
public class DeckBroadcastCommand {
    /**
     * Registers {@code /hellas deck broadcast <bool>}.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(Commands.func_197057_a("broadcast").then(((RequiredArgumentBuilder)Commands.func_197056_a("toggle", BoolArgumentType.bool()).requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            boolean toggle = BoolArgumentType.getBool(ctx, "toggle");
            DeckSessions.setBroadcast(player, toggle);
            ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Deck list broadcast set to: " + toggle), false);
            return 1;
        })))));
    }
}
