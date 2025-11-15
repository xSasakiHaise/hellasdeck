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
 * Administrative counterpart to {@link DeckTokensGiveCommand} that removes
 * tokens.
 */
public class DeckTokensTakeCommand {
    /**
     * Wires the {@code take} literal and permissions into Brigadier.
     */
 public static void register(CommandDispatcher<CommandSource> dispatcher) {
  dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(Commands.func_197057_a("tokens").then(Commands.func_197057_a("take").then(Commands.func_197056_a("player", StringArgumentType.string()).then(((RequiredArgumentBuilder)Commands.func_197056_a("amount", IntegerArgumentType.integer(1)).requires((src) -> src.func_197034_c(2))).executes((ctx) -> {
   String name = StringArgumentType.getString(ctx, "player");
   int amount = IntegerArgumentType.getInteger(ctx, "amount");
   ServerPlayerEntity target = ((CommandSource)ctx.getSource()).func_197028_i().func_184103_al().func_152612_a(name);
   if (target == null) {
    ((CommandSource)ctx.getSource()).func_197021_a(new StringTextComponent("Player not found."));
    return 0;
   } else {
    String uuid = target.func_110124_au().toString();
    TokenManager.remove(uuid, amount);
    ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Took " + amount + " tokens from " + name), false);
    return 1;
   }
  })))))));
 }
}
