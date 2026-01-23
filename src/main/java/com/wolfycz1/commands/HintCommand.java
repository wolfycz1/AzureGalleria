package com.wolfycz1.commands;

import com.wolfycz1.Command;

public class HintCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
    }

    @Override
    public String getDescription() {
        return "Provides a context-sensitive hint. [h]";
    }

    @Override
    public String getDetails() {
        return """
               HINT
               Asks for confirmation before offering a hint for when the player is stuck.""";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
