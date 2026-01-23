package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Console;
import com.wolfycz1.Room;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GoCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return "No argument specified. See 'help go'.";

        Room room = console.getCurrentRoom().getExit(argument);

        if (room == null) return String.format("There is no exit to the '%s' from here.", argument);
        if (room.isLocked()) return String.format("The entrance to the %s is locked. You'll need to unlock it first.", room.getName());

        console.setCurrentRoom(room);
        return String.format("You moved to the %s.", room.getName());
    }

    @Override
    public boolean exit() {
        return false;
    }
}
