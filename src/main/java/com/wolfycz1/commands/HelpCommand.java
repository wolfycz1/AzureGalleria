package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Language;
import lombok.AllArgsConstructor;
import com.wolfycz1.Console;

import java.util.Arrays;

@AllArgsConstructor
public class HelpCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(Language.get("cmd.help.moreInfo")).append("\n\n");

            for (String name : console.getCommandList()) {
                Command command = console.getCommands().get(name);
                sb.append(String.format("%-15s %s %n", name.toUpperCase(), command.getDescription()));
            }

            return sb.deleteCharAt(sb.length() -1).toString();
        }

        Command command = console.getCommands().get(argument);
        if (command == null) return Language.get("cmd.help.err.unknownCmd", argument);

        return command.getDescription() + "\n\n" + command.getDetails();
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.help.desc") + " " + Arrays.toString(Language.getArray("cmd.help.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s""", Language.get("man.help.cmd"), Language.get("man.help.arg"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
