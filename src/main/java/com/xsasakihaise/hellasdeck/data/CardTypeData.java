package com.xsasakihaise.hellasdeck.data;

/**
 * Plain data holder mirroring entries in {@code card_types.json}.
 */
public class CardTypeData {
    /** Number of entries for the type that should be inserted into the draw pool. */
    public int amount;
    /** Display text that is shown when the type is drawn. */
    public String tag;
}
