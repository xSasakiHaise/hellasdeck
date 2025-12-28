package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.DeckOfManyMons;
import com.xsasakihaise.hellasdeck.data.DeckSessions;
import java.util.List;
import java.util.Set;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Implements {@code /hellas deck end}, finalising the run and issuing rewards.
 */
public class DeckEndCommand {
    /**
     * Hooks the command into Brigadier.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(((LiteralArgumentBuilder)Commands.literal("end").requires((src) -> src.hasPermission(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).getPlayerOrException();
            DeckOfManyMons deck = DeckSessions.getSession(player);
            if (deck == null) {
                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("No active deck session."), false);
                return 0;
            } else {
                deck.endGame();
                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("Deck session ended!"), false);
                List<String> commands = deck.getCommandData();
                List<Boolean> shinies = deck.getShinyFlags();
                Set<Integer> protectedIndices = deck.getProtectedIndices();
                boolean deathTriggered = deck.isDeathTriggered();

                for(int i = 0; i < commands.size(); ++i) {
                    if (!deathTriggered || protectedIndices.contains(i)) {
                        String cmdData = (String)commands.get(i);
                        if (cmdData != null && !cmdData.isEmpty()) {
                            boolean shiny = (Boolean)shinies.get(i);
                            String fullCommand = "/minecraft:pokegive @p " + cmdData + (shiny ? " shiny" : "");
                            ((CommandSource)ctx.getSource()).getServer().getCommands().performCommand(((CommandSource)ctx.getSource()).withPermission(2).withEntity(player), fullCommand);
                        }
                    }
                }

                DeckSessions.endSession(player);
                return 1;
            }
        }))));
    }
}
