package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.DeckOfManyMons;
import com.xsasakihaise.hellasdeck.data.DeckSessions;
import com.xsasakihaise.hellasdeck.data.TokenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Handles {@code /hellas deck start} which consumes a token and begins a run.
 */
public class DeckStartCommand {
    /**
     * Registers the {@code start} literal against the shared {@code /hellas deck}
     * command tree.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(((LiteralArgumentBuilder)Commands.literal("start").requires((src) -> src.hasPermissionLevel(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).asPlayer();
            String uuid = player.getUUID().toString();
            int tokens = TokenManager.get(uuid);
            if (tokens <= 0) {
                ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("You have 0 tokens! Cannot start."), false);
                return 0;
            } else if (DeckSessions.getSession(player) != null) {
                ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("You already have a running session."), false);
                return 0;
            } else {
                try {
                    TokenManager.take(uuid, 1);
                    DeckOfManyMons deck = new DeckOfManyMons(player);
                    DeckSessions.startSession(player, deck);
                    ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("Deck session started! 1 token consumed."), false);
                    return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("Error starting deck session."), false);
                    return 0;
                }
            }
        }))));
    }
}
