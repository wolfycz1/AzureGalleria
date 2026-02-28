package com.wolfycz1;

import java.util.List;

/**
 * Manages the state and progression of an active conversation between the player and a character.
 * Handles input parsing, node transitions, and formatting the dialogue for console output.
 * @author wolfycz1
 */
public class DialogueHandler {
    private DialogueNode currentNode;
    private Character character;

    /**
     * Initiates a conversation starting from the character's default starting node.
     * @param character The {@link Character} the player is speaking to.
     * @return A DialogueResult containing the formatted text, or an ERROR if the character has no dialogue/is missing.
     */
    public DialogueResult startDialogue(Character character) {
        if (character == null || character.getStartNode() == null) {
            return new DialogueResult(DialogueStatus.ENDED, null);
        }

        this.character = character;
        this.currentNode = character.getStartNode();
        return formatNode();
    }

    /**
     * Initiates a conversation starting from a provided node.
     * @param character The {@link Character} the player is speaking to.
     * @param startDialogue The {@link DialogueNode} to begin the conversation at.
     * @return A DialogueResult containing the formatted text, or an ERROR if the character/node is missing.
     */
    public DialogueResult startDialogue(Character character, DialogueNode startDialogue) {
        if (character == null || startDialogue == null) {
            return new DialogueResult(DialogueStatus.ENDED, null);
        }
        this.character = character;
        this.currentNode = startDialogue;
        return formatNode();
    }

    /**
     * Processes the player's numerical input to select a dialogue option.
     * @param input The raw string input provided by the player.
     * @return A DialogueResult containing the status of the conversation and the corresponding text to be displayed
     * or {@code null} if the dialogue status is {@code ENDED}.
     */
    public DialogueResult processInput(String input) {
        if (currentNode == null) {
            return new DialogueResult(DialogueStatus.ENDED, null);
        }

        int choiceIndex;
        try {
            choiceIndex = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            return new DialogueResult(DialogueStatus.ERROR, Language.get("dialogueHandler.err.notNum"));
        }

        List<DialogueOption> options = currentNode.getOptions();
        if (choiceIndex < 0 || choiceIndex > options.size()) {
            return new DialogueResult(DialogueStatus.ERROR, Language.get("dialogueHandler.err.invOpt"));
        }

        if (choiceIndex == options.size()) {
            return new DialogueResult(DialogueStatus.ENDED, null);
        }

        DialogueOption option = options.get(choiceIndex);
        currentNode = option.getNextNode();

        if (currentNode == null) {
            return new DialogueResult(DialogueStatus.ENDED, null);
        }

        return formatNode();
    }

    /**
     * Represents the outcome of the player's attempt to progress a conversation.
     */
    public enum DialogueStatus {
        CONTINUE,
        ENDED,
        ERROR,
    }

    /**
     * A data carrier holding the complete result of a single dialogue interaction.
     * @param status The state of the dialogue after attempting to process the player's input.
     * @param output The text to display to the player. If the status is {@code ENDED}, the output is {@code null}.
     */
    public record DialogueResult(DialogueStatus status, String output) {}

    /**
     * Constructs the visuals of the current node including word-wrapping and available options.
     * @return A DialogueResult containing the status of the conversation and the corresponding text to be displayed.
     */
    private DialogueResult formatNode() {
        if (character == null || currentNode == null) {
            return new DialogueResult(DialogueStatus.ENDED, Language.get("dialogueHandler.err.gone"));
        }
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

        return new DialogueResult(DialogueStatus.CONTINUE, sb.toString());
    }
}