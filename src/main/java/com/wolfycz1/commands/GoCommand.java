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
     * @return A localized status message. Appends {@code Investigate} on success.
     */
    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.go")));

        Optional<Room> optRoom = console.getCurrentRoom().getExit(argument);
        if (optRoom.isEmpty()) {
            return Language.get("cmd.go.err.noExit", argument);
        }
        Room room = optRoom.get();

        if (room.isLocked()) return Language.get("cmd.go.err.locked", room.getName());

        console.setCurrentRoom(room);
        return Language.get("cmd.go.execute", room.getName()) + "\n"
                + console.getCommands().get(Language.get("cmd.investigate")).execute("INTERNAL");
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

    /**
     * Indicates whether executing this command terminates the game.
     * @return always {@code false}
     */
    @Override
    public boolean exit() {
        return false;
    }
}
