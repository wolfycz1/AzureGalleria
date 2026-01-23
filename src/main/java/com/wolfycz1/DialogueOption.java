package com.wolfycz1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DialogueOption {
    private String label;
    private DialogueNode nextNode;
}
