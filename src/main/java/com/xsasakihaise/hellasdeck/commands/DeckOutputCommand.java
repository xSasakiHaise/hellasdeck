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

public class DeckOutputCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(Commands.func_197057_a("output").then(((RequiredArgumentBuilder)Commands.func_197056_a("amount", IntegerArgumentType.integer(1)).requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            String uuid = player.func_110124_au().toString();
            int amount = IntegerArgumentType.getInteger(ctx, "amount");
            int tokens = TokenManager.get(uuid);
            if (tokens < amount) {
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Not enough tokens."), false);
                return 0;
            } else {
                TokenManager.remove(uuid, amount);
                ItemStack deckItem = new ItemStack((IItemProvider)ForgeRegistries.ITEMS.getValue(new ResourceLocation("pixelmon", "deck-of-many-mons")), amount);
                if (!player.field_71071_by.func_70441_a(deckItem)) {
                    player.func_71019_a(deckItem, false);
                }

                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Converted " + amount + " tokens into deck items."), false);
                return 1;
            }
        })))));
    }
}
