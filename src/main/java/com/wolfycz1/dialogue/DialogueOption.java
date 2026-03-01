package com.wolfycz1.dialogue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a specific choice available during a conversation.
 * @author wolfycz1
 */
@Getter
@AllArgsConstructor
public class DialogueOption {
    private String label;
    private DialogueNode nextNode;
}
