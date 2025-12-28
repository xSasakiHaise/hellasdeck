package com.xsasakihaise.hellasdeck.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * Thread-safe registry that tracks which players currently have an active Deck
 * of Many Mons session. Commands consult this singleton to ensure players do
 * not run overlapping games and to remember broadcast preferences.
 */
public class DeckSessions {
    private static final Map<String, DeckOfManyMons> sessions = new ConcurrentHashMap();
    private static final Map<String, Boolean> broadcastMap = new ConcurrentHashMap();

    /**
     * Stores the player's session and resets their broadcast state.
     */
    public static void startSession(ServerPlayerEntity player, DeckOfManyMons deck) {
        sessions.put(player.getUUID().toString(), deck);
        broadcastMap.put(player.getUUID().toString(), false);
    }

    /**
     * @return currently active deck for the player, if any
     */
    public static DeckOfManyMons getSession(ServerPlayerEntity player) {
        return (DeckOfManyMons)sessions.get(player.getUUID().toString());
    }

    /**
     * Removes all stored data for the player's finished session.
     */
    public static void endSession(ServerPlayerEntity player) {
        sessions.remove(player.getUUID().toString());
        broadcastMap.remove(player.getUUID().toString());
    }

    /**
     * Updates the player's broadcast preference so draw/list output can be sent
     * to chat.
     */
    public static void setBroadcast(ServerPlayerEntity player, boolean enabled) {
        broadcastMap.put(player.getUUID().toString(), enabled);
    }

    /**
     * @return whether the player's session should broadcast to the server
     */
    public static boolean isBroadcast(ServerPlayerEntity player) {
        return (Boolean)broadcastMap.getOrDefault(player.getUUID().toString(), false);
    }
}
