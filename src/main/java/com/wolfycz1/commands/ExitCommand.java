package com.wolfycz1.commands;

import com.wolfycz1.Command;

public class ExitCommand implements Command {

    @Override
    public String execute(String argument) {
        return "Exiting...";
    }

    @Override
    public String getDescription() {
        return "Exits the game. [e]";
    }

    @Override
    public String getDetails() {
        return """
               EXIT     (ALIASES: %a)
               Quits the game without saving.""";
    }

    @Override
    public boolean exit() {
        return true;
    }
}
