package com.wolfycz1.commands;

import com.wolfycz1.Command;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class HelpCommand implements Command {
    private final List<String> commandList;

    @Override
    public String execute(String argument) {
        return commandList.toString();
    }

    @Override
    public boolean exit() {
        return false;
    }
}
