package com.wolfycz1.commands;

import com.wolfycz1.Command;

public class InteractCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
    }

    @Override
    public String getDescription() {
        return "Talks to a character. [i]";
    }

    @Override
    public String getDetails() {
        return """
               INTERACT character
                    character - character in the current room to initiate a conversation with.
               
               Example:
                    interact The Angler""";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
