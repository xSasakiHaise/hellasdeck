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
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(((LiteralArgumentBuilder)Commands.func_197057_a("draw").requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            DeckOfManyMons deck = DeckSessions.getSession(player);
            if (deck == null) {
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("No active deck session."), false);
                return 0;
            } else {
                String result;
                try {
                    result = deck.drawCard();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Error drawing card."), false);
                    return 0;
                }

                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent(result), false);
                if (DeckSessions.isBroadcast(player)) {
                    player.func_184102_h().func_184103_al().func_232641_a_(new StringTextComponent(player.func_200200_C_().getString() + " drew: " + result), ChatType.SYSTEM, player.func_110124_au());
                }

                return 1;
            }
        }))));
    }
}
