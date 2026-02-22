package com.wolfycz1;

import java.util.List;

public class DialogueHandler {
    private DialogueNode currentNode;
    private Character character;

    public String startDialogue(Character character) {
        this.character = character;
        this.currentNode = character.getStartNode();
        return formatNode();
    }

    public String startDialogue(Character character, DialogueNode startDialogue) {
        this.character = character;
        this.currentNode = startDialogue;
        return formatNode();
    }

    public String processInput(String input) {
        int choiceIndex;
        try {
            choiceIndex = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            return Language.get("dialogueHandler.err.notNum");
        }

        List<DialogueOption> options = currentNode.getOptions();
        if (choiceIndex < 0 || choiceIndex > options.size()) {
            return Language.get("dialogueHandler.err.invOpt");
        }

        if (choiceIndex == options.size()) return null;

        DialogueOption option = options.get(choiceIndex);
        currentNode = option.getNextNode();

        if (currentNode == null) return null;

        return formatNode();
    }

    private String formatNode() {
        StringBuilder sb = new StringBuilder();
        sb.append("-".repeat(97)).append("\n");

        String text = currentNode.getText();
        StringBuilder line = new StringBuilder();
        StringBuilder formattedText = new StringBuilder();
        for (String word : text.split(" ")) {
            if (word.contains("\n")) {
                formattedText.append(line);
                line.setLength(0);
            }

            if (line.length() + word.length() > 98 - character.getName().length()) {
                formattedText.append(line).append("\n");
                line.setLength(0);
            }

            line.append(word).append(" ");
        }
        formattedText.append(line).append("\n");
        sb.append(character.getName()).append(": ").append(formattedText);

        sb.append("-".repeat(100)).append("\n");

        List<DialogueOption> options = currentNode.getOptions();
        for (int i = 0; i < options.size(); i++) {
            sb.append(String.format("[%d] %s%n", (i + 1), options.get(i).getLabel()));
        }
        sb.append(String.format("[%d] %s%n", (options.size() + 1), Language.get("dialogueHandler.opt.walkAway")));

        return sb.toString();
    }
}