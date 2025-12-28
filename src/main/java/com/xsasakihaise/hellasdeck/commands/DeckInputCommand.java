package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.TokenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

/**
 * Converts Deck-of-Many-Mons items into token balance via
 * {@code /hellas deck input}.
 */
public class DeckInputCommand {
    /**
     * Adds the command to Brigadier.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(((LiteralArgumentBuilder)Commands.literal("input").requires((src) -> src.hasPermission(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).getPlayerOrException();
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem().getRegistryName().toString().equals("pixelmon:deck-of-many-mons")) {
                int count = stack.getCount();
                stack.setCount(0);
                String uuid = player.getUUID().toString();
                TokenManager.add(uuid, count);
                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("Added " + count + " tokens."), false);
                return 1;
            } else {
                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("You must hold a Deck of Many Mons item in your hand."), false);
                return 0;
            }
        }))));
    }
}
