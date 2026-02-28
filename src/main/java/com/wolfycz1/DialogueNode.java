package com.wolfycz1;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single node within a conversation tree.
 * Contains written text and a list of player response options.
 * @author wolfycz1
 */
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

    /**
     * Adds a player response option to this node.
     * @param dialogueOption The {@link DialogueOption} to add to the available choices.
     * @return {@code true} if option was successfully added, {@code false} otherwise
     */
    public boolean addOption(DialogueOption dialogueOption) {
        if (dialogueOption == null) return false;
        options.add(dialogueOption);
        return true;
    }
}
