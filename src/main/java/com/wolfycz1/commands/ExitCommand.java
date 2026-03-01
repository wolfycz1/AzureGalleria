package com.wolfycz1.commands;

import com.wolfycz1.utils.Language;

import java.util.Arrays;

/**
 * Handles the player action of quitting the game.
 * @author wolfycz1
 */
public class ExitCommand implements Command {

    /**
     * Executes the exit sequence. Ignores any arguments provided.
     * @param argument (ignored)
     * @return The localized string confirming the game is closing.
     */
    @Override
    public CommandResponse execute(String argument) {
        return new CommandResponse(Language.get("cmd.exit.execute"), true);
    }

    /**
     * Retrieves a summary of the exit command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.exit.desc") + " " + Arrays.toString(Language.getArray("cmd.exit.aliases"));
    }

    /**
     * Retrieves the manual entry for the exit command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
               %s""", Language.get("man.exit.cmd"), Language.get("man.exit.desc"));
    }
}
