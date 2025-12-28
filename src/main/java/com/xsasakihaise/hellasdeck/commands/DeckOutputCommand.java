package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.TokenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Converts tokens back into Deck items via {@code /hellas deck output <amount>}.
 */
public class DeckOutputCommand {
    /**
     * Installs the output literal within the command tree.
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("hellas").then(Commands.literal("deck").then(Commands.literal("output").then(((RequiredArgumentBuilder)Commands.argument("amount", IntegerArgumentType.integer(1)).requires((src) -> src.hasPermission(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).getPlayerOrException();
            String uuid = player.getUUID().toString();
            int amount = IntegerArgumentType.getInteger(ctx, "amount");
            int tokens = TokenManager.get(uuid);
            if (tokens < amount) {
                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("Not enough tokens."), false);
                return 0;
            } else {
                TokenManager.remove(uuid, amount);
                ItemStack deckItem = new ItemStack((IItemProvider)ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "deck-of-many-mons")), amount);
                if (!player.inventory.add(deckItem)) {
                    player.drop(deckItem, false);
                }

                ((CommandSource)ctx.getSource()).sendSuccess(new StringTextComponent("Converted " + amount + " tokens into deck items."), false);
                return 1;
            }
        })))));
    }
}
