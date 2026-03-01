package com.wolfycz1.commands;

import com.wolfycz1.*;
import com.wolfycz1.models.Inventory;
import com.wolfycz1.models.Item;
import com.wolfycz1.models.Room;
import com.wolfycz1.utils.Language;
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
     * @return A {@code CommandResponse} with a reponse and exit status. Appends {@code Investigate} on success.
     */
    @Override
    public CommandResponse execute(String argument) {
        if (argument.isEmpty()) return new CommandResponse(String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"),Language.get("cmd.drop"))), false);

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();

        Optional<Item> optItem = inventory.removeItem(argument);
        if (optItem.isEmpty()) {
            return new CommandResponse(Language.get("cmd.drop.err.noItem"), false);
        }
        Item item = optItem.get();

        if (!currentRoom.addItem(item)) {
            inventory.addItem(item);
            return new CommandResponse(Language.get("cmd.drop.err.noItem"), false);
        }

        return new CommandResponse(Language.get("cmd.drop.execute", item.getName()) + "\n"
                + console.getCommands().get(Language.get("cmd.investigate")).execute("INTERNAL").response(), false);
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
}
