package com.xsasakihaise.hellasdeck.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

/**
 * Utility loader for {@code card_types.json}, exposing card tags and counts to
 * administrative tooling.
 */
public class CardTypeLoader {
    /**
     * Reads the bundled card type configuration.
     *
     * @return mapping of type id to metadata or an empty map if the file is missing
     */
    public static Map<String, CardTypeData> load() {
        try (InputStream is = CardTypeLoader.class.getClassLoader().getResourceAsStream("data/card_types.json")) {
            if (is != null) {
                InputStreamReader reader = new InputStreamReader(is);
                Map var3 = (Map)(new Gson()).fromJson(reader, (new TypeToken<Map<String, CardTypeData>>() {
                }).getType());
                return var3;
            } else {
                Map var2 = Collections.emptyMap();
                return var2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}
