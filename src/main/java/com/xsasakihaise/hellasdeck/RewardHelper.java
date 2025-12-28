package com.xsasakihaise.hellasdeck;

import com.xsasakihaise.hellasdeck.data.TableLoader;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

/**
 * Helper for materialising rewards by translating drawn card labels into
 * {@code /pokegive} commands executed on the dedicated server.
 */
public class RewardHelper {
    /**
     * Iterates over drawn cards and grants each reward to the supplied player.
     *
     * @param player      recipient of the Pokémon
     * @param drawnCards  formatted strings from {@link DeckOfManyMons#drawCard()}
     */
    public static void giveCards(ServerPlayerEntity player, List<String> drawnCards) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            CommandSource commandSource = server.getCommandSource().withPermission(2).withEntity(player);
            for(String card : drawnCards) {
                boolean shiny = card.contains("(Shiny)");
                String type;
                String name;
                if (card.contains(" - ")) {
                    String[] split = card.split(" - ", 2);
                    type = split[0].trim();
                    name = split[1].replace(" (Shiny)", "").trim();
                } else {
                    type = card;
                    name = card;
                }

                Map<String, String> table = TableLoader.getTable(type.toLowerCase());
                String pokeData = (String)table.getOrDefault(name, name);
                String cmd = "minecraft:pokegive @p " + pokeData + (shiny ? " shiny" : "");
                server.getCommands().performCommand(commandSource, cmd);
            }

        }
    }
}
