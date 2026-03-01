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
 * Handles the player action of taking an item from the current room and storing it in their inventory.
 * @author wolfycz1
 */
@AllArgsConstructor
public class PickupCommand implements Command {
    private final Console console;

    /**
     * Executes the pickup sequence. Validates the player's input, attempts to remove the specified item from the current room,
     * and tries to add it to the inventory.
     * @param argument The name of the item the player wants to pick up.
     * @return A localized status message indicating success or failure. Appends {@code Investigate} on success.
     */
    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.pickup")));

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();

        Optional<Item> optItem = currentRoom.removeItem(argument);
        if (optItem.isEmpty()) {
            return Language.get("cmd.pickup.err.noItem", argument);
        }
        Item item = optItem.get();

        Optional<Inventory.InventoryFailure> failure = inventory.addItem(item);
        if (failure.isPresent()) {
            currentRoom.addItem(item);
            switch (failure.get()) {
                case INVENTORY_FULL -> {
                    return Language.get("cmd.pickup.err.invFull");
                }
                case NO_ITEM -> {
                    return Language.get("cmd.pickup.err.noItem", argument);
                }
                case NOT_PICKUPABLE -> {
                    return Language.get("cmd.pickup.err.notPickupable", item.getName());
                }
            }
        }

        return Language.get("cmd.pickup.execute", item.getName()) + "\n"
                + console.getCommands().get(Language.get("cmd.investigate")).execute("INTERNAL");
    }

    /**
     * Retrieves a summary of the pickup command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.pickup.desc") + " " + Arrays.toString(Language.getArray("cmd.pickup.aliases"));
    }

    /**
     * Retrieves the manual entry for the pickup command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s
               
               %s
                    %s""", Language.get("man.pickup.cmd"), Language.get("man.pickup.arg"), Language.get("man.example"),
                Language.get("man.pickup.example"), Language.get("man.note"),
                Language.get("man.pickup.note", console.getInventory().getCapacity()));
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
