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

/**
 * Lightweight loader for the informational {@code hellasdeck.json} file
 * distributed with the mod pack.
 *
 * <p>The JSON is not currently editable at runtime, but bundling the document
 * allows staff to check which version, dependencies, and bullet-point features
 * are present on a server.</p>
 */
public class HellasDeckInfoConfig {
    /**
     * Raw JSON payload describing the mod metadata.
     */
    public JsonObject config;

    /**
     * Reads the bundled JSON file immediately so downstream consumers always
     * work with a populated object.
     */
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

    /**
     * Safely returns an array value for the provided key.
     *
     * @param key JSON property name such as {@code features}
     * @return array value or an empty array if the key is absent
     */
    public JsonArray getAsJsonArray(String key) {
        return this.config != null && this.config.has(key) && this.config.get(key).isJsonArray() ? this.config.getAsJsonArray(key) : new JsonArray();
    }

    /**
     * @return {@code true} when the JSON looks valid (currently only checks for
     *     the {@code version} string)
     */
    public boolean isValid() {
        return this.config != null && this.config.has("version") && this.config.get("version").isJsonPrimitive();
    }

    /**
     * Human readable version of the config payload.
     *
     * @return configured version or {@code Unknown}
     */
    public String getVersion() {
        return this.isValid() ? this.config.get("version").getAsString() : "Unknown";
    }
}
