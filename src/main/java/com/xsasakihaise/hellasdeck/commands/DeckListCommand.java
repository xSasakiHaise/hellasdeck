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
 * Implements {@code /hellas deck list} to show the current draw order and
 * protection status.
 */
public class DeckListCommand {
    /**
     * Installs the list literal.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(((LiteralArgumentBuilder)Commands.func_197057_a("list").requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            DeckOfManyMons deck = DeckSessions.getSession(player);
            if (deck == null) {
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("No active deck session."), false);
                return 0;
            } else {
                StringBuilder sb = new StringBuilder();
                int index = 0;

                for(String card : deck.getDrawnCards()) {
                    boolean prot = deck.getProtectedIndices().contains(index);
                    sb.append(index).append(": ").append(card).append(prot ? " [protected]" : "").append("\n");
                    ++index;
                }

                if (DeckSessions.isBroadcast(player)) {
                    ((CommandSource)ctx.getSource()).func_197028_i().func_184103_al().func_232641_a_(new StringTextComponent(sb.toString()), ChatType.SYSTEM, player.func_110124_au());
                } else {
                    ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent(sb.toString()), false);
                }

                return 1;
            }
        }))));
    }
}
