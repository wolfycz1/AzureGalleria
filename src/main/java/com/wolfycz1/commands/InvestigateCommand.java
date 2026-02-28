package com.wolfycz1.commands;

import com.wolfycz1.*;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * Handles the player action of looking around their current environment.
 * @author wolfycz1
 */
@AllArgsConstructor
public class InvestigateCommand implements Command {
    private final Console console;

    /**
     * Executes the investigate sequence.
     * @param argument (ignored).
     * If the argument matches exactly {@code "INTERNAL"}, the command suppresses its standard narrative preamble.
     * @return Formatted multi-line string representing the visual state of the room.
     */
    @Override
    public String execute(String argument) {
        Room currentRoom = console.getCurrentRoom();
        String roomName = currentRoom.getName();

        StringBuilder sb = new StringBuilder();
        if (!argument.equals("INTERNAL")) sb.append(Language.get("cmd.investigate.execute")).append("\n");
        sb.append("=".repeat(100)).append("\n");
        sb.append(" ".repeat(100 / 2 - roomName.length() / 2)).append(roomName)
                .append(" ".repeat(100 / 2 - roomName.length() / 2)).append("\n");
        sb.append("=".repeat(100)).append("\n\n");

        sb.append(console.breakupStringToLines(currentRoom.getDescription())).append("\n");

        if (currentRoom.listItems().isEmpty())
            sb.append(Language.get("cmd.investigate.noItems")).append("\n");
        else
            sb.append(Language.get("cmd.investigate.label.items")).append(": ").append(currentRoom.listItems()).append("\n");

        if (currentRoom.listCharacters().isEmpty())
            sb.append(Language.get("cmd.investigate.noCharacters")).append("\n");
        else
            sb.append(Language.get("cmd.investigate.label.characters")).append(": ").append(currentRoom.listCharacters()).append("\n");

        if (currentRoom.listExits().isEmpty())
            sb.append(Language.get("cmd.investigate.noExits")).append("\n");
        else
            sb.append(Language.get("cmd.investigate.label.exits")).append(": ").append(currentRoom.listExits()).append("\n");

        if (console.getInventory().listItems().isEmpty())
            sb.append(Language.get("cmd.investigate.noInvItems")).append("\n");
        else
            sb.append(Language.get("cmd.investigate.label.inventory")).append(": ").append(console.getInventory().listItems()).append("\n");

        return sb.toString();
    }

    /**
     * Retrieves a summary of the investigate command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.investigate.desc") + " " + Arrays.toString(Language.getArray("cmd.investigate.aliases"));
    }

    /**
     * Retrieves the manual entry for the investigate command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
               %s""", Language.get("man.investigate.cmd"), Language.get("man.investigate.desc"));
    }

    /**
     * Indicates whether executing this command terminates the game.
     * @return always {@code false}
     */
    @Override
    public boolean exit() {
        return false;
    }
}
