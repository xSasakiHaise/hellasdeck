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

public class DeckEndCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(((LiteralArgumentBuilder)Commands.func_197057_a("end").requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            DeckOfManyMons deck = DeckSessions.getSession(player);
            if (deck == null) {
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("No active deck session."), false);
                return 0;
            } else {
                deck.endGame();
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Deck session ended!"), false);
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
                            ((CommandSource)ctx.getSource()).func_197028_i().func_195571_aL().func_197059_a(((CommandSource)ctx.getSource()).func_197033_a(2).func_197024_a(player), fullCommand);
                        }
                    }
                }

                DeckSessions.endSession(player);
                return 1;
            }
        }))));
    }
}
