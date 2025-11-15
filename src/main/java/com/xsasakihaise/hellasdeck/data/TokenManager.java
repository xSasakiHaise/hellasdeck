package com.xsasakihaise.hellasdeck.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks in-memory Deck token balances by player UUID.
 *
 * <p>The storage is intentionally simple: balances are reset when the server
 * restarts, so staff should treat this as a temporary currency backing the
 * Deck sessions.</p>
 */
public class TokenManager {
    private static final Map<String, Integer> tokens = new HashMap();

    /**
     * @return the number of tokens currently available for the UUID
     */
    public static int get(String uuid) {
        return (Integer)tokens.getOrDefault(uuid, 0);
    }

    /**
     * Adds the provided amount to the player's balance.
     */
    public static void add(String uuid, int amount) {
        tokens.put(uuid, get(uuid) + amount);
    }

    /**
     * Removes tokens, never allowing the balance to fall below zero.
     */
    public static void remove(String uuid, int amount) {
        int current = get(uuid);
        tokens.put(uuid, Math.max(0, current - amount));
    }

    public static void take(String uuid, int amount) {
        remove(uuid, amount);
    }
}
