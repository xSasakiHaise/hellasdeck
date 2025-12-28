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
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(((LiteralArgumentBuilder)Commands.literal("tokens").requires((src) -> src.hasPermission(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).getPlayerOrException();
            String uuid = player.getUUID().toString();
            int amount = TokenManager.get(uuid);
            ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("\ud83d\udcb0 Tokens: " + amount), false);
            return 1;
        }))));
    }
}
