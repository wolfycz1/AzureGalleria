package com.wolfycz1.commands;

import com.wolfycz1.Command;
import lombok.AllArgsConstructor;
import com.wolfycz1.Console;

@AllArgsConstructor
public class HelpCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("For more information on a specific command, type HELP command-name.\n\n");

            for (String name : console.getCommandList()) {
                Command command = console.getCommands().get(name);
                sb.append(String.format("%-15s %s %n", name.toUpperCase(), command.getDescription()));
            }

            return sb.deleteCharAt(sb.length() -1).toString();
        }

        Command command = console.getCommands().get(argument);
        if (command == null) return String.format("Unknown command '%s'. Type 'help' for a list of commands.", argument);

        return command.getDescription() + "\n\n" + command.getDetails();
    }

    @Override
    public String getDescription() {
        return "Displays a list of commands or a specific command's details. [?]";
    }

    @Override
    public String getDetails() {
        return """
               HELP [command]
                    command - command name to display information on.""";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
