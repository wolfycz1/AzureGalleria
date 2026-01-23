package com.wolfycz1.commands;

import com.wolfycz1.Command;

public class InvestigateCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
    }

    @Override
    public String getDescription() {
        return "Describes the current room again. [f]";
    }

    @Override
    public String getDetails() {
        return """
               INVESTIGATE
               Prints the full description of the room, including visible items and characters.""";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
