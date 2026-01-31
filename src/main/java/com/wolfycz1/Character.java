package com.wolfycz1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

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

    public void addTrade(String tradeIn, Item tradeOut, DialogueNode tradeDialogue) {
        trades.put(tradeIn, new Trade(tradeIn, tradeOut, tradeDialogue));
    }

    public String talk() {
        return null;
    }

    @Data
    @AllArgsConstructor
    public static class Trade {
        private String tradeIn;
        private Item tradeOut;
        private DialogueNode tradeDialogue;
    }
}
