package com.xsasakihaise.hellasdeck;

import com.xsasakihaise.hellasdeck.data.TableLoader;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

public class RewardHelper {
    public static void giveCards(ServerPlayerEntity player, List<String> drawnCards) {
        MinecraftServer server = player.func_184102_h();
        if (server != null) {
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
                server.func_195571_aL().func_197059_a(server.func_195573_aM(), cmd);
            }

        }
    }
}
