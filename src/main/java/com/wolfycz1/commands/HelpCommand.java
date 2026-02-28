package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Language;
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
     * @return The formatted help text or an error message if the requested command is unknown.
     */
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

    /**
     * Indicates whether executing this command terminates the game.
     * @return always {@code false}
     */
    @Override
    public boolean exit() {
        return false;
    }
}
