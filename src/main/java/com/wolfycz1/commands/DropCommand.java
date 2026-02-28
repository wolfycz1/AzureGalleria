package com.wolfycz1.commands;

import com.wolfycz1.*;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles the player action of transferring an item from their inventory into the current room.
 * @author wolfycz1
 */
@AllArgsConstructor
public class DropCommand implements Command {
    private final Console console;

    /**
     * Executes the drop sequence. Validates the player's input, attempts to remove the specified item from the inventory,
     * and places it into the current room.
     * @param argument The name of the item the player wants to drop.
     * @return A localized status message indicating success or failure. Appends {@code Investigate} on success.
     */
    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"),Language.get("cmd.drop")));

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();

        Optional<Item> optItem = inventory.removeItem(argument);
        if (optItem.isEmpty()) {
            return Language.get("cmd.drop.err.noItem");
        }
        Item item = optItem.get();

        if (!currentRoom.addItem(item)) {
            inventory.addItem(item);
            return Language.get("cmd.drop.err.noItem");
        }

        return Language.get("cmd.drop.execute", item.getName()) + "\n"
                + console.getCommands().get(Language.get("cmd.investigate")).execute("INTERNAL");
    }

    /**
     * Retrieves a brief summary of the drop command with its acceptable aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.drop.desc") + " " + Arrays.toString(Language.getArray("cmd.drop.aliases"));
    }

    /**
     * Retrieves the manual entry for the drop command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.drop.cmd"), Language.get("man.drop.arg"),
                Language.get("man.example"), Language.get("man.drop.example"));
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
