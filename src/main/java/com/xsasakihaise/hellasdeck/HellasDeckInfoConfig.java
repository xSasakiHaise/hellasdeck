//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xsasakihaise.hellasdeck;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HellasDeckInfoConfig {
    public JsonObject config;

    public HellasDeckInfoConfig() {
        this.loadConfig();
    }

    private void loadConfig() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("config/hellasdeck.json")) {
            if (is != null) {
                InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                Throwable var4 = null;

                try {
                    JsonParser parser = new JsonParser();
                    this.config = parser.parse(reader).getAsJsonObject();
                } catch (Throwable var31) {
                    var4 = var31;
                    throw var31;
                } finally {
                    if (reader != null) {
                        if (var4 != null) {
                            try {
                                reader.close();
                            } catch (Throwable var30) {
                                var4.addSuppressed(var30);
                            }
                        } else {
                            reader.close();
                        }
                    }

                }

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.config = new JsonObject();
        this.config.addProperty("version", "Unknown");
        this.config.add("dependencies", new JsonArray());
        this.config.add("features", new JsonArray());
    }

    public JsonArray getAsJsonArray(String key) {
        return this.config != null && this.config.has(key) && this.config.get(key).isJsonArray() ? this.config.getAsJsonArray(key) : new JsonArray();
    }

    public boolean isValid() {
        return this.config != null && this.config.has("version") && this.config.get("version").isJsonPrimitive();
    }

    public String getVersion() {
        return this.isValid() ? this.config.get("version").getAsString() : "Unknown";
    }
}
