package com.wolfycz1.commands;

import com.wolfycz1.Console;
import com.wolfycz1.utils.Language;
import com.wolfycz1.models.Room;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles player movement between interconnected rooms in the game world.
 * @author wolfycz1
 */
@AllArgsConstructor
public class GoCommand implements Command {
    private final Console console;

    /**
     * Executes the movement sequence. Validates the destination, checks if the current room has a matching exit,
     * and verifies the destination is unlocked.
     * @param argument The name or alias of the exit the player wants to travel to.
     * @return A {@code CommandResponse} with a reponse and exit status. Appends {@code Investigate} on success.
     */
    @Override
    public CommandResponse execute(String argument) {
        if (argument.isEmpty()) return new CommandResponse(String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.go"))), false);

        Optional<Room> optRoom = console.getCurrentRoom().getExit(argument);
        if (optRoom.isEmpty()) {
            return new CommandResponse(Language.get("cmd.go.err.noExit", argument), false);
        }
        Room room = optRoom.get();

        if (room.isLocked()) return new CommandResponse(Language.get("cmd.go.err.locked", room.getName()), false);

        console.setCurrentRoom(room);
        return new CommandResponse(Language.get("cmd.go.execute", room.getName()) + "\n"
                + console.getCommands().get(Language.get("cmd.investigate")).execute("INTERNAL").response(), false);
    }

    /**
     * Retrieves a summary of the movement command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.go.desc") + " " + Arrays.toString(Language.getArray("cmd.go.aliases"));
    }

    /**
     * Retrieves the manual entry for the movement command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
                    %s
               
               %s
                    %s
                    %s""", Language.get("man.go.cmd"), Language.get("man.go.arg.room"), Language.get("man.go.arg.alias"),
                Language.get("man.example"), Language.get("man.go.example.room"), Language.get("man.go.example.alias"));
    }
}
