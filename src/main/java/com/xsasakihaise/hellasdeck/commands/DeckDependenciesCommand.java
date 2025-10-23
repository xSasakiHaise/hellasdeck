package com.xsasakihaise.hellasdeck.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.xsasakihaise.hellasdeck.HellasDeckInfoConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class DeckDependenciesCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher, HellasDeckInfoConfig infoConfig) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("hellas").then(Commands.func_197057_a("deck").then(Commands.func_197057_a("dependencies").executes((ctx) -> {
            if (!infoConfig.isValid()) {
                ((CommandSource)ctx.getSource()).func_197030_a(new StringTextComponent("Fehler: HellasDeck-Info nicht geladen (fehlende oder ungültige JSON)."), false);
                return 0;
            } else {
                StringBuilder sb = new StringBuilder();
                infoConfig.getAsJsonArray("dependencies").forEach((d) -> sb.append(d.getAsString()).append("\n"));
                return sendFormatted((CommandSource)ctx.getSource(), sb.toString().trim());
            }
        }))));
    }

    private static int sendFormatted(CommandSource source, String text) {
        source.func_197030_a(new StringTextComponent("-----------------------------------\n" + text + "\n-----------------------------------"), false);
        return 1;
    }
}
