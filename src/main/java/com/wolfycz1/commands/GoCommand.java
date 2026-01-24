package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Console;
import com.wolfycz1.Language;
import com.wolfycz1.Room;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class GoCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.go")));

        Room room = console.getCurrentRoom().getExit(argument);

        if (room == null) return Language.get("cmd.go.err.noExit", argument);
        if (room.isLocked()) return Language.get("cmd.go.err.locked", room.getName());

        console.setCurrentRoom(room);
        return Language.get("cmd.go.execute", room.getName());
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.go.desc") + " " + Arrays.toString(Language.getArray("cmd.go.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
                    %s
               
               %s
                    %s
                    %s""", Language.get("man.go.cmd"), Language.get("man.go.arg.room"), Language.get("man.go.arg.alias"),
                Language.get("man.example"), Language.get("man.go.example.room"), Language.get("man.go.example.alias"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
