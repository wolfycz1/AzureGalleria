package com.wolfycz1.commands;

import com.wolfycz1.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DropCommand implements Command {
    private Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return "No argument specified. See 'help drop'";

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();
        Item item = inventory.getItem(argument);

        if (item == null) return String.format("You don't have the '%s' in your inventory.", argument);
        inventory.removeItem(item);
        currentRoom.addItem(item);

        return String.format("You dropped the %s onto the floor.", item.getName());
    }

    @Override
    public boolean exit() {
        return false;
    }
}
