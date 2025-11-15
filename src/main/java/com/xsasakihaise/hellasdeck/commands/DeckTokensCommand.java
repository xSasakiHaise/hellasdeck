package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.TokenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Displays the current token balance via {@code /hellas deck tokens}.
 */
public class DeckTokensCommand {
    /**
     * Adds the command literal.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(((LiteralArgumentBuilder)Commands.func_197057_a("tokens").requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            String uuid = player.func_110124_au().toString();
            int amount = TokenManager.get(uuid);
            ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("\ud83d\udcb0 Tokens: " + amount), false);
            return 1;
        }))));
    }
}
