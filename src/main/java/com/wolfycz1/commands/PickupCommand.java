package com.wolfycz1.commands;

import com.wolfycz1.*;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class PickupCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.pickup")));

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();
        Item item = currentRoom.getItem(argument);

        if (item == null) return Language.get("cmd.pickup.err.noItem", argument);
        if (!item.isPickupable()) return Language.get("cmd.pickup.err.notPickupable", item.getName());
        if (!inventory.addItem(item)) return Language.get("cmd.pickup.err.invFull");

        currentRoom.removeItem(item);
        return Language.get("cmd.pickup.execute", item.getName());
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.pickup.desc") + " " + Arrays.toString(Language.getArray("cmd.pickup.aliases"));
    }

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

    @Override
    public boolean exit() {
        return false;
    }
}
