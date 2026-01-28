package com.wolfycz1.commands;

import com.wolfycz1.*;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class DropCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"),Language.get("cmd.drop")));

        Inventory inventory = console.getInventory();
        Room currentRoom = console.getCurrentRoom();
        Item item = inventory.getItem(argument);

        if (item == null) return Language.get("cmd.drop.err.noItem", argument);
        inventory.removeItem(item);
        currentRoom.addItem(item);

        return Language.get("cmd.drop.execute", item.getName()) + "\n"
                + console.getCommands().get(Language.get("cmd.investigate")).execute("INTERNAL");
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.drop.desc") + " " + Arrays.toString(Language.getArray("cmd.drop.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.drop.cmd"), Language.get("man.drop.arg"),
                Language.get("man.example"), Language.get("man.drop.example"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
