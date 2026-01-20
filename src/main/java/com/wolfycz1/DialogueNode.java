package com.wolfycz1;

import java.util.ArrayList;
import java.util.List;

public class DialogueNode {
    private String id;
    private String text;
    private List<DialogueOption> options;

    public DialogueNode(String id, String text) {
        this.id = id;
        this.text = text;
        this.options = new ArrayList<>();
    }

    public void addOption(DialogueOption dialogueOption) {
        options.add(dialogueOption);
    }
}
