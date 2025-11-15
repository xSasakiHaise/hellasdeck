package com.xsasakihaise.hellasdeck.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * Runtime representation of an active Deck of Many Mons run.
 *
 * <p>The class keeps track of card pools, special draws, command payloads to be
 * executed later, shiny rolls, and bookkeeping such as the death/protect state.
 * Instances live for the duration of a player's session and are referenced via
 * {@link DeckSessions}.</p>
 */
public class DeckOfManyMons {
    private final Map<String, Integer> cardTypeAmounts = new HashMap();
    private final Map<String, String> cardTypeTags = new HashMap();
    private final List<String> drawnCards = new ArrayList();
    private final Set<Integer> protectedIndices = new HashSet();
    private final List<String> runLog = new ArrayList();
    private final List<String> endLog = new ArrayList();
    private final Set<String> specialTypes = new HashSet(Arrays.asList("protect", "death", "retry"));
    private final Random random = new Random();
    private final ServerPlayerEntity player;
    private boolean ended = false;
    private boolean retryProtected = false;
    private boolean deathTriggered = false;
    private final List<String> commandData = new ArrayList();
    private final List<Boolean> shinyFlags = new ArrayList();
    private final Gson gson = new Gson();

    /**
     * Builds a deck session for the supplied player, pre-loading the
     * {@code card_types.json} metadata that defines the weighting and labels for
     * each card category.
     *
     * @param player owner of the session
     * @throws Exception when the card type resource is missing or cannot be parsed
     */
    public DeckOfManyMons(ServerPlayerEntity player) throws Exception {
        this.player = player;
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/card_types.json");
        if (is == null) {
            throw new Exception("card_types.json not found!");
        } else {
            Type mapType = (new TypeToken<Map<String, Map<String, Object>>>() {
            }).getType();
            Map<String, Map<String, Object>> root = (Map)this.gson.fromJson(new InputStreamReader(is, "UTF-8"), mapType);

            for(Map.Entry<String, Map<String, Object>> entry : root.entrySet()) {
                Map<String, Object> data = (Map)entry.getValue();
                int amount = ((Number)data.get("amount")).intValue();
                String tag = data.get("tag").toString();
                this.cardTypeAmounts.put(entry.getKey(), amount);
                this.cardTypeTags.put(entry.getKey(), tag);
            }

            this.runLog.add("Run started for " + player.getDisplayName().getString());
        }
    }

    /**
     * Performs a weighted draw from all card types and records the result.
     *
     * @return the formatted string that should be shown to the player
     * @throws Exception when an underlying table cannot be read
     */
    public String drawCard() throws Exception {
        if (this.ended) {
            return "The game has already ended.";
        } else {
            List<String> weighted = new ArrayList();

            // Build a basic weighted list so each card type maintains its configured odds
            for(Map.Entry<String, Integer> e : this.cardTypeAmounts.entrySet()) {
                for(int i = 0; i < (Integer)e.getValue(); ++i) {
                    weighted.add(e.getKey());
                }
            }

            String drawnType = (String)weighted.get(this.random.nextInt(weighted.size()));
            String result;
            if (this.specialTypes.contains(drawnType)) {
                switch (drawnType) {
                    case "protect":
                        result = (String)this.cardTypeTags.get("protect") + " drawn! Choose a card to protect.";
                        this.drawnCards.add("protect");
                        this.commandData.add("");
                        this.shinyFlags.add(false);
                        this.runLog.add(result);
                        break;
                    case "death":
                        result = (String)this.cardTypeTags.get("death") + " drawn! Only protected cards remain.";
                        this.drawnCards.add("death");
                        this.commandData.add("");
                        this.shinyFlags.add(false);
                        this.runLog.add(result);
                        this.handleDeath();
                        this.deathTriggered = true;
                        break;
                    case "retry":
                        result = (String)this.cardTypeTags.get("retry") + " drawn! This card is automatically protected.";
                        this.drawnCards.add("retry");
                        this.commandData.add("");
                        this.shinyFlags.add(false);
                        this.protectedIndices.add(this.drawnCards.size() - 1);
                        this.retryProtected = true;
                        this.runLog.add(result);
                        break;
                    default:
                        result = "Unknown special type";
                }
            } else {
                result = this.handleNormal(drawnType);
                this.drawnCards.add(result);
                this.runLog.add("Drawn: " + result);
            }

            return result;
        }
    }

    /**
     * Handles the "normal" card flow where a draw references a table entry in
     * {@code data/tables/<type>.json}.
     */
    private String handleNormal(String type) throws Exception {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("data/tables/" + type + ".json");
        if (is == null) {
            return "No file for type: " + type;
        } else {
            Type mapType = (new TypeToken<Map<String, String>>() {
            }).getType();
            Map<String, String> table = (Map)this.gson.fromJson(new InputStreamReader(is, "UTF-8"), mapType);
            if (table.isEmpty()) {
                return "No cards found for type: " + type;
            } else {
                List<String> keys = new ArrayList(table.keySet());
                String displayName = (String)keys.get(this.random.nextInt(keys.size()));
                String cmdData = (String)table.get(displayName);
                boolean shiny = this.random.nextInt(10) == 0;
                this.commandData.add(cmdData);
                this.shinyFlags.add(shiny);
                return displayName + (shiny ? " (Shiny)" : "");
            }
        }
    }

    /**
     * Resolves the "death" card effect by removing every card that is not
     * protected or intrinsically safe (retry).
     */
    private void handleDeath() {
        List<String> kept = new ArrayList();
        List<String> newCmd = new ArrayList();
        List<Boolean> newShiny = new ArrayList();
        Set<Integer> newProtected = new HashSet();
        int newIdx = 0;

        for(int i = 0; i < this.drawnCards.size(); ++i) {
            String c = (String)this.drawnCards.get(i);
            if (this.protectedIndices.contains(i) || "retry".equals(c)) {
                // This card survives the purge and therefore needs to maintain
                // its command payload and shiny state alignment.
                kept.add(c);
                newCmd.add(this.commandData.get(i));
                newShiny.add(this.shinyFlags.get(i));
                if (this.protectedIndices.contains(i)) {
                    newProtected.add(newIdx);
                }

                ++newIdx;
            }
        }

        this.drawnCards.clear();
        this.drawnCards.addAll(kept);
        this.commandData.clear();
        this.commandData.addAll(newCmd);
        this.shinyFlags.clear();
        this.shinyFlags.addAll(newShiny);
        this.protectedIndices.clear();
        this.protectedIndices.addAll(newProtected);
    }

    /**
     * Marks a card index as safe from the next death trigger.
     *
     * @param index zero-based index displayed to the player via /deck list
     */
    public void protectCard(int index) {
        if (index >= 0 && index < this.drawnCards.size() && !this.protectedIndices.contains(index)) {
            this.protectedIndices.add(index);
            this.runLog.add("Card protected: " + (String)this.drawnCards.get(index));
        }

    }

    /**
     * Flags the deck as finished and records a summary into the persistent log
     * entries. Rewards are not granted here; commands are executed by
     * {@link com.xsasakihaise.hellasdeck.commands.DeckEndCommand}.
     */
    public void endGame() {
        this.ended = true;
        this.endLog.add("Game ended. Player: " + this.player.getDisplayName().getString());

        for(int i = 0; i < this.drawnCards.size(); ++i) {
            String card = (String)this.drawnCards.get(i);
            boolean prot = this.protectedIndices.contains(i) || "retry".equals(card);
            this.endLog.add(card + (prot ? " [protected]" : ""));
        }

    }

    /**
     * @return {@code true} if the death card has ever been drawn during this run
     */
    public boolean isDeathTriggered() {
        return this.deathTriggered;
    }

    /**
     * @return copy of the cards drawn so far (including labels for special cards)
     */
    public List<String> getDrawnCards() {
        return new ArrayList(this.drawnCards);
    }

    /**
     * @return indices currently immune to the death effect
     */
    public Set<Integer> getProtectedIndices() {
        return new HashSet(this.protectedIndices);
    }

    /**
     * @return chronological narration of the session used for auditing
     */
    public List<String> getRunLog() {
        return new ArrayList(this.runLog);
    }

    /**
     * @return final summary recorded when {@link #endGame()} is called
     */
    public List<String> getEndLog() {
        return new ArrayList(this.endLog);
    }

    /**
     * Command payloads mirror {@link #drawnCards} indices and allow staff to
     * grant rewards after the run concludes.
     */
    public List<String> getCommandData() {
        return new ArrayList(this.commandData);
    }

    /**
     * @return list of shiny toggles aligned with the command data list
     */
    public List<Boolean> getShinyFlags() {
        return new ArrayList(this.shinyFlags);
    }
}
