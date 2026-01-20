package com.wolfycz1;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Character {
    private String name;
    private DialogueNode startNode;

    public Character(String name) {
        this.name = name;
    }

    public String talk() {
        return null;
    }
}
