package com.xsasakihaise.hellasdeck.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides cached access to the numerous reward tables located under
 * {@code data/tables/}. Each table defines friendly display names mapped to
 * command payloads, keeping IO off the main thread once cached.
 */
public class TableLoader {
    private static final Map<String, Map<String, String>> tableCache = new ConcurrentHashMap();
    private static final String TABLE_PATH = "data/tables/";

    /**
     * Loads a table by name, caching it for future lookups.
     *
     * @param type table identifier such as {@code normal} or {@code shiny}
     * @return mapping of display text to command data
     */
    public static Map<String, String> getTable(String type) {
        if (tableCache.containsKey(type)) {
            return (Map)tableCache.get(type);
        } else {
            String fileName = "data/tables/" + type + ".json";

            try (InputStream is = TableLoader.class.getClassLoader().getResourceAsStream(fileName)) {
                if (is != null) {
                    InputStreamReader reader = new InputStreamReader(is);
                    Map<String, String> table = (Map)(new Gson()).fromJson(reader, (new TypeToken<Map<String, String>>() {
                    }).getType());
                    tableCache.put(type, table);
                    Map var6 = table;
                    return var6;
                } else {
                    Map var4 = Collections.emptyMap();
                    return var4;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        }
    }
}
