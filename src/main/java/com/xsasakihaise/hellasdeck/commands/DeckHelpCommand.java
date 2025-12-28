package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

/**
 * Provides an in-game primer for the Deck command suite.
 */
public class DeckHelpCommand {
    /**
     * Adds the help literal and prints a curated description of each command.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(((LiteralArgumentBuilder)Commands.literal("help").requires((src) -> src.hasPermissionLevel(0))).executes((ctx) -> {
            CommandSource source = (CommandSource)ctx.getSource();
            source.sendFeedback(new StringTextComponent("---------- Hellas Deck Commands ----------"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck broadcast <true|false> - Lts you toggle if you want to broadcast your tries"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck draw - Draw a card from your current deck session"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck end - Ends your current deck session"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck help - Shows this help message"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck input - Convert Deck-of-Many-Mons items in hand into tokens"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck list - List all drawn cards with indexes"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck output <amount> - Convert tokens back into Deck-of-Many-Mons items"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck protect <index> - Protect a card in your current session"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck start - Start a new deck session (consumes a token)"), false);
            source.sendFeedback(new StringTextComponent("/hellas deck tokens - Shows your current token count"), false);
            source.sendFeedback(new StringTextComponent("----------------------------------------"), false);
            return 1;
        }))));
    }
}
