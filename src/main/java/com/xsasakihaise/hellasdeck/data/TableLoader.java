package com.xsasakihaise.hellasdeck.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableLoader {
    private static final Map<String, Map<String, String>> tableCache = new ConcurrentHashMap();
    private static final String TABLE_PATH = "data/tables/";

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
