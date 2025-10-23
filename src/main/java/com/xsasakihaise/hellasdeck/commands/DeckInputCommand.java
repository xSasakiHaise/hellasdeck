package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.data.TokenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class DeckInputCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(((LiteralArgumentBuilder)Commands.func_197057_a("input").requires((src) -> src.func_197034_c(0))).executes((ctx) -> {
            ServerPlayerEntity player = ((CommandSource)ctx.getSource()).func_197035_h();
            ItemStack stack = player.func_184614_ca();
            if (!stack.func_190926_b() && stack.func_77973_b().getRegistryName().toString().equals("pixelmon:deck-of-many-mons")) {
                int count = stack.func_190916_E();
                stack.func_190920_e(0);
                String uuid = player.func_110124_au().toString();
                TokenManager.add(uuid, count);
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Added " + count + " tokens."), false);
                return 1;
            } else {
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("You must hold a Deck of Many Mons item in your hand."), false);
                return 0;
            }
        }))));
    }
}
