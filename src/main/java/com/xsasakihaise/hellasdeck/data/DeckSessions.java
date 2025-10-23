package com.xsasakihaise.hellasdeck.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DeckSessions {
    private static final Map<String, DeckOfManyMons> sessions = new ConcurrentHashMap();
    private static final Map<String, Boolean> broadcastMap = new ConcurrentHashMap();

    public static void startSession(ServerPlayerEntity player, DeckOfManyMons deck) {
        sessions.put(player.func_110124_au().toString(), deck);
        broadcastMap.put(player.func_110124_au().toString(), false);
    }

    public static DeckOfManyMons getSession(ServerPlayerEntity player) {
        return (DeckOfManyMons)sessions.get(player.func_110124_au().toString());
    }

    public static void endSession(ServerPlayerEntity player) {
        sessions.remove(player.func_110124_au().toString());
        broadcastMap.remove(player.func_110124_au().toString());
    }

    public static void setBroadcast(ServerPlayerEntity player, boolean enabled) {
        broadcastMap.put(player.func_110124_au().toString(), enabled);
    }

    public static boolean isBroadcast(ServerPlayerEntity player) {
        return (Boolean)broadcastMap.getOrDefault(player.func_110124_au().toString(), false);
    }
}
