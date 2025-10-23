package com.xsasakihaise.hellasdeck.data;

import java.util.HashMap;
import java.util.Map;

public class TokenManager {
    private static final Map<String, Integer> tokens = new HashMap();

    public static int get(String uuid) {
        return (Integer)tokens.getOrDefault(uuid, 0);
    }

    public static void add(String uuid, int amount) {
        tokens.put(uuid, get(uuid) + amount);
    }

    public static void remove(String uuid, int amount) {
        int current = get(uuid);
        tokens.put(uuid, Math.max(0, current - amount));
    }

    public static void take(String uuid, int amount) {
        remove(uuid, amount);
    }
}
