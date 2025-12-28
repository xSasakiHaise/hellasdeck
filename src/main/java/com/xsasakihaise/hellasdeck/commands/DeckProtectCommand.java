package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.DeckOfManyMons;
import com.xsasakihaise.hellasdeck.data.DeckSessions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Implements {@code /hellas deck protect <index>} which shields a card from the
 * death effect.
 */
public class DeckProtectCommand {
    /**
     * Registers the literal and integer argument with Brigadier.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(Commands.literal("protect").then(((RequiredArgumentBuilder)Commands.argument("index", IntegerArgumentType.integer(0)).requires((src) -> src.hasPermissionLevel(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).asPlayer();
            int idx = IntegerArgumentType.getInteger(ctx, "index");
            DeckOfManyMons deck = DeckSessions.getSession(player);
            if (deck == null) {
                ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("No active deck session."), false);
                return 0;
            } else {
                deck.protectCard(idx);
                ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("Protected card at index " + idx), false);
                return 1;
            }
        })))));
    }
}
