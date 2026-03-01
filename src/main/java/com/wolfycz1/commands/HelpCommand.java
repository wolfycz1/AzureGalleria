package com.wolfycz1.commands;

import com.wolfycz1.utils.Language;
import lombok.AllArgsConstructor;
import com.wolfycz1.Console;

import java.util.Arrays;

/**
 * Provides the player with in-game documentation and a detailed manual for specific actions when provided with an argument.
 * @author wolfycz1
 */
@AllArgsConstructor
public class HelpCommand implements Command {
    private final Console console;

    /**
     * Executes the help sequence. If no argument is provided, it builds a formatted list of all registered commands.
     * If a valid command name is passed as an argument it returns the detailed manual entry for that specific command.
     * @param argument The specific command the player wants more details on, or an empty string for the general list.
     * @return A {@code CommandResponse} with a reponse and exit status.
     */
    @Override
    public CommandResponse execute(String argument) {
        if (argument.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(Language.get("cmd.help.moreInfo")).append("\n\n");

            for (String name : console.getCommandList()) {
                Command command = console.getCommands().get(name);
                sb.append(String.format("%-15s %s %n", name.toUpperCase(), command.getDescription()));
            }

            return new CommandResponse(sb.deleteCharAt(sb.length() -1).toString(), false);
        }

        Command command = console.getCommands().get(argument);
        if (command == null) return new CommandResponse(Language.get("cmd.help.err.unknownCmd", argument), false);

        return new CommandResponse(command.getDescription() + "\n\n" + command.getDetails(), false);
    }

    /**
     * Retrieves a summary of the help command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.help.desc") + " " + Arrays.toString(Language.getArray("cmd.help.aliases"));
    }

    /**
     * Retrieves the manual entry for the help command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s""", Language.get("man.help.cmd"), Language.get("man.help.arg"));
    }
}
