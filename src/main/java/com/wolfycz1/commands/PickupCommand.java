package com.wolfycz1.commands;

import com.wolfycz1.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PickupCommand implements Command {
    private Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return "No argument specified. See 'help pickup'";

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();
        Item item = currentRoom.getItem(argument);

        if (item == null) return String.format("There is no '%s' here to pick up.", argument);
        if (!item.isPickupable()) return String.format("You cannot pick up the %s as it's fixed in place.", item.getName());
        if (!inventory.addItem(item)) return "Your backpack is full! You must drop something before picking this up.";

        currentRoom.removeItem(item);
        return String.format("You picked up the %s and put it in your inventory.", item.getName());
    }

    @Override
    public boolean exit() {
        return false;
    }
}
