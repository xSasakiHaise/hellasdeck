package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.TokenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Administrative command {@code /hellas deck tokens give <player> <amount>}.
 */
public class DeckTokensGiveCommand {
    /**
     * Registers the give literal and arguments.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(Commands.literal("tokens").then(Commands.literal("give").then(Commands.argument("player", StringArgumentType.string()).then(((RequiredArgumentBuilder)Commands.argument("amount", IntegerArgumentType.integer(1)).requires((src) -> src.hasPermissionLevel(2))).executes((ctx) -> {
            String name = StringArgumentType.getString(ctx, "player");
            int amount = IntegerArgumentType.getInteger(ctx, "amount");
            ServerPlayerEntity target = ((CommandSource)ctx.getSource()).getServer().getPlayerList().getPlayerByName(name);
            if (target == null) {
                ((CommandSource)ctx.getSource()).sendErrorMessage(new StringTextComponent("Player not found."));
                return 0;
            } else {
                String uuid = target.getUUID().toString();
                TokenManager.add(uuid, amount);
                ((CommandSource)ctx.getSource()).sendFeedback(new StringTextComponent("Gave " + amount + " tokens to " + name), false);
                return 1;
            }
        })))))));
    }
}
