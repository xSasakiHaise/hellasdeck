package com.xsasakihaise.hellasdeck.data;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * Persists lightweight JSON logs that describe each Deck run.
 */
public class Logger {
    private static Gson gson = new Gson();
    private static final File root = new File("config/hellas/deck/logs");

    /**
     * Serialises the run log and token balance to {@code config/hellas/deck/logs}.
     *
     * <p>The files are timestamped so staff can audit rewards long after the
     * session has finished.</p>
     */
    public static void logRun(ServerPlayerEntity player, DeckOfManyMons deck) {
        try {
            root.mkdirs();
            String name = player.func_200200_C_().getString() + "_" + player.func_110124_au().toString() + "_" + System.currentTimeMillis();
            File file = new File(root, name + ".json");
            Map<String, Object> log = new HashMap();
            log.put("runLog", deck.getRunLog());
            log.put("endLog", deck.getEndLog());
            log.put("tokens", TokenManager.get(player.func_110124_au().toString()));
            gson.toJson(log, new FileWriter(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
