package com.wolfycz1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an NPC in the game.
 * Characters can engage in conversations and trade with the player.
 * @author wolfycz1
 */
@Setter
@Getter
public class Character {
    private String name;
    private DialogueNode startNode;
    private Map<String, Trade> trades;

    public Character(String name) {
        this.name = name;
        this.trades = new HashMap<>();
    }

    /**
     * Registers a new trade that this character is willing to make.
     * @param tradeIn The name of the item the player must give to the character.
     * @param tradeOut The {@link Item} the character will give to the player in return.
     * @param tradeDialogue The {@link DialogueNode} triggered upon successfully completing this trade.
     * @return {@code true} if the trade was successfully added, {@code false} otherwise
     */
    public boolean addTrade(String tradeIn, Item tradeOut, DialogueNode tradeDialogue) {
        if (tradeIn == null) return false;
        trades.put(tradeIn, new Trade(tradeIn, tradeOut, tradeDialogue));
        return true;
    }

    /**
     * Represents a single transaction or exchange offered by a character.
     */
    @Data
    @AllArgsConstructor
    public static class Trade {
        private String tradeIn;
        private Item tradeOut;
        private DialogueNode tradeDialogue;
    }
}
