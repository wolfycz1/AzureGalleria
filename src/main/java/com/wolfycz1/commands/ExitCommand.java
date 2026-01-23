package com.wolfycz1.commands;

import com.wolfycz1.Command;

public class ExitCommand implements Command {

    @Override
    public String execute(String argument) {
        return "Exiting...";
    }

    @Override
    public boolean exit() {
        return true;
    }
}
