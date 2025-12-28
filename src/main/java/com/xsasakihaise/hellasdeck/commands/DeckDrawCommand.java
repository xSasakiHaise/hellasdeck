package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.DeckOfManyMons;
import com.xsasakihaise.hellasdeck.data.DeckSessions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

/**
 * Exposes {@code /hellas deck draw}, the primary action performed during a run.
 */
public class DeckDrawCommand {
    /**
     * Adds the draw literal to the {@code /hellas deck} tree.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(((LiteralArgumentBuilder)Commands.literal("draw").requires((src) -> src.hasPermission(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).getPlayerOrException();
            DeckOfManyMons deck = DeckSessions.getSession(player);
            if (deck == null) {
                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("No active deck session."), false);
                return 0;
            } else {
                String result;
                try {
                    result = deck.drawCard();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("Error drawing card."), false);
                    return 0;
                }

                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent(result), false);
                if (DeckSessions.isBroadcast(player)) {
                    player.getServer().getPlayerList().broadcastMessage(new StringTextComponent(player.getDisplayName().getString() + " drew: " + result), ChatType.SYSTEM, player.getUUID());
                }

                return 1;
            }
        }))));
    }
}
