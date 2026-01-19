package com.wolfycz1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Character {
    private String name;
    private DialogueNode startNode;

    public String talk() {
        return null;
    }
}
