package com.wolfycz1;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DialogueNode {
    private final String id;
    private final String text;
    private final List<DialogueOption> options;

    public DialogueNode(String id, String text) {
        this.id = id;
        this.text = text;
        this.options = new ArrayList<>();
    }

    public void addOption(DialogueOption dialogueOption) {
        options.add(dialogueOption);
    }
}
