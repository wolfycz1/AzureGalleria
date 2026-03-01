package com.wolfycz1.commands;

import com.wolfycz1.Console;
import com.wolfycz1.utils.Language;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * Retrieves the contextual clue associated with the player's current location to assist them if they are stuck.
 * @author wolfycz1
 */
@AllArgsConstructor
public class HintCommand implements Command {
    private final Console console;

    /**
     * Executes the hint sequence. Ignores any arguments. Results in a hint contextually dependent on the current room.
     * @param argument (ignored)
     * @return A {@code CommandResponse} with a reponse and exit status.
     */
    @Override
    public CommandResponse execute(String argument) {
        return new CommandResponse(console.getCurrentRoom().getHint(), false);
    }

    /**
     * Retrieves a summary of the hint command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.hint.desc") + " " + Arrays.toString(Language.getArray("cmd.hint.aliases"));
    }

    /**
     * Retrieves the manual entry for the hint command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
               %s""", Language.get("man.hint.cmd"), Language.get("man.hint.desc"));
    }
}
